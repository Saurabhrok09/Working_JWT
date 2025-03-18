package lazyprogrammer.jwtdemo.dtos;

import lombok.Data;

@Data
public class UserDto {
    private long userId;
    private String username;
    private String password;
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public String toString() {
		return "UserDto [userId=" + userId + ", username=" + username + ", password=" + password + "]";
	}
	public UserDto(long userId, String username, String password) {
		super();
		this.userId = userId;
		this.username = username;
		this.password = password;
	}
    
}
