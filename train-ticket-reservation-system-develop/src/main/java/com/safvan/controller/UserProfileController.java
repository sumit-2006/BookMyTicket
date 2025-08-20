package com.safvan.controller;

import java.util.Base64;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.safvan.beans.User;
import com.safvan.constants.endpoints.UserProfileEndpoints;
import com.safvan.service.mvc.ILoginManagementService;

/**
 * The UserProfileController handles all the functionalities related to user
 * profiles.
 * 
 * The controller is responsible for retrieving the user data and passing it to
 * the view for rendering.
 * 
 * 
 * @author Safvan
 * @version 1.0
 * @since 1.0
 */
@Controller
@RequestMapping(UserProfileEndpoints.USER_PROFILE_BASE_URI)
public class UserProfileController {

	@Autowired
	private ILoginManagementService loginManagementService;

	/**
	 * Displays the user profile based on the session ID.
	 * 
	 * @param session The HttpSession object to retrieve the session ID.
	 * @param model   The model object to pass data to the view.
	 * @return The view name for displaying the user profile.
	 */
	@GetMapping(UserProfileEndpoints.VIEW_USER_PROFILE)
	public String showUserProfile(
			HttpSession session, 
			Map<String, Object> model) {

		String sessionId = (String) session.getAttribute("sessionId");

		User user = null;
		if (sessionId != null) {
			// retrieve the user based on the session ID from the database
			user = loginManagementService.getUserbySessionId(sessionId);
		}

		if (Objects.nonNull(sessionId) && Objects.nonNull(user)) {
			model.put("user", user);
			byte[] imageByteArray = user.getUserProfile().getImage();
			/*
			 * Note: The user's profile image is encoded as a byte array in the database.
			 * here we are converting the image byte array to Base64 encoding before passing
			 * it to the view.
			 */
			if (Objects.nonNull(imageByteArray)) {
				model.put("userImage", Base64.getEncoder().encodeToString(imageByteArray));
			}
			// If imageByteArray is null or empty, no action needed to add "userImage" to
			// model
		}
		return "display_profile";
	}
}
