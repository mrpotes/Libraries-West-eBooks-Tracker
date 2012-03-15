
package potes.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.jdo.Extent;
import javax.jdo.PersistenceManager;

import potes.model.EBook;

public class BookRepository {

	public Collection<EBook> getAll() {
		PersistenceManager pm = PersistanceManagerFactorySingleton.getInstance();
		try {
			List<EBook> books = new ArrayList<EBook>();
		    Extent<EBook> extent = pm.getExtent(EBook.class, false);
		    for (EBook book : extent) {
		        books.add(book);
		    }
		    extent.closeAll();
			
		    return books;
		} finally {
			pm.close();
		}
	}

	public void create(EBook message) {
		PersistenceManager pm = PersistanceManagerFactorySingleton.getInstance();
		try {
		    pm.makePersistent(message);
		} finally {
			pm.close();
		}
	}

}
