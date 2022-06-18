package yoon.community.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yoon.community.entity.message.Message;
import yoon.community.entity.user.User;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<Message> findAllByReceiverAndDeletedByReceiverFalseOrderByIdDesc(User user);
    List<Message> findAllBySenderAndDeletedBySenderFalseOrderByIdDesc(User user);
}
