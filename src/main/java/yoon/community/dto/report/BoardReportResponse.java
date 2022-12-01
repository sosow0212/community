package yoon.community.dto.report;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import yoon.community.entity.report.BoardReport;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardReportResponse {
    private int id;
    private int reportedBoardId;
    private String content;
    private LocalDateTime createdAt;

    public BoardReportResponse toDto(BoardReport boardReport) {
        return new BoardReportResponse(
                boardReport.getId(),
                boardReport.getReportedBoardId(),
                boardReport.getContent(),
                boardReport.getCreatedAt()
        );
    }
}
