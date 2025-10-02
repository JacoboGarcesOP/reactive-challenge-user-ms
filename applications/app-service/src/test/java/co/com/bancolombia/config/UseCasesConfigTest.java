package co.com.bancolombia.config;

import co.com.bancolombia.model.user.gateway.BootcampGateway;
import co.com.bancolombia.model.user.gateway.UserGateway;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UseCasesConfigTest {

    @Test
    void testUseCaseBeansExist() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestConfig.class)) {
            String[] beanNames = context.getBeanDefinitionNames();

            boolean useCaseBeanFound = false;
            for (String beanName : beanNames) {
                if (beanName.endsWith("UseCase")) {
                    useCaseBeanFound = true;
                    break;
                }
            }

            assertTrue(useCaseBeanFound, "No beans ending with 'Use Case' were found");
        }
    }

    @Configuration
    @Import(UseCasesConfig.class)
    static class TestConfig {

        @Bean
        public UserGateway userGateway() {
            return new MockUserGateway();
        }

        @Bean
        public BootcampGateway bootcampGateway() {
            return new MockBootcampGateway();
        }

        @Bean
        public MyUseCase myUseCase() {
            return new MyUseCase();
        }
    }

    static class MyUseCase {
        public String execute() {
            return "MyUseCase Test";
        }
    }

    static class MockUserGateway implements UserGateway {
        @Override
        public reactor.core.publisher.Mono<co.com.bancolombia.model.user.User> save(co.com.bancolombia.model.user.User user) {
            return reactor.core.publisher.Mono.empty();
        }

        @Override
        public reactor.core.publisher.Mono<co.com.bancolombia.model.user.User> findById(Long id) {
            return reactor.core.publisher.Mono.empty();
        }

        @Override
        public reactor.core.publisher.Flux<co.com.bancolombia.model.user.User> findByBootcampId(Long bootcampId) {
            return reactor.core.publisher.Flux.empty();
        }

        @Override
        public reactor.core.publisher.Mono<co.com.bancolombia.model.user.User> enrollUserInBootcamp(Long userId, Long bootcampId) {
            return reactor.core.publisher.Mono.empty();
        }

        @Override
        public reactor.core.publisher.Mono<Boolean> existsUserBootcampRelation(Long userId, Long bootcampId) {
            return reactor.core.publisher.Mono.empty();
        }

        @Override
        public reactor.core.publisher.Mono<Long> countUserBootcamps(Long userId) {
            return reactor.core.publisher.Mono.empty();
        }

        @Override
        public reactor.core.publisher.Flux<Long> findUserBootcampIds(Long userId) {
            return reactor.core.publisher.Flux.empty();
        }
    }

    static class MockBootcampGateway implements BootcampGateway {
        @Override
        public reactor.core.publisher.Mono<co.com.bancolombia.model.user.Bootcamp> findById(Long id) {
            return reactor.core.publisher.Mono.empty();
        }
    }
}