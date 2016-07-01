package org.mobicents.cluster.test;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.mobicents.cache.MobicentsCache;
import org.mobicents.cluster.DefaultMobicentsCluster;
import org.mobicents.cluster.election.DefaultClusterElector;
import org.mobicents.timers.timer.FaultTolerantTimer;

@ManagedBean
public class InitMBean {

	@Resource(lookup="java:jboss/infinispan/container/singleton")
	private org.infinispan.manager.EmbeddedCacheManager container;
	
	@Resource(lookup="java:jboss/TransactionManager")
	private javax.transaction.TransactionManager transactionManager;
	
	//private org.infinispan.Cache cache;
	private MobicentsCache mc;
	private DefaultClusterElector de;
	private DefaultMobicentsCluster dmc;
	private FaultTolerantTimer ft;
	
	@PostConstruct
	public void start() {
		mc = new MobicentsCache(container);
		de = new DefaultClusterElector();
		dmc = new DefaultMobicentsCluster(mc, transactionManager, de);
		dmc.startCluster();
		ft = new FaultTolerantTimer("timer",dmc, (byte)10, transactionManager);
	}
	
	
	
	public javax.transaction.TransactionManager getTManager(){
		
		return transactionManager;
		
	}
	
	public FaultTolerantTimer getTimer(){
		
		return ft;
		
	}
	
	
}