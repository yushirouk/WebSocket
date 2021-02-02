package com.polling.core.config;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.polling.persistence.dao.PassSessionRedisRepository;
import com.polling.persistence.model.PassSession;

@EnableAutoConfiguration
@SpringBootTest
@TestPropertySource("classpath:application-local.properties")
public class RedisTest {

    @Autowired
    private PassSessionRedisRepository pointRedisRepository;

    @AfterEach
    public void tearDown() throws Exception {
        pointRedisRepository.deleteAll();
    }

    @Test
    public void 기본_등록_조회기능() {
        //given
        String id = "jojoldu";
        LocalDateTime refreshTime = LocalDateTime.of(2018, 5, 26, 0, 0);
        PassSession point = PassSession.builder()
                .sessionId(id)
                .updateYn(true)
                .serverNode("1")
                .build();
        PassSession point2 = PassSession.builder()
                .sessionId(id + "2")
                .updateYn(false)
                .serverNode("2")
                .build();
        
        PassSession point3 = PassSession.builder()
                .sessionId(id + "3")
                .updateYn(false)
                .serverNode("1")
                .build();
        //when
        pointRedisRepository.save(point);
        pointRedisRepository.save(point2);
        pointRedisRepository.save(point3);

        //then
        PassSession savedPoint = pointRedisRepository.findById(id).get();
        assertEquals(savedPoint.getSessionId(), "jojoldu");
        //assertEquals(savedPoint.isUpdateYn(),false);
        
        System.out.println(savedPoint.getSessionId());
        System.out.println(savedPoint.isUpdateYn());
        System.out.println(pointRedisRepository.findByUpdateYn(false).size());
        System.out.println(pointRedisRepository.findByUpdateYnAndServerNode(false, "1").size());
    }
}
