package yoon.community.entity.report;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import lombok.Setter;
import yoon.community.entity.common.EntityDate;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Entity
public class UserReportHistory extends EntityDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long reporterId;

    @Column(nullable = false)
    private Long reportedUserId;

    @Column(nullable = false)
    private String content;

    public UserReportHistory(Long reporterId, Long reportedUserId, String content) {
        this.reporterId = reporterId;
        this.reportedUserId = reportedUserId;
        this.content = content;
    }
}
