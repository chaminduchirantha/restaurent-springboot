package lk.ijse.gdse.restaurentspringbootbackend.controller;

import lk.ijse.gdse.restaurentspringbootbackend.dto.MailDetailsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/mail")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:63342")
public class MailController {
    private final JavaMailSender mailSender;
    @PostMapping("send")
    @PreAuthorize("hasRole('ADMIN')")
    public String sendEmail(@RequestBody MailDetailsDto mailDetailsDTO) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("chaminduchirantha10@gmail.com");
            message.setTo(mailDetailsDTO.getToMail().trim());
            message.setSubject(mailDetailsDTO.getSubject());
            message.setText(mailDetailsDTO.getMassage());

            mailSender.send(message);
            return "Email sent successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error sending email: " + e.getMessage();
        }
    }
}
