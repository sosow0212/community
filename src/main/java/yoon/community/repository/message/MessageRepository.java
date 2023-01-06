package yoon.community.repository.message;

import org.springframework.data.jpa.repository.JpaRepository;
import yoon.community.domain.message.Message;
import yoon.community.domain.member.Member;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByReceiverAndDeletedByReceiverFalseOrderByIdDesc(Member member);
    List<Message> findAllBySenderAndDeletedBySenderFalseOrderByIdDesc(Member member);
}
