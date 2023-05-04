package yoon.community.dto.report;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import yoon.community.domain.member.Member;
import yoon.community.domain.report.MemberReportHistory;
import yoon.community.dto.member.MemberEditRequestDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberReportResponseDto {

    private Long id;
    private MemberEditRequestDto reportedUser;
    private String content;
    private LocalDateTime createdAt;

    public static MemberReportResponseDto toDto(MemberReportHistory memberReportHistory, Member reportedMember) {
        return new MemberReportResponseDto(
                memberReportHistory.getId(),
                MemberEditRequestDto.toDto(reportedMember),
                memberReportHistory.getContent(),
                memberReportHistory.getCreatedAt()
        );
    }

    public static MemberReportResponseDto toDto(final MemberReportHistory memberReportHistory, final MemberEditRequestDto editRequestDto, final MemberReportRequestDto memberReportRequestDto) {
        return new MemberReportResponseDto(memberReportRequestDto.getReportedUserId(),
                editRequestDto,
                memberReportRequestDto.getContent(),
                memberReportHistory.getCreatedAt());
    }
}
