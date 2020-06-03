package nawrocki.demo.controller

import nawrocki.demo.service.MessageService
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import spock.mock.DetachedMockFactory

@TestConfiguration
class MessageControllerConfig {
    DetachedMockFactory factory = new DetachedMockFactory()

    @Bean
    MessageService messageService() {
        factory.Mock(MessageService)
    }
}
