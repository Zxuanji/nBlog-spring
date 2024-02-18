package sg.nus.iss.blog.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sg.nus.iss.blog.model.BlogUserStatusEnum;
import sg.nus.iss.blog.model.BlogUser;
import sg.nus.iss.blog.repository.UserRepository;

@Service
@Transactional(readOnly=true)
public class UserServiceImpl implements UserService {
    
    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional(readOnly=false)
    public BlogUser saveUser(BlogUser blogUser) {
        return userRepository.save(blogUser);
    }

    @Override
    public List<BlogUser> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<BlogUser> findAllActiveUsers() {
        List<BlogUser> users = new ArrayList<>();
        for (BlogUser u: userRepository.findAll()) {
            if (u.getUserStatus().equals(BlogUserStatusEnum.ACTIVE)) {
                users.add(u);
            }
        }
        return users;
    }

    @Override
    public Optional<BlogUser> findUserById(int userId) {
        return userRepository.findById(userId);
    }

    @Override
    public BlogUser findUserByDisplayName(String displayName) {
        return userRepository.findUserByDisplayName(displayName);
    }

    @Override
    @Transactional(readOnly=false)
    public BlogUser createUser(BlogUser user) {
        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly=false)
    public BlogUser followBlogUser(int activeUserId, int viewUserId) {
        //add following
        BlogUser viewUserFromDB = userRepository.findById(viewUserId).get();
        BlogUser activeUserFromDB = userRepository.findById(activeUserId).get();

        List<BlogUser> currentUserFollowing =  activeUserFromDB.getFollowing() == null ? new ArrayList<>() : activeUserFromDB.getFollowing();
        currentUserFollowing.add(viewUserFromDB);
        activeUserFromDB.setFollowing(currentUserFollowing);

        //add corresponding user's followers
        List<BlogUser> viewUserFollowers = viewUserFromDB.getFollowers() == null ? new ArrayList<>() : viewUserFromDB.getFollowers();
        viewUserFollowers.add(activeUserFromDB);
        viewUserFromDB.setFollowers(viewUserFollowers);

        userRepository.save(activeUserFromDB);
        userRepository.save(viewUserFromDB);

        return activeUserFromDB;
    }

    @Override
    @Modifying
    @Transactional(readOnly=false)
    public BlogUser unfollowBlogUser(int activeUserId, int viewUserId) {

        BlogUser viewUserFromDB = userRepository.findById(viewUserId).get();
        BlogUser activeUserFromDB = userRepository.findById(activeUserId).get();

        //remove following
        List<BlogUser> activeUserFollowing =  activeUserFromDB.getFollowing();
        for (BlogUser u: activeUserFollowing) {
            if (u.getUserId() == viewUserId) {
                activeUserFollowing.remove(u);
                break;
            }
        }
        activeUserFromDB.setFollowing(activeUserFollowing);

        //remove corresponding user's followers
        List<BlogUser> viewUserFollowers = viewUserFromDB.getFollowers();
        for (BlogUser u: viewUserFollowers) {
            if (u.getUserId() == activeUserId) {
                viewUserFollowers.remove(u);
                break;
            }
        }
        viewUserFromDB.setFollowers(viewUserFollowers);
        
        userRepository.save(viewUserFromDB);
        userRepository.save(activeUserFromDB);
        
        return activeUserFromDB;
    }
}
