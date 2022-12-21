package yoon.community.repository.report;

import org.springframework.data.jpa.repository.JpaRepository;
import yoon.community.entity.report.UserReportHistory;

import java.util.List;

public interface UserReportRepository extends JpaRepository<UserReportHistory, Integer> {
    boolean existsByReporterIdAndReportedUserId(int reporterId, int reportedUserId);

    List<UserReportHistory> findByReportedUserId(int reportedId);

    void deleteAllByReportedUserId(int id);
}
