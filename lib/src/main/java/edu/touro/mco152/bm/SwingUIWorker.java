package edu.touro.mco152.bm;

import edu.touro.mco152.bm.ui.Gui;

import javax.swing.*;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import static edu.touro.mco152.bm.App.dataDir;


/**
 * Swing implementation of methods that Diskworker needs to run, can be passed into diskworker to enable swing usage.
 */


public class SwingUIWorker extends SwingWorker<Boolean, DiskMark> implements UIWorker<Boolean, DiskMark> {
    // Record any success or failure status returned from SwingWorker (might be us or super)
    Boolean lastStatus = null;  // so far unknown
    private Callable<Boolean> doInBackground;

    @Override
    public void setDoInBackground(Callable<Boolean> doInBackground){
        this.doInBackground = doInBackground;
    }

    @Override
    public boolean isProcessCancelled() {
        return isCancelled();
    }

    @Override
    public void setProcessProgress(int progress) {
            setProgress(progress);
    }

    @Override
    public void publishProcessChunks(DiskMark... chunks) {
            publish(chunks);
    }

    @Override
    public Boolean getProcessResult() throws InterruptedException, ExecutionException {
        return super.get();
    }

    @Override
    public boolean cancelProcess(boolean mayInterruptIfRunning) {
        return cancel(mayInterruptIfRunning);
    }

    @Override
    public void addPropertyChangeListenerToProcess(PropertyChangeListener listener) {
            addPropertyChangeListener(listener);
    }

    @Override
    public void executeProcess() {
        execute();
    }

    @Override
    protected Boolean doInBackground() throws Exception {
        return doInBackground.call();
    }

    /**
     * Process a list of 'chunks' that have been processed, ie that our thread has previously
     * published to Swing. For my info, watch Professor Cohen's video -
     * Module_6_RefactorBadBM Swing_DiskWorker_Tutorial.mp4
     * @param markList a list of DiskMark objects reflecting some completed benchmarks
     */
    @Override
    protected void process(List<DiskMark> markList) {
        markList.stream().forEach((dm) -> {
            if (dm.type == DiskMark.MarkType.WRITE) {
                Gui.addWriteMark(dm);
            } else {
                Gui.addReadMark(dm);
            }
        });
    }


    @Override
    protected void done() {
        // Obtain final status, might from doInBackground ret value, or SwingWorker error
        try {
            lastStatus = getProcessResult();   // record for future access
        } catch (Exception e) {
            Logger.getLogger(App.class.getName()).warning("Problem obtaining final status: " + e.getMessage());
        }

        if (App.autoRemoveData) {
            Util.deleteDirectory(dataDir);
        }
        App.state = App.State.IDLE_STATE;
        Gui.mainFrame.adjustSensitivity();
    }

    public Boolean getLastStatus() {
        return lastStatus;
    }
}
