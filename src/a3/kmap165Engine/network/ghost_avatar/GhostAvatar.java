package a3.kmap165Engine.network.ghost_avatar;

import java.util.UUID;
import graphicslib3D.Vector3D;
import graphicslib3D.Matrix3D;
import sage.scene.TriMesh;
import sage.scene.shape.Pyramid;
import sage.scene.shape.Teapot;
import a3.kmap165Engine.network.*;

public class GhostAvatar extends Teapot{
   private UUID ghostID;
   private Vector3D ghostPosition;
   private Matrix3D ghostMatrix;
   private boolean created = false;
   private MyClient theClient;
   public GhostAvatar(UUID ID, Vector3D position, MyClient client){
      ghostID = ID;
      ghostPosition = position;
      theClient = client;
      ghostMatrix = this.getLocalTranslation();
      ghostMatrix.setCol(3, ghostPosition);
      setLocalTranslation(ghostMatrix);
      updateWorldBound();
   }
   public boolean isCreated(){
      return created;
   }
   public void setCreated(boolean created){
      this.created = created;
   }
   public UUID getGhostID(){
      return ghostID;
   }
   public void setGhostID(UUID ghostID){
      this.ghostID = ghostID;
   }
   
   public Vector3D getGhostPosition(){
      return ghostPosition;
   }
   public void setGhostPosition(Vector3D gV){
      ghostMatrix.setCol(3, gV);
      setLocalTranslation(ghostMatrix);
      updateWorldBound();
      if(theClient != null) theClient.sendMoveMessage(this.getLocalTranslation().getCol(3));
   }
}