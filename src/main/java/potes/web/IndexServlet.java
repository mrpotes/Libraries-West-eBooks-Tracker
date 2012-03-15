
package potes.web;

import java.io.IOException;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Script;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import potes.model.Credentials;
import potes.model.EBook;
import potes.server.BookRepository;
import potes.server.CredentialsRepository;

import com.gargoylesoftware.htmlunit.Cache;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

public class IndexServlet extends HttpServlet {

	private static final Pattern ISBN_PATTERN = Pattern.compile("(\\?|&)isbn=([^&]+)");
	private static final Logger log = LoggerFactory
			.getLogger(IndexServlet.class);

	private BookRepository bookRepository = new BookRepository();
	private CredentialsRepository credentialsRepository = new CredentialsRepository();
	
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		if (log.isDebugEnabled()) {
			log.debug("doGet");
		}

		Credentials c = credentialsRepository.get();
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		if ((c == null && username == null) ||  request.getParameter("relogin") != null) {
			forward(request, response, "login.jsp");
			return;
		} else if (c == null)
		
		if (username != null && password != null) {
			c = new Credentials();
			c.setUsername(username);
			c.setPassword(password);
			credentialsRepository.create(c);
		}
		
		Collection<EBook> books = bookRepository.getAll();

		final WebClient client = new WebClient();
		client.setThrowExceptionOnFailingStatusCode(false);
		client.setJavaScriptEnabled(false);
		client.setCache(new Cache() {
			@Override
			public Object getCachedObject(WebRequest request) {
				if (request.getUrl().toExternalForm().endsWith("/jquery-1.4.4.js")) {
					return new Script() {
						
						@Override
						public Object exec(Context cx, Scriptable scope) {
							return null;
						}
						
					};
				}
				return super.getCachedObject(request);
			}
		});
		HtmlPage page = client.getPage("http://librarieswest.libraryebooks.co.uk");
		
		HtmlTextInput usernameField = page.getElementByName("login_username");
		usernameField.setValueAttribute(c.getUsername());
		HtmlPasswordInput passwordField = page.getElementByName("login_password");
		passwordField.setValueAttribute(c.getPassword());
		
		page = ((HtmlInput)page.getElementByName("submitbutton")).click();
		page = page.getAnchorByText("Latest Arrivals").click();
		
		for (HtmlAnchor anchor : page.getAnchors()) {
			if (anchor.getHrefAttribute().contains("catpage3.asp") && anchor.getChildNodes().size() == 1 && anchor.getFirstChild() instanceof DomText) {
				Matcher matcher = ISBN_PATTERN.matcher(anchor.getHrefAttribute());
				if (matcher.matches()) {
					String isbn = matcher.group(2);
					EBook book = new EBook();
					book.setIsbn(isbn);
					book.setTitle(anchor.getTextContent().trim());
					if (!books.contains(book)) {
						createBook(books, book);
					}
				}
			}
		}
		
		// get
		request.setAttribute("books", books);

		if (log.isDebugEnabled()) {
			log.debug("messages: " + books);
		}

		forward(request, response, "index.jsp");
	}

	private void createBook(Collection<EBook> books, EBook book) {
		bookRepository.create(book);
		books.add(book);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * Forwards request and response to given path. Handles any exceptions
	 * caused by forward target by printing them to logger.
	 * 
	 * @param request 
	 * @param response
	 * @param path 
	 */
	protected void forward(HttpServletRequest request,
			HttpServletResponse response, String path) {
		try {
			RequestDispatcher rd = request.getRequestDispatcher(path);
			rd.forward(request, response);
		} catch (Throwable tr) {
			if (log.isErrorEnabled()) {
				log.error("Cought Exception: " + tr.getMessage());
				log.debug("StackTrace:", tr);
			}
		}
	}
}
