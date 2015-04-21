package a3.kmap165Engine.network.ghost_avatar;

import java.util.UUID;
import graphicslib3D.Vector3D;
import graphicslib3D.Matrix3D;
import sage.scene.TriMesh;
<<<<<<< HEAD

import a3.kmap165Engine.network.*;

import sage.model.loader.OBJLoader;

public class GhostAvatar extends TriMesh{
=======
import sage.scene.shape.Pyramid;
import sage.scene.shape.Teapot;
import a3.kmap165Engine.network.*;

public class GhostAvatar extends Teapot{
>>>>>>> a99e1bde9c96765ba62941d5f0e6db547d306078
   private UUID ghostID;
   private Vector3D ghostPosition;
   private Matrix3D ghostMatrix;
   private boolean created = false;
   private MyClient theClient;
<<<<<<< HEAD
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
=======
   public GhostAvatar(UUID ID, Vector3D position, MyClient client){
      ghostID = ID;
      ghostPosition = position;
      theClient = client;
      ghostMatrix = this.getLocalTranslation();
      ghostMatrix.setCol(3, ghostPosition);
      setLocalTranslation(ghostMatrix);
      updateWorldBound();
>>>>>>> a99e1bde9c96765ba62941d5f0e6db547d306078
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
<<<<<<< HEAD
      //ghostMatrix.setCol(3, gV);
	   ghostMatrix.translate(gV.getX(), gV.getY(), gV.getZ()); 
=======
      ghostMatrix.setCol(3, gV);
>>>>>>> a99e1bde9c96765ba62941d5f0e6db547d306078
      setLocalTranslation(ghostMatrix);
      updateWorldBound();
      if(theClient != null) theClient.sendMoveMessage(this.getLocalTranslation().getCol(3));
   }
}