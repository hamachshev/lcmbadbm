package edu;

import edu.touro.mco152.bm.Observer;
import edu.touro.mco152.bm.persist.DiskRun;

public class TestObserverFlag implements Observer {
     private boolean flag = false;

    @Override
    public void update(DiskRun run) {
        flag = true;
    }

    public boolean getFlag() {
        return flag;
    }
}
