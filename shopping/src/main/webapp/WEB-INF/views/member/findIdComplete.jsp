<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
  <title>아이디 찾기 성공</title>
  <meta charset="utf-8">
</head>
<body>
<div class="container">

<h1 class="col-sm-offset-2 col-sm-10">아이디 찾기 성공</h1>
  <form class="form-horizontal" 
        action="/member/find/id"
        method="post">
        
    <div class="form-group">
      <label class="control-label col-sm-2" for="id">찾은 아이디</label>
      <div class="col-sm-4">
        <input type="text" class="form-control" id="id" value="${id}"
        name="id" required="required" readonly>
      </div>
    </div>
    <div class="form-group">
      <div class="col-sm-offset-2 col-sm-8">
        <button type="button" class="btn btn-default" onclick="location.href='../../../login'">로그인</button>
        <button type="button" class="btn btn-default"  onclick="location.href='../../../find/password'">비밀번호 찾기</button>
      </div>
    </div>
  </form>

</div>
</body>
</html>