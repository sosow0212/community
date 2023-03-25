package yoon.community.dto.board;

import static java.util.stream.Collectors.toList;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import yoon.community.domain.board.Board;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardResponseDto {

    private Long id;
    private String writer_nickname;
    private String title;
    private String content;
    private int likeCount;
    private int favoriteCount;
    private List<ImageDto> images;
    private LocalDateTime createdAt;

    public static BoardResponseDto toDto(Board board, String writer_nickname) {
        return new BoardResponseDto(
                board.getId(),
                writer_nickname,
                board.getTitle(),
                board.getContent(),
                board.getLiked(),
                board.getFavorited(),
                board.getImages().stream().map(ImageDto::toDto).collect(toList()),
                board.getCreatedAt()
        );
    }
}
