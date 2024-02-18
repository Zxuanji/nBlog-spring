package sg.nus.iss.blog.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import sg.nus.iss.blog.model.BlogUserStatusEnum;
import sg.nus.iss.blog.model.Comment;
import sg.nus.iss.blog.model.Blog;
import sg.nus.iss.blog.model.BlogHistory;
import sg.nus.iss.blog.model.BlogUser;
import sg.nus.iss.blog.model.EmailDetails;
import sg.nus.iss.blog.service.EmailService;
import sg.nus.iss.blog.service.UserService;
import sg.nus.iss.blog.validator.AccountValidator;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private AccountValidator accountValidator;

    @InitBinder
    private void initCourseBinder(WebDataBinder binder) {
        binder.addValidators(accountValidator);
    }

    @GetMapping("/create")
    public String createAccount(Model model) {
        model.addAttribute("user", new BlogUser());

        return "account-create";
    }

    @PostMapping("/create")
    public String createAccount(@Valid @ModelAttribute("user") BlogUser inUser, BindingResult bindingResult,
            HttpSession sessionObj, Model model) {
        if (bindingResult.hasErrors()) {
            return "account-create";
        } else {
            inUser.setProfilePicture("https://cdn.pixabay.com/photo/2018/11/13/21/43/avatar-3814049_1280.png");
            inUser.setSignupTime(LocalDate.now());
            inUser.setUserStatus(BlogUserStatusEnum.ACTIVE);
            inUser.setFavouriteBlogIds("");
            inUser.setLikedBlogIds("");
            inUser.setLocation("");
            inUser.setAboutMe("");
            inUser.setProfileTagline("");
            inUser.setMyTechStack("");
            inUser.setLinkedinLink("");
            inUser.setGithubLink("");
            inUser.setFollowing(new ArrayList<>());
            inUser.setFollowers(new ArrayList<>());
            inUser.setBlogHistories(new ArrayList<>());
            inUser.setPostedBlogs(new ArrayList<>());
            inUser.setBlogComments(new ArrayList<>());

            userService.createUser(inUser);
            sessionObj.setAttribute("activeUser", inUser);
            EmailDetails emailDetails = new EmailDetails(inUser.getEmail(),
                    "Welcome to NBlog, " + inUser.getDisplayName() + "!", "Have a rewarding time exploring NBlog!");
            emailService.sendEmail(emailDetails);

            return "redirect:/home/list";
        }
    }

    @GetMapping("/login")
    public String login(Model model, HttpSession sessionObj) {
        BlogUser activeUser = (BlogUser) sessionObj.getAttribute("activeUser");
        if (activeUser == null) {
            model.addAttribute("user", new BlogUser());
            return "account-login";
        } else {
            if (activeUser.getEmail().equalsIgnoreCase("nblog_12@outlook.com")) {
                return "redirect:/admin/main";
            } else {
                return "redirect:/home/list";
            }
        }
    }

    @GetMapping("/api/blogusers/name/{name}")
    public ResponseEntity<BlogUser> getUsersByDisplayName(@PathVariable String name) {
        BlogUser user = userService.findUserByDisplayName(name);
        if (user != null) {
            BlogUser user2 = toSimpleUser(user);
            return new ResponseEntity<>(user2, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("user") BlogUser inUser, HttpSession sessionObj, Model model) {
        List<BlogUser> users = userService.findAllUsers();
        boolean validAccount = false;
        BlogUser user = new BlogUser();

        for (BlogUser u : users) {
            if (inUser.getEmail().equals(u.getEmail()) && inUser.getPassword().equals(u.getPassword())) {
                validAccount = true;
                user = u;
                sessionObj.setAttribute("activeUser", u);
            }
        }

        if (validAccount == true) {
            if (user.getEmail().equalsIgnoreCase("nblog_12@outlook.com")) {
                return "redirect:/admin/main";
            } else {
                return "redirect:/home/list";
            }
        } else {
            model.addAttribute("invalidAccount", "The e-mail address and/or password you specified are not correct.");
            return "account-login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession sessionObj) {
        sessionObj.invalidate();
        return "redirect:/nblog";
    }

    @PutMapping("api/blogusers/{id}")
    public ResponseEntity<ResponseBody> updateBloguser(@PathVariable("id") int id,
            @RequestBody BlogUser inUser) {
        Optional<BlogUser> user = userService.findUserById(id);
        if (user.isPresent()) {
            BlogUser updatedUser = user.get();
            updatedUser.setDisplayName(inUser.getDisplayName());
            updatedUser.setLocation(inUser.getLocation());
            updatedUser.setProfileTagline(inUser.getProfileTagline());
            updatedUser.setGithubLink(inUser.getGithubLink());
            updatedUser.setLinkedinLink(inUser.getLinkedinLink());
            updatedUser.setAboutMe(inUser.getAboutMe());
            updatedUser.setMyTechStack(inUser.getMyTechStack());
            updatedUser.setEmail(inUser.getEmail());
            userService.saveUser(updatedUser);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/api/blogusers/{id}")
    public ResponseEntity<BlogUser> getUsersById(@PathVariable int id) {
        Optional<BlogUser> user = userService.findUserById(id);
        if (user != null) {
            BlogUser simpleBlogUser = toSimpleUser(user.get());
            return new ResponseEntity<>(simpleBlogUser, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public BlogUser toSimpleUser(BlogUser blogUser) {
        blogUser.setBlogComments(null);
        List<BlogUser> newFollowers = new ArrayList<>();
        List<BlogUser> newFollowing = new ArrayList<>();

        for (BlogUser user : blogUser.getFollowers()) {

            BlogUser newUser = new BlogUser();
            newUser.setAboutMe(user.getAboutMe());
            newUser.setProfilePicture(user.getProfilePicture());
            newUser.setLocation(user.getLocation());
            newUser.setDisplayName(user.getDisplayName());
            newUser.setUserId(user.getUserId());
            newFollowers.add(newUser);
        }

        blogUser.setFollowers(newFollowers);

        for (BlogUser user : blogUser.getFollowing()) {
            BlogUser newUser = new BlogUser();
            newUser.setAboutMe(user.getAboutMe());
            newUser.setProfilePicture(user.getProfilePicture());
            newUser.setLocation(user.getLocation());
            newUser.setDisplayName(user.getDisplayName());
            newUser.setUserId(user.getUserId());
            newFollowing.add(newUser);
        }
        blogUser.setFollowing(newFollowing);

        for (BlogHistory blogHistory : blogUser.getBlogHistories()) {
            blogHistory.setBlogUser(null);
            blogHistory.getBlog().setBlogUser(null);
            blogHistory.getBlog().setBlogComments(null);
            blogHistory.getBlog().setBlogHistories(null);
            // blogHistory.setReadDate(null);
        }

        for (Blog blog : blogUser.getPostedBlogs()) {
            List<Comment> simpleComments = new ArrayList<>();
            if (blog.getBlogComments() != null) {
                for (Comment comment : blog.getBlogComments()) {
                    Comment simpleComment = new Comment();
                    simpleComment.setCommentContent(comment.getCommentContent());
                    simpleComment.setCommentId(comment.getCommentId());
                    simpleComment.setCommentBlog(null);

                    BlogUser blogUser1 = new BlogUser();
                    comment.getCommentBlogUser();
                    blogUser1.setDisplayName((comment.getCommentBlogUser().getDisplayName()));
                    blogUser1.setUserId(comment.getCommentBlogUser().getUserId());

                    simpleComment.setCommentBlogUser(blogUser1);
                    simpleComments.add(simpleComment);
                }
            }

            blog.setBlogComments(simpleComments);
            blog.setBlogUser(null);
            blog.setBlogHistories(null);
        }

        return blogUser;
    }

}
