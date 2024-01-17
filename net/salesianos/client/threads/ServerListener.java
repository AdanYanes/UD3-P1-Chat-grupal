package net.salesianos.client.threads;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import net.salesianos.models.Message;

public class ServerListener extends Thread{
    private ObjectInputStream objInStream;
    private ArrayList<Message> msgList;

    public ServerListener(ObjectInputStream socketObjectInputStream) {
        this.objInStream = socketObjectInputStream;
    }

    @Override
    public void run() {
        try {
            //msgList = (ArrayList<Message>)this.objInStream.readObject();

            if(msgList != null){
                for (Message message : msgList) {
                    System.out.println(message.getFormattedMessage());
                }
            }

            while (true) {
                Message newMsg = (Message) this.objInStream.readObject();
                System.out.println(newMsg.getFormattedMessage());
            }
        } catch (ClassNotFoundException e1) {
            System.out.println("No se puede crear el mensaje");
        } catch (IOException e2) {
            System.out.println("Se dejo de escuchar los envios del servidor.");
        }
    }
}