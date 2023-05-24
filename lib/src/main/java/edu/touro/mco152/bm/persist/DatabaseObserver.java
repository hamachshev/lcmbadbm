package edu.touro.mco152.bm.persist;

import edu.touro.mco152.bm.Observer;
import edu.touro.mco152.bm.TestUtil;
import jakarta.persistence.EntityManager;

public class DatabaseObserver implements Observer {

    @Override
    public void update(DiskRun run) {
        EntityManager em = EM.getEntityManager();
        em.getTransaction().begin();
//                            em.persist(run);
        // instead of persisting bc not working for this assignment, sent to TestUtil
        TestUtil.setDiskRun(run);
        em.getTransaction().commit();
    }
}
