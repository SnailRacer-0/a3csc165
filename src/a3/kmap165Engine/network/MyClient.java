package a3.kmap165Engine.network;

import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;

import sage.networking.client.GameConnectionClient;
import sage.networking.server.IClientInfo;

import java.util.Vector;

import a3.games.fighter2015.FightingGame;
import a3.kmap165Engine.network.ghost_avatar.GhostAvatar;
import graphicslib3D.Vector3D;
import graphicslib3D.Matrix3D;
import a3.games.fighter2015.*;
import a3.kmap165Engine.network.ghost_avatar.*;
import java.lang.Double;

public class MyClient extends GameConnectionClient{ 
   private FightingGame game;
   private UUID id;
   private Vector<GhostAvatar> ghostAvatars;
   
   public MyClient(InetAddress remAddr, int remPort, ProtocolType pType,
      FightingGame game) throws IOException{ 
      
      super(remAddr, remPort, pType);
      this.game = game;
      this.id = UUID.randomUUID();
      this.ghostAvatars = new Vector<GhostAvatar>();
   }
   protected void processPacket (Object msg){ // override
      String message = (String) msg;
      String[] messageTokens = message.split(",");
      // extract incoming message into substrings. Then process:
      if(messageTokens[0].compareTo("join") == 0){ // receive “join”
         // format: join, success or join, failure
         if(messageTokens[1].compareTo("success") == 0){
            game.setIsConnected(true);
            System.out.println("success obtained");
            sendCreateMessage(game.getPlayerPosition());
         }
         if(messageTokens[1].compareTo("failure") == 0)
            System.out.println("failure obtained");
            game.setIsConnected(false);
      }
      if(messageTokens[0].compareTo("bye") == 0){ // receive “bye”
         // format: bye, remoteId
         UUID ghostID = UUID.fromString(messageTokens[1]);
         sendByeMessage();
         System.out.println("bye obtained");
         removeGhostAvatar(ghostID);
      }
      if (messageTokens[0].compareTo("dsfr") == 0 ){ // receive “details for”
         // format: create, remoteId, x,y,z or dsfr, remoteId, x,y,z
         UUID ghostID = UUID.fromString(messageTokens[1]);
         //Vector3D ghostPosition = new Vector3D();
         // extract ghost x,y,z, position from message, then:
         double[] values = {new Double(messageTokens[2]),new Double(messageTokens[3]),new Double(messageTokens[4]),new Double(messageTokens[5]),
                                     new Double(messageTokens[6]),new Double(messageTokens[7]),new Double(messageTokens[8]),new Double(messageTokens[9]),
                                     new Double(messageTokens[10]),new Double(messageTokens[11]),new Double(messageTokens[12]),new Double(messageTokens[13]),
                                     new Double(messageTokens[14]),new Double(messageTokens[15]),new Double(messageTokens[16]),new Double(messageTokens[17])};
         Matrix3D ghostPosition = new Matrix3D(values);
         System.out.println("dsfr obtained");
         createGhostAvatar(ghostID, ghostPosition);
      }
      if(messageTokens[0].compareTo("create") == 0){ // receive “create…”
         System.out.println("create obtained");
         // format: create, remoteId, x,y,z 
         UUID ghostID = UUID.fromString(messageTokens[1]);
         //String[] ghostPosition = {messageTokens[2],messageTokens[3],messageTokens[4]};
         //Vector3D ghostPosition = new Vector3D(new Double(messageTokens[2]), new Double(messageTokens[3]), new Double(messageTokens[4]));
         // extract ghost x,y,z, position from message, then:
         double[] values = {new Double(messageTokens[2]),new Double(messageTokens[3]),new Double(messageTokens[4]),new Double(messageTokens[5]),
                                     new Double(messageTokens[6]),new Double(messageTokens[7]),new Double(messageTokens[8]),new Double(messageTokens[9]),
                                     new Double(messageTokens[10]),new Double(messageTokens[11]),new Double(messageTokens[12]),new Double(messageTokens[13]),
                                     new Double(messageTokens[14]),new Double(messageTokens[15]),new Double(messageTokens[16]),new Double(messageTokens[17])};
         Matrix3D ghostPosition = new Matrix3D(values);
         createGhostAvatar(ghostID, ghostPosition);
      }
      if(messageTokens[0].compareTo("wsds") == 0){ // receive “wants…”
         System.out.println("wsds obtained");
         // format: wsds, remoteID
         UUID remoteID = UUID.fromString(messageTokens[1]);
         Vector3D pos = new Vector3D();
         sendDetailsForMessage(remoteID, pos);
      }
      if(messageTokens[0].compareTo("move") == 0){ // receive “move”
         // format: move, remoteId, x,y,z 
         UUID remoteID = UUID.fromString(messageTokens[1]);
         //Vector3D pos = new Vector3D(new Double(messageTokens[2]), new Double(messageTokens[3]), new Double(messageTokens[4]));
         double[] values = {new Double(messageTokens[2]),new Double(messageTokens[3]),new Double(messageTokens[4]),new Double(messageTokens[5]),
                                     new Double(messageTokens[6]),new Double(messageTokens[7]),new Double(messageTokens[8]),new Double(messageTokens[9]),
                                     new Double(messageTokens[10]),new Double(messageTokens[11]),new Double(messageTokens[12]),new Double(messageTokens[13]),
                                     new Double(messageTokens[14]),new Double(messageTokens[15]),new Double(messageTokens[16]),new Double(messageTokens[17])};
         Matrix3D ghostPosition = new Matrix3D(values);
         System.out.println("move obtained");
         updateGhostAvatar(remoteID, ghostPosition);
      }
   }
   public void sendCreateMessage(Matrix3D pos){
      // format: (create, localId, x,y,z)
      try{
         String message = new String("create," + id.toString());
         message += "," + pos.toString();
         System.out.println(message);
         sendPacket(message);
      }
      catch (IOException e) { 
         e.printStackTrace(); 
      }
   }
   public void sendJoinMessage(){ 
      // format: join, localId
      try{ 
         sendPacket(new String("join," + id.toString()));
         System.out.println("joined");
      }
      catch (IOException e) { 
         e.printStackTrace(); 
      }
   }
   public void sendByeMessage(){
      // format: bye, localId
      try{ 
         sendPacket(new String("bye," + id.toString()));
         System.out.println("bye"); 
      }
      catch (IOException e) { 
         e.printStackTrace(); 
      }
   }
   public void sendDetailsForMessage(UUID remId, Vector3D pos){
      // format: (dsfr, remId, localID, x,y,z)
      try{
         String message = new String("dsfr," + remId.toString()+"," + id.toString());
         message += "," + pos.getX()+"," + pos.getY() + "," + pos.getZ();
         sendPacket(message);
         System.out.println(message); 
      }
      catch (IOException e) { 
         e.printStackTrace(); 
      } 
   }
   public void sendMoveMessage(Matrix3D pos){
      // format: (move, localId, x,y,z)
      try{
         String message = new String("move," + id.toString());
         message += "," + pos.toString();
         System.out.println(message);
         sendPacket(message);
      }
      catch (IOException e) { 
         e.printStackTrace(); 
      }
   }
   private void removeGhostAvatar(UUID ghostID){
      for(GhostAvatar avatar : ghostAvatars){
         if(avatar.getGhostID() == ghostID){
            ghostAvatars.remove(avatar);
         }
      } 
   }
   private void createGhostAvatar(UUID ghostID, Matrix3D ghostPosition){
      ghostAvatars.add(new GhostAvatar(ghostID, ghostPosition));
   }
   private void updateGhostAvatar(UUID ghostID, Matrix3D pos){
      for(GhostAvatar avatar : ghostAvatars){
         if(avatar.getGhostID() == ghostID){
            avatar.setGhostPosition(pos);
         }
      }
   }   
}