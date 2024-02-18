package sg.nus.iss.blog.validator;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import sg.nus.iss.blog.model.BlogUser;
import sg.nus.iss.blog.service.UserService;

@Component
public class AccountValidator implements Validator {
    
    @Autowired
    UserService userService;

    @Override
    public boolean supports(Class<?> clazz){
        return BlogUser.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors){
        BlogUser user = (BlogUser) obj;
        Optional<BlogUser> optUserFromDatabase = userService.findUserById(user.getUserId());
        BlogUser userFromDatabase = new BlogUser();
        if (optUserFromDatabase.isPresent()) {
            userFromDatabase = optUserFromDatabase.get();
        }

        List<BlogUser> allUsers = userService.findAllUsers();
        if (allUsers != null) {
            for (BlogUser u: allUsers) {
                // for creation of new user account
                if (user.getUserId() == 0) {
                    if (user.getEmail().equalsIgnoreCase(u.getEmail())) {
                        errors.rejectValue("email", "error.email", "A user is already registered with this e-mail address.");
                    }
                    if (user.getDisplayName().equalsIgnoreCase(u.getDisplayName())) {
                        errors.rejectValue("displayName", "error.displayName", "Display name cannot be used. Please choose another display name.");
                    }
                }
                // for updating of user profile - validate if there is change to email
                else {
                    if (!user.getEmail().equalsIgnoreCase(userFromDatabase.getEmail()) && user.getEmail().equalsIgnoreCase(u.getEmail())) {
                        errors.rejectValue("email", "error.email", "A user is already registered with this e-mail address.");
                    }
                    if (!user.getDisplayName().equalsIgnoreCase(userFromDatabase.getDisplayName()) && user.getDisplayName().equalsIgnoreCase(u.getDisplayName())) {
                        errors.rejectValue("displayName", "error.displayName", "Display name cannot be used. Please choose another display name.");
                    }
                }
            }
        }
    }
}
