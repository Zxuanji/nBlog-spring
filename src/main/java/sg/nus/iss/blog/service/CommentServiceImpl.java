package sg.nus.iss.blog.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sg.nus.iss.blog.model.Comment;
import sg.nus.iss.blog.repository.CommentRepository;


@Service
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Override
    public List<Object[]> findAllCommentsWithUserAndBlog(int blogId) {
        return commentRepository.findAllCommentsWithUserAndBlog(blogId);
    }

    @Override
    @Transactional(readOnly = false)
    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
    }
    
}
