package sg.nus.iss.blog.model;


import org.hibernate.annotations.Cascade;

import jakarta.persistence.*;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int commentId;
    private String commentContent;
    @ManyToOne(cascade = {CascadeType.ALL, CascadeType.PERSIST})
    private Blog commentBlog;
    @ManyToOne(cascade = {CascadeType.ALL, CascadeType.PERSIST})
    private BlogUser commentBlogUser;

    public Comment() {
    }

    public Comment(String commentContent, Blog commentBlog, BlogUser commentBlogUser) {
        this.commentContent = commentContent;
        this.commentBlog = commentBlog;
        this.commentBlogUser = commentBlogUser;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public Blog getCommentBlog() {
        return commentBlog;
    }

    public void setCommentBlog(Blog commentBlog) {
        this.commentBlog = commentBlog;
    }

    public BlogUser getCommentBlogUser() {
        return commentBlogUser;
    }

    public void setCommentBlogUser(BlogUser commentBlogUser) {
        this.commentBlogUser = commentBlogUser;
    }

    
    
    
}