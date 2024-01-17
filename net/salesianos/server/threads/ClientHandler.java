package net.salesianos.server.threads;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import net.salesianos.models.Message;

public class ClientHandler extends Thread {
  private ObjectInputStream clientObjInStream;
  private ObjectOutputStream clientObjOutStream;
  private ArrayList<ObjectOutputStream> connectedObjOutputStreamList;
  private String msg = "";

  public ClientHandler(ObjectInputStream clientObjInStream, ObjectOutputStream clientObjOutStream,
      ArrayList<ObjectOutputStream> connectedObjOutputStreamList) {
    this.clientObjInStream = clientObjInStream;
    this.clientObjOutStream = clientObjOutStream;
    this.connectedObjOutputStreamList = connectedObjOutputStreamList;
  }

  public String getMsg(){
    return msg;
  }

  @Override
  public void run() {
    String username = "";
    try {

      username = this.clientObjInStream.readUTF();

      while (true) {
        Message msgObj = (Message) this.clientObjInStream.readObject();
        msg = msgObj.getMessage();
        for (ObjectOutputStream otherObjOutputStream : connectedObjOutputStreamList){
          if(msg != null && msg.startsWith("msg:") && otherObjOutputStream != this.clientObjOutStream){
            msgObj.setMessage(msgObj.getMessage().substring(4));
            otherObjOutputStream.writeObject(msgObj.getFormattedMessage());
          }
        }
      }

    } catch (EOFException eofException) {
      this.connectedObjOutputStreamList.remove(this.clientObjOutStream);
      System.out.println("CERRANDO CONEXIÓN CON " + username.toUpperCase());
    } catch (IOException | ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }
}
