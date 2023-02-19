package com.tekbista.authentication.services;

import java.util.List;

import com.tekbista.authentication.entities.State;
import com.tekbista.authentication.entities.User;
import com.tekbista.authentication.models.ResetPasswordRequest;
import com.tekbista.authentication.models.UserProfile;



public interface IUserService {

	User registerUser(User user, String url);
	boolean existByEmail(String email);
	boolean validateVerificationToken(String token);
	void forgetPassword(String email, String url);
	boolean resetPassword( ResetPasswordRequest passwordRequest, String token);
	boolean isUserLoggedIn(String token);
	UserProfile getUserDetails(String token);
	UserProfile updateUserProfile(String token, UserProfile profile);
	List<State> getAllStates();
	
}
