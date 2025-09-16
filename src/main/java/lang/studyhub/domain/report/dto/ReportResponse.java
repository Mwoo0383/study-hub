package lang.studyhub.domain.report.dto;

import lang.studyhub.domain.report.entity.Report;
import lang.studyhub.domain.report.entity.ReportStatus;
import lang.studyhub.domain.report.entity.ReportTargetType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportResponse {
    private Long id;
    private Long reporterId;
    private ReportTargetType targetType;
    private Long targetId;
    private String reason;
    private ReportStatus status;

    public static ReportResponse from(Report report) {
        return ReportResponse.builder()
                .id(report.getId())
                .reporterId(report.getReporter().getId())
                .targetType(report.getTargetType())
                .targetId(report.getTargetId())
                .reason(report.getReason())
                .status(report.getStatus())
                .build();
    }
}