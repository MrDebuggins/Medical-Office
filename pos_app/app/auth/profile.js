'use client';

import { useState } from "react";

var State;

export function Profile({setState})
{
    State = setState;

    const role = localStorage.getItem("role");
    if(role == 0)
    {
        return (
            <div>
                <div className="header">
                    <button onClick={patientsHandler}>Patients</button>
                    <button onClick={doctorsHandler}>Doctors</button>
                </div>
                <h2>Admin</h2>
            </div>
          );
    }
    else if(role == 1)
    {
        var doctor = JSON.parse(localStorage.getItem("entity"));

        return (
            <div>
                <header>
                    <button onClick={patientsHandler}>Patients</button>
                </header>
                <h2>Doctor</h2>
                <h4>Lastname: {doctor.lastname}</h4>
                <h4>Dirstname: {doctor.firstname}</h4>
                <h4>Email: {doctor.email}</h4>
                <h4>Phone: {doctor.phone}</h4>
                <h4>Specialization: {doctor.specialization}</h4>
            </div>
          );
    }
    else
    {
        var patient = JSON.parse(localStorage.getItem("entity"));

        return (
            <div>
                <header>
                    <button onClick={doctorsHandler}>Doctors</button>
                </header>
                <h2>Patient</h2>
                <h4>CNP: {doctor.cnp}</h4>
                <h4>Lastname: {doctor.lastname}</h4>
                <h4>Dirstname: {doctor.firstname}</h4>
                <h4>Email: {doctor.email}</h4>
                <h4>Phone: {doctor.phone}</h4>
                <h4>Born: {doctor.born}</h4>    
            </div>
          );
    }
}



function patientsHandler()
{
    State(2);
}

function doctorsHandler()
{
    State(3);
}

