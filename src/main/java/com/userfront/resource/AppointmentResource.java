package com.userfront.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.userfront.domain.Appointment;
import com.userfront.service.AppointmnetService;

@RestController
@RequestMapping("/api/appointment")
public class AppointmentResource {
	
	@Autowired
	private AppointmnetService appointmnetService;
	
	@RequestMapping("/all")
	public List<Appointment> findAppointmentList(){
		return appointmnetService.findAll();
	}
	
	@RequestMapping("/{id}/confirm")
	public void confirmAppointment(@PathVariable("id") Long id) {
		appointmnetService.confirmAppointment(id);
	}
}
