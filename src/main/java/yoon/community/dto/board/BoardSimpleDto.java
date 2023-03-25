package yoon.community.dto.board;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import yoon.community.domain.board.Board;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardSimpleDto {
    private Long id;
    private String title;
    private String nickname;
    private int liked;
    private int favorited;

    public static BoardSimpleDto toDto(Board board) {
        return new BoardSimpleDto(board.getId(), board.getTitle(), board.getMember().getNickname(), board.getLiked(),
                board.getFavorited());
    }
}
