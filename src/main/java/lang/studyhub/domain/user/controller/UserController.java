package lang.studyhub.domain.user.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lang.studyhub.domain.user.dto.UserDto.*;
import lang.studyhub.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "회원 관리 API")
public class UserController {
    private final UserService userService;

    @PostMapping
    public Response create(@Valid @RequestBody CreateRequest req) {
        return userService.create(req);
    }

    @GetMapping("/{id}")
    public Response get(@PathVariable Long id) {
        return userService.get(id);
    }
}
