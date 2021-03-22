package com.seerpharma.workmate.payload.response;

import java.util.List;

import com.seerpharma.workmate.util.Constants;

public class JwtResponse {
	private String accessToken;
	private String refreshToken;
	private int accessExpire;
	private int refreshExpire;
	private String tokenType = Constants.ACCESS_TOKEN_TYPE;
	private Long id;
	private String username;
	private String email;
	private List<String> roles;
	private Long accountId;

	public JwtResponse(String accessToken, String refreshToken, int	accessExpire, int refreshExpire, Long id,
			String username, String email, List<String> roles, Long accountId) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.accessExpire = accessExpire;
		this.refreshExpire = refreshExpire;
		this.id = id;
		this.username = username;
		this.email = email;
		this.roles = roles;
		this.accountId = accountId;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public int getAccessExpire() {
		return accessExpire;
	}

	public int getRefreshExpire() {
		return refreshExpire;
	}

	public String getTokenType() {
		return tokenType;
	}

	public Long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getEmail() {
		return email;
	}

	public List<String> getRoles() {
		return roles;
	}

	public Long getAccountId() {
		return accountId;
	}

}
