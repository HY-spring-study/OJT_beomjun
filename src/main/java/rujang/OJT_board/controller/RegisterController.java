package rujang.OJT_board.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import rujang.OJT_board.domain.User;
import rujang.OJT_board.dto.RegisterDTO;
import rujang.OJT_board.service.UserService;

@Controller
@RequiredArgsConstructor
public class RegisterController {

    private final UserService userService;

    //회원가입 폼을 보여주는 GET 요청
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        //빈 유저 객체를 모델에 추가 (폼 바인딩 용)
        model.addAttribute("registerDTO", new RegisterDTO());
        return "register";
    }

    //회원가입 요청을 처리하는 POST 요청
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("registerDTO") RegisterDTO registerDTO,
                               Model model,
                               HttpSession session) {
        try{
            //UserService의 register 메서드를 통해 회원가입 처리
            userService.register(registerDTO.getUsername(), registerDTO.getPassword());
            //회원가입 성공 시 메인 페이지로 리다이렉트
            return "redirect:/";
        } catch (IllegalArgumentException e) {
            //회원가입 실패 시, 오류 메시지를 모델에 담고 다시 회원가입 폼으로 이동
            model.addAttribute("errorMessage", e.getMessage());
            return "register";
        }
    }
}
