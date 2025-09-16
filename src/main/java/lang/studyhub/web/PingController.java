package lang.studyhub.web;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import lang.studyhub.common.ApiResponse;

@RestController
@Tag(name = "Ping-Pong", description = "테스트 API")
public class PingController {
    @GetMapping("/api/ping")
    public ApiResponse<String> ping() {
        return ApiResponse.ok("pong");
    }
}