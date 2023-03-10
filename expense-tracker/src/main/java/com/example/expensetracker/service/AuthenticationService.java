package com.example.expensetracker.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.expensetracker.model.AuthenticationRequest;
import com.example.expensetracker.model.AuthenticationResponse;
import com.example.expensetracker.model.RegisterRequest;
import com.example.expensetracker.model.Role;
import com.example.expensetracker.model.Token;
import com.example.expensetracker.model.TokenType;
import com.example.expensetracker.model.User;
import com.example.expensetracker.repository.TokenRepository;
import com.example.expensetracker.repository.UsersRepository;

@Service
public class AuthenticationService {

	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private UsersRepository usersRepository;
	@Autowired
	private TokenRepository tokenRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private JwtUtility jwtUtility;

	public AuthenticationResponse register(RegisterRequest request) {
		User user = new User();
		user.setEmail(request.getEmail());
		user.setEncryptedPassword(passwordEncoder.encode(request.getPassword()));
		user.setFirstName(request.getFirstName());
		user.setLastName(request.getLastName());
		user.setRole(Role.USER);

		User savedUser = usersRepository.save(user);
		String jwtToken = jwtUtility.generateJwtToken(savedUser.getEmail());
		saveUserToken(savedUser, jwtToken);
		return new AuthenticationResponse(jwtToken);
	}

	public AuthenticationResponse authenticate(AuthenticationRequest request) {

		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

		// if user is authenticated this is executed
		User user = usersRepository.findByEmail(request.getEmail()).orElseThrow();
		revokeAllUserTokens(user);
		String token = jwtUtility.generateJwtToken(user.getEmail());
		saveUserToken(user, token);
		return new AuthenticationResponse(token);

	}

	private void saveUserToken(User savedUser, String token) {
		Token newToken = new Token();
		newToken.setExpired(false);
		newToken.setRevoked(false);
		newToken.setToken(token);
		newToken.setTokenType(TokenType.BEARER);
		newToken.setUser(savedUser);

		tokenRepository.save(newToken);
	}

	private void revokeAllUserTokens(User user) {
		List<Token> validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
		if (validUserTokens.isEmpty())
			return;
		validUserTokens.forEach(token -> {
			token.setExpired(true);
			token.setRevoked(true);
		});
		tokenRepository.saveAll(validUserTokens);
	}
}