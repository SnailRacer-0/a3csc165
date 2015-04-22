package a3.kmap165Engine.camera;

import sage.util.MathUtils;
import net.java.games.input.*;
import sage.camera.*;
import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;
import sage.scene.SceneNode;
import sage.input.*;
import sage.input.action.*;
import net.java.games.input.Component.Identifier.*;
import sage.input.IInputManager.INPUT_ACTION_TYPE.*;

public class Camera3Pcontroller{
   private ICamera cam; //the camera being controlled
   private SceneNode target; //the target the camera looks at
   private float cameraAzimuth; //rotation of camera around target Y axis
   private float cameraElevation; //elevation of camera above target
   private float cameraDistanceFromTarget;
   private Point3D targetPos; // avatar’s position in the world
   private Vector3D worldUpVec;
   private float mult = 1f;
   
   public Camera3Pcontroller(ICamera cam, SceneNode target,
      IInputManager inputMgr, String controllerName){
      
      this.cam = cam;
      this.target = target;
      worldUpVec = new Vector3D(0,1,0);
      cameraDistanceFromTarget = 50.0f;

      // start from FRONT OF and ABOVE the target
      cameraAzimuth = 0; 
      cameraElevation = 20.0f; // elevation is in degrees
      setupInput(inputMgr, controllerName);
      update(0.0f); // initialize camera state
   }
   
   public void update(float time){
      updateTarget();
      updateCameraPosition();
      cam.lookAt(targetPos, worldUpVec); // SAGE built-in function
   }
   
   private void updateTarget(){ 
      targetPos = new Point3D(target.getWorldTranslation().getCol(3)); 
   }
   private void updateCameraPosition(){
      double theta = cameraAzimuth;
      double phi = cameraElevation ;
      double r = cameraDistanceFromTarget;
      // calculate new camera position in Cartesian coords
      Point3D relativePosition = MathUtils.sphericalToCartesian(theta, phi, r);
      Point3D desiredCameraLoc = relativePosition.add(targetPos);
      cam.setLocation(desiredCameraLoc);
   }
   private void setupInput(IInputManager im, String cn){ 
      IAction orbitAction = new OrbitAroundAction();
      IAction zoomInAction = new ZoomInAction();
      IAction zoomOutAction = new ZoomOutAction();
      if(cn == im.getFirstGamepadName()){
         im.associateAction(cn, Axis.RX, orbitAction, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
         im.associateAction(cn, Button._4, zoomInAction, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
         im.associateAction(cn, Button._5, zoomOutAction, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
      }
      if(cn == im.getMouseName()){
         im.associateAction(cn, Axis.X, orbitAction, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
         mult = 3f;
      }
      if(cn == im.getKeyboardName()){
         im.associateAction(cn, Key.W, zoomInAction, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
         im.associateAction(cn, Key.S, zoomOutAction, IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
      }

   }
   private class OrbitAroundAction extends AbstractInputAction{
      public void performAction(float time, Event evt){
         float rotAmount;
         if (evt.getValue() < -0.2){ 
            rotAmount=0.5f;
         }
         else{ 
            if (evt.getValue() > 0.2){ 
               rotAmount=-0.5f;
            }
            else{ 
               rotAmount=0.0f; 
            }
         }
      cameraAzimuth += mult * rotAmount ;
      cameraAzimuth = cameraAzimuth % 360 ;
      } 
   }
   private class ZoomInAction extends AbstractInputAction{
      public void performAction(float time, Event evt){
         cameraDistanceFromTarget -= 0.1f;
      } 
   } 
   private class ZoomOutAction extends AbstractInputAction{
      public void performAction(float time, Event evt){
         cameraDistanceFromTarget += 0.1f;
      } 
   }
   
   public Point3D getLocation(){
      return cam.getLocation();
   }
}