package com.seerpharma.workmate.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.seerpharma.workmate.model.Account;
import com.seerpharma.workmate.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seerpharma.workmate.model.ERole;
import com.seerpharma.workmate.model.Role;
import com.seerpharma.workmate.model.User;
import com.seerpharma.workmate.payload.request.LoginRequest;
import com.seerpharma.workmate.payload.request.SignupRequest;
import com.seerpharma.workmate.payload.response.JwtResponse;
import com.seerpharma.workmate.payload.response.MessageResponse;
import com.seerpharma.workmate.repository.RoleRepository;
import com.seerpharma.workmate.repository.UserRepository;
import com.seerpharma.workmate.security.jwt.JwtUtils;
import com.seerpharma.workmate.security.service.impl.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	CompanyRepository companyRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		String accessToken = jwtUtils.generateAccessToken(userDetails.getUsername());
		String refreshToken = jwtUtils.generateRefreshToken(userDetails.getUsername());
		
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());

		return ResponseEntity.ok(new JwtResponse(accessToken,
												 refreshToken,
												 jwtUtils.getJwtAtExpirationMs(),
												 jwtUtils.getJwtRtExpirationMs(),
												 userDetails.getId(), 
												 userDetails.getUsername(), 
												 userDetails.getEmail(), 
												 roles, userDetails.getAccountId()));
	}

	@PostMapping("/signup")
	//@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse(HttpStatus.BAD_REQUEST.value(), "Error: Username is already taken!"));
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse(HttpStatus.BAD_REQUEST.value(), "Error: Email is already in use!"));
		}

		Account company = companyRepository.findByShortName(signUpRequest.getCompanyShortName())
				.orElseThrow(() -> new RuntimeException("Error: Company does not exist"));

		// Create new user's account
		User user = new User(signUpRequest.getUsername(), 
							 signUpRequest.getEmail(),
							 encoder.encode(signUpRequest.getPassword()),
							 System.currentTimeMillis(),
							 company);

		Set<String> strRoles = signUpRequest.getRoles();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);

					break;
				default:
					Role userRole = roleRepository.findByName(ERole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);
				}
			});
		}

		user.setRoles(roles);
		userRepository.save(user);

		return ResponseEntity.ok(new MessageResponse(HttpStatus.CREATED.value(), "User registered successfully!"));
	}
}