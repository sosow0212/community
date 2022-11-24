package yoon.community.entity.report;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import yoon.community.entity.common.EntityDate;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
@Entity
public class BoardReport extends EntityDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(nullable = false)
    private int reporterId;

    @Column(nullable = false)
    private int reportedBoardId;

    @Column(nullable = false)
    private String content;

    public BoardReport(int reporterId, int reportedBoardId, String content) {
        this.reporterId = reporterId;
        this.reportedBoardId = reportedBoardId;
        this.content = content;
    }
}