package telran.spring.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import com.fasterxml.jackson.databind.ObjectMapper;
import telran.spring.controller.SenderController;
import telran.spring.model.Message;
import telran.spring.security.AuthorizationConfiguration;
import telran.spring.security.SenderAuthorizationConfiguration;
import telran.spring.security.jwt.JwtFilter;
import telran.spring.security.jwt.JwtUtil;
import telran.spring.security.jwt.model.LoginData;
import telran.spring.security.jwt.model.LoginResponse;
import telran.spring.service.Sender;
@Service //Annotation for MockSender add in Application context
class MockSender implements Sender {

	@Override
	public String send(Message message) {
		
		return "test";
	}

	@Override
	public String getType() {
		
		return "test";
	}

	@Override
	public Class<? extends Message> getMessageTypeObject() {
		
		return Message.class;
	}
	
}
@WithMockUser(roles = {"USER","ADMIN"})


@WebMvcTest(value = {SenderController.class,MockSender.class, SecurityConfiguration.class},
excludeFilters = @ComponentScan.Filter(
		type = FilterType.ASSIGNABLE_TYPE,
		classes = JwtFilter.class))//Annotation for Spring tests without applications beans, without implementations
//Parameters of annotation it is array of classes for add to Application context
class SendersControllerTest {
	
	Message message;
	@BeforeEach
	void setUp() {
		message = new Message();
		message.text = "test";
		message.type = "test";
	}
	
	@Autowired //Annotation for dependency injection
	MockMvc mockMvc; //VM Web server for spring tests
	@Autowired
	ObjectMapper mapper;
	String sendUrl = "http://localhost:8080/sender";
	String getTypesUrl = sendUrl;
	String isTypePath = String.format("%s/type", sendUrl);
	@Test
	void mockMvcExists() {
		assertNotNull(mockMvc);
	}
		
	@Test
	void sendRigtFlow() throws Exception {
		String messageJson = mapper.writeValueAsString(message);
		String response = getRequestBase(messageJson).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		assertEquals("test", response);
	}
	
	@Test
	@WithMockUser(roles = {"USER"})
	void sendFlow403() throws Exception {
		String messageJson = mapper.writeValueAsString(message);
		getRequestBase(messageJson).andExpect(status().isForbidden());
	}
	
	@Test
	void sendNotFoundFlow() throws Exception {
		message.type = "abc";
		String messageJson = mapper.writeValueAsString(message);
		String response = getRequestBase(messageJson).andExpect(status().isNotFound()).andReturn().getResponse().getContentAsString();
		assertEquals(message.type + " type not found", response);
	}
	
	@Test
	void sendNoValidFlow() throws Exception {
		message.type = "123";
		String messageJson = mapper.writeValueAsString(message);
		String response = getRequestBase(messageJson).andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
		assertTrue(response.contains("mismatches"));
	}
	
	@Test
	void getTypesTest() throws Exception {
		String responseJson = mockMvc.perform(get(getTypesUrl))
				.andDo(print()).andExpect(status().isOk())
		.andReturn().getResponse().getContentAsString();
		String[] typesResponse = mapper.readValue(responseJson, String[].class);
		assertArrayEquals(new String[] {"test"}, typesResponse);
	}
	
	@Test
	void isTypePathExists() throws Exception {
		String responseJson = mockMvc.perform(get(isTypePath + "/test"))
				.andDo(print()).andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		Boolean booleanResponse = mapper.readValue(responseJson, boolean.class);
		assertTrue(booleanResponse);
	}
	
	@Test
	void isTypePathNoExists() throws Exception {
		String responseJson = mockMvc.perform(get(isTypePath + "/test1"))
				.andDo(print()).andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		Boolean booleanResponse = mapper.readValue(responseJson, boolean.class);
		assertFalse(booleanResponse);
	}
	
	@Test
	void isTypePathParamExists() throws Exception {
		String responseJson = mockMvc.perform(get(isTypePath + "?type=test"))
				.andDo(print()).andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		Boolean booleanResponse = mapper.readValue(responseJson, boolean.class);
		assertTrue(booleanResponse);
	}
	
	@Test
	void isTypePathParamNoExists() throws Exception {
		String responseJson = mockMvc.perform(get(isTypePath + "?type=test1")).andDo(print()).andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		Boolean booleanResponse = mapper.readValue(responseJson, boolean.class);
		assertFalse(booleanResponse);
	}
	
	@Test
	void isTypePathParamEmpty() throws Exception {
		String responseJson = mockMvc.perform(get(isTypePath)).andDo(print()).andExpect(status().isBadRequest())
				.andReturn().getResponse().getContentAsString();
		assertTrue(responseJson.contains("must not be empty"));
	}

	private ResultActions getRequestBase(String messageJson) throws Exception {
		return mockMvc.perform(post(sendUrl).contentType(MediaType.APPLICATION_JSON).content(messageJson)).andDo(print());
	}

}
