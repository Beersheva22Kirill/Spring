package telran.spring.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.spring.exceptions.NotFoundException;
import telran.spring.model.Message;
import telran.spring.service.Sender;

@RestController 
@RequestMapping("sender")
@RequiredArgsConstructor
@Slf4j
@Validated
public class SenderController {
	
	final ObjectMapper mapper;
	final List<Sender> sendersList;
	Map<String,Sender> sendersMap;
	
	@PostMapping //Post requests annotation
	String send(@RequestBody @Valid Message message) {
		log.debug("Controller recieved message " + message);
		Sender sender = sendersMap.get(message.type);
		String resWrong = message.type + " type not found";
		String resRight = null;
		String res = null;
		if (sender != null) {
			res = sender.send(message);	
		} else {
			throw new NotFoundException(resWrong);		
		}
		return res ;
	} 
	
	@GetMapping //get requests annotation without parameters
	Set<String> getTypes(){
		return sendersMap.keySet();
	}
	
	@GetMapping("type/{typeName}") //get requests annotation with parameters
	// example of request - http://localhost:8080/sender/type/sms
	boolean isTypeExistsPath(@PathVariable(name = "typeName") String type) {
		// @PathVariable(name = "typeName") if name of annotation path and name of parameter not exists
		log.debug("Type inside a path {}", type);
		return sendersMap.containsKey(type);
	}
	@GetMapping("type") //get requests annotation with parameters
	//example of request - http://localhost:8080/sender/type?type=sms
	boolean isTypeExistsParam(@RequestParam(name = "type", defaultValue = "") @NotEmpty String type) {
		log.debug("Type inside a parameter {}", type);
		return sendersMap.containsKey(type);
	}
	
	@PostConstruct
	void init() {	
		sendersMap = sendersList.stream().collect(Collectors.toMap(Sender::getType, s -> s));
		sendersList.forEach(s -> mapper.registerSubtypes(s.getMessageTypeObject()));
		log.info("Registration senders complited: {}",sendersMap.keySet() );
	}
	
	@PreDestroy
	void shutdown() {
		
		log.info("Application context closed");
	}
}
