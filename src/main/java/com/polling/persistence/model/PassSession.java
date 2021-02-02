package com.polling.persistence.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@RedisHash("passSession")
public class PassSession implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2799880891132301047L;
	@Id
	private String sessionId;
	
	@Indexed
	@Setter
	private boolean updateYn;
	
	@Indexed
	private String serverNode;

	@Builder
	public PassSession(String sessionId, Boolean updateYn, String serverNode) {
		this.sessionId = sessionId;
		this.updateYn = updateYn;
		this.serverNode = serverNode;
	}
}
