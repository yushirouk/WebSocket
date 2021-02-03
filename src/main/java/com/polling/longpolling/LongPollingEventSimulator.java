package com.polling.longpolling;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.polling.core.handler.SocketHandler;
import com.polling.persistence.dao.PassSessionRedisRepository;
import com.polling.persistence.model.PassSession;

@Service
public class LongPollingEventSimulator {

	private static final Logger LOGGER = Logger.getLogger(LongPollingEventSimulator.class.getName());
	private final BlockingQueue<LongPollingSession> longPollingQueue = new ArrayBlockingQueue<LongPollingSession>(100);

	@Value("${server.nodeId}")
	private String nodeId;
	
	@Autowired
	SocketHandler handler;
	
	@Autowired
    private PassSessionRedisRepository pointRedisRepository;

	// 이벤트 발생시 call
	public void simulateIncomingNotification(final String sessionId) {
		LOGGER.log(Level.INFO, "simulateIncomingNotification called :" + sessionId);
		
		//TODO redis update
		if(pointRedisRepository.findById(sessionId).isPresent()) {
			PassSession passSessionUpdate = pointRedisRepository.findById(sessionId).get();
			passSessionUpdate.setUpdateYn(true);
			pointRedisRepository.save(passSessionUpdate);
		}
		LOGGER.log(Level.INFO, "simulateIncomingNotification end");
	}
	
	// 이벤트 값을 클라이언트에게 전달.
	public void simulateOutgoingNotification(ArrayList<String> sessionIds) {
		System.out.println("simulateOutgoingNotification sessionId.size():" + sessionIds.size());

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