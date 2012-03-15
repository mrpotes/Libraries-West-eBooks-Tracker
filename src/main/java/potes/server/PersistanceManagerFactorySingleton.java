package potes.server;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

class PersistanceManagerFactorySingleton {

	private static class Instance {
		private static final PersistenceManagerFactory pmfInstance = JDOHelper.getPersistenceManagerFactory("transactions-optional");
	}
	
	public static PersistenceManager getInstance() {
		return Instance.pmfInstance.getPersistenceManager();
	}
}
