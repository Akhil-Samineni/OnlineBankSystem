package com.userfront.service.UserServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.userfront.Dao.AppointmentDao;
import com.userfront.domain.Appointment;
import com.userfront.service.AppointmnetService;

@Service
public class AppointmentServiceImpl implements AppointmnetService {
	@Autowired 
	private AppointmentDao appointmentDao;

	@Override
	public Appointment createAppointment(Appointment appointment) {
		// TODO Auto-generated method stub
		return appointmentDao.save(appointment);
		 
	}

	@Override
	public List<Appointment> findAll() {
		// TODO Auto-generated method stub
		return appointmentDao.findAll();
	}

	@Override
	public Appointment findAppointment(Long id) {
		// TODO Auto-generated method stub
		
		return appointmentDao.findById(id).orElse(null);
	}

	@Override
	public void confirmAppointment(Long id) {
		// TODO Auto-generated method stub
		Appointment appointment = findAppointment(id);
        appointment.setConfirmed(true);
        appointmentDao.save(appointment);
		
	}
	
}
