package telran.spring.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Data;

@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
	
@Data
public class Message {
	
	public String type;
	public String text;

}
