package sg.nus.iss.blog.model;

import java.time.LocalDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class BlogHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int blogHistoryId;

    @ManyToOne(cascade = {CascadeType.ALL, CascadeType.PERSIST})
    private Blog blog;

    @ManyToOne(cascade = {CascadeType.ALL, CascadeType.PERSIST})
    private BlogUser blogUser;
    
    private LocalDate readDate;

    public BlogHistory() {}

    public BlogHistory(Blog blog, BlogUser blogUser, LocalDate readDate) {
        this.blog = blog;
        this.blogUser = blogUser;
        this.readDate = readDate;
    }

    public int getBlogHistoryId() {
        return blogHistoryId;
    }

    public void setBlogHistoryId(int blogHistoryId) {
        this.blogHistoryId = blogHistoryId;
    }

    public Blog getBlog() {
        return blog;
    }

    public void setBlog(Blog blog) {
        this.blog = blog;
    }

    public BlogUser getBlogUser() {
        return blogUser;
    }

    public void setBlogUser(BlogUser blogUser) {
        this.blogUser = blogUser;
    }

    public LocalDate getReadDate() {
        return readDate;
    }

    public void setReadDate(LocalDate readDate) {
        this.readDate = readDate;
    }

    @Override
    public String toString() {
        return "BlogHistory [blogHistoryId=" + blogHistoryId + ", readDate=" + readDate + "]";
    }

    

    
}
