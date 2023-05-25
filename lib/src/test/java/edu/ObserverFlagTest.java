package edu;

import edu.touro.mco152.bm.App;
import edu.touro.mco152.bm.TestsUIWorker;
import edu.touro.mco152.bm.command.CommandExecutor;
import edu.touro.mco152.bm.command.WriteCommand;
import edu.touro.mco152.bm.persist.DatabaseObserver;
import edu.touro.mco152.bm.persist.DiskRun;
import edu.touro.mco152.bm.ui.Gui;
import edu.touro.mco152.bm.ui.MainFrame;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test that the ObserverFlag works as an observer
 */

public class ObserverFlagTest {
    static TestObserverFlag testObserverFlag = new TestObserverFlag();
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
    void testObserverFlag() {
        TestsUIWorker testsUIWorker = new TestsUIWorker();
        setupDefaultAsPerProperties();
        WriteCommand writeCommand =  new WriteCommand(testsUIWorker, 25, 128, 2048, DiskRun.BlockSequence.SEQUENTIAL);
        writeCommand.addObserver(testObserverFlag);

        CommandExecutor executor = new CommandExecutor(writeCommand);
        assertTrue(executor.execute());

    }

    @AfterAll
    static void testObserverFlagIsTrueNow(){
        assertTrue(testObserverFlag.getFlag());
    }
}

