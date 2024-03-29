package net.salesianos.client.threads;

import java.io.IOException;
import java.io.ObjectInputStream;
public class ServerListener extends Thread{
    private ObjectInputStream objInStream;

    public ServerListener(ObjectInputStream socketObjectInputStream) {
        this.objInStream = socketObjectInputStream;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String newMsg = (String) this.objInStream.readObject();
                System.out.println(newMsg);
            }
        } catch (ClassNotFoundException e1) {
            System.out.println("No se puede crear el mensaje");
        } catch (IOException e2) {
            System.out.println("Se dejo de escuchar los envios del servidor.");
        }
    }
}