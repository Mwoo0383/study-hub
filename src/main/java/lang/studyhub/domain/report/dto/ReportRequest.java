package lang.studyhub.domain.report.dto;

import lang.studyhub.domain.report.entity.ReportTargetType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportRequest {
    private Long reporterId;
    private ReportTargetType targetType;
    private Long targetId;
    private String reason;
}