package edu.touro.mco152.bm;

import edu.touro.mco152.bm.externalsys.SlackManager;
import edu.touro.mco152.bm.persist.DiskRun;

/**
 * Observer that gets a run when it is complete and based on certain logic responds accordingly
 */
public class RulesObserver implements Observer{
    @Override
    public void update(DiskRun run) {
        if(run.getRunMax() > (run.getRunAvg() *1.03) && run.getIoMode().equals(DiskRun.IOMode.READ)){
            SlackManager slackmgr = new SlackManager("BadBM");
            Boolean worked = slackmgr.postMsg2OurChannel(":shocked_face_with_exploding_head: :face_with_peeking_eye: Max time of the read benchmark has exceeded the average by 3% or more :loudspeaker: Houston we have a problem!");
        }
    }
}
