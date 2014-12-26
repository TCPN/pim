package pim;

import java.lang.String;
import java.util.Properties;  
import javax.mail.Authenticator;  
import javax.mail.Message;  
import javax.mail.MessagingException;  
import javax.mail.PasswordAuthentication;  
import javax.mail.Session;  
import javax.mail.Transport;  
import javax.mail.internet.InternetAddress;  
import javax.mail.internet.MimeMessage;  
  
public class SendMail extends java.lang.Thread {  
	public SendMail(String addressee, String mailTitle, String mailContent){
		
      
        final String username = "projectinmotion2014@gmail.com";  
        final String password = "20140923";
        
        
  
        Properties props = new Properties();  
        props.put("mail.smtp.host", "smtp.gmail.com");  
        props.put("mail.smtp.socketFactory.port", "465");  
        props.put("mail.smtp.socketFactory.class",  
                "javax.net.ssl.SSLSocketFactory");  
        props.put("mail.smtp.auth", "true");  
        props.put("mail.smtp.port", "465");  
  
        Session session = Session.getInstance(props, new Authenticator() {  
            protected PasswordAuthentication getPasswordAuthentication() {  
                return new PasswordAuthentication(username, password);  
            }  
        });  
  
        
        try {  
  
            Message message = new MimeMessage(session);  
            message.setFrom(new InternetAddress("projectinmotion2014@gmail.com.tw"));  
            message.setRecipients(Message.RecipientType.TO, InternetAddress  
                    .parse(addressee));  
            message.setSubject(mailTitle);  
            message  
                    .setText(mailContent);  
  
            Transport.send(message);  
            System.out.println("Done");  
  
        } catch (MessagingException e) {  
            throw new RuntimeException(e);  
        }
	}
    public static void main(String[] args) {}  
}  