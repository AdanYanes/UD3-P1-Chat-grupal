package net.salesianos.client.threads;

import java.io.ObjectInputStream;


public class ServerListener extends Thread {

private ObjectInputStream objInStream;

  public ServerListener(ObjectInputStream socketObjectInputStream) {
    this.objInStream = socketObjectInputStream;
  
  }

  @Override
  public void run() {
    try {
      while(true) {
        System.out.println(this.objInStream.readUTF());
      }
    } catch (Exception e) {
      // TODO: handle exception
    }
    
  }
}
