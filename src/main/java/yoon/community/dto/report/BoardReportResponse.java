package yoon.community.dto.report;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import yoon.community.domain.report.BoardReportHistory;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardReportResponse {
    private Long id;
    private Long reportedBoardId;
    private String content;
    private LocalDateTime createdAt;

    public BoardReportResponse toDto(BoardReportHistory boardReportHistory) {
        return new BoardReportResponse(
                boardReportHistory.getId(),
                boardReportHistory.getReportedBoardId(),
                boardReportHistory.getContent(),
                boardReportHistory.getCreatedAt()
        );
    }
}
