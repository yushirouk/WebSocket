package com.polling.persistence.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import lombok.Builder;
import lombok.Getter;

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
	private boolean updateYn;

	@Builder
	public PassSession(String sessionId, Boolean updateYn) {
		this.sessionId = sessionId;
		this.updateYn = updateYn;
	}
}
