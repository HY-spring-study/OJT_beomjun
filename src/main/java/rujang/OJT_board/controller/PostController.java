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
public class PostController {

    private final PostService postService;

    //일반 게시판 페이지
    @GetMapping("/posts")
    public String showPosts(Model model, HttpSession session) {
        //세션에서 로그인 사용자 정보 가져오기 (없으면 null)
        User currentUser = (User) session.getAttribute("loggedInUser");

        //생성시간을 기준으로 전체 게시글을 조회
        List<Post> posts = postService.findAllPostsOrderByCreatedAtDesc();

        model.addAttribute("loggedInUser", currentUser);
        model.addAttribute("posts", posts);
        return "posts";
    }
}
