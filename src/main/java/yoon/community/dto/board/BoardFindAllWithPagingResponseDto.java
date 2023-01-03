package yoon.community.dto.board;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BoardFindAllWithPagingResponseDto {
    private List<BoardSimpleDto> boards;
    private PageInfoDto pageInfoDto;

    public static BoardFindAllWithPagingResponseDto toDto(List<BoardSimpleDto> boards, PageInfoDto pageInfoDto) {
        return new BoardFindAllWithPagingResponseDto(boards, pageInfoDto);
    }
}
