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

    /**
     * 댓글 삭제 기능
     * 삭제 요청 유저와 댓글 작성 유저가 일치하면 삭제
     */
    @Transactional
    public void deleteComment(Long commentId, User user) {
        //댓글 조회
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));
        //삭제 권한 확인
        if(!comment.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }

        commentRepository.delete(comment);
    }

}
