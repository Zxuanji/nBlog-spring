package sg.nus.iss.blog.model;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int blogId; 
    private String image;
    private int readingTime;
    private String contentTitle;
    private String subTitle;
    @Column(columnDefinition = "TEXT")
    private String content; 
    private String blogTime; 
    private String languageSelected;
    private String techniqueSelected;
    private BlogStatusEnum blogStatus;
    private int blogCommentCount; //delete
    private int blogLikeCount;
    private String labelList;
    
    @OneToMany(mappedBy = "commentBlog", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> blogComments;

    @JsonIgnore
    @OneToMany(mappedBy="blog", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true) // to count the number of read for each blog post
    private List<BlogHistory> blogHistories;

    @ManyToOne(cascade = {CascadeType.ALL, CascadeType.PERSIST})
    private BlogUser blogUser;

    public Blog() {
    }

    

    public Blog(String image, int readingTime, String contentTitle, String subTitle, String content,
            String blogTime, String languageSelected, String techniqueSelected, BlogStatusEnum blogStatus,
            int blogCommentCount, int blogLikeCount, List<Comment> blogComments, List<BlogHistory> blogHistories,
            BlogUser blogUser) {
        this.image = image;
        this.readingTime = readingTime;
        this.contentTitle = contentTitle;
        this.subTitle = subTitle;
        this.content = content;
        this.blogTime = blogTime;
        this.languageSelected = languageSelected;
        this.techniqueSelected = techniqueSelected;
        this.blogStatus = blogStatus;
        this.blogCommentCount = blogCommentCount;
        this.blogLikeCount = blogLikeCount;
        this.blogComments = blogComments;
        this.blogHistories = blogHistories;
        this.blogUser = blogUser;
    }



    public int getBlogId() {
        return blogId;
    }

    public void setBlogId(int blogId) {
        this.blogId = blogId;
    }
    
    public String getLabelList() {
        return labelList;
    }

    public void setLabeList(String labelList) {
        this.labelList = labelList;
    }
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    

    public int getReadingTime() {
        return readingTime;
    }

    public void setReadingTime(int readingTime) {
        this.readingTime = readingTime;
    }

    public String getContentTitle() {
        return contentTitle;
    }

    public void setContentTitle(String contentTitle) {
        this.contentTitle = contentTitle;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBlogTime() {
        return blogTime;
    }

    public void setBlogTime(String blogTime) {
        this.blogTime = blogTime;
    }

    public String getLanguageSelected() {
        return languageSelected;
    }

    public void setLanguageSelected(String languageSelected) {
        this.languageSelected = languageSelected;
    }

    public String getTechniqueSelected() {
        return techniqueSelected;
    }

    public void setTechniqueSelected(String techniqueSelected) {
        this.techniqueSelected = techniqueSelected;
    }

    public BlogStatusEnum getBlogStatus() {
        return blogStatus;
    }

    public void setBlogStatus(BlogStatusEnum blogStatus) {
        this.blogStatus = blogStatus;
    }

    public int getBlogCommentCount() {
        return blogCommentCount;
    }

    public void setBlogCommentCount(int blogCommentCount) {
        this.blogCommentCount = blogCommentCount;
    }

    public int getBlogLikeCount() {
        return blogLikeCount;
    }

    public void setBlogLikeCount(int blogLikeCount) {
        this.blogLikeCount = blogLikeCount;
    }

    public List<BlogHistory> getBlogHistories() {
        return blogHistories;
    }

    public void setBlogHistories(List<BlogHistory> blogHistories) {
        this.blogHistories = blogHistories;
    }

    public BlogUser getBlogUser() {
        return blogUser;
    }

    public void setBlogUser(BlogUser blogUser) {
        this.blogUser = blogUser;
    }

    public List<Comment> getBlogComments() {
        return blogComments;
    }

    public void setBlogComments(List<Comment> blogComments) {
        this.blogComments = blogComments;
    }

    @Override
    public String toString() {
        return "Blog [blogId=" + blogId + ", image=" + image + ", readingTime=" + readingTime + ", contentTitle="
                + contentTitle + ", subTitle=" + subTitle + ", content=" + content + ", blogTime=" + blogTime
                + ", languageSelected=" + languageSelected + ", techniqueSelected=" + techniqueSelected
                + ", blogStatus=" + blogStatus + ", blogCommentCount=" + blogCommentCount + ", blogLikeCount="
                + blogLikeCount + "]";
    }

    
}
