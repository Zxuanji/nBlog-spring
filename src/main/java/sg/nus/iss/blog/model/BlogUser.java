package sg.nus.iss.blog.model;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import sg.nus.iss.blog.validation.ValidPassword;

@Entity
public class BlogUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    @NotBlank(message="Required")
    private String displayName;
    @NotBlank(message="Required")
    @Email(message="Invalid email address")
    private String email;
    @NotBlank(message="Please enter your password")
    @ValidPassword
    private String password;
    private LocalDate signupTime;
    private String profilePicture;
    @Column(columnDefinition = "TEXT")
    private String profileTagline;
    private String location;
    @Column(columnDefinition = "TEXT")
    private String aboutMe;
    private String myTechStack;
    private String githubLink;
    private String linkedinLink;
    private BlogUserStatusEnum userStatus;
    private String likedBlogIds;
    private String favouriteBlogIds;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL, CascadeType.PERSIST})
    @JoinTable(name="following_followers", joinColumns=@JoinColumn(name="follower_id"), inverseJoinColumns=@JoinColumn(name="following_id"))
    private List<BlogUser> following;

    @ManyToMany(mappedBy = "following", fetch = FetchType.EAGER, cascade = {CascadeType.ALL, CascadeType.PERSIST})
    private List<BlogUser> followers;

    @OneToMany(mappedBy="blogUser", fetch = FetchType.EAGER, cascade = CascadeType.ALL)// to keep track of blog post read by each user
    private List<BlogHistory> blogHistories;

    @OneToMany(mappedBy = "blogUser", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Blog> postedBlogs;

    @OneToMany(mappedBy = "commentBlogUser", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Comment> blogComments;
    
    public BlogUser() {}
    public BlogUser(@NotBlank(message = "Required") String displayName,
            @NotBlank(message = "Required") @Email(message = "Invalid email address") String email,
            @NotBlank(message = "Please enter your password") String password, LocalDate signupTime,
            String profilePicture, String profileTagline, String location, String aboutMe, String myTechStack,
            String githubLink, String linkedinLink, BlogUserStatusEnum userStatus, List<BlogUser> following,
            List<BlogUser> followers, List<BlogHistory> blogHistories, List<Blog> postedBlogs, String likedBlogIds,String favouriteBlogIds) {
        this.displayName = displayName;
        this.email = email;
        this.password = password;
        this.signupTime = signupTime;
        this.profilePicture = profilePicture;
        this.profileTagline = profileTagline;
        this.location = location;
        this.aboutMe = aboutMe;
        this.myTechStack = myTechStack;
        this.githubLink = githubLink;
        this.linkedinLink = linkedinLink;
        this.userStatus = userStatus;
        this.following = following;
        this.followers = followers;
        this.blogHistories = blogHistories;
        this.postedBlogs = postedBlogs;
        this.likedBlogIds = likedBlogIds;
        this.favouriteBlogIds = favouriteBlogIds;
    }

    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public LocalDate getSignupTime() {
        return signupTime;
    }
    public void setSignupTime(LocalDate signupTime) {
        this.signupTime = signupTime;
    }
    public String getProfilePicture() {
        return profilePicture;
    }
    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
    public String getProfileTagline() {
        return profileTagline;
    }
    public void setProfileTagline(String profileTagline) {
        this.profileTagline = profileTagline;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public String getAboutMe() {
        return aboutMe;
    }
    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }
    public String getMyTechStack() {
        return myTechStack;
    }
    public void setMyTechStack(String myTechStack) {
        this.myTechStack = myTechStack;
    }
    public String getGithubLink() {
        return githubLink;
    }
    public void setGithubLink(String githubLink) {
        this.githubLink = githubLink;
    }
    public String getLinkedinLink() {
        return linkedinLink;
    }
    public void setLinkedinLink(String linkedinLink) {
        this.linkedinLink = linkedinLink;
    }
    public BlogUserStatusEnum getUserStatus() {
        return userStatus;
    }
    public void setUserStatus(BlogUserStatusEnum userStatus) {
        this.userStatus = userStatus;
    }
    public List<BlogUser> getFollowing() {
        return following;
    }
    public void setFollowing(List<BlogUser> following) {
        this.following = following;
    }
    public List<BlogUser> getFollowers() {
        return followers;
    }
    public void setFollowers(List<BlogUser> followers) {
        this.followers = followers;
    }
    public List<BlogHistory> getBlogHistories() {
        return blogHistories;
    }
    public void setBlogHistories(List<BlogHistory> blogHistories) {
        this.blogHistories = blogHistories;
    }
    public List<Blog> getPostedBlogs() {
        return postedBlogs;
    }
    public void setPostedBlogs(List<Blog> postedBlogs) {
        this.postedBlogs = postedBlogs;
    }
    public List<Comment> getBlogComments() {
        return blogComments;
    }
    public void setBlogComments(List<Comment> blogComments) {
        this.blogComments = blogComments;
    }
    public String getLikedBlogIds() {
        return likedBlogIds;
    }
    public void setLikedBlogIds(String likedBlogIds) {
        this.likedBlogIds = likedBlogIds;
    }
    public String getFavouriteBlogIds() {
        return favouriteBlogIds;
    }
    public void setFavouriteBlogIds(String favouriteBlogIds) {
        this.favouriteBlogIds = favouriteBlogIds;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.userId;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        BlogUser other = (BlogUser) obj;
        if (this == other) {return true;}
        if (other == null) {return false;}
        if (this.getClass() != other.getClass()) {return false;}
        return this.userId == other.userId;
    }
}
