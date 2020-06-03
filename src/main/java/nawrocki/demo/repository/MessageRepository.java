package nawrocki.demo.repository;

import nawrocki.demo.model.ContactType;
import nawrocki.demo.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    Page<Message> findAllByType(ContactType type, Pageable pageable);
}
