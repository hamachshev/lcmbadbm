package edu.touro.mco152.bm.command;

import edu.touro.mco152.bm.*;
import edu.touro.mco152.bm.persist.DiskRun;
import edu.touro.mco152.bm.persist.EM;
import edu.touro.mco152.bm.ui.Gui;
import jakarta.persistence.EntityManager;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import static edu.touro.mco152.bm.App.*;
import static edu.touro.mco152.bm.DiskMark.MarkType.READ;

public class ReadCommand implements BenchmarkCommand{

    private UIWorker<Boolean, DiskMark> uiWorker;
    private int numOfMarks;

    private int numOfBlocks;

    private int blockSizeKb;

    // declare local vars formerly in DiskWorker


    private int unitsTotal;

    private float percentComplete;

    private int blockSize;
    private byte [] blockArr;

    private final int KILOBYTE = 1024;

    private DiskRun.BlockSequence blockSequence;

    private int wUnitsComplete = 0, rUnitsComplete = 0, unitsComplete;

    private int wUnitsTotal;



    public ReadCommand(UIWorker<Boolean, DiskMark> uiWorker, int numOfMarks, int numOfBlocks, int blockSizeKb, DiskRun.BlockSequence blockSequence){
        this.blockSizeKb = blockSizeKb;
        this.numOfMarks = numOfMarks;
        this.uiWorker = uiWorker;
        this.numOfBlocks = numOfBlocks;
        unitsTotal = numOfBlocks * numOfMarks;
        blockSize = blockSizeKb*KILOBYTE;
        blockArr = new byte [blockSize];
        this.blockSequence = blockSequence;


        int wUnitsTotal = App.writeTest ? numOfBlocks * numOfMarks : 0;
        int rUnitsTotal = App.readTest ? numOfBlocks * numOfMarks : 0;
        int unitsTotal = wUnitsTotal + rUnitsTotal;


        for(int b=0; b<blockArr.length; b++) {
            if (b%2==0) {
                blockArr[b]=(byte)0xFF;
            }
        }
    }


    DiskMark rMark;
    int startFileNum = App.nextMarkNumber;

    public boolean execute() {
        DiskRun run = new DiskRun(DiskRun.IOMode.READ, blockSequence);
        run.setNumMarks(numOfMarks);
        run.setNumBlocks(numOfBlocks);
        run.setBlockSize(blockSizeKb);
        run.setTxSize(targetTxSizeKb());
        run.setDiskInfo(Util.getDiskInfo(dataDir));

        msg("disk info: (" + run.getDiskInfo() + ")");

        Gui.chartPanel.getChart().getTitle().setVisible(true);
        Gui.chartPanel.getChart().getTitle().setText(run.getDiskInfo());

        for (int m = startFileNum; m < startFileNum + numOfMarks && !uiWorker.isProcessCancelled(); m++) {

            if (App.multiFile) {
                testFile = new File(dataDir.getAbsolutePath()
                        + File.separator + "testdata" + m + ".jdm");
            }
            rMark = new DiskMark(READ);  // starting to keep track of a new benchmark
            rMark.setMarkNum(m);
            long startTime = System.nanoTime();
            long totalBytesReadInMark = 0;

            try {
                try (RandomAccessFile rAccFile = new RandomAccessFile(testFile, "r")) {
                    for (int b = 0; b < numOfBlocks; b++) {
                        if (blockSequence == DiskRun.BlockSequence.RANDOM) {
                            int rLoc = Util.randInt(0, numOfBlocks - 1);
                            rAccFile.seek((long) rLoc * blockSize);
                        } else {
                            rAccFile.seek((long) b * blockSize);
                        }
                        rAccFile.readFully(blockArr, 0, blockSize);
                        totalBytesReadInMark += blockSize;
                        rUnitsComplete++;
                        unitsComplete = rUnitsComplete + wUnitsComplete;
                        percentComplete = (float) unitsComplete / (float) unitsTotal * 100f;
                        uiWorker.setProcessProgress((int) percentComplete);
                    }
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
                String emsg = "May not have done Write Benchmarks, so no data available to read." +
                        ex.getMessage();
                JOptionPane.showMessageDialog(Gui.mainFrame, emsg, "Unable to READ", JOptionPane.ERROR_MESSAGE);
                msg(emsg);
                return false;
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            long endTime = System.nanoTime();
            long elapsedTimeNs = endTime - startTime;
            double sec = (double) elapsedTimeNs / (double) 1000000000;
            double mbRead = (double) totalBytesReadInMark / (double) MEGABYTE;
            rMark.setBwMbSec(mbRead / sec);
            msg("m:" + m + " READ IO is " + rMark.getBwMbSec() + " MB/s    "
                    + "(MBread " + mbRead + " in " + sec + " sec)");
            App.updateMetrics(rMark);
            uiWorker.publishProcessChunks(rMark);

            run.setRunMax(rMark.getCumMax());
            run.setRunMin(rMark.getCumMin());
            run.setRunAvg(rMark.getCumAvg());
            run.setEndTime(new Date());
        }

            /*
              Persist info about the Read BM Run (e.g. into Derby Database) and add it to a GUI panel
             */
        EntityManager em = EM.getEntityManager();
        em.getTransaction().begin();
//                            em.persist(run);
        // instead of persisting bc not working for this assignment, sent to TestUtil
        TestUtil.setDiskRun(run);
        em.getTransaction().commit();

        Gui.runPanel.addRun(run);
        return true;


    }

    private long targetTxSizeKb() {
        return (long) blockSizeKb * numOfBlocks * numOfMarks;
    }
}
