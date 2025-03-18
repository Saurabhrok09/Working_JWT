package lazyprogrammer.jwtdemo.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AuthenticationResponse {
    final private String jwt;

	public String getJwt() {
		return jwt;
	}

	@Override
	public String toString() {
		return "AuthenticationResponse [jwt=" + jwt + "]";
	}

//	public AuthenticationResponse(String jwt) {
//		super();
//		this.jwt = jwt;
//	}
	
    
}
