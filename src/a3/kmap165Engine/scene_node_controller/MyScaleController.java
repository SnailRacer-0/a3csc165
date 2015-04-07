package a3.kmap165Engine.scene_node_controller;

import sage.scene.Controller;
import sage.scene.SceneNode;
import graphicslib3D.Matrix3D;

public class MyScaleController extends Controller{
   private double defaultScale = 1;
   private double scaleRate = 0.0005; // scale per second
   private double cycleTime = 1000.0; // default cycle time
   private double totalTime;
   private double scaleAmount = 1.005;
   public void setCycleTime(double c){ 
      cycleTime = c; 
   }
   public void update(double time){ // example controller
      totalTime += time;
      //double scaleAmount = scaleRate;// * time ;
      if (totalTime > cycleTime){
         scaleAmount = 1/scaleAmount;
      }
      //scaleAmount = defaultScale + scaleRate;
      Matrix3D newScale = new Matrix3D();
      newScale.scale(scaleAmount,1,1);
      for (SceneNode node : controlledNodes){
         Matrix3D curScale = node.getLocalScale();
         curScale.concatenate(newScale);
         node.setLocalScale(curScale);
      }
   }
}