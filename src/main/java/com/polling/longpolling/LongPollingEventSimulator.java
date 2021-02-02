package com.polling.longpolling;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.polling.core.handler.SocketHandler;

@Service
public class LongPollingEventSimulator {

	private static final Logger LOGGER = Logger.getLogger(LongPollingEventSimulator.class.getName());
	private final BlockingQueue<LongPollingSession> longPollingQueue = new ArrayBlockingQueue<LongPollingSession>(100);

	@Value("${server.nodeId}")
	private String nodeId;
	
	@Autowired
	SocketHandler handler;

	// 이벤트 발생시 call
	public void simulateIncomingNotification(final String sessionId) {

		//TODO REDIS에서 sessionId 정보를 찾는다. key: sessionId value: json nodeId, port
		
		
		
		//1번서버에서 발생한 요청이라고 가정...
		String redisNodId = "server1";
		String redisPort = "8090";

//		// request가 발생한 서버에 이벤트발생을 알린다.
//		if (nodeId.equals(redisNodId)) {
//			// 현재 서버에서 받았다면 직접처리
//			simulateOutgoingNotification(sessionId);
//		} else {
//			// 아닌경우 해당서버로 이벤트 발생전달.
//			//simulateOutgoingNotification(sessionId);
//			HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(); factory.setReadTimeout(5000); // 읽기시간초과, ms 
//			factory.setConnectTimeout(3000); // 연결시간초과, ms 
//			//factory.setHttpClient(httpClient); // 동기실행에 사용될 HttpClient 세팅 
//			HttpClient httpClient = HttpClientBuilder.create().setMaxConnTotal(100) // connection pool 적용 
//					.setMaxConnPerRoute(5) // connection pool 적용 
//					.build(); factory.setHttpClient(httpClient); // 동기실행에 사용될 HttpClient 세팅
//
//			factory.setHttpClient(httpClient);
//			
//			RestTemplate restTemplate = new RestTemplate(factory);
//			
//			String [] tmp =  sessionId.split("_");
//			
//			String url = "http://localhost:" + redisPort + "/simulate/" + tmp[0] +"/" + tmp[1]; 
//			String tmp2 = restTemplate.getForObject(url, String.class);
//			System.out.println("전송했다...................." + tmp2);
//		}
	}
	
//	public void simulateOutgoingNotification(Iterable<PassSession> iterablePoint) {
//		
//		WebSocketSession wss = handler.getSessionMap().get(sessionId);
//		
//		while(iterablePoint.iterator().hasNext()) {
//			
//			
//			handler.getSessionMap().containsKey(iterablePoint.iterator().next().getId());
//			
//			if(handler.getSessionMap().containsKey(iterablePoint.iterator().next().getId())) {
//				
//			}
//		}
//		
//	}

	// 이벤트 값을 클라이언트에게 전달.
	public void simulateOutgoingNotification(ArrayList<String> sessionIds) {
		System.out.println("simulateOutgoingNotification sessionId:" + sessionIds.size());

		sessionIds.stream().forEach((sessionId) -> 
			{
				WebSocketSession wss = handler.getSessionMap().get(sessionId);
		
				// 웹소켓인지?
				if (wss != null) {
					try {
						wss.sendMessage(new TextMessage("웹소켓 이벤트가 도착했습니다"));
						wss.close(); // 서버에서 종료한다.
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					if (!getPollingQueue().isEmpty()) {
		
						// 동일한 ID를 가지고 있는 값을 찾아서
						getPollingQueue().stream().filter(e -> e.getSessionId().equals(sessionId))
								// DeferredResult 에 값을 채워준다. (Http response 발생)
								.forEach((final LongPollingSession lps) -> {
									try {
										lps.getDeferredResult().setResult("롱폴링 이벤트가 도착했습니다");
									} catch (Exception e) {
										throw new RuntimeException();
									}
								});
						// 큐에서 처리된 DeferredResult를 삭제한다.
						getPollingQueue().removeIf(e -> e.getDeferredResult().isSetOrExpired());
					} else {
						System.out.println("대기중인 request 정보 없음.");
					}
				}
			}
		);
	}

	public BlockingQueue<LongPollingSession> getPollingQueue() {
		return longPollingQueue;
	}
}