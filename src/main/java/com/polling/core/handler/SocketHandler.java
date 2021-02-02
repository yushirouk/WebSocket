package com.polling.core.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import lombok.Getter;

@Component
@Getter
public class SocketHandler extends AbstractWebSocketHandler {

	HashMap<String, WebSocketSession> sessionMap = new HashMap<>();

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		super.afterConnectionEstablished(session);

		String userId = getId(session);
		sessionMap.put(userId, session);
		System.out.println("userId :" + userId);
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		sessionMap.remove(session.getId());
		super.afterConnectionClosed(session, status);
	}

	private String getId(WebSocketSession session) {
		String[] test = session.getUri().getQuery().split("&");
		Map<String, String> map = new HashMap<String, String>();

		for (String tmpStr : test) {
			System.out.println(tmpStr);

			String[] tmplist = tmpStr.split("=");
			if (tmplist.length == 2) {
				map.put(tmplist[0], tmplist[1]);
			}
		}

		return map.get("cpCd") + "_" + map.get("txSeqNo");
	}
}
