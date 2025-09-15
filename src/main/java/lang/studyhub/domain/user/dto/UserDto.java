package lang.studyhub.domain.user.dto;

import jakarta.validation.constraints.*;

public class UserDto {

    public record CreateRequest(
            @NotBlank @Size(min=3, max=50) String username,
            @NotBlank @Size(min=6) String password,
            @NotBlank @Size(max=50) String nickname,
            @Email String email
    ) {}

    public record Response(
            Long id, String username, String nickname, String email, String role
    ) {}
}
