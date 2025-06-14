package com.andreafueyo.TFCesteticien_AndreaGomezFueyo.control;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/session")
public class SessionController {

    
    @GetMapping("/create")
    public String createSession(HttpSession session) {
        session.setAttribute("username", "JohnDoe");

        
        String sessionId = session.getId();
        return "Session created with ID: " + sessionId;
    }

    
    @GetMapping("/get")
    public String getSession(HttpSession session) {
        String username = (String) session.getAttribute("username");

        if (username == null) {
            return "No session found!";
        }

        return "Session found with username: " + username;
    }

    @GetMapping("/invalidate")
    public String invalidateSession(HttpSession session) {
        session.invalidate();
        return "Session invalidated!";
    }
}