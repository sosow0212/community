package yoon.community.repository.report;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import yoon.community.domain.report.BoardReportHistory;

import java.util.List;

public interface BoardReportRepository extends JpaRepository<BoardReportHistory, Long> {
    Optional<BoardReportHistory> findByReporterIdAndReportedBoardId(Long reporterId, Long reportedBoardId);

    boolean existsByReporterIdAndReportedBoardId(Long reporterId, Long reportedBoardId);

    List<BoardReportHistory> findByReportedBoardId(Long reportedBoardId);

    void deleteAllByReportedBoardId(Long id);
}
