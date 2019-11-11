package com.userfront.service;

import java.util.List;

import com.userfront.domain.Appointment;

public interface AppointmnetService {
	Appointment createAppointment(Appointment appointment);

    List<Appointment> findAll();

    Appointment findAppointment(Long id);

    void confirmAppointment(Long id);
    
}
