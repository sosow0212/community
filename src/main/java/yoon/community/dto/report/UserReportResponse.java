package yoon.community.dto.report;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import yoon.community.dto.user.UserDto;
import yoon.community.entity.report.UserReport;
import yoon.community.entity.user.User;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserReportResponse {
    private int id;
    private UserDto reportedUser;
    private String content;
    private LocalDateTime createdAt;


    public UserReportResponse toDto(UserReport userReport, User reportedUser) {
        return new UserReportResponse(
                userReport.getId(),
                UserDto.toDto(reportedUser),
                userReport.getContent(),
                userReport.getCreatedAt()
        );
    }
}
