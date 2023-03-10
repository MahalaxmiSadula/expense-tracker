package com.example.expensetracker.service;


import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtUtility {

	private final String SECRET_KEY = "6B58703273357638792F423F4528482B4B6250655368566D597133743677397A";

	public String extractUserEmail(String jwtToken) {
		return extractClaim(jwtToken, Claims::getSubject);
	}

	// to extract single claim from jwt token
	public <T> T extractClaim(String jwtToken, Function<Claims, T> claimsResolver) {
		Claims claims = extractAllClaims(jwtToken);
		return claimsResolver.apply(claims);
	}

	// to extract all the claims from jwt token
	public Claims extractAllClaims(String jwtToken) {
		return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(jwtToken).getBody();
	}

	private Key getSigningKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	public boolean isTokenValid(String jwtToken, UserDetails userDetails) {
		return userDetails.getUsername().equals(extractUserEmail(jwtToken)) && !isTokenExpired(jwtToken);
	}

	public boolean isTokenExpired(String jwtToken) {
		return extractExpiration(jwtToken).before(new Date());
	}

	private Date extractExpiration(String jwtToken) {
		return extractClaim(jwtToken, Claims::getExpiration);
	}

	public String generateJwtToken(String username) {
		return generateJwtToken(new HashMap<String, Object>(), username);
	}

	private String generateJwtToken(Map<String, Object> extraClaims, String username) {
		return Jwts.builder().setClaims(extraClaims)
				.setExpiration(new Date(System.currentTimeMillis() + 10000 * 60 * 24))
				.setIssuedAt(new Date(System.currentTimeMillis())).setSubject(username).signWith(getSigningKey())
				.compact();
	}
}