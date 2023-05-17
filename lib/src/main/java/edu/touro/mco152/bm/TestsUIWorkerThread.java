package edu.touro.mco152.bm;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class TestsUIWorkerThread implements UIWorker<Boolean, DiskMark>{

    private Callable<Boolean> doInBackground;
    private ExecutorService executorService;

    private Boolean isProcessCanceled = false;
    private Boolean isRunning = false;

    private Future<Boolean> returnedResult;

    private List<DiskMark> chunks = new ArrayList<>();

    private int progress;

    public TestsUIWorkerThread(){
          executorService = Executors.newFixedThreadPool(10);
    }
    @Override
    public boolean isProcessCancelled() {
        return isProcessCanceled;
    }

    @Override
    public void setProcessProgress(int progress) {
        this.progress = progress;
        System.out.println(progress);
    }

    @Override
    public void publishProcessChunks(DiskMark... chunks) {
            this.chunks.addAll(List.of(chunks));
    }

    @Override
    public Boolean getProcessResult() throws InterruptedException, ExecutionException {
        return returnedResult.get();
    }

    @Override
    public boolean cancelProcess(boolean mayInterruptIfRunning) {
//        if(mayInterruptIfRunning && isRunning) {
//            isProcessCanceled = true;
//            executorService.shutdownNow();
//            return true;
//        } else if (isProcessCanceled) {
//            return true;
//        }
        return false;
    }

    @Override
    public void addPropertyChangeListenerToProcess(PropertyChangeListener listener) {

    }

    @Override
    public void executeProcess() {
        isRunning = true;
        System.out.println("Excecuting...");
        returnedResult = executorService.submit(doInBackground);
        System.out.println("Done");
//        try {
//            doInBackground.call();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }

//        Thread thread = new Thread(doInBackground);
    }

    @Override
    public void setDoInBackground(Callable doInBackground) {
        this.doInBackground = (Callable<Boolean>)doInBackground;

    }

    public void done(){
        System.out.println("done");
    }

    public int getProgress() {
        return progress;
    }
}
