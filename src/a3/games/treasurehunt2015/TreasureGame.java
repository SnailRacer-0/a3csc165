package a3.games.treasurehunt2015;

import sage.scene.Controller;
import sage.app.BaseGame;
import sage.renderer.*;
import sage.scene.Group;
import sage.display.*;
import sage.camera.*;
import sage.input.*;
import sage.scene.SceneNode;
import sage.scene.shape.*;
import sage.scene.HUDString;
import sage.scene.TriMesh;
import net.java.games.input.*;
import sage.input.action.IAction;
import graphicslib3D.Point3D;
import graphicslib3D.Matrix3D;
import graphicslib3D.Vector3D;

import java.awt.event.*;
import java.util.Random;
import java.awt.Color;
import java.text.DecimalFormat;

import sage.scene.shape.Line;
import sage.scene.shape.Cube;
import sage.scene.shape.Cylinder;
import sage.scene.shape.Pyramid;
import sage.scene.shape.Teapot;
import sage.scene.shape.Sphere;
import sage.scene.shape.Cube;
import sage.scene.shape.Rectangle;

import java.nio.*;
import java.util.ArrayList;

import a3.kmap165Engine.action.BackwardAction;
import a3.kmap165Engine.action.ForwardAction;
import a3.kmap165Engine.action.LeftAction;
import a3.kmap165Engine.action.QuitAction;
import a3.kmap165Engine.action.RightAction;
import a3.kmap165Engine.action.X_Action_Controller;
import a3.kmap165Engine.action.Z_Action_Controller;
import a3.kmap165Engine.camera.Camera3Pcontroller;
import a3.kmap165Engine.custom_objects.MyDiamond;
import a3.kmap165Engine.custom_objects.event_listener_objects.TheChest;
import a3.kmap165Engine.display.MyDisplaySystem;
import a3.kmap165Engine.event.CrashEvent;
import a3.kmap165Engine.scene_node_controller.ClockwiseRotationController;
import a3.kmap165Engine.scene_node_controller.MyTranslate_RotateController;
import sage.event.*;

public class TreasureGame extends BaseGame implements KeyListener{
   private Camera3Pcontroller c0c, c1c, c2c;
   private IRenderer renderer;
   private int score1 = 0, score2 = 0, numCrashes = 0;
   private float time1 = 0, time2 = 0 ;
   private HUDString player1ScoreString, player1TimeString, player2ScoreString, player2TimeString;
   private boolean isOver = false;
   private IDisplaySystem fullDisplay, display;
   private Point3D origin;
   private Random rng;
   private ICamera camera1, camera2;
   private IInputManager im;
   private IEventManager eventMgr;
   private Cylinder cyl;
   private Teapot tpt;
   private Sphere sph;
   private Pyramid p1;
   private Cube p2;
   private MyDiamond jade;
   private TheChest chest;
   private boolean collidedWTeapot = false, collidedWPyramid = false, collidedWCylinder = false, 
      collidedWDiamond = false;
   
