package net.salesianos.utils;

import java.util.ArrayList;

public class Chat {

    ArrayList<String> chatArrayList = new ArrayList<>();

    public Chat(){}

    public synchronized void addMessage(String message) {
        chatArrayList.add(message);
    }

    public ArrayList<String> getChatList(){
        return chatArrayList;
    }



    
}