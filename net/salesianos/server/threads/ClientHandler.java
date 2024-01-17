package net.salesianos.server.threads;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import net.salesianos.models.Message;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter; 

public class ClientHandler extends Thread {
  private ObjectInputStream clientObjInStream;
  private ObjectOutputStream clientObjOutStream;
  private ArrayList<ObjectOutputStream> connectedObjOutputStreamList;
  private ArrayList<Message> msgList = new ArrayList<>();
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

      if(!msgList.isEmpty()){
        this.clientObjOutStream.writeObject(msgList);
      }

      while (true) {
        Message msgObj = (Message) this.clientObjInStream.readObject();
        msg = msgObj.getMessage();
        for (ObjectOutputStream otherObjOutputStream : connectedObjOutputStreamList){
          if(msg.startsWith("msg:")){
            msgObj.setMessage(msgObj.getMessage().substring(4));
            msgList.add(msgObj);
          }
          if(otherObjOutputStream != this.clientObjOutStream){
            System.out.println(msgObj.getFormattedMessage());
            otherObjOutputStream.writeObject(msgObj);
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
