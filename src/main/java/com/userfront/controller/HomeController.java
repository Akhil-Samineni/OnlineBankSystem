package com.userfront.controller;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.userfront.Dao.RoleDao;
import com.userfront.domain.PrimaryAccount;
import com.userfront.domain.SavingsAccount;
import com.userfront.domain.User;
import com.userfront.domain.security.UserRole;
import com.userfront.service.UserService;

@Controller
public class HomeController {

	@Autowired
	private UserService userService;
	
	@Autowired
    private RoleDao roleDao;
	
	@RequestMapping("/")
	public String home() {
		System.out.print("home");
		return "redirect:/index";
	}
	
	@RequestMapping("/index")
	public String index() {
		System.out.print("index");
		return "index";
	}
	
	@RequestMapping(value="/signup", method=RequestMethod.GET)
	public String signup(Model model) {
		User user= new User();
		model.addAttribute("user",user);
		return "signup";
	}
	
	@RequestMapping(value="/signup", method=RequestMethod.POST)
	public String signupPost(@ModelAttribute("user") User user, Model model) {
		
		System.out.print(user.toString());
		if(userService.checkUserExists(user.getUsername(), user.getEmail())) {
			if(userService.checkUsernameExists(user.getUsername())) {
				model.addAttribute("usernameExists",true);
			}

			if(userService.checkUserEmailExists(user.getEmail())){
				model.addAttribute("emailExists",true);
			}
			return "signup";
		}else {
			
			 Set<UserRole> userRoles = new HashSet<>();
             userRoles.add(new UserRole(user, roleDao.findByName("ROLE_USER")));
             
			userService.createUser(user, userRoles);
			return "redirect:/";
		}
		
	}
	
	@RequestMapping("/userFront")
	public String userFront(Principal principal, Model model)
	{
		User user=userService.findByUsername(principal.getName());
		PrimaryAccount primaryAccount=user.getPrimaryAccount();
		SavingsAccount savingsAccount=user.getSavingsAccount();
		model.addAttribute("primaryAccount",primaryAccount);
		model.addAttribute("savingsAccount", savingsAccount);
		return "userFront";
	}
}