   protected void initGame(){ 
      getDisplaySystem().setTitle("Kevin's Treasure Game");
      //System.out.println(getDisplaySystem());
      renderer = getDisplaySystem().getRenderer();
      im = getInputManager();
      eventMgr = EventManager.getInstance();
      initGameObjects();
      createPlayers();
      initInput();
   }
   private void initInput(){
      
 /*     String gpName = im.getFirstGamepadName();
      String Keyboard = im.getKeyboardName();
      String mouseName = im.getMouseName();
      
      c1c = new Camera3Pcontroller(camera1,p1,im,mouseName);
      c2c = new Camera3Pcontroller(camera2,p2,im,gpName);
      //Controls for P1
      ForwardAction mvForward = new ForwardAction(p1);
      im.associateAction(Keyboard,
         net.java.games.input.Component.Identifier.Key.S,
         mvForward,
         IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
         
      BackwardAction mvBackward = new BackwardAction(p1);
      im.associateAction(Keyboard,
         net.java.games.input.Component.Identifier.Key.W,
         mvBackward,
         IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
         
      LeftAction mvLeft = new LeftAction(p1);
      im.associateAction(Keyboard,
         net.java.games.input.Component.Identifier.Key.A,
         mvLeft,
         IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
         
      RightAction mvRight = new RightAction(p1);
      im.associateAction(Keyboard,
         net.java.games.input.Component.Identifier.Key.D,
         mvRight,
         IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
      
      //Controls for P2
      X_Action_Controller xControl = new X_Action_Controller(p2);
      im.associateAction(gpName,
         net.java.games.input.Component.Identifier.Axis.X,
         xControl,
         IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
         
      Z_Action_Controller zControl = new Z_Action_Controller(p2);
      im.associateAction(gpName,
         net.java.games.input.Component.Identifier.Axis.Y,
         zControl,
         IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);  
               
      QuitAction stop = new QuitAction(this);
      im.associateAction(Keyboard,
         net.java.games.input.Component.Identifier.Key.ESCAPE,
         stop,
         IInputManager.INPUT_ACTION_TYPE.ON_PRESS_AND_RELEASE);   
         */   
   }
   private void createPlayers(){ 
      p1 = new Pyramid("PLAYER1");
      Matrix3D p1MT = p1.getWorldTranslation();
      Matrix3D p1MR = p1.getWorldRotation();
      
      p1MT.translate(0, 1, 50);
      p1.setLocalTranslation(p1MT);
      
      p1MR.rotate(180, new Vector3D(0, 1, 0));
      p1.setLocalRotation(p1MR);
      
      p1.updateWorldBound();
      addGameWorldObject(p1);
      
      
      camera1 = new JOGLCamera(renderer);
      camera1.setPerspectiveFrustum(60, 2, 1, 1000);
      camera1.setViewport(0.0, 1.0, 0.0, 0.45);
      
      p2 = new Cube("PLAYER2");
      Matrix3D p2MT = p2.getWorldTranslation();
      Matrix3D p2MR = p2.getWorldRotation();
      
      p2MT.translate(50, 1, 0);
      p2.setLocalTranslation(p2MT);
      
      p2MR.rotate(-90, new Vector3D(0, 1, 0));
      p2.setLocalRotation(p2MR);
      
      p2.updateWorldBound();
      addGameWorldObject(p2);
      
      camera2 = new JOGLCamera(renderer);
      camera2.setPerspectiveFrustum(60, 2, 1, 1000);
      camera2.setViewport(0.0, 1.0, 0.55, 1.0);
      
      createPlayerHUDs();
   }
   
