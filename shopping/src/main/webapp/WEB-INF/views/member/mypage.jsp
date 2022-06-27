<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <title>Bootstrap Example</title>
  <meta charset="utf-8">
</head>
<body>
 
<div class="container">
  <div class="row">
  <div class="col-sm-3">
  <h3><span class="glyphicon glyphicon-user"></span> 나의 정보</h3>
      <div id="image_preview">
        <img id="output" class="img-rounded" src="/member/storage/${dto.fname}" style="width:280px">${dto.fname}
      </div>
      <form action="updateFile"
            method="post"
            enctype="multipart/form-data">
          <div style="display: none">
            <input type="text" class="form-control" id="id"
                   name="oldfile" value="${dto.fname}">
          </div>
        <div class="form-group">
            <div style="text-align: end">
                <input type="file" class="form-control" id="fnameMF"
                       name="fnameMF" accept=".jpg,.gif,.png" onchange="resizeImage()">
                <button type="submit" class="btn" style="margin-top: 0.25rem;">프로필 사진 수정</button>
            </div>
        </div>
      </form>
  <div class="caption">
    <p>ID:${dto.id}, 성명:${dto.mname}</p>
    <p>배송지:(${dto.zipcode })${dto.address1 }  ${dto.address2}</p>
  </div>
  </div>
  <div class="col-sm-6">
  <h3><span class="glyphicon glyphicon-gift"></span> 주문 내역 및 리뷰 작성</h3>
  <ul class="list-group">
   <c:choose>
    <c:when test="${empty dto.list }">주문한 상품이 없습니다.</c:when>
    
    <c:otherwise>
    <c:forEach var="order" items="${dto.list}" >
    <p class='h4'>${fn:substring(order.odate,0,10)}<p>
       <li class="list-group-item">결제금액 : ${order.total} 원, 주문상태 : ${order.ostate}
    <c:forEach var="detail" items="${order.list}">
    <li class="list-group-item">${detail.pname}, ${detail.quantity}개
     <a href="review/create/${detail.contentsno}"><span class="badge">Rivew</span></a></li>
    </c:forEach>
</c:forEach>
    </c:otherwise>
    </c:choose>
  </ul>
  </div>
  </div>
</div>

<%--<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.js"></script>--%>
<script type="text/javascript">

    function resizeImage() {
        var filesToUpload = document.getElementById('fnameMF').files;
        var file = filesToUpload[0];

        // 문서내에 img 객체를 생성합니다
        var img = document.createElement("img");
        // 파일을 읽을 수 있는 File Reader 를 생성합니다
        var reader = new FileReader();

        // 파일이 읽혀지면 아래 함수가 실행됩니다
        reader.onload = function (e) {
            img.src = e.target.result;
            // HTML5 canvas 객체를 생성합니다
            var canvas = document.createElement("canvas");
            var ctx = canvas.getContext("2d");
            // 캔버스에 업로드된 이미지를 그려줍니다
            ctx.drawImage(img, 0, 0);
            // 최대폭을 400 으로 정했다고 가정했을때
            // 최대폭을 넘어가는 경우 canvas 크기를 변경해 줍니다.
            // var MAX_WIDTH = 150;
            // var MAX_HEIGHT = 150;
            var width = img.width;
            var height = img.height;
            // if (width > height) {
            //     if (width > MAX_WIDTH) {
            //         height *= MAX_WIDTH / width;
            //         width = MAX_WIDTH;
            //     }
            // } else {
            //     if (height > MAX_HEIGHT) {
            //         width *= MAX_HEIGHT / height;
            //         height = MAX_HEIGHT;
            //     }
            // }
            width = 434;
            height = 291;
            canvas.width = width;
            canvas.height = height;
            // canvas에 변경된 크기의 이미지를 다시 그려줍니다.
            var ctx = canvas.getContext("2d");
            ctx.drawImage(img, 0, 0, width, height);
            // canvas 에 있는 이미지를 img 태그로 넣어줍니다
            var dataurl = canvas.toDataURL("image/png");
            document.getElementById('output').src = dataurl;
        }
        reader.readAsDataURL(file);
    }

    // 이미지 업로드
    $('#fnameMF').on('change', function () {
        ext = $(this).val().split('.').pop().toLowerCase(); //확장자
        //배열에 추출한 확장자가 존재하는지 체크
        if ($.inArray(ext, ['gif', 'png', 'jpg', 'jpeg']) == -1) {
            window.alert('이미지 파일이 아닙니다! (gif, png, jpg, jpeg 만 업로드 가능)');
        } else {
            file = $('#fnameMF').prop("files")[0];
            blobURL = window.URL.createObjectURL(file);
            $('#image_preview fnameMF').attr('src', blobURL);
            $('#image_preview').slideDown(); //업로드한 이미지 미리보기
            resizeImage();
        }
    });



</script>
</body>
</html>
