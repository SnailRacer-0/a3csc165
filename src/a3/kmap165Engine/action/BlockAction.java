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
import sage.terrain.TerrainBlock;
import a3.kmap165Engine.network.*;
import sage.scene.Model3DTriMesh;

public class BlockAction extends AbstractInputAction{ 
   private Model3DTriMesh s;
   private Matrix3D sM;
   private MyClient client;
   private TerrainBlock terrain;
   
   public BlockAction (Model3DTriMesh n, MyClient thisClient){ 
      s = n;
      sM = s.getLocalTranslation();
      client = thisClient;
   }
   public void performAction(float time, Event e){
      s.stopAnimation();
      s.startAnimation("blockAnimation");
      
      System.out.println("Block");
      
      s.updateWorldBound();
      //client.sendMoveMessage(s.getLocalTranslation().getCol(3));
      s.startAnimation("Idle_Stance");
   }
}