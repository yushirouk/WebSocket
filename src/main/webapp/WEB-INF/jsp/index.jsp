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
jQuery.fn.serializeObject = function() { 
    const obj = null; 
    try { 
        if(this[0].tagName && this[0].tagName.toUpperCase() == "FORM" ) { 
            const arr = this.serializeArray(); 
            if(arr){ obj = {}; 
            jQuery.each(arr, function() { 
                obj[this.name] = this.value; }); 
            } 
        } 
    }catch(e) { 
        alert(e.message); 
    }finally {} 
    return obj;
  }

$(function (){

	const useWebSockets = false;

// 	if(!"WebSocket" in window){
// 		useWebSockets = false;
// 	}

    $( document ).ready( function() {

        $( '.btn__start' ).click( function( event ) {
            event.preventDefault();
            
            if(useWebSockets) {
                const wsUri = 'ws://localhost:8090/ws?txSeqNo=' + $('#txSeqNo').val() + "&cpCd=" + $('#cpCd').val();
                const websocket = new WebSocket(wsUri);

                websocket.onopen = function (evt) {
                    $('.type').text('웹소켓 연결됨.');
                }
                websocket.onerror = function (evt) {
                    useWebSockets = false;
                    // on WebSocket error, gracefully fall back to long polling strategy
                    console.log("WebSocket error, gracefully fall back to long polling strategy.");
                    poll();
                }
                websocket.onmessage = function(event){
            		//alert('통신사 응답 수신 완료! 결과:' + JSON.parse(event.data).statCd);
            		alert(event.data);
            		websocket.close();
            	}
            	websocket.onclose = function (evt){
            		$('.type').text('웹소켓 종료됨.');
            	}
            } else {
                console.log("Calling poll");
                poll();
            }
        });

        /* 반복적으로 서버에 롱폴링을 요청한다.
         * - 서버에서 보낸 데이터가 없을 경우 1분마다 요청
         */
        function poll(){
        	$('.type').text('롱폴링 시작됨.');
            
            $.ajax({
                url: "register/"+$('#cpCd').val() + "/" + $('#txSeqNo').val(),
                success: function(data, status, jqXHR) {
					alert(data);
                    //결과를 받았을 경우 그대로 종료
					$('.type').text('롱폴링 종료됨.');
                },
                error: function(jqXHR, status, errorThrown) {
                    if (status=='timeout') {
                        console.log( 'request timed out.' );
                        poll();
                    }
                    else {
                    	$('.status').text("Http code:" + status);
                        poll();
                    }
                },
                dataType: "text",
                type:'GET',
                timeout: 60000
            });
        }

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
		<input type="text" id="cpCd" name="cpCd" value="<c:out value="${initForm.cpCd}" />">
		<input type="text" id="txSeqNo" name="txSeqNo" value="<c:out value="${initForm.txSeqNo}" />">
	</form>
	<p id="analytics" ></p>
	
		<p>연결방식</p>
		<p class="type"></p>
		<p class="status"></p>
	</div>
	
	<div class="btnArea double">
		<button class="btn btn__start" >폴링요청</button>
<!-- 		<button class="btn btn__send" >send </button> -->
		<button class="btn btn__event" >통신사이벤트 </button>
	</div>
</body>
</html>