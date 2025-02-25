package rujang.OJT_board.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import rujang.OJT_board.domain.User;
import rujang.OJT_board.service.UserService;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;

    //로그인 화면을 보여주는 GET 요청
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        return "login";
    }

    //로그인 폼 제출 시 처리하는 POST 요청
    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        HttpSession session,
                        Model model) {
        try{
            //UserService의 login 메서드가 username, password를 검증 후 User 반환
            User user = userService.login(username, password);
            //로그인 성공 시 세션에 저장
            session.setAttribute("loggedInUser", user);
            return "redirect:/"; // 메인 페이지로 이동
        } catch (IllegalArgumentException e) {
            //로그인 실패 시 오류 메시지를 모델에 담아서 다시 로그인 페이지로 이동
            model.addAttribute("errorMessage", e.getMessage());
            return "login";
        }
    }

}
