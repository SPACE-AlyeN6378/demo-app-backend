package com.dsi.demoappbackend.email;

public interface EmailSender {
    void send(String to, String email);
}
