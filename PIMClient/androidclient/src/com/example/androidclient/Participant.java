package com.example.androidclient;

import java.io.Serializable;

public class Participant implements Serializable {

    // ATTRIBUTES:
    public String name;
    public String email;
    public String role;
    public boolean presence;

    // CONSTRUCTOR for Front-End:
    public Participant(String name, String email, String role, boolean presence) {
        this.name = name;
        this.email = email;
        this.role = role;
        this.presence = presence;
    }
}
