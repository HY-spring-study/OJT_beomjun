package rujang.OJT_board.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterDTO {

    @NotBlank(message = "아이디를 입력해주세요.")
    @Size(min = 3, max = 50, message = "아이디는 3자 이상 50자 이하로 입력해주세요.")
    private String username;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(min = 4, max = 100, message = "비밀번호는 4자 이상 100자 이하로 입력해주세요.")
    private String password;
}
