package com.polling.controller;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

import com.polling.longpolling.LongPollingEventSimulator;
import com.polling.longpolling.LongPollingSession;
import com.polling.persistence.dao.PassSessionRedisRepository;
import com.polling.persistence.model.PassSession;


@Controller
public class PollingController {

	private static final Logger LOGGER = Logger.getLogger(PollingController.class.getName());

	@Autowired
	LongPollingEventSimulator simulator;
	
	@Autowired
    private PassSessionRedisRepository redisRepository;
	
	@Value("${server.nodeId}")
	private String nodeId;

	
	/**
	 * 롱폴링 처리를 받아서 request를 queue에 담는다.
	 * @param cpCd
	 * @param txSeqNo
	 * @return DeferredResult
	 */
	@RequestMapping("/register/{cpCd}/{txSeqNo}")
	@ResponseBody
	public DeferredResult<String> registerClient(@PathVariable("cpCd") final String cpCd, @PathVariable("txSeqNo") final String txSeqNo) {
		LOGGER.log(Level.INFO, "Registering client for cpCd + txSeqNo: " + cpCd + "_" + txSeqNo);
		String tmp = cpCd + "_" + txSeqNo;
		
		final DeferredResult<String> deferredResult = new DeferredResult<>();
		// 지연된 request요청을 이벤트 큐에 담는다.
		simulator.getPollingQueue().add(new LongPollingSession(tmp, deferredResult));
		
		//redis에 등록
		redisRepository.save(PassSession.builder().sessionId(tmp).serverNode(nodeId).updateYn(false).build());
	
		return deferredResult; //아직 값이 담겨있지 않으므로 클라이언트에 응답을 지연시킨다(pending...).
	}

	/**
	 * 통신사 이벤트를 받아서 처리한다.
	 * @param cpCd
	 * @param txSeqNo
	 * @return
	 */
	@RequestMapping("/simulate/{cpCd}/{txSeqNo}")
	@ResponseBody
	public String simulateEvent(@PathVariable("cpCd") final String cpCd, @PathVariable("txSeqNo") final String txSeqNo) {
		LOGGER.log(Level.INFO, "Simulating event for session id: " + cpCd + "_" + txSeqNo);
		String tmp = cpCd + "_" + txSeqNo;
		
		simulator.simulateIncomingNotification(tmp);
		
		return "nothing....";
	}
}
