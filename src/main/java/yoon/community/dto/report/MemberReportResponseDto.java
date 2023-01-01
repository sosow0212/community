package yoon.community.dto.report;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import yoon.community.dto.member.MemberEditRequestDto;
import yoon.community.entity.report.MemberReportHistory;
import yoon.community.entity.member.Member;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberReportResponseDto {
    private Long id;
    private MemberEditRequestDto reportedUser;
    private String content;
    private LocalDateTime createdAt;

    public MemberReportResponseDto toDto(MemberReportHistory memberReportHistory, Member reportedMember) {
        return new MemberReportResponseDto(
                memberReportHistory.getId(),
                MemberEditRequestDto.toDto(reportedMember),
                memberReportHistory.getContent(),
                memberReportHistory.getCreatedAt()
        );
    }
}
