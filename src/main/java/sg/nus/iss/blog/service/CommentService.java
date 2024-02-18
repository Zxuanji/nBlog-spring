package sg.nus.iss.blog.service;

import java.util.List;

import sg.nus.iss.blog.model.Blog;
import sg.nus.iss.blog.model.Comment;

public interface CommentService {
    List<Object[]> findAllCommentsWithUserAndBlog(int blogId);

    Comment saveComment(Comment comment);
}
