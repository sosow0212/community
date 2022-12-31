package yoon.community.dto.board;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import yoon.community.entity.board.Board;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardSimpleDto {
    // 메인 페이지에서 보여지는 게시글 정보
    private Long id;
    private String title;
    private String nickname;
    private int liked;
    private int favorited;

    public BoardSimpleDto toDto(Board board) {
        return new BoardSimpleDto(board.getId() ,board.getTitle(), board.getUser().getNickname(), board.getLiked(), board.getFavorited());
    }

}
