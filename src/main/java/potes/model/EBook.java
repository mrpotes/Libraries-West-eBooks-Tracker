
package potes.model;

import java.io.Serializable;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.apache.commons.lang.builder.EqualsBuilder;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class EBook implements Serializable {

    private static final long serialVersionUID = 1L;

    @PrimaryKey
    @Persistent
    private String isbn;

    @Persistent
	private String title;
	
    public String getIsbn() {
		return isbn;
	}
    
    public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
    
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof EBook) {
			return new EqualsBuilder().append(isbn, ((EBook) obj).isbn).isEquals();
		}
		return false;
	}
}
