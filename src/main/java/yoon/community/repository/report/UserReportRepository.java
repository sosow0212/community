package yoon.community.repository.report;

import org.springframework.data.jpa.repository.JpaRepository;
import yoon.community.entity.report.UserReport;
import yoon.community.entity.user.User;

import java.util.List;

public interface UserReportRepository extends JpaRepository<UserReport, Integer> {
    UserReport findByReporterIdAndReportedUserId(int reporterId, int reportedUserId);
    List<UserReport> findByReportedUserId(int reportedId);
}
