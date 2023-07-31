package telran.spring.service;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import telran.spring.model.Message;
import telran.spring.model.SmsMessage;
@Service
@Slf4j
public class SmsSender implements Sender {

	@Override
	public String send(Message message) {
		log.debug("Sms service recieved message {}", message);
		String res = errorMessage;	
		if(message instanceof SmsMessage) {
			SmsMessage smsMessage = (SmsMessage) message;
			res = String.format("text: %s has been send to %s" , smsMessage.text, smsMessage.getPhoneNumber());
		}else {
			throw new IllegalArgumentException(res);
		}
		return res;
	}

	@Override
	public String getType() {
		
		return "sms";
	}

	@Override
	public Class<? extends Message> getMessageTypeObject() {
		
		return SmsMessage.class;
	}
	

}
