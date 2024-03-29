package yoon.community.dto.board;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import yoon.community.domain.board.Board;

@Data
@AllArgsConstructor
@NotBlank
public class BoardCreateResponse {

    private Long id;
    private String title;
    private String content;

    public static BoardCreateResponse toDto(Board board) {
        return new BoardCreateResponse(board.getId(), board.getTitle(), board.getContent());
    }
}
