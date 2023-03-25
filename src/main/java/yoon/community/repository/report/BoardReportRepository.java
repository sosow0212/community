package yoon.community.repository.report;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import yoon.community.domain.report.BoardReportHistory;

public interface BoardReportRepository extends JpaRepository<BoardReportHistory, Long> {

    boolean existsByReporterIdAndReportedBoardId(Long reporterId, Long reportedBoardId);

    List<BoardReportHistory> findByReportedBoardId(Long reportedBoardId);

    void deleteAllByReportedBoardId(Long id);
}
