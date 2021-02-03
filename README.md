# WebSocket
WebSocket + redis

1. 단독테스트 (본 프로젝트 내의 embedded redis사용)
  1) application-local.properties를 configuration으로 프로젝트 기동.
  2) http://localhost:8090/ 접속 (다중접속테스트를 위해 2개이상을 띄워서 진행하는것을 권장)
  3) '요청시작' 클릭시 브라우저 환경에 따라 Websocket또는 롱폴링 시작
  4) 별도의 창에서 http://localhost:8090/comm 접속
  5) 접속중인 사용자의 거래일련번호를 복사하여 통신사이벤트 거래일련번호에 입력 후 '통신사이벤트' 클릭.
  6) 접속중인 사용자 창에서 이벤트 메세지 확인.
  
2. 통합환경테스트 (서버 2대, 별도의 redis서버 1대)
  1) embedded redis 서버를 별도로 생성필요.
  2) 본 프로젝트 내용을 가진 boot 프로젝트 2개 생성.
  3) application.properties의 redis 정보 및 server.nodeId,server.port 정보 수정
  4) application.properties를 configuration으로 프로젝트 기동.
  5) 이후 단독테스트와 내용 동일함.
