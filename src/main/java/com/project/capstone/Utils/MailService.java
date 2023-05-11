package com.project.capstone.Utils;

import com.project.capstone.Models.User;
import com.project.capstone.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Random;

@Service
public class MailService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    JavaMailSender MailSender;

    public void register(User user) throws MessagingException,UnsupportedEncodingException{
        Random r = new Random();
        int n = r.nextInt();
        String code = Integer.toHexString(n);
        user.setVerifyotp(code);
        userRepository.save(user);
        sendVerificationMail(user,code);
    }

    public void sendVerificationMail(User user,String code) throws MessagingException, UnsupportedEncodingException {
        String toAddress = user.getEmail();
        String fromAddress = "vidhudanzz@gmail.com";
        String senderName = "Vidhyasakar";
        String subject = "Please verify your mail for registration";
        String content = "Dear [[name]],<br>"
                +"Please click the link below to verify your registration:<br>"
                +"<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                +"Thank You,<br>"
                +"Project Backend.";
        MimeMessage message = MailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(fromAddress,senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);
        content = content.replace("[[name]]", user.getFullname());
        String verifyUrl ="http://127.0.0.1:8080/restapi/verifysignup?code="+code+"-"+user.getEmail();
        content = content.replace("[[URL]]",verifyUrl);
        helper.setText(content,true);
        MailSender.send(message);
    }

    public void forgotPassword(User user) throws MessagingException,UnsupportedEncodingException{
        Random r= new Random();
        int n = r.nextInt();
        String code = Integer.toHexString(n);
        user.setVerifyotp(code);
        System.out.println(code);
        userRepository.save(user);
        sendForgotMail(user,code);
    }

    public void sendForgotMail(User user,String code)throws MessagingException,UnsupportedEncodingException{
        String toAddress = user.getEmail();
        String fromAddress = "vidhudanzz@gmail.com";
        String senderName = "Vidhyasakar";
        String subject = "Forgot Password";
        String content = "Dear [[name]],<br>" +
                "Please click the link blow to change your password:<br><br>"+
                "<h3><a href=\"[[URL]]\" target=\"_self\">Click Me to Change Password</a></h3><br><br>"+
                "Thank you,<br>"+
                "vidhyasakar";

        MimeMessage message = MailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(fromAddress,senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);
        content = content.replace("[[name]]", user.getFullname());
        String verifyUrl = "http://127.0.0.1:8080/restapi/password/verify?code="+code+"-"+user.getEmail();
        content = content.replace("[[URL]]",verifyUrl);
        helper.setText(content,true);
        MailSender.send(message);
    }
//    @Scheduled(cron = "0/15 * * * * * ")
    public void newsLetter()throws MessagingException,UnsupportedEncodingException{
        String toAddress = "";
        String fromAddress = "vidhudanzz@gmail.com";
        String senderName = "Capstone";
        String subject = "Christmas sale";
        String content = "Dear [[name]] ,<br>"
                + "Buy one get one free..";
        List<User>users= (List<User>) userRepository.findAll();
        for (User usersForMail:users){
            if (usersForMail.getEnabled()){
                toAddress = usersForMail.getEmail();
                MimeMessage message = MailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message);
                helper.setFrom(fromAddress,senderName);
                helper.setTo(toAddress);
                helper.setSubject(subject);
                content= content.replace("[[name]]",usersForMail.getFullname());
                helper.setText(content,true);
                MailSender.send(message);
                System.out.println("Newsletter Sended");
            }
        }
    }
}
