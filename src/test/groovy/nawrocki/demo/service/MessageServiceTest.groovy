package nawrocki.demo.service

import nawrocki.demo.model.ContactType
import nawrocki.demo.model.Message
import nawrocki.demo.repository.MessageRepository
import org.springframework.data.domain.Pageable
import spock.lang.Specification

class MessageServiceTest extends Specification {

    def "When user create new message Id need to be null and date need to be set  "() {

        def messageRepository = Mock(MessageRepository)
        def message = Mock(Message)
        def messageService = new MessageService(messageRepository)

        when:
        messageService.create(message)

        then:
        1 * message.setDate(_)
        1 * message.setId(null)
        1 * messageRepository.save(_)

    }

    def "When user get massages with specified type findAllByType need to be executed "() {

        def messageRepository = Mock(MessageRepository)
        def pageable = Mock(Pageable)
        def messageService = new MessageService(messageRepository)

        when:
        messageService.findAll(pageable, type)

        then:
        findAllByType * messageRepository.findAllByType(type, pageable)
        findAll * messageRepository.findAll(pageable)

        where:
        type                  || findAll || findAllByType
        null                  || 1       || 0
        ContactType.GENERAL   || 0       || 1
        ContactType.MARKETING || 0       || 1
        ContactType.SUPPORT   || 0       || 1

    }
}
