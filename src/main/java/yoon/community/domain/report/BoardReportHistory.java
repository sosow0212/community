package yoon.community.domain.report;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import lombok.Setter;
import yoon.community.domain.common.EntityDate;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Entity
public class BoardReportHistory extends EntityDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long reporterId;

    @Column(nullable = false)
    private Long reportedBoardId;

    @Column(nullable = false)
    private String content;

    public BoardReportHistory(Long reporterId, Long reportedBoardId, String content) {
        this.reporterId = reporterId;
        this.reportedBoardId = reportedBoardId;
        this.content = content;
    }
}
