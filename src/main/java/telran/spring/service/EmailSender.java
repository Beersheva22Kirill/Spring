package telran.spring.service;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import telran.spring.model.EmailMessage;
import telran.spring.model.Message;
@Service
@Slf4j
public class EmailSender implements Sender {
	
			
	@Override
	public String send(Message message) {
		log.debug("Email service recieved message {}", message);
		System.out.println(message);
		String res = errorMessage;	
		if(message instanceof EmailMessage) {
			EmailMessage emailMessage = (EmailMessage) message;
			res = String.format("text: %s has been send to %s" , message.text, emailMessage.getEmailAddress());
		} else {
			throw new IllegalArgumentException(res);
		}
		return res;
	}

	@Override
	public String getType() {
		
		return "email";
	}

	@Override
	public Class<? extends Message> getMessageTypeObject() {
		
		return EmailMessage.class;
	}
	

}
