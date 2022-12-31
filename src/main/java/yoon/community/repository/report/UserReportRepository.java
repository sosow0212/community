package yoon.community.repository.report;

import org.springframework.data.jpa.repository.JpaRepository;
import yoon.community.entity.report.UserReportHistory;

import java.util.List;

public interface UserReportRepository extends JpaRepository<UserReportHistory, Long> {
    boolean existsByReporterIdAndReportedUserId(Long reporterId, Long reportedUserId);

    List<UserReportHistory> findByReportedUserId(Long reportedId);

    void deleteAllByReportedUserId(Long id);
}
