
package potes.server;

import javax.jdo.Extent;
import javax.jdo.PersistenceManager;

import potes.model.Credentials;

public class CredentialsRepository {

	public Credentials get() {
		PersistenceManager pm = PersistanceManagerFactorySingleton.getInstance();
		try {
		    Extent<Credentials> extent = pm.getExtent(Credentials.class, false);
		    Credentials creds = null;
		    for (Credentials c : extent) {
		    	creds = c;
		    }
		    extent.closeAll();
			
		    return creds;
		} finally {
			pm.close();
		}
	}

	public void create(Credentials o) {
		PersistenceManager pm = PersistanceManagerFactorySingleton.getInstance();
		try {
			Credentials existing = get();
			if (existing != null) {
				pm.deletePersistent(existing);
			}
		    pm.makePersistent(o);
		} finally {
			pm.close();
		}
	}

}
