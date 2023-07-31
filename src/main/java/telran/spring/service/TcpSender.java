package telran.spring.service;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import telran.spring.model.Message;
import telran.spring.model.TcpMessage;
@Service
@Slf4j
public class TcpSender implements Sender {

	@Override
	public String send(Message message) {
		log.debug("TCP service received message {}", message);
		String res = errorMessage;
		if(message instanceof TcpMessage) {
			TcpMessage tcpMessage = (TcpMessage) message;
			res = String.format("TCP sender -  text: %s has been sent to host %s:%d", tcpMessage.text,
					tcpMessage.getHostName(), tcpMessage.getPort());
		}else {
			throw new IllegalArgumentException(res);
		}
		return res;
	}

	@Override
	public String getType() {
		
		return "tcp";
	}

	@Override
	public Class<? extends Message> getMessageTypeObject() {
		
		return TcpMessage.class;
	}

}
