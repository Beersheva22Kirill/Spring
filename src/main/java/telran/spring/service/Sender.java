package telran.spring.service;

import telran.spring.model.Message;

public interface Sender {
	String errorMessage = "The message has wrong type";
	String send(Message message);
	String getType();
	Class<? extends Message> getMessageTypeObject();
}
