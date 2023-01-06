package yoon.community.repository.report;

import org.springframework.data.jpa.repository.JpaRepository;
import yoon.community.domain.report.MemberReportHistory;

import java.util.List;

public interface MemberReportRepository extends JpaRepository<MemberReportHistory, Long> {
    boolean existsByReporterIdAndReportedUserId(Long reporterId, Long reportedUserId);

    List<MemberReportHistory> findByReportedUserId(Long reportedId);

    void deleteAllByReportedUserId(Long id);
}
