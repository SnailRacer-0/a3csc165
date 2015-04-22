package a3.kmap165Engine.scene_node_controller;

import sage.scene.Controller;
import sage.scene.SceneNode;
import graphicslib3D.Matrix3D;
import graphicslib3D.Vector3D;
import sage.scene.Group;

public class ClockwiseRotationController extends Controller{
   private double rotationRate = 90; // degrees per second
   private double cycleTime; // default cycle time
   private double totalTime;
   private Vector3D axis;
   public ClockwiseRotationController(double c, Vector3D g){
      cycleTime = c;
      axis = g;
   }
   public void setCycleTime(double c){ 
      cycleTime = c; 
   }
   /*public void addControllerNode(Group g){
      for (SceneNode node : Group g){
         addControllerNode(node);
      }
   }*/
   public void update(double time){ // example controller
      totalTime += time;
      double rotAmount = -1 * rotationRate + time ;
      if (totalTime > cycleTime){
         totalTime = 0.0;
      }
      rotAmount++;
      rotAmount = -0.25 % 360;
      Matrix3D newRot = new Matrix3D();
      newRot.rotate(rotAmount, axis);
      for (SceneNode node : controlledNodes){
         Matrix3D curRot = node.getLocalRotation();
         curRot.concatenate(newRot);
         node.setLocalRotation(curRot);
      }
   }
}