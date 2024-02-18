package sg.nus.iss.blog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import sg.nus.iss.blog.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer>{
    @Query("SELECT c.commentContent, c.commentBlog.blogTime, c.commentBlogUser.displayName FROM Comment c WHERE c.commentBlog.blogId = :blogId")
    List<Object[]> findAllCommentsWithUserAndBlog(@Param("blogId") int blogId);
}
