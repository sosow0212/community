package yoon.community.entity.comment;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;
import yoon.community.entity.board.Board;
import yoon.community.entity.user.User;

import javax.persistence.*;
import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Data
@Builder
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(nullable = false)
    @Lob
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Board board;


    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDate createDate; // 날짜

    @PrePersist // DB에 INSERT 되기 직전에 실행. 즉 DB에 값을 넣으면 자동으로 실행됨
    public void createDate() {
        this.createDate = LocalDate.now();
    }

    public Comment(String content, User user, Board board) {
        this.content = content;
        this.user = user;
        this.board = board;
    }
}
