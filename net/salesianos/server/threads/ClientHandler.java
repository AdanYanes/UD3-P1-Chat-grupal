package net.salesianos.server.threads;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import net.salesianos.models.Message;
import net.salesianos.utils.Chat;

public class ClientHandler extends Thread {
  private ObjectInputStream clientObjInStream;
  private ObjectOutputStream clientObjOutStream;
  private ArrayList<ObjectOutputStream> connectedObjOutputStreamList;
  Chat chat;

  public ClientHandler(ObjectInputStream clientObjInStream, ObjectOutputStream clientObjOutStream,
      ArrayList<ObjectOutputStream> connectedObjOutputStreamList, Chat chat) {
    this.clientObjInStream = clientObjInStream;
    this.clientObjOutStream = clientObjOutStream;
    this.connectedObjOutputStreamList = connectedObjOutputStreamList;
    this.chat = chat;
  }

  @Override
  public void run() {
    String username = "";
    try {

      username = this.clientObjInStream.readUTF();

      ArrayList<String> msgList = chat.getChatList();

      this.clientObjOutStream.writeObject("Bienvenido al chat grupal " + username);

      if(!msgList.isEmpty()){
        this.clientObjOutStream.writeObject("El historial de mensajes es:");
        for (String msg : msgList) {
          this.clientObjOutStream.writeObject(msg);;
        }
      }

      while (true) {
        Message msgObj = (Message) this.clientObjInStream.readObject();
        if(msgObj.getMessage().startsWith("msg:")){
          msgObj.setMessage(msgObj.getMessage().substring(4));
          for (ObjectOutputStream otherObjOutputStream : connectedObjOutputStreamList){
            if(otherObjOutputStream != this.clientObjOutStream){
              otherObjOutputStream.writeObject(msgObj.getFormattedMessage());
            }else{
              chat.addMessage(msgObj.getFormattedMessage());
            }
          }
        }else{
          clientObjOutStream.writeObject("Error de formato en el mensaje");
        }
      }

    } catch (EOFException eofException) {
      this.connectedObjOutputStreamList.remove(this.clientObjOutStream);
      System.out.println("CERRANDO CONEXIÓN CON " + username.toUpperCase());
      for (ObjectOutputStream otherObjOutputStream : connectedObjOutputStreamList){
        if(otherObjOutputStream != this.clientObjOutStream){
          try {
            otherObjOutputStream.writeObject(username + " se ha desconectado");
          } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
        }
      }
    } catch (IOException | ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }
}
