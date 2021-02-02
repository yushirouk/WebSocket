package com.polling.persistence.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.polling.persistence.model.PassSession;

public interface PassSessionRedisRepository extends CrudRepository<PassSession, String> {
	
	public List<PassSession> findByUpdateYn(Boolean updateYn);
	public List<PassSession> findByUpdateYnAndServerNode(Boolean updateYn, String serverNode);

}
