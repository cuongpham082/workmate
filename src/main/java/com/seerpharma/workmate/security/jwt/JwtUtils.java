package com.seerpharma.workmate.security.jwt;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtUtils {
	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

	@Value("${app.jwt.secret}")
	private String jwtSecret;

	@Value("${app.jwt.access.expiration.ms}")
	private int jwtAtExpirationMs;

	@Value("${app.jwt.refresh.expiration.ms}")
	private int jwtRtExpirationMs;

	public String generateAccessToken(String userName) {
		return Jwts.builder()
				.setSubject((userName))
				.setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + jwtAtExpirationMs))
				.signWith(SignatureAlgorithm.HS512, jwtSecret)
				.compact();
	}

	public String generateActiveToken(String userName) {
		return Jwts.builder()
				.setSubject((userName))
				.setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + (jwtRtExpirationMs*3)))
				.signWith(SignatureAlgorithm.HS512, jwtSecret)
				.compact();
	}

	public String generateRefreshToken(String userName) {
		return Jwts.builder()
				.setSubject((userName))
				.setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + jwtRtExpirationMs))
				.signWith(SignatureAlgorithm.HS512, jwtSecret)
				.compact();
	}

	public String getUserNameFromJwtToken(String token) {
		Jws<Claims> claimsJws = getClaimsJwsViaJwtToken(token);
		if (claimsJws != null) {
			return claimsJws.getBody().getSubject();
		} else {
			return null;
		}
	}

	public Date getExpirationFromJwtToken(String token) {
		Jws<Claims> claimsJws = getClaimsJwsViaJwtToken(token);
		if (claimsJws != null) {
			return claimsJws.getBody().getExpiration();
		} else {
			return null;
		}
	}

	public int getJwtAtExpirationMs() {
		return jwtAtExpirationMs;
	}

	public int getJwtRtExpirationMs() {
		return jwtRtExpirationMs;
	}

	public Jws<Claims> getClaimsJwsViaJwtToken(String authToken) {
		try {
			Jws<Claims> claimsJws = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			return claimsJws;
		} catch (SignatureException e) {
			logger.error("Invalid JWT signature: {}", e.getMessage());
		} catch (MalformedJwtException e) {
			logger.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			logger.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			logger.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.error("JWT claims string is empty: {}", e.getMessage());
		}

		return null;
	}
}
