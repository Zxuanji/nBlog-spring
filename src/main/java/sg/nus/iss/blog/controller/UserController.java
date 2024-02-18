package sg.nus.iss.blog.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import sg.nus.iss.blog.model.BlogUser;
import sg.nus.iss.blog.service.UserService;
import sg.nus.iss.blog.validator.AccountValidator;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    private AccountValidator accountValidator;

    @InitBinder
    private void initCourseBinder(WebDataBinder binder) {
        binder.addValidators(accountValidator);
    }

    @GetMapping("/settings")
    public String updateBlogUser(Model model, HttpSession sessionObj) {
        BlogUser activeUser = (BlogUser) sessionObj.getAttribute("activeUser");
        BlogUser user = userService.findUserById(activeUser.getUserId()).get();
        model.addAttribute("blogUser", user);
        model.addAttribute("activeUser", user);
        return "user-settings";
    }

    @PostMapping("/settings")
    public String updateBlogUser(@Valid @ModelAttribute("blogUser") BlogUser inUser, BindingResult bindingResult,
            HttpSession sessionObj, @RequestParam("myTechStack") String myTechStack, Model model) {
        if (!bindingResult.hasErrors()) {
            inUser.setMyTechStack(myTechStack);
            userService.saveUser(inUser);
            sessionObj.setAttribute("activeUser", inUser);
            model.addAttribute("updateSuccess", true);
        } else {
            model.addAttribute("updateSuccess", false);
        }
        return "redirect:/user/settings";
    }

    @GetMapping("/@{userDisplayName}")
    public String viewBlogUser(@PathVariable("userDisplayName") String displayName, Model model,
            HttpSession sessionObj) {
        BlogUser viewUser = userService.findUserByDisplayName(displayName);
        BlogUser activeUser = (BlogUser) sessionObj.getAttribute("activeUser");

        boolean followingViewUser = false;
        if (activeUser.getFollowing() != null) {
            for (BlogUser u : activeUser.getFollowing()) {
                if (u.getUserId() == viewUser.getUserId()) {
                    followingViewUser = true;
                }
            }
        }

        String[] viewUserTechStack;
        if (viewUser.getMyTechStack() != null) {
            viewUserTechStack = viewUser.getMyTechStack().split(",");
        } else {
            viewUserTechStack = new String[] {};
        }

        model.addAttribute("viewUserTechStack", viewUserTechStack);
        model.addAttribute("activeUserProfile", activeUser.equals(viewUser));
        model.addAttribute("activeUser", activeUser);
        model.addAttribute("viewUser", viewUser);
        model.addAttribute("followingViewUser", followingViewUser);
        model.addAttribute("following", viewUser.getFollowing().size());
        model.addAttribute("followers", viewUser.getFollowers().size());
        return "user-view";
    }

    @GetMapping("/follow")
    public String followBlogUser(@RequestParam(name = "userId") String userId, HttpSession sessionObj) {
        int viewUserId = Integer.parseInt(userId);
        BlogUser viewUser = userService.findUserById(viewUserId).get();

        BlogUser userFromSession = (BlogUser) sessionObj.getAttribute("activeUser");
        int activeUserId = userFromSession.getUserId();
        BlogUser activeUser = userService.findUserById(activeUserId).get();

        boolean isFollowing = false;
        for (BlogUser u : activeUser.getFollowing()) {
            if (u.getUserId() == viewUserId) {
                activeUser = userService.unfollowBlogUser(activeUserId, viewUserId);
                sessionObj.setAttribute("activeUser", activeUser);
                isFollowing = true;
                break;
            }
        }
        if (!isFollowing) { // if activeUser is currently not following viewUser
            activeUser = userService.followBlogUser(activeUserId, viewUserId);
            sessionObj.setAttribute("activeUser", activeUser);
        }
        return "redirect:/user/@" + viewUser.getDisplayName();
    }

    @PostMapping("/api/follow")
    public ResponseEntity<String> followBlogUserFromMobile(int viewUserId, int activeUserId) {
        BlogUser activeUser = userService.findUserById(activeUserId).get();

        boolean isFollowing = false;
        for (BlogUser u : activeUser.getFollowing()) {
            if (u.getUserId() == viewUserId) {
                activeUser = userService.unfollowBlogUser(activeUserId, viewUserId);
                isFollowing = true;
                break;
            }
        }
        if (!isFollowing) { // if activeUser is currently not following viewUser
            activeUser = userService.followBlogUser(activeUserId, viewUserId);
        }
        return ResponseEntity.ok("okok");
    }
}
