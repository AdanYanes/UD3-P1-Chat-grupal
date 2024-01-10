package net.salesianos.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.Scanner;

import net.salesianos.client.threads.ServerListener;

public class ClientApp {
    public static void main(String[] args) throws Exception {
        
        int userOption = 0;
        final Scanner SCANNER = new Scanner(System.in);
        String msg = "";

        System.out.println("Bienvenido cliente");
        System.out.println("¿Cómo te llamas?");
        String username = SCANNER.nextLine();
        
        Socket socket = new Socket("localhost", 55000);
        ObjectOutputStream objOutStream = new ObjectOutputStream(socket.getOutputStream());
        objOutStream.writeUTF(username);

        ObjectInputStream objInStream = new ObjectInputStream(socket.getInputStream());
        ServerListener serverListener = new ServerListener(objInStream);
        serverListener.start();

        while (!msg.equals("bye")) {
            
            
            try {
                msg = SCANNER.nextLine();
                objOutStream.writeUTF(msg);
                SCANNER.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Continuamos...");
            }
        }

        SCANNER.close();

        objInStream.close();
        objOutStream.close();
        socket.close();
    }
}
