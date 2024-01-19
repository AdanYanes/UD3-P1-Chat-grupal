package net.salesianos.models;

import java.io.Serializable;

public class Message implements Serializable{
    private String date;
    private String name;
    private String message;

    public Message(String date, String name, String message) {
        this.date = date;
        this.name = name;
        this.message = message;
    }

    public Message() {
        //TODO Auto-generated constructor stub
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setName(String username) {
        this.name = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFormattedMessage(){
        return date + " " + name + ": " + message;
    }
 
}