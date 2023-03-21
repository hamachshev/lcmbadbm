package edu.touro.mco152.bm;


import edu.touro.mco152.bm.persist.DiskRun;

import java.util.ArrayList;

/**
 * Helper class to allow the testing suite to get the results of a benchmark, and other status updates about a benchmark.
 */
public class  TestUtil {

    private static boolean stillRunning;

    public static DiskRun getDiskRun() {
        return diskRun;
    }

    public static void setDiskRun(DiskRun diskRun) {
        TestUtil.diskRun = diskRun;
    }

    private static DiskRun diskRun;
    private static ArrayList<Integer> progresses = new ArrayList<>();
    private static boolean completedSuccessfully = false;

    private static ArrayList<DiskMark> chunks;


    public static ArrayList<Integer> getProgresses() {
        return progresses;
    }

    public static void addAProgress(int progress){
        progresses.add(progress);
    }

    public static boolean isCompletedSuccessfully() {
        return completedSuccessfully;
    }

    public static void setCompletedSuccessfully(boolean completedSuccessfully) {
        TestUtil.completedSuccessfully = completedSuccessfully;
    }

    public static ArrayList<DiskMark> getChunks() {
        return chunks;
    }

    public static void setChunks(ArrayList<DiskMark> chunks) {
        TestUtil.chunks = chunks;
    }

    public static boolean isStillRunning() {
        return stillRunning;
    }

    public static void setStillRunning(boolean stillRunning) {
        TestUtil.stillRunning = stillRunning;
    }
}
