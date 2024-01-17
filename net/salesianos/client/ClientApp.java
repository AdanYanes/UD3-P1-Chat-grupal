package net.salesianos.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.Scanner;

import net.salesianos.client.threads.ServerListener;
import net.salesianos.models.Message;

public class ClientApp {
    public static void main(String[] args) throws Exception {
        int userOption = 0;
        final Scanner SCANNER = new Scanner(System.in);
        
        System.out.println("¿Cómo te llamas?");
        String username = SCANNER.nextLine();
        Socket socket = new Socket("localhost", 55000);

        ObjectOutputStream objOutStream = new ObjectOutputStream(socket.getOutputStream());
        objOutStream.writeUTF(username);
        
        ObjectInputStream objInStream = new ObjectInputStream(socket.getInputStream());
        ServerListener serverListener = new ServerListener(objInStream);
        serverListener.start();

        while (userOption != -1) {
            System.out.println("Indique el mensaje: ");
            Message msg = new Message();
            msg.setName(username);
            String content = SCANNER.nextLine();

            if (content.equalsIgnoreCase("bye")){
                userOption = -1;
            }else{
                msg.setMessage(content);
                msg.setDate(LocalTime.now().format(DateTimeFormatter.ISO_TIME));
            }
            objOutStream.writeObject(msg);
        }
        SCANNER.close();
        objOutStream.close();
        socket.close();
    }
}