<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>Insert title here</title>
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript">
$(function (){
    $( document ).ready( function() {

         $( '.btn__event' ).click( function( event ) {
             event.preventDefault();
             simulate();
         });
         
		//이벤트 발생
        function simulate(){
            $.ajax({
                url: "simulate/" + $('#cpCd').val() + "/" + $('#txSeqNo').val(),
                success: function(data, status, jqXHR) {
                    console.log("Event simulated successfully.");
                },
                type:'POST'
            });
        }

    }); // (document).ready
});
</script>
</head>
<body>
	<div>
	<form id="initForm" name="initForm" method="post" >
		통신사이벤트 회원사코드  : <input type="text" id="cpCd" name="cpCd" value="V06880000000"><br/>
		통신사이벤트 거래일련번호: <input type="text" id="txSeqNo" name="txSeqNo" value="">
	</form>
	</div>
	
	<div class="btnArea double">
		<button class="btn btn__event" >통신사이벤트 </button>
	</div>
</body>
</html>