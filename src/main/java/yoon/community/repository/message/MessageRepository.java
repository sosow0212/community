package yoon.community.repository.message;

import org.springframework.data.jpa.repository.JpaRepository;
import yoon.community.entity.message.Message;
import yoon.community.entity.user.User;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByReceiverAndDeletedByReceiverFalseOrderByIdDesc(User user);
    List<Message> findAllBySenderAndDeletedBySenderFalseOrderByIdDesc(User user);
}
