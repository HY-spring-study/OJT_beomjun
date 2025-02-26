package rujang.OJT_board.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import rujang.OJT_board.domain.User;
import rujang.OJT_board.service.CommentService;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    //댓글 작성
    @PostMapping("/posts/{postId}/comments")
    public String addComment(@PathVariable("postId") Long postId,
                             @RequestParam("content") String content,
                             HttpSession session) {
        User currentUser = (User) session.getAttribute("loggedInUser");
        if (currentUser == null) {
            return "redirect:/login";
        }

        //댓글 작성
        commentService.addComment(postId, content, currentUser);

        return "redirect:/posts/" + postId;
    }

    //댓글 삭제
    @PostMapping("/comments/{commentId}/delete")
    public String deleteComment(@PathVariable("commentId") Long commentId,
                                HttpSession session,
                                @RequestParam("postId") Long postId) {
        User currentUser = (User) session.getAttribute("loggedInUser");
        if (currentUser == null) {
            return "redirect:/login";
        }

        //댓글 삭제
        commentService.deleteComment(commentId, currentUser);

        return "redirect:/posts/" + postId;
    }

}
