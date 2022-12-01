package com.tekbista.authentication.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenHelper {

	@Value("${security.jwt.token.expire-length}")
	private  Long JWT_TOKEN_VALIDITY;
	
	@Value("${security.jwt.token.secret-key}")
	private  String SECRET;
	
	
	// Retrieve email from JWT token
	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}
	
	
	// Retrieve expiration date from JWT token
	public Date getExiprationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}


	private Claims getAllClaimsFromToken(String token) {
		
		return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
	}
	
	
	// Check if the token has expired
	private Boolean isTokenExpired(String token) {
		final Date expiration = getExiprationDateFromToken(token);
		return expiration.before(new Date());
	}
	


	// Generation token for user
	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		
		return doGenerateToken(claims, userDetails.getUsername());
	}


	private String doGenerateToken(Map<String, Object> claims, String subject) {

		return Jwts.builder()
				.setClaims(claims)
				.setSubject(subject)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
				.signWith(SignatureAlgorithm.HS512, SECRET).compact();
				
	}
	
	
	// Validate token
	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = getUsernameFromToken(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
	
	
	
	
	
}
