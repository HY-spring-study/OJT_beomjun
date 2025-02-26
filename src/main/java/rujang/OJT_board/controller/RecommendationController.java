package rujang.OJT_board.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import rujang.OJT_board.domain.User;
import rujang.OJT_board.service.RecommendationService;

@Controller
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @PostMapping("/posts/{postId}/recommend")
    public String toggleRecommendation(@PathVariable("postId") Long postId, HttpSession session) {
        //로그인 여부 확인
        User currentUser = (User) session.getAttribute("loggedInUser");
        if(currentUser == null) {
            return "redirect:/login";
        }
        //추천 토글 처리
        recommendationService.toggleRecommendation(postId, currentUser);
        return "redirect:/posts/" + postId;
    }
}
