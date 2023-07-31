package telran.spring.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import jakarta.validation.constraints.*;
import lombok.Data;

@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
	
@Data
public class Message {
	@Pattern(regexp = "[a-z]{3,5}", message = "type value mismatches pattern")
	@NotEmpty
	public String type;
	@NotEmpty
	public String text;

}
