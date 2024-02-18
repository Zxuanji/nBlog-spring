package sg.nus.iss.blog.service;

import sg.nus.iss.blog.model.EmailDetails;

public interface EmailService {
    
    String sendEmail(EmailDetails emailDetails);

    //String sendEmailWithAttachment(EmailDetails emailDetails);
}
