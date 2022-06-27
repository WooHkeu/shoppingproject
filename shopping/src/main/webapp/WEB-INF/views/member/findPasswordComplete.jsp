<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
  <title>새 패스워드 입력</title>
  <meta charset="utf-8">
</head>
<body>
<div class="container">

<h1 class="col-sm-offset-2 col-sm-10">새 패스워드 입력</h1>
  <form class="form-horizontal" 
        action="/member/new/password"
        method="post">
        
    <div class="form-group">
      <label class="control-label col-sm-2" for="id">아이디</label>
      <div class="col-sm-4">
        <input type="text" class="form-control" id="id" value="${id}"
        name="id" required="required" readonly>
      </div>
    </div>

    <div class="form-group">
      <label class="control-label col-sm-2" for="passwd">새 패스워드</label>
      <div class="col-sm-4">
        <input type="password" class="form-control" id="passwd"
               name="passwd" required="required">
      </div>
    </div>

    <div class="form-group">
      <label class="control-label col-sm-2" for="id">새 패스워드 확인</label>
      <div class="col-sm-4">
        <input type="password" class="form-control" id="newPasswd"
               name="newPasswd" required="required">
      </div>
    </div>

    <div class="form-group">
      <div class="col-sm-offset-2 col-sm-8">
        <button type="button" class="btn btn-default" onclick="location.href='../../../login'">로그인</button>
        <button type="submit" class="btn btn-default">새 패스워드 수정</button>
      </div>
    </div>
  </form>

</div>
</body>
</html>