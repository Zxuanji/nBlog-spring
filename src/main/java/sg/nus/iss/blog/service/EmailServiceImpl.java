package sg.nus.iss.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import sg.nus.iss.blog.model.EmailDetails;

@Service
@Transactional
public class EmailServiceImpl  implements EmailService {
    
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @Override
    public String sendEmail(EmailDetails emailDetails) {
        try {
        // Create email
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(sender);
            mailMessage.setTo(emailDetails.getRecipient());
            mailMessage.setSubject(emailDetails.getSubject());
            mailMessage.setText(emailDetails.getMessageBody());

        // Send email
            javaMailSender.send(mailMessage);
            return "Mail Sent Successfully...";
        }
 
        // Catch block to handle the exceptions
        catch (Exception e) {
            return "Error while Sending Mail";
        }
    }
    
    //@Override
    /*public String sendEmailWithAttachment(EmailDetails emailDetails) {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;
        try {
            
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true); // multipart set as true for attachments to be send
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(emailDetails.getRecipient());
            mimeMessageHelper.setSubject(emailDetails.getSubject());
            mimeMessageHelper.setText(emailDetails.getMessageBody());
            
            // Adding the attachment
            FileSystemResource file = new FileSystemResource(new File(emailDetails.getAttachment()));
            mimeMessageHelper.addAttachment(
                file.getFilename(), file);
 
            // Sending the mail
            javaMailSender.send(mimeMessage);
            return "Mail sent Successfully";
        }
 
        // Catch block to handle MessagingException
        catch (MessagingException e) {
 
            // Display message when exception occurred
            return "Error while sending mail!!!";
        }
    }*/
}
