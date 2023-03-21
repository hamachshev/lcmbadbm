package edu.touro.mco152.bm;

import java.beans.PropertyChangeListener;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * Generic implementation of interface that includes all the methods necessary for DiskWorker to run, enabling DIP.
 * @param <T>
 * @param <V>
 */

public interface UIWorker<T, V> {
    boolean isProcessCancelled();
    void setProcessProgress(int progress);

    void publishProcessChunks(V... chunks);

    T getProcessResult() throws InterruptedException, ExecutionException;

    boolean cancelProcess(boolean mayInterruptIfRunning);

    void addPropertyChangeListenerToProcess(PropertyChangeListener listener);

    void executeProcess();

    void setDoInBackground(Callable<Boolean> doInBackground);
}
