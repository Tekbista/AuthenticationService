package com.tekbista.authentication.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.tekbista.authentication.entities.Address;
import com.tekbista.authentication.entities.State;
import com.tekbista.authentication.entities.User;
import com.tekbista.authentication.events.ForgetPasswordEvent;
import com.tekbista.authentication.events.PasswordResetSuccessEvent;
import com.tekbista.authentication.events.RegistrationCompleteEvent;
import com.tekbista.authentication.models.ResetPasswordRequest;
import com.tekbista.authentication.models.UserProfile;
import com.tekbista.authentication.repositories.StateRepository;
import com.tekbista.authentication.repositories.UserRepository;
import com.tekbista.authentication.security.JwtTokenHelper;




@Service
@Transactional
public class UserService implements IUserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private StateRepository stateRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@Autowired
	private JwtTokenHelper jwtTokenHelper;
	
	@Override
	public User registerUser(User user, String url) {
		
		/* Encode the password using bcrypt password encoder before
		   Saving into the database so that the encode password will
		   be saved in database.
		*/
		String hashPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(hashPassword);
		
		if(userRepository.save(user) != null) {
			publisher.publishEvent(new RegistrationCompleteEvent(user, url));
			return user;
		}else {
			return null;
		}
		
	}


	
	@Override
	public boolean validateVerificationToken(String token) {
		String email = "";
		User user = new User();
		if(token != null && !token.isEmpty()) {
			email = jwtTokenHelper.getUsernameFromToken(token);
			user = userRepository.findByEmail(email);
			
			if(user != null && jwtTokenHelper.validateToken(token, user)) {
				
				user.setEnabled(true);
				userRepository.save(user);
				return true;
			}
		}
		
		return false;
		
	}

	@Override
	public void forgetPassword(String email, String url) {
		
		publisher.publishEvent(new ForgetPasswordEvent(email, url));
		
	}

	
	@Override
	public boolean resetPassword(ResetPasswordRequest passwordRequest, String token) {
		
		if(passwordRequest.getPassword().matches(passwordRequest.getConfirmPassword()) && !token.isEmpty()) {
			
			User user = new User();
			user = userRepository.findByEmail(jwtTokenHelper.getUsernameFromToken(token));
			
			if(user != null && jwtTokenHelper.validateToken(token, user)) {
				user.setPassword(passwordRequest.getPassword());
				userRepository.save(user);
				String message = "We are asked to reset your password and your password is reset successfully. "
						+ "If this is not you, please reach out to our customer service @ customersuppport@ecommerce.com or call "
						+ "(###)-###-####";
				publisher.publishEvent(new PasswordResetSuccessEvent(user.getEmail(), message));
				return true;
			}
		}
		
		return false;
		
	}

	@Override
	public boolean existByEmail(String email) {
		 return userRepository.existsByEmail(email);
	}



	@Override
	public boolean isUserLoggedIn(String token) {
		if(token != null && !token.isEmpty() && token.startsWith("Bearer ")) {
			String jwtToken = token.substring(7);
			User user = userRepository.findByEmail(jwtTokenHelper.getUsernameFromToken(jwtToken));
			
			return jwtTokenHelper.validateToken(jwtToken, user);
		}
		return false;
	}



	@Override
	public UserProfile getUserDetails(String token) {
		User user = new User();
		UserProfile userProfile = new UserProfile();
		if(token != null && !token.isEmpty() && token.startsWith("Bearer ")) {
			String jwtToken = token.substring(7);
			user = userRepository.findByEmail(jwtTokenHelper.getUsernameFromToken(jwtToken));
			userProfile.setFirstName(user.getFirstName());
			userProfile.setLastName(user.getLastName());
			userProfile.setAddress(user.getAddress());
			userProfile.setPhone(user.getPhone());
		}
		
		return userProfile;
	}



	@Override
	public List<State> getAllStates() {
		return stateRepository.findAll();
	}



	@Override
	public UserProfile updateUserProfile(String token, UserProfile profile) {
		
		User user = new User();
		UserProfile userProfile = new UserProfile();
		if(token != null && !token.isEmpty() && token.startsWith("Bearer ")) {
			String jwtToken = token.substring(7);
			user = userRepository.findByEmail(jwtTokenHelper.getUsernameFromToken(jwtToken));
			user.setAddress(profile.getAddress());
			user.setPhone(profile.getPhone());
			user = userRepository.save(user);
			
			userProfile.setFirstName(user.getFirstName());
			userProfile.setLastName(user.getLastName());
			userProfile.setAddress(user.getAddress());
			userProfile.setPhone(user.getPhone());
		}
		
		return userProfile;
	}

}
