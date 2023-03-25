package yoon.community.dto.board;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
@AllArgsConstructor
public class BoardListDto {

    private Integer totalElements;
    private Integer totalPages;
    private boolean hasNext;
    private List<BoardSimpleDto> boardList;

    public static BoardListDto toDto(Page<BoardSimpleDto> page) {
        return new BoardListDto(page.getTotalPages(), (int) page.getTotalElements(), page.hasNext(), page.getContent());
    }
}
