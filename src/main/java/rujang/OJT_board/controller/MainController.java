package rujang.OJT_board.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import rujang.OJT_board.domain.Post;
import rujang.OJT_board.domain.User;
import rujang.OJT_board.service.PostService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final PostService postService;

    @GetMapping("/")
    public String showMainPage(Model model, HttpSession session) {
        //세션에서 로그인 사용자 정보 가져오기, 없으면 null
        User currentUser = (User) session.getAttribute("loggedInUser");

        //추천순으로 정렬된 게시물 조회
        List<Post> posts = postService.getPostsOrderByRecommendation();

        //모델에서 데이터 담기
        model.addAttribute("posts", posts);
        model.addAttribute("loggedInUser", currentUser);

        return "main";
    }

}
