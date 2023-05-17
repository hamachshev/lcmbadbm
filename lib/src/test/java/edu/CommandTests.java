package edu;

import edu.touro.mco152.bm.*;
import edu.touro.mco152.bm.command.CommandExecutor;
import edu.touro.mco152.bm.command.ReadCommand;
import edu.touro.mco152.bm.command.WriteCommand;
import edu.touro.mco152.bm.persist.DiskRun;
import edu.touro.mco152.bm.ui.Gui;
import edu.touro.mco152.bm.ui.MainFrame;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CommandTests {


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

        //setup new diskworker with TestsUiWorker, and excecute with Executor




    }

    @Test
    void testCompletedSuccessfullyWrite() {
        TestsUIWorker testsUIWorker = new TestsUIWorker();
        setupDefaultAsPerProperties();
        CommandExecutor executor = new CommandExecutor(new WriteCommand(testsUIWorker, 25, 128, 2048, DiskRun.BlockSequence.SEQUENTIAL));
        assertTrue(executor.execute());

    }

    @Test
    void testCompletedSuccessfullyRead() {
        TestsUIWorker testsUIWorker = new TestsUIWorker();
        setupDefaultAsPerProperties();
        CommandExecutor executor = new CommandExecutor(new ReadCommand(testsUIWorker, 25, 128, 2048, DiskRun.BlockSequence.SEQUENTIAL));
        assertTrue(executor.execute());

    }

    @Test
    void testValidProgressesWrite() {
        TestsUIWorker testsUIWorker = new TestsUIWorker();
        setupDefaultAsPerProperties();
        CommandExecutor executor = new CommandExecutor(new WriteCommand(testsUIWorker, 25, 128, 2048, DiskRun.BlockSequence.SEQUENTIAL));
        executor.execute();
        for (int i : TestUtil.getProgresses()) {
            if (i < 0 || i > 100)
                fail();
        }
    }

    @Test
    void testValidProgressesRead() {
        TestsUIWorker testsUIWorker = new TestsUIWorker();
        setupDefaultAsPerProperties();
        CommandExecutor executor = new CommandExecutor(new WriteCommand(testsUIWorker, 25, 128, 2048, DiskRun.BlockSequence.SEQUENTIAL));
        executor.execute();
        for (int i : TestUtil.getProgresses()) {
            if (i < 0 || i > 100)
                fail();
        }
    }
    @Test
    void testValidLookingDiskrunRead() {
        TestsUIWorker testsUIWorker = new TestsUIWorker();
        setupDefaultAsPerProperties();
        CommandExecutor executor = new CommandExecutor(new ReadCommand(testsUIWorker, 25, 128, 2048, DiskRun.BlockSequence.SEQUENTIAL));
        executor.execute();

        assertEquals(25,TestUtil.getDiskRun().getNumMarks());
        assertEquals(128,TestUtil.getDiskRun().getNumBlocks());
        assertEquals(2048,TestUtil.getDiskRun().getBlockSize());
        assertEquals(Util.getDiskInfo(App.dataDir),TestUtil.getDiskRun().getDiskInfo());
        assertTrue(TestUtil.getDiskRun().getIoMode() instanceof DiskRun.IOMode);
        assertTrue(TestUtil.getDiskRun().getRunMin() > 500);
        assertTrue(TestUtil.getDiskRun().getRunMax() < 8000);
        assertTrue(TestUtil.getDiskRun().getRunAvg() > 1000 && TestUtil.getDiskRun().getRunAvg() < 3200);
    }

    @Test
    void testValidLookingDiskrunWrite() {
        TestsUIWorker testsUIWorker = new TestsUIWorker();
        setupDefaultAsPerProperties();
        CommandExecutor executor = new CommandExecutor(new WriteCommand(testsUIWorker, 25, 128, 2048, DiskRun.BlockSequence.SEQUENTIAL));
        executor.execute();

        assertEquals(25,TestUtil.getDiskRun().getNumMarks());
        assertEquals(128,TestUtil.getDiskRun().getNumBlocks());
        assertEquals(2048,TestUtil.getDiskRun().getBlockSize());
        assertEquals(Util.getDiskInfo(App.dataDir),TestUtil.getDiskRun().getDiskInfo());
        assertTrue(TestUtil.getDiskRun().getIoMode() instanceof DiskRun.IOMode);
        assertTrue(TestUtil.getDiskRun().getRunMin() > 500);
        assertTrue(TestUtil.getDiskRun().getRunMax() < 8000);
        assertTrue(TestUtil.getDiskRun().getRunAvg() > 1000 && TestUtil.getDiskRun().getRunAvg() < 3200);
    }




}
