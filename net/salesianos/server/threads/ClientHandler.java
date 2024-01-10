package net.salesianos.server.threads;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter; 

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
        msg = clientObjInStream.readUTF();
        for (ObjectOutputStream otherObjOutputStream : connectedObjOutputStreamList){
          if(msg.startsWith("msg:") && otherObjOutputStream != this.clientObjOutStream){
            otherObjOutputStream.writeUTF(LocalTime.now().format(DateTimeFormatter.ISO_TIME) +  username + ": " + msg.substring(4));
          }
        }

        for (ObjectOutputStream otherObjOutputStream : connectedObjOutputStreamList) {
          if (otherObjOutputStream != this.clientObjOutStream) {
          }
        }

      }

    } catch (EOFException eofException) {
      this.connectedObjOutputStreamList.remove(this.clientObjOutStream);
      System.out.println("CERRANDO CONEXIÓN CON " + username.toUpperCase());
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }
}
