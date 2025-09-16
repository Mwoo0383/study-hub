package lang.studyhub.domain.report.service;

import lang.studyhub.domain.report.dto.ReportRequest;
import lang.studyhub.domain.report.dto.ReportResponse;
import lang.studyhub.domain.report.entity.Report;
import lang.studyhub.domain.report.entity.ReportStatus;
import lang.studyhub.domain.report.repository.ReportRepository;
import lang.studyhub.domain.user.entity.User;
import lang.studyhub.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;

    public ReportResponse create(ReportRequest req) {
        User reporter = userRepository.findById(req.getReporterId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Report report = Report.builder()
                .reporter(reporter)
                .targetType(req.getTargetType())
                .targetId(req.getTargetId())
                .reason(req.getReason())
                .status(ReportStatus.OPEN)
                .build();

        return ReportResponse.from(reportRepository.save(report));
    }

    public List<ReportResponse> findAll() {
        return reportRepository.findAll()
                .stream().map(ReportResponse::from).toList();
    }

    public ReportResponse updateStatus(Long id, ReportStatus status) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Report not found"));
        report.setStatus(status);
        return ReportResponse.from(reportRepository.save(report));
    }
}
