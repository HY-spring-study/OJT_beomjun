package rujang.OJT_board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rujang.OJT_board.domain.Post;
import rujang.OJT_board.domain.User;
import rujang.OJT_board.dto.PostUpdateDTO;
import rujang.OJT_board.repository.PostRepository;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    //게시글 생성
    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    //게시글 수정
    public Post updatePost(Long postId, PostUpdateDTO updateDTO, User user) {
        //게시글 조회
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다."));
        //수정 권한 확인 : 게시글 작성자와 현재 사용자가 일치해야 함
        if(!post.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }
        // 게시글 수정: 엔티티의 update() 메서드 호출
        post.update(updateDTO.getTitle(), updateDTO.getContent());

        return postRepository.save(post);
    }

    //게시글 삭제
    public void deletePost(Long postId, User user) {
        //게시글 조회
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다."));
        //삭제 권한 확인
        if(!post.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }
        
        //게시글 삭제
        postRepository.delete(post);
    }
}
