package yoon.community.repository.message;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import yoon.community.domain.member.Member;
import yoon.community.domain.message.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findAllByReceiverAndDeletedByReceiverFalseOrderByIdDesc(Member member);

    List<Message> findAllBySenderAndDeletedBySenderFalseOrderByIdDesc(Member member);
}
