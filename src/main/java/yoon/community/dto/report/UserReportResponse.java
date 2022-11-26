package yoon.community.dto.report;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import yoon.community.dto.user.UserEditRequestDto;
import yoon.community.entity.report.UserReport;
import yoon.community.entity.user.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserReportResponse {
    private int id;
    private UserEditRequestDto reportedUser;
    private String content;
    private LocalDateTime createdAt;


    public UserReportResponse toDto(UserReport userReport, User reportedUser) {
        return new UserReportResponse(
                userReport.getId(),
                UserEditRequestDto.toDto(reportedUser),
                userReport.getContent(),
                userReport.getCreatedAt()
        );
    }
}
