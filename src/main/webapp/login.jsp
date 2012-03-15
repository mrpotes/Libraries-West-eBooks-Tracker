<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Welcome</title>
</head>
<body>
  <p>
  Enter login details:
  </p>
  <form action="index" method="post">
      <input type="text" name="username" /><br/>
      <input type="text" name="password" /><br/>
      <input type="submit" value="Create" />
  </form>
</body>
</html>
