package yoon.community.domain.comment;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import yoon.community.domain.board.Board;
import yoon.community.domain.common.EntityDate;
import yoon.community.domain.member.Member;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Comment extends EntityDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Lob
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Board board;

    public Comment(final String content, final Member member, final Board board) {
        this.content = content;
        this.member = member;
        this.board = board;
    }

    public boolean isOwnComment(final Member member) {
        return this.member.equals(member);
    }
}
