package hello.springtx.apply;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.PostConstruct;

@SpringBootTest
public class InitTTest {

    @Autowired
    Hello hello;

    @Test
    void go() {

    }

    @TestConfiguration
    static class InitTxTestConfig {
        @Bean
        Hello hello() {
            return new Hello();
        }
    }

    @Slf4j
    static class Hello {

        @PostConstruct
        @Transactional
        public void initV1() {
            // 초기화 코드가 먼저 호출되고, 그 다음에 트랜잭션 AOP가 적용된다.
            boolean isActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("Hello init @Postconstruct tx active={}", isActive);
        }

        @EventListener(value = ApplicationReadyEvent.class)
        @Transactional
        public void init2() {
            // 스프링 컨테이너가 완성된 후에 호출
            boolean isActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("Hello init ApplicationReadyEvent tx active={}", isActive);
        }
    }

}
