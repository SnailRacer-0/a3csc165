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
   private Matrix3D ghostMatrix, newGhostMatrix, currentGhostMatrix;
   private boolean created = false;
   private MyClient theClient;
   private TriMesh theMesh;
   private OBJLoader objectLoader = new OBJLoader();
   public GhostAvatar(UUID ID, Vector3D position, MyClient client){
	  theMesh = objectLoader.loadModel("./a3/kmap165Engine/external_models/albertTestMesh.obj");
	  this.setVertexBuffer(theMesh.getVertexBuffer());
     this.setIndexBuffer(theMesh.getIndexBuffer());
	  /*Matrix3D theMeshT = theMesh.getLocalTranslation();
      theMeshT.translate(70, 0, 50);
      setLocalTranslation(theMeshT);*/
      
	  Matrix3D theMeshS = theMesh.getLocalScale();
      theMeshS.scale(.3, 0.3, .3);
      setLocalScale(theMeshS);
      
	   
      ghostID = ID;
      ghostPosition = position;
      theClient = client;
      
      ghostMatrix = this.getLocalTranslation();
      ghostMatrix.translate(ghostPosition.getX() + 20.0, ghostPosition.getY(), ghostPosition.getZ() + 20.0);
      
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
      //System.out.println(gV);
      newGhostMatrix = new Matrix3D();
      //currentGhostMatrix = ghostMatrix.getLocalTranslation();
      
      newGhostMatrix.translate(gV.getX(), gV.getY(), gV.getZ());
	   //currentGhostMatrix.concatenate(newGhostMatrix);
      //System.out.println(" 7778" + ghostMatrix); 
      //setLocalTranslation(currentGhostMatrix);
      setLocalTranslation(newGhostMatrix);
      updateLocalBound();
      updateWorldBound();
      //if(theClient != null) theClient.sendMoveMessage(this.getLocalTranslation().getCol(3));
   }
}