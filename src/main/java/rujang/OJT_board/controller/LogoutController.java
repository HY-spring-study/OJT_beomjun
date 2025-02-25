package rujang.OJT_board.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LogoutController {

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        //세션 무효화: 현재 세션에 저장된 모든 정보를 삭제합니다.
        session.invalidate();
        //로그아웃 후 메인 페이지로 리다이렉트합니다.
        return "redirect:/";
    }
}