   private void createPlayerHUDs(){
      // Player 1 identity HUD
      HUDString player1ID = new HUDString("Player1");
      player1ID.setName("Player1ID");
      player1ID.setLocation(0.01, 0.12);
      player1ID.setRenderMode(sage.scene.SceneNode.RENDER_MODE.ORTHO);
      player1ID.setColor(Color.red);
      player1ID.setCullMode(sage.scene.SceneNode.CULL_MODE.NEVER);
      camera1.addToHUD(player1ID);
      // Player 1 time HUD
      player1TimeString = new HUDString("Time = " + time1);
      player1TimeString.setLocation(0.01,0.06); // (0,0) [lower-left] to (1,1)
      player1TimeString.setRenderMode(sage.scene.SceneNode.RENDER_MODE.ORTHO);
      player1TimeString.setColor(Color.red);
      player1TimeString.setCullMode(sage.scene.SceneNode.CULL_MODE.NEVER);
      camera1.addToHUD(player1TimeString);
      // Player 1 score HUD
      player1ScoreString = new HUDString ("Score = " + score1); //default is (0,0)
      player1ScoreString.setLocation(0.01,0.00); 
      player1ScoreString.setRenderMode(sage.scene.SceneNode.RENDER_MODE.ORTHO);
      player1ScoreString.setColor(Color.red);
      player1ScoreString.setCullMode(sage.scene.SceneNode.CULL_MODE.NEVER);
      camera1.addToHUD(player1ScoreString);
      
      // Player 2 identity HUD
      HUDString player2ID = new HUDString("Player2");
      player2ID.setName("Player2ID");
      player2ID.setLocation(0.01, 0.12);
      player2ID.setRenderMode(sage.scene.SceneNode.RENDER_MODE.ORTHO);
      player2ID.setColor(Color.yellow);
      player2ID.setCullMode(sage.scene.SceneNode.CULL_MODE.NEVER);
      camera2.addToHUD(player2ID);
      // Player 2 time HUD
      player2TimeString = new HUDString("Time = " + time2);
      player2TimeString.setLocation(0.01,0.06); // (0,0) [lower-left] to (1,1)
      player2TimeString.setRenderMode(sage.scene.SceneNode.RENDER_MODE.ORTHO);
      player2TimeString.setColor(Color.yellow);
      player2TimeString.setCullMode(sage.scene.SceneNode.CULL_MODE.NEVER);
      camera2.addToHUD(player2TimeString);
      // Player 2 score HUD
      player2ScoreString = new HUDString ("Score = " + score2); //default is (0,0)
      player2ScoreString.setLocation(0.01,0.00);
      player2ScoreString.setRenderMode(sage.scene.SceneNode.RENDER_MODE.ORTHO);
      player2ScoreString.setColor(Color.yellow);
      player2ScoreString.setCullMode(sage.scene.SceneNode.CULL_MODE.NEVER);
      camera2.addToHUD(player2ScoreString);
   }
   private void initGameObjects(){
      
      // configure game display      
      /*display.setTitle();
      //display.addKeyListener(this);
      
   camera = new JOGLCamera(renderer);
   camera.setPerspectiveFrustum(60, 2, 1, 1000);
   camera.setLocation(new Point3D(0,0,30));
   camera.setViewport(0.0, 1.0, 0.0, 0.45);*/
      
      origin = new Point3D();
      rng = new Random();
      
      // add some lines
      Line xLine = new Line(origin, new Point3D(100,0,0), new Color(255,0,0), 1);
      addGameWorldObject(xLine);
      
      Line yLine = new Line(origin, new Point3D(0,100,0), new Color(0,255,0), 1);
      addGameWorldObject(yLine);
      
      Line zLine = new Line(origin, new Point3D(0,0,100), new Color(0,0,255), 1);
      addGameWorldObject(zLine);
      
      // add a rectangle, and turn it into a plane
      Rectangle plane = new Rectangle(1000, 1000);
      plane.rotate(90, new Vector3D(1,0,0));
      plane.translate(0.0f,-2f,0.0f);
      plane.setColor(new Color(0,255,130));
      addGameWorldObject(plane);
      
      //add cube that will grow bigger later
      
      chest = new TheChest();
      Matrix3D chestM = chest.getLocalScale();   
      chestM.scale(0.5f, 0.5f, 0.5f);
      chest.setLocalScale(chestM);
      addGameWorldObject(chest);
      eventMgr.addListener(chest, CrashEvent.class);
      
      //create some treasure
      cyl = new Cylinder();
      Matrix3D cylM = cyl.getLocalTranslation();
      cylM.translate(20, 0, 20);
      cyl.setLocalTranslation(cylM);
      addGameWorldObject(cyl);
      cyl.updateWorldBound();
      
      sph = new Sphere();
      Matrix3D sphM = sph.getLocalTranslation();
      sphM.translate(0, 0, 40);
      sph.setLocalTranslation(sphM);
      addGameWorldObject(sph);
      sph.updateWorldBound();
      
      tpt = new Teapot();
      Matrix3D tptM = tpt.getLocalTranslation();
      tptM.translate(25, 0, 40);
      tpt.setLocalTranslation(tptM);
      addGameWorldObject(tpt);
      tpt.updateWorldBound();
      
      jade = new MyDiamond();
      Matrix3D jadeM = jade.getLocalTranslation();
      jadeM.translate(35, 1, 0);
      jade.setLocalTranslation(jadeM);
      addGameWorldObject(jade);
      jade.updateWorldBound();
      
      eventMgr.addListener(chest, CrashEvent.class);
      
      //create a group
      createPillar();
      
      // a HUD
      /*timeString = new HUDString("Time = " + time);
      timeString.setLocation(0,0.05); // (0,0) [lower-left] to (1,1)
      addGameWorldObject(timeString);
      scoreString = new HUDString ("Score = " + score); //default is (0,0)
      addGameWorldObject(scoreString);*/

   }
   public void update(float elapsedTimeMS){
      //Player 1's crash events 
      if (tpt.getWorldBound().intersects(p1.getWorldBound()) && collidedWTeapot == false){
         collidedWTeapot = true;
         numCrashes++;
         score1 += 100;
         CrashEvent newCrash = new CrashEvent(numCrashes);
         removeGameWorldObject(tpt);
         eventMgr.triggerEvent(newCrash);
      }
      if (cyl.getWorldBound().intersects(p1.getWorldBound()) && collidedWCylinder == false){
         collidedWCylinder = true; 
         numCrashes++;
         score1 += 500;
         CrashEvent newCrash = new CrashEvent(numCrashes);
         removeGameWorldObject(cyl);
         eventMgr.triggerEvent(newCrash);
      }
      if (sph.getWorldBound().intersects(p1.getWorldBound()) && collidedWPyramid == false){
         collidedWPyramid = true;
         System.out.println(sph.getWorldBound());
         System.out.println(p1.getWorldBound());
         numCrashes++;
         score1 += 250;
         CrashEvent newCrash = new CrashEvent(numCrashes);
         removeGameWorldObject(sph);
         eventMgr.triggerEvent(newCrash);
      }
      if (jade.getWorldBound().intersects(p1.getWorldBound()) && collidedWDiamond == false){
         collidedWDiamond = true; 
         numCrashes++;
         score1 += 1000;
         CrashEvent newCrash = new CrashEvent(numCrashes);
         removeGameWorldObject(jade);
         eventMgr.triggerEvent(newCrash);
      }
      //Player 2's crash events
      if ((tpt.getWorldBound().intersects(p2.getWorldBound())) && collidedWTeapot == false){
         collidedWTeapot = true;
         numCrashes++;
         score2 += 100;
         CrashEvent newCrash = new CrashEvent(numCrashes);
         removeGameWorldObject(tpt);
         eventMgr.triggerEvent(newCrash);
      }
      if ((cyl.getWorldBound().intersects(p2.getWorldBound())) && collidedWCylinder == false){
         collidedWCylinder = true; 
         numCrashes++;
         score2 += 500;
         CrashEvent newCrash = new CrashEvent(numCrashes);
         removeGameWorldObject(cyl);
         eventMgr.triggerEvent(newCrash);
      }
      if ((sph.getWorldBound().intersects(p2.getWorldBound())) && collidedWPyramid == false){
         collidedWPyramid = true; 
         System.out.println(sph.getWorldBound());
         System.out.println(p2.getWorldBound());
         numCrashes++;
         score2 += 250;
         CrashEvent newCrash = new CrashEvent(numCrashes);
         removeGameWorldObject(sph);
         eventMgr.triggerEvent(newCrash);
      }
      if ((jade.getWorldBound().intersects(p2.getWorldBound())) && collidedWDiamond == false){
         collidedWDiamond = true; 
         numCrashes++;
         score2 += 1000;
         CrashEvent newCrash = new CrashEvent(numCrashes);
         removeGameWorldObject(jade);
         eventMgr.triggerEvent(newCrash);
      }
      // update player 1's HUD
      player1ScoreString.setText("Score = " + score1);
      time1 += elapsedTimeMS;
      DecimalFormat df1 = new DecimalFormat("0.0");
      player1TimeString.setText("Time = " + df1.format(time1/1000));
      
      // update player 2's HUD
      player2ScoreString.setText("Score = " + score2);
      time2 += elapsedTimeMS;
      DecimalFormat df2 = new DecimalFormat("0.0");
      player2TimeString.setText("Time = " + df2.format(time2/1000));
      
      // tell BaseGame to update game world state
      //c0c.update(elapsedTimeMS);
      c1c.update(elapsedTimeMS);
      c2c.update(elapsedTimeMS);
      super.update(elapsedTimeMS);
   }
   private void createPillar(){
      Cube pillarBase = new Cube();
      pillarBase.scale(1,4,1);
      Cube pillarEye = new Cube();
      pillarEye.translate(0,8,0);
      
      Pyramid upperCenterEyeSpike = new Pyramid();
      
      Pyramid lowerCenterEyeSpike = new Pyramid();
      
      Group group1 = new Group(); //sun pillar system position
      Group group2 = new Group(); //pillar piece system translation
      Group group3 = new Group(); //piece spike system rotation
      
      group1.addChild(pillarBase);
      group1.addChild(group2);
      
      group2.addChild(group3);
      group2.addChild(pillarEye);
      
      group3.addChild(upperCenterEyeSpike);
      
      group3.addChild(lowerCenterEyeSpike);
      
      group1.setIsTransformSpaceParent(true);
      group2.setIsTransformSpaceParent(true);
      group3.setIsTransformSpaceParent(true);
      
      pillarBase.setIsTransformSpaceParent(true);
      pillarEye.setIsTransformSpaceParent(true);
      
      upperCenterEyeSpike.setIsTransformSpaceParent(true);
      
      lowerCenterEyeSpike.setIsTransformSpaceParent(true);
      
      group1.translate(-10,2,-10);
      
      Vector3D pillarSpinV = new Vector3D(0,1,0);
      ClockwiseRotationController pillarSpin = new ClockwiseRotationController(200, pillarSpinV);
      pillarSpin.addControlledNode(group1);
      group1.addController(pillarSpin);
      
      
      group2.translate(0,5,0);
      
      MyTranslate_RotateController eyeMove = new MyTranslate_RotateController();
      eyeMove.addControlledNode(pillarEye);
      pillarEye.addController(eyeMove);
      
      
      Vector3D spikeSpinV = new Vector3D(8,0,0);
      spikeSpinV.cross(new Vector3D(0,0,1));
      ClockwiseRotationController spikeSpin = new ClockwiseRotationController(200, spikeSpinV);
      spikeSpin.addControlledNode(group3);
      group3.addController(spikeSpin);
      
      upperCenterEyeSpike.translate(0,12,0);
      
      lowerCenterEyeSpike.translate(0,4,0);
      lowerCenterEyeSpike.rotate(180f, new Vector3D(5,0,0));
      
      addGameWorldObject(group1);
   }
   protected void render(){
      renderer.setCamera(camera1);
      super.render();
      renderer.setCamera(camera2);
      super.render();
   }
   private IDisplaySystem createDisplaySystem(){
      display = new MyDisplaySystem(1920, 1200, 32, 60, true,
      "sage.renderer.jogl.JOGLRenderer");
      System.out.print("\nWaiting for display creation...");
      int count = 0;
      // wait until display creation completes or a timeout occurs
      while (!display.isCreated()){
         try{ 
            Thread.sleep(10); 
         }
         catch (InterruptedException e){ 
            throw new RuntimeException("Display creation interrupted"); 
         }
         count++;
         System.out.print("+");
         if (count % 80 == 0) { System.out.println(); }
         if (count > 2000){ // 20 seconds (approx.)
            throw new RuntimeException("Unable to create display");
         }
      }
      System.out.println();
      return display;
   }
   /*protected void shutdown(){
      display.close();
   }
   protected void initSystem(){ 
      //call a local method to create a DisplaySystem object
      IDisplaySystem display = createDisplaySystem();
      setDisplaySystem(display);
      //create an Input Manager
      IInputManager inputManager = new InputManager();
      setInputManager(inputManager);
      //create an (empty) gameworld
      ArrayList<SceneNode> gameWorld = new ArrayList<SceneNode>();
      setGameWorld(gameWorld);
   }*/
   public void keyPressed(KeyEvent e){}
   public void keyReleased(KeyEvent e){}
   public void keyTyped(KeyEvent e){}
}