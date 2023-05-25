package edu.touro.mco152.bm;

import edu.touro.mco152.bm.persist.DiskRun;

/**
 * Observer interface to be passed into subject so that it can notify observer by calling the updateMethod
 */
public interface Observer {
    public void update(DiskRun run);
}
