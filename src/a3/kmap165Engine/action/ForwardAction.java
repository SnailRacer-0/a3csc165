package a3.kmap165Engine.action;

import sage.input.action.AbstractInputAction;
import sage.app.AbstractGame;
import net.java.games.input.Event;
import sage.camera.*;
import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;
import graphicslib3D.Matrix3D;
import sage.scene.SceneNode;
import sage.scene.shape.*;
import a3.kmap165Engine.network.*;
import sage.terrain.*;
import sage.scene.Model3DTriMesh;

public class ForwardAction extends AbstractInputAction{ 
   private Model3DTriMesh s;
   private Matrix3D sM;
   private MyClient client;
   private TerrainBlock terrain;
   public ForwardAction(Model3DTriMesh n, TerrainBlock t, MyClient thisClient){ 
      s = n;
      terrain = t;
      sM = s.getLocalTranslation();
      client = thisClient;
   }
   public void performAction(float time, Event e){
      sM.translate(0,0,-0.1f);
      s.setLocalTranslation(sM);

      updateVerticalPosition();
      
      s.updateWorldBound();
		s.updateLocalBound();
		s.updateGeometricState((double) time, false);
      
     // s.getWorldBound().computeFromPoints(s.getVertexBuffer());
      //System.out.println(client);
      if(client != null) client.sendMoveMessage(sM.getCol(3));
   }
   private void updateVerticalPosition()
   {
	   Point3D avLoc = new Point3D(s.getLocalTranslation().getCol(3));
	   float x = (float) avLoc.getX();
	   float z = (float) avLoc.getZ();
	   float tHeight = terrain.getHeight(x, z);
	   float desiredHeight = tHeight + (float)terrain.getOrigin().getY() + 0.5f;
	   s.getLocalTranslation().setElementAt(1, 3, desiredHeight);
   }
}