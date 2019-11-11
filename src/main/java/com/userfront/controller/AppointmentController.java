package com.userfront.controller;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.userfront.domain.Appointment;
import com.userfront.domain.User;
import com.userfront.service.AppointmnetService;
import com.userfront.service.UserService;

@Controller
@RequestMapping("/appointment")
public class AppointmentController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private AppointmnetService appointmnetService;
	
	@RequestMapping(value="/create", method=RequestMethod.GET)
	public String createAppointment(Model model) {
		Appointment appointment=new Appointment();
		model.addAttribute("appointment",appointment);
		model.addAttribute("dateString","");
		return "appointment";
	}
	
	@RequestMapping(value="/create", method=RequestMethod.POST)
	public String createAppointmentPOST(@ModelAttribute("appointment") Appointment appointment, @ModelAttribute("dateString") String dateString,
			Principal principal) throws ParseException {
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm");
		Date date=simpleDateFormat.parse(dateString);
		appointment.setDate(date);
		User user=userService.findByUsername(principal.getName());
		appointment.setUser(user);
		appointmnetService.createAppointment(appointment);
		return "redirect:/userFront";
	}
	
}
