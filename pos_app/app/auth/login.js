'use client';

import { useState } from "react";

var State;

export function Login({setState})
{
    State = setState;

    return (
        <div>
            <h2>Login</h2>
            <form onSubmit={signIn}>
                <label htmlFor="login">Username:</label><br/>
                <input type="text" name="login"/><br/>
                <label htmlFor="password">Password:</label><br/>
                <input type="password" name="password"/><br/>
                <input type="submit" name="auth"/>
            </form>
        </div>
      );
}

async function signIn(event)
{
    event.preventDefault();
    const formData = new FormData(event.currentTarget);
    const json = JSON.stringify(Object.fromEntries(formData));

    var response = fetch("http://localhost:8080/api/medical_office/login", 
    {
        method: "POST",
        body: json,
        headers: {'Content-Type': 'application/json'}
    });

    var stream = (await response).body;
    var byteArray = (await stream.getReader().read()).value;
    var str = new TextDecoder().decode(byteArray);

    try
    {
        var body = JSON.parse(str);
        localStorage.setItem("token", body.token);
        localStorage.setItem("role", body.role);
        localStorage.setItem("entity", body.entity);
    }
    catch(err)
    {
        localStorage.setItem("token", str);
        localStorage.setItem("role", 0);
    }

    State(1);
}