package telran.spring.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.RequestBuilder;

import telran.spring.model.EmailMessage;
import telran.spring.model.TcpMessage;
import telran.spring.service.EmailSender;

@SpringBootTest(classes = {EmailSender.class})

class EmailSenderTest {
	@Autowired
	EmailSender sender;
	
	@Test
	void emailSenderRigthFlow() {
		EmailMessage message = new EmailMessage();
		message.emailAddress = "kirill@gmail.com";
		message.type = "email";
		message.text = "test";
		String response = sender.send(message);
		String expected = String.format("text: %s has been send to %s" , message.text, message.emailAddress);
		assertEquals(expected, response);
	}
	
	@Test
	void emailSenderWrongType() {
		TcpMessage message = new TcpMessage();
		message.setHostName("kirill@gmail.com");
		message.setPort(3000);
		message.type = "tcp";
		message.text = "test";
		assertThrowsExactly(IllegalArgumentException.class,() -> sender.send(message));	
	}
	
}
