<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="util" uri="/ELFunctions"%>
<c:set var="root" value="${pageContext.request.contextPath }" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script>
function detail(contentsno) {
	let url = "detail";
	url += "?contentsno=" + contentsno;
	url += "&nowPage=${nowPage}";
	url += "&col=${col}";
	url += "&word=${word}";
	location.href = url;
}
</script>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-sm-3">
				<h4>
					<img src="/svg/box2-heart.svg"> 상품 정보
				</h4>
				<img class="img-rounded" src="/contents/storage/${dto.filename}"
					style="width: 250px">
				<div class="caption">
					<p>상품명:${dto.pname}</p>
					<p>${dto.detail }</p>
				</div>
			</div>
			<div class="col-sm-6">
				<h4>
					<img src="/svg/rulers.svg">  수량
				</h4>
				<ul class="list-group">
					<li class="list-group-item"><c:choose>
							<c:when test="${empty list}">
								<tr>
									<td colspan="6">상품명:${dto.pname}</td>
							</c:when>
							<c:otherwise>

								<c:forEach var="dto" items="${list}">

									<tr>
										<td>${dto.contentsno}</td>
										<td><img src="/contents/storage/${dto.filename}"
											class="img-rounded" width="100px" height="100px"></td>
										<td><a href="javascript:read('${dto.contentsno}')">${dto.pname}</a>
											<c:if test="${util:newImg(fn:substring(dto.rdate,0,10)) }">
												<img src="/images/new.gif">
											</c:if></td>
										<td>${dto.price}</td>
										<td>${dto.rdate}</td>
										<td>${dto.stock}</td>
										<td><a href="./update/${dto.contentsno }"> <span
												class="glyphicon glyphicon-edit"></span>
										</a> / <a href="./delete/${dto.contentsno }"> <span
												class="glyphicon glyphicon-trash"></span>
										</a> / <a href="./updateFile/${dto.contentsno }/${dto.filename}">
												<span class="glyphicon glyphicon-picture"></span>
										</a></td>
									</tr>
								</c:forEach>
							</c:otherwise>
						</c:choose>
					<li class="list-group-item">가격 : ${dto.price }
					<li class="list-group-item">재고 : ${dto.stock }
					<li class="list-group-item"><a
						href="/contents/update/${dto.contentsno}"> <span
							class="glyphicon glyphicon-edit"></span>
					</a> <a href="/contents/delete/${dto.contentsno }"> <span
							class="glyphicon glyphicon-trash"></span>
					</a> <a href="/contents/updateFile/${dto.contentsno }/${dto.filename}">
							<span class="glyphicon glyphicon-picture"></span>
					</a>
				</ul>
			</div>
		</div>
		
	</div>
</body>
</html>