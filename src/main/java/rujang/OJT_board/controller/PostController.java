package rujang.OJT_board.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import rujang.OJT_board.domain.Post;
import rujang.OJT_board.domain.User;
import rujang.OJT_board.dto.PostUpdateDTO;
import rujang.OJT_board.service.CommentService;
import rujang.OJT_board.service.PostService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final CommentService commentService;

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

    /**
     * 새 게시물 작성 폼
     * 로그인하지 않은 경우 로그인 페이지로 리다이렉트
     */
    @GetMapping("/posts/new")
    public String showPostForm(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("loggedInUser");
        if (currentUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("postUpdateDTO", new PostUpdateDTO());
        return "post-form";
    }

    //게시물 작성 폼 제출 후 게시물을 저장
    @PostMapping("/posts")
    public String createPost(@Valid @ModelAttribute("postUpdateDTO") PostUpdateDTO postUpdateDTO,
                             BindingResult bindingResult,
                             HttpSession session,
                             Model model) {
        //유효성 검증 오류가 있을 경우 다시 폼으로 이동
        if (bindingResult.hasErrors()) {
            return "post-form";
        }

        //로그인 여부 확인
        User currentUser = (User) session.getAttribute("loggedInUser");
        if (currentUser == null) {
            return "redirect:/login";
        }

        //DTO의 값을 사용하여 Post 엔티티 생성
        Post post = Post.builder()
                .title(postUpdateDTO.getTitle())
                .content(postUpdateDTO.getContent())
                .build();
        //엔티티에 작성자 할당 (setter 없이 도메인 메서드를 사용)
        post.assignUser(currentUser);

        postService.createPost(post);
        return "redirect:/posts";  //일반 게시판 목록 페이지로 이동
    }

    //게시글 상세 페이지
    @GetMapping("/posts/{postId}")
    public String getPostDetail(@PathVariable("postId") Long postId,
                                Model model,
                                HttpSession session) {
        User currentUser = (User) session.getAttribute("loggedInUser");

        //게시글 조회
        Post post = postService.getPostDetail(postId);

        model.addAttribute("loggedInUser", currentUser);
        model.addAttribute("post", post);
        return "post-detail";
    }
    
    //게시글 수정 폼
    @GetMapping("/posts/{postId}/edit")
    public String showEditForm(@PathVariable("postId") Long postId,
                               Model model,
                               HttpSession session) {
        //로그인 확인
        User currentUser = (User) session.getAttribute("loggedInUser");
        if (currentUser == null) {
            return "redirect:/login";
        }

        //게시글 조회
        Post post = postService.getPostDetail(postId);
        //권한 확인 (작성자만 수정 가능)
        if (!post.getUser().getId().equals(currentUser.getId())) {
            return "redirect:/posts/" + postId;  //권한 없으면 상세 페이지로
        }

        //기존 게시글 정보를 DTO에 담아 뷰로 전달
        PostUpdateDTO postUpdateDTO = new PostUpdateDTO();
        postUpdateDTO.setTitle(post.getTitle());
        postUpdateDTO.setContent(post.getContent());

        model.addAttribute("postId", postId);
        model.addAttribute("postUpdateDTO", postUpdateDTO);
        return "post-edit";
    }

    //게시글 수정 처리
    @PostMapping("/posts/{postId}/edit")
    public String updatePost(@PathVariable("postId") Long postId,
                             @Valid @ModelAttribute("postUpdateDTO") PostUpdateDTO postUpdateDTO,
                             BindingResult bindingResult,
                             HttpSession session,
                             Model model) {
        //유효성 검증 실패 시 다시 폼으로
        if (bindingResult.hasErrors()) {
            model.addAttribute("postId", postId);
            return "post-edit";
        }

        User currentUser = (User) session.getAttribute("loggedInUser");
        if (currentUser == null) {
            return "redirect:/login";
        }

        //게시글 수정
        postService.updatePost(postId, postUpdateDTO, currentUser);

        //수정 완료 후 상세 페이지로 이동
        return "redirect:/posts/" + postId;
    }

    //게시글 삭제
    @PostMapping("/posts/{postId}/delete")
    public String deletePost(@PathVariable("postId") Long postId,
                             HttpSession session) {
        User currentUser = (User) session.getAttribute("loggedInUser");
        if (currentUser == null) {
            return "redirect:/login";
        }

        //게시글 삭제
        postService.deletePost(postId, currentUser);

        //삭제 후 게시판 목록 페이지로 이동
        return "redirect:/posts";
    }
}
