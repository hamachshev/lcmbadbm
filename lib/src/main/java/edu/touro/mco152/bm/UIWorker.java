package edu.touro.mco152.bm;

import java.beans.PropertyChangeListener;
import java.util.concurrent.ExecutionException;

public interface UIWorker<T, V> {
    boolean isCancelled();
    void setProgress(int progress);

    void publish(V... chunks);

    T get() throws InterruptedException, ExecutionException;

    boolean cancel(boolean mayInterruptIfRunning);

    void addPropertyChangeListener(PropertyChangeListener listener);

    void execute();
}
