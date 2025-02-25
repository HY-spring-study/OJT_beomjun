package rujang.OJT_board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rujang.OJT_board.domain.Comment;
import rujang.OJT_board.domain.Post;
import rujang.OJT_board.domain.User;
import rujang.OJT_board.repository.CommentRepository;
import rujang.OJT_board.repository.PostRepository;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    /**
     * 댓글 생성 기능
     * 게시글이 존재하는지 확인 후, 댓글 생성
     */
    @Transactional
    public Comment addComment(Long postId, String content, User user) {
        //게시글 존재 여부 확인
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        //댓글 엔티티 생성
        Comment comment = Comment.builder()
                .content(content)
                .user(user)
                .post(post)
                .build();

        return commentRepository.save(comment);
    }

}
