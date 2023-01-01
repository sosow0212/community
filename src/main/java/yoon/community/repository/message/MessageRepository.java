package yoon.community.repository.message;

import org.springframework.data.jpa.repository.JpaRepository;
import yoon.community.entity.message.Message;
import yoon.community.entity.member.Member;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByReceiverAndDeletedByReceiverFalseOrderByIdDesc(Member member);
    List<Message> findAllBySenderAndDeletedBySenderFalseOrderByIdDesc(Member member);
}
