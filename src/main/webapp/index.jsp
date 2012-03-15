<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>EBooks</title>
</head>
<body>
  <ul>
    <c:forEach var="book" items="${books}">
      <li>
        <a href="http://librarieswest.libraryebooks.co.uk/site/EB/ebooks/catpage3.asp?isbn=${book.isbn}">${book.title}</a>
      </li>
    </c:forEach>
  </ul>
</body>
</html>
