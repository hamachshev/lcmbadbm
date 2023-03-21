package edu.touro.mco152.bm;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.logging.Logger;

/**
 * Alternative to swinguiworker, to run the benchmark from JUnit tests.
 */

public class TestsUIWorker implements UIWorker<Boolean, DiskMark>{

    Boolean lastStatus = null;  // so far unknown

    private Callable<Boolean> doInBackground;



    private Boolean isRunning = false;

    private boolean returnedResult;

    private ArrayList<DiskMark> chunks = new ArrayList<>();

    private int progress;

    @Override
    public boolean isProcessCancelled() {
        return false;
    }

    @Override
    public void setProcessProgress(int progress) {
        if(progress < 0 || progress > 100)
            throw new IllegalArgumentException("Progress must be between 0 and 100");
        this.progress = progress;
        TestUtil.addAProgress(progress);
    }

    @Override
    public void publishProcessChunks(DiskMark... chunks) {
        this.chunks.addAll(List.of(chunks));
    }

    @Override
    public Boolean getProcessResult() throws InterruptedException, ExecutionException {
        return returnedResult;
    }

    @Override
    public boolean cancelProcess(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public void addPropertyChangeListenerToProcess(PropertyChangeListener listener) {

    }

    @Override
    public void executeProcess() {
        try {
            TestUtil.setStillRunning(true);
            returnedResult = doInBackground.call();
            done();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void setDoInBackground(Callable<Boolean> doInBackground) {
        this.doInBackground = doInBackground;

    }

    public void done(){
        try {
            lastStatus = getProcessResult();
            TestUtil.setCompletedSuccessfully(true);
            TestUtil.setChunks(chunks);
            TestUtil.setStillRunning(false);
        } catch (Exception e) {
            Logger.getLogger(App.class.getName()).warning("Problem obtaining final status: " + e.getMessage());
        }
    }

    public int getProgress() {
        return progress;
    }

    public Boolean getLastStatus() {
        return lastStatus;
    }
}
