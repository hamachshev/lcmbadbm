package edu.touro.mco152.bm;

import java.beans.PropertyChangeListener;
import java.util.concurrent.ExecutionException;

public interface UIWorker<T, V> {
    boolean isProcessCancelled();
    void setProcessProgress(int progress);

    void publishProcessChunks(V... chunks);

    T getProcessResult() throws InterruptedException, ExecutionException;

    boolean cancelProcess(boolean mayInterruptIfRunning);

    void addPropertyChangeListenerToProcess(PropertyChangeListener listener);

    void executeProcess();

    void setDoInBackground(Runnable doInBackground);
}
