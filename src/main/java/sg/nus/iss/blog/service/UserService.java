package sg.nus.iss.blog.service;

import java.util.List;
import java.util.Optional;

import sg.nus.iss.blog.model.BlogUser;

public interface UserService {

    BlogUser saveUser(BlogUser blogUser);
    List<BlogUser> findAllUsers();
    List<BlogUser> findAllActiveUsers();
    Optional<BlogUser> findUserById(int userId);
    BlogUser findUserByDisplayName(String displayName);
    BlogUser createUser(BlogUser user);
    BlogUser followBlogUser(int activeUserId, int viewUserId);
    BlogUser unfollowBlogUser(int activeUserId, int viewUserId);
    
}
