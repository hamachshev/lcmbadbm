package edu;

import edu.touro.mco152.bm.*;
import edu.touro.mco152.bm.persist.DiskRun;
import edu.touro.mco152.bm.ui.Gui;
import edu.touro.mco152.bm.ui.MainFrame;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import java.util.Properties;
import edu.touro.mco152.bm.TestUtil;

public class BenchmarkingTests {

    /**
     * Bruteforce setup of static classes/fields to allow DiskWorker to run.
     *
     * @author lcmcohen
     */
    private void setupDefaultAsPerProperties() {
        /// Do the minimum of what  App.init() would do to allow to run.
        Gui.mainFrame = new MainFrame();
        App.p = new Properties();
        App.loadConfig();
        System.out.println(App.getConfigString());
        Gui.progressBar = Gui.mainFrame.getProgressBar(); //must be set or get Nullptr

        // configure the embedded DB in .jDiskMark
        System.setProperty("derby.system.home", App.APP_CACHE_DIR);

        // code from startBenchmark
        //4. create data dir reference
        App.dataDir = new File(App.locationDir.getAbsolutePath() + File.separator + App.DATADIRNAME);

        //5. remove existing test data if exist
        if (App.dataDir.exists()) {
            if (App.dataDir.delete()) {
                App.msg("removed existing data dir");
            } else {
                App.msg("unable to remove existing data dir");
            }
        } else {
            App.dataDir.mkdirs(); // create data dir if not already present
        }

        //setup new diskworker with TestsUiWorker, and excecute
        TestsUIWorker testsUIWorker = new TestsUIWorker();
        DiskWorker diskWorker = new DiskWorker(testsUIWorker);
        diskWorker.execute();
    }
    @Test
    void testCompletedSuccessfully() {
        setupDefaultAsPerProperties();

        assertTrue(TestUtil.isCompletedSuccessfully());
    }

    @Test
    void testValidProgresses() {
        for (int i : TestUtil.getProgresses()) {
            if (i < 0 || i > 100)
                fail();
        }
    }

    @Test
    void testValidLookingDiskrun() {
        assertEquals(App.numOfMarks,TestUtil.getDiskRun().getNumMarks());
        assertEquals(App.numOfBlocks,TestUtil.getDiskRun().getNumBlocks());
        assertEquals(App.blockSizeKb,TestUtil.getDiskRun().getBlockSize());
        assertEquals(App.targetTxSizeKb(),TestUtil.getDiskRun().getTxSize());
        assertEquals(Util.getDiskInfo(App.dataDir),TestUtil.getDiskRun().getDiskInfo());
        assertTrue(TestUtil.getDiskRun().getIoMode() instanceof DiskRun.IOMode);
        assertTrue(TestUtil.getDiskRun().getRunMin() > 500);
        assertTrue(TestUtil.getDiskRun().getRunMax() < 8000);
        assertTrue(TestUtil.getDiskRun().getRunAvg() > 1000 && TestUtil.getDiskRun().getRunAvg() < 3200);







    }
}
