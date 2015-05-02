package a3.kmap165Engine.network.ghost_avatar;

import java.util.UUID;
import graphicslib3D.Vector3D;
import graphicslib3D.Matrix3D;
import sage.scene.TriMesh;

import a3.kmap165Engine.network.*;

import sage.model.loader.OBJLoader;

public class GhostAvatar extends TriMesh{
   private UUID ghostID;
   private Vector3D ghostPosition;
   private Matrix3D ghostMatrix;
   private boolean created = false;
   private MyClient theClient;
   private TriMesh theMesh;
   private OBJLoader objectLoader = new OBJLoader();
   public GhostAvatar(UUID ID, Vector3D position, MyClient client){
	  theMesh = objectLoader.loadModel("src/a3/kmap165Engine/external_models/albertTestMesh.obj");
	  
	  /*Matrix3D theMeshT = theMesh.getLocalTranslation();
      theMeshT.translate(70, 0, 50);
      setLocalTranslation(theMeshT);*/
      
	  Matrix3D theMeshS = theMesh.getLocalScale();
      theMeshS.scale(.3, 0.3, .3);
      setLocalScale(theMeshS);
      
	  this.setVertexBuffer(theMesh.getVertexBuffer());
      this.setIndexBuffer(theMesh.getIndexBuffer()); 
      ghostID = ID;
      ghostPosition = position;
      theClient = client;
      
      ghostMatrix = this.getLocalTranslation();
      ghostMatrix.translate(ghostPosition.getX(), ghostPosition.getY(), ghostPosition.getZ());
      
      setLocalTranslation(ghostMatrix);
      
      updateWorldBound();
      updateLocalBound();
      updateGeometricState(0, true);
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
      //ghostMatrix.setCol(3, gV);
	   ghostMatrix.translate(gV.getX(), gV.getY(), gV.getZ()); 
      setLocalTranslation(ghostMatrix);
      updateWorldBound();
      if(theClient != null) theClient.sendMoveMessage(this.getLocalTranslation().getCol(3));
   }
}