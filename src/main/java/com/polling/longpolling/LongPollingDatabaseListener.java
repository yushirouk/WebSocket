package com.polling.longpolling;


import java.util.ArrayList;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.polling.persistence.dao.PassSessionRedisRepository;

@Component
public class LongPollingDatabaseListener {

	private static final Logger LOGGER = Logger.getLogger(LongPollingDatabaseListener.class.getName());

	@Autowired
	private PassSessionRedisRepository redisRepository;
	
	@Autowired
	LongPollingEventSimulator simulator;
	
	@Value("${server.nodeId}")
	private String nodeId;

	//5초마다 들어온 이벤트가 있는지 감지한다..
	@Scheduled(fixedRate = 5000)
	public void checkNotifications() {
		System.out.println("--------------------------------------------------------------------------");
		System.out.println("DB감지중....");
		
		// 현재 Redis에 통신사이벤트가 발생한 것을 찾는다.
		if (redisRepository.findByUpdateYnAndServerNode(true, nodeId).size() > 0) {
			System.out.println("통신사 이벤트 발생 확인");
			
			ArrayList<String> sessionIdList = new ArrayList<String>();
			redisRepository.findByUpdateYnAndServerNode(true, nodeId).stream().forEach((e) ->
					{//세션에서 ID를 꺼내 별도의 sessionId list를 만들고 원 list에서는 제거.
						sessionIdList.add(e.getSessionId());
						redisRepository.deleteById(e.getSessionId());
					}
				);
					
			simulator.simulateOutgoingNotification(sessionIdList);
		}
	}

}
