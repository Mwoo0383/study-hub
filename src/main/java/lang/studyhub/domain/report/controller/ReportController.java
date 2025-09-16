package lang.studyhub.domain.report.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lang.studyhub.domain.report.dto.ReportRequest;
import lang.studyhub.domain.report.dto.ReportResponse;
import lang.studyhub.domain.report.entity.ReportStatus;
import lang.studyhub.domain.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Tag(name = "Reports", description = "신고 API")
public class ReportController {

    private final ReportService reportService;

    @PostMapping
    public ReportResponse create(@RequestBody ReportRequest req) {
        return reportService.create(req);
    }

    @GetMapping
    public List<ReportResponse> findAll() {
        return reportService.findAll();
    }

    @PatchMapping("/{id}/status")
    public ReportResponse updateStatus(@PathVariable Long id,
                                       @RequestParam ReportStatus status) {
        return reportService.updateStatus(id, status);
    }
}
