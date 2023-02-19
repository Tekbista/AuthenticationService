package com.tekbista.authentication.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tekbista.authentication.entities.State;
import com.tekbista.authentication.entities.User;
import com.tekbista.authentication.exceptions.UserAlreadyExistException;
import com.tekbista.authentication.models.AuthRequest;
import com.tekbista.authentication.models.AuthResponse;
import com.tekbista.authentication.models.ResetPasswordRequest;
import com.tekbista.authentication.models.StringResponse;
import com.tekbista.authentication.models.UserProfile;
import com.tekbista.authentication.security.JwtTokenHelper;
import com.tekbista.authentication.services.UserService;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping(value = "/api/v1/auth", produces = "application/json")
public class AuthController {

	@Autowired
	private JwtTokenHelper jwtTokenHelper;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	
	@PostMapping("/register")
	public ResponseEntity<StringResponse> registerNewUser(@Valid @RequestBody User user, final HttpServletRequest request){
		StringResponse res = new StringResponse();
		
		if(userService.existByEmail(user.getEmail())) {
			throw new UserAlreadyExistException("User already exist with that email.");
		}
		
		User newUser = userService.registerUser(user, applicationUrl(request));
		if(newUser == null) {
			res.setMessage("Could not register user.");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
		}
		res.setMessage("User registered successfully. Please check your email for verification.");
		return ResponseEntity.status(HttpStatus.CREATED).body(res);
	}
	
	
	@PostMapping("/login")
	public ResponseEntity<AuthResponse> userAuthentication(@Valid @RequestBody AuthRequest authRequest) throws Exception{

		AuthResponse authResponse = new AuthResponse();
		try {
			this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
		} catch (BadCredentialsException e) {
			throw new BadCredentialsException("Invalid email/password.");
		}
		
		UserDetails userDetails = this.userDetailsService.loadUserByUsername(authRequest.getEmail());
		
		String token = this.jwtTokenHelper.generateToken(userDetails);
		authResponse.setToken(token);
		return ResponseEntity.status(HttpStatus.OK).body(authResponse);
	}
	
	@GetMapping("/verifyRegistration")
	public ResponseEntity<String> verifyRegistration(@RequestParam("token") String token){
		
		if(!userService.validateVerificationToken(token)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User could not verified");
		}
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User verified successfully. Now you can log in.");
	}
	
	
	@PostMapping("/forgetPassword")
	public ResponseEntity<String> forgetPassword(@RequestParam("email") String email, HttpServletRequest req){
		userService.forgetPassword(email, applicationUrl(req));
		
		return new ResponseEntity<String>(
				"If we find your email in our system, we will send the password reset link to your email. Please check your email.",
				HttpStatus.OK);
	}
	
	
	
	@PostMapping("/resetPassword")
	public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest, @RequestParam("token") String token){
		
		if(userService.resetPassword(resetPasswordRequest, token)) {
			return ResponseEntity.status(HttpStatus.CREATED).body("Password reset successfully. Now, you can log in with new password.");
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Could not verify user. Please reach out to the customer service.");
	}
	
	@GetMapping("/verifyLogin")
	public ResponseEntity<Boolean> verifyLogin(HttpServletRequest request){
		String token = request.getHeader("Authorization");
		System.out.println(request);
		Boolean isLoggedIn = userService.isUserLoggedIn(token);
		
		return ResponseEntity.status(HttpStatus.OK).body(isLoggedIn);
	}
	
	
	@GetMapping("/getUserProfile")
	public ResponseEntity<UserProfile> getUserProfile(HttpServletRequest request){
		String token = request.getHeader("Authorization");
		UserProfile user = userService.getUserDetails(token);
		 
		return ResponseEntity.status(HttpStatus.OK).body(user);
	}
	
	
	@PostMapping("/updateUserProfile")
	public ResponseEntity<UserProfile> updateUserProfile(HttpServletRequest request, @RequestBody UserProfile userProfile){
		String token = request.getHeader("Authorization");
		UserProfile profile = userService.updateUserProfile(token, userProfile);
		
		return ResponseEntity.status(HttpStatus.OK).body(profile);
	}
	
	
	@GetMapping("/getStates")
	public ResponseEntity<List<State>> getAllStates(){
		List<State> states = new ArrayList<>();
		states = userService.getAllStates();
		 
		return ResponseEntity.status(HttpStatus.OK).body(states);
	}
	
	
	private String applicationUrl(HttpServletRequest request) {
		return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	}
}
