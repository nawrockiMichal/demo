package nawrocki.demo.service;

import nawrocki.demo.model.ContactType;
import nawrocki.demo.model.Message;
import nawrocki.demo.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message create(Message message) {
        message.setId(null);
        message.setDate(new Date());
        return messageRepository.save(message);
    }

    public Page<Message> findAll(Pageable pageable, ContactType type) {

        if (type != null) {
            return messageRepository.findAllByType(type, pageable);
        }
        return messageRepository.findAll(pageable);
    }
}
