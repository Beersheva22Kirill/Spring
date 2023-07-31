package telran.spring.model;

import jakarta.validation.constraints.*;
import lombok.Data;
@Data
public class TcpMessage extends Message {
String hostName;
@Min(1024) @Max(5000)
int port;
}
