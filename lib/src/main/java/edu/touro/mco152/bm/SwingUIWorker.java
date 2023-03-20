package edu.touro.mco152.bm;

import javax.swing.*;
import java.beans.PropertyChangeListener;
import java.util.concurrent.ExecutionException;

public class SwingUIWorker extends SwingWorker<Boolean, DiskMark> implements UIWorker<Boolean, DiskMark> {

    private final Runnable doInBackground;

    public SwingUIWorker(Runnable doInBackground){
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
        return get();
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

        SwingUtilities.invokeLater(doInBackground);

        //how to return st??
        return null;
    }
}
