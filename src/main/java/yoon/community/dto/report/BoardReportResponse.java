package yoon.community.dto.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import yoon.community.dto.board.BoardDto;
import yoon.community.entity.report.BoardReport;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardReportResponse {
    private int id;
    private int reportedBoardId;
    private String content;
    private LocalDate createdAt;

    public BoardReportResponse toDto(BoardReport boardReport) {
        return new BoardReportResponse(
                boardReport.getId(),
                boardReport.getReportedBoardId(),
                boardReport.getContent(),
                boardReport.getCreateDate()
        );
    }
}
