package a3.kmap165Engine.network.ghost_avatar;

import java.util.UUID;
import graphicslib3D.Vector3D;
import graphicslib3D.Matrix3D;
import sage.scene.TriMesh;
import sage.scene.shape.Pyramid;

public class GhostAvatar extends Pyramid{
   private UUID ghostID;
   private Matrix3D ghostPosition;
   public GhostAvatar(UUID ID, Matrix3D position){
      ghostID = ID;
      ghostPosition = position;
   }
   
   public UUID getGhostID(){
      return ghostID;
   }
   public void setGhostID(UUID ghostID){
      this.ghostID = ghostID;
   }
   
   public Matrix3D getGhostPostion(){
      return ghostPosition;
   }
   public void setGhostPosition(Matrix3D gM){
      gM.translate(-0.1f,0,0);
      setLocalTranslation(gM);
      updateWorldBound();
   }
}