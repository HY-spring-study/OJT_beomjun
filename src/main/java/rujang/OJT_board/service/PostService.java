package rujang.OJT_board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rujang.OJT_board.domain.Post;
import rujang.OJT_board.repository.PostRepository;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    //게시물 생성
    public Post createPost(Post post) {
        return postRepository.save(post);
    }
}
