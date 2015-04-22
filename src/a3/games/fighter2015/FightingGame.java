package a3.games.fighter2015;

import sage.scene.Controller;
import a3.kmap165Engine.action.*;
import a3.kmap165Engine.camera.*;
import a3.kmap165Engine.custom_objects.*;
import a3.kmap165Engine.custom_objects.event_listener_objects.*;
import a3.kmap165Engine.display.*;
import a3.kmap165Engine.event.*;
import a3.kmap165Engine.network.*;
import a3.kmap165Engine.network.ghost_avatar.*;
import a3.kmap165Engine.scene_node_controller.*;
import sage.app.BaseGame;
import sage.physics.IPhysicsEngine;
import sage.physics.IPhysicsObject;
import sage.physics.PhysicsEngineFactory;
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
import sage.input.action.AbstractInputAction;
import sage.input.action.IAction;
import graphicslib3D.Point3D;
import graphicslib3D.Matrix3D;
import graphicslib3D.Vector3D;

import java.awt.event.*;
import java.util.List;
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
import sage.scene.state.RenderState;
import sage.scene.state.TextureState;

import java.nio.*;
import java.util.ArrayList;
import java.awt.image.BufferedImage; 
import java.io.BufferedReader;
import java.io.File; 
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException; 
import java.net.UnknownHostException;
import java.net.URL; 

import sage.model.loader.OBJLoader;
import sage.model.loader.OBJMaterial;
import sage.networking.IGameConnection.ProtocolType;

import java.net.InetAddress;


import javax.imageio.ImageIO; 
 
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import a3.kmap165Engine.action.BackwardAction;
import a3.kmap165Engine.action.ForwardAction;
import a3.kmap165Engine.action.LeftAction;
import a3.kmap165Engine.action.QuitAction;
import a3.kmap165Engine.action.RightAction;
import a3.kmap165Engine.camera.Camera3Pcontroller;
import a3.kmap165Engine.custom_objects.event_listener_objects.TheChest;
import a3.kmap165Engine.event.CrashEvent;
import a3.kmap165Engine.scene_node_controller.ClockwiseRotationController;
import sage.camera.ICamera; 
import sage.display.IDisplaySystem;
import sage.scene.SkyBox; 
import sage.terrain.*;
import sage.texture.Texture; 
import sage.texture.TextureManager; 
 
import sage.event.*;

public class FightingGame extends BaseGame implements KeyListener{
   private Camera3Pcontroller c0c, c1c, c2c;
   // adding Scripting stuff
   private ScriptEngine engine;
   private String sName = "src/a3/games/fighter2015/TestScriptColor.js";
   private File scriptFile;
  
  
   // test
   
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
   private TriMesh playerOne, fightingRingTriMesh;
   private Pyramid p1;


   private Cube p2;
   private MyDiamond jade;
   private TheChest chest;
   private boolean collidedWTeapot = false, collidedWPyramid = false, collidedWCylinder = false, 
      collidedWDiamond = false, isConnected = false;
   
   
 	private ICamera camera; 

 	private Texture tf; 
 	private Group scene; 
 	private final String dir = "." + File.separator + "a3"+ File.separator + "images" + File.separator, serverAddress; 
   private final int serverPort;
   private final ProtocolType serverProtocol;
   private MyClient thisClient;
   
   private TerrainBlock parkingLot1, parkingLot2, parkingLot3, parkingLot4;
   private TerrainBlock hillTerr;
   private Group rootNode;
   private SceneNode lineNodes; 
   private SkyBox skybox;
   // physics
   private boolean running;
   private IPhysicsEngine physicsEngine;
   private IPhysicsObject powerUpP, terrainP;
   private Sphere powerUp;
   private OBJLoader objectLoader;

   public FightingGame(String serverAddr, int sPort) throws IOException{ 
      super();
      this.serverAddress = serverAddr;
      this.serverPort = sPort;
      this.serverProtocol = ProtocolType.TCP;
   }
   protected void initGame(){ 
      getDisplaySystem().setTitle("Fighting Game");
      renderer = getDisplaySystem().getRenderer();
      im = getInputManager();
      eventMgr = EventManager.getInstance();
      // adding scripting stuff
      ScriptEngineManager factory = new ScriptEngineManager();
      List<ScriptEngineFactory> list = factory.getEngineFactories();
      engine = factory.getEngineByName("js");
      scriptFile = new File(sName);
      this.runScript();
      objectLoader = new OBJLoader();

      initGameObjects();
      initTerrain();
      createPlayers();
      initPhysicsSystem();
      createSagePhysicsWorld();
      initInput();
      try{ 
         thisClient = new MyClient(InetAddress.getByName(serverAddress), serverPort, serverProtocol, this); 
      }
      catch (UnknownHostException e) { 
         e.printStackTrace(); 
      }
      catch (IOException e) { 
         e.printStackTrace(); 
      }
      if (thisClient != null) { 
         thisClient.sendJoinMessage(); 
      }  
   }
   public MyClient getClient(){
      return thisClient;
   }
   private void initInput(){
      
      String gpName = im.getFirstGamepadName();
      String Keyboard = im.getKeyboardName();
      String mouseName = im.getMouseName();
      
      // physicsTest
      IAction startAction = new StartAction();
      im.associateAction(Keyboard, Component.Identifier.Key.Q, startAction, IInputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
      
      // scriptTestInput
      UpdatePlayerColor updateColor = new UpdatePlayerColor();
      im.associateAction(Keyboard, net.java.games.input.Component.Identifier.Key.SPACE, updateColor, 
    		  IInputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
      
      
      c1c = new Camera3Pcontroller(camera1,playerOne,im,mouseName);
      //c2c = new Camera3Pcontroller(camera2,p2,im,mouseName);
      //c2c = new Camera3Pcontroller(camera2,p2,im,gpName);
      //Controls for P1
      ForwardAction mvForward = new ForwardAction(playerOne, hillTerr, thisClient);
      //c2c = new Camera3Pcontroller(camera2,p2,im,mouseName);
      //c2c = new Camera3Pcontroller(camera2,p2,im,gpName);
      //Controls for P1

      im.associateAction(Keyboard,
         net.java.games.input.Component.Identifier.Key.S,
         mvForward,
         IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
         
      BackwardAction mvBackward = new BackwardAction(playerOne, hillTerr, thisClient);

      im.associateAction(Keyboard,
         net.java.games.input.Component.Identifier.Key.W,
         mvBackward,
         IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
         
      LeftAction mvLeft = new LeftAction(playerOne, hillTerr, thisClient);

      im.associateAction(Keyboard,
         net.java.games.input.Component.Identifier.Key.A,
         mvLeft,
         IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

      RightAction mvRight = new RightAction(playerOne, hillTerr, thisClient);

      im.associateAction(Keyboard,
         net.java.games.input.Component.Identifier.Key.D,
         mvRight,
         IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
      
      //Controls for P2
      /*X_Action_Controller xControl = new X_Action_Controller(p2);
      im.associateAction(gpName,
         net.java.games.input.Component.Identifier.Axis.X,
         xControl,
         IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
         
      Z_Action_Controller zControl = new Z_Action_Controller(p2);
      im.associateAction(gpName,
         net.java.games.input.Component.Identifier.Axis.Y,
         zControl,
         IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
      ForwardAction mvForward2 = new ForwardAction(p2);
      im.associateAction(Keyboard,
         net.java.games.input.Component.Identifier.Key.K,
         mvForward2,
         IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
         
      BackwardAction mvBackward2 = new BackwardAction(p2);
      im.associateAction(Keyboard,
         net.java.games.input.Component.Identifier.Key.I,
         mvBackward2,
         IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
         
      LeftAction mvLeft2 = new LeftAction(p2);
      im.associateAction(Keyboard,
         net.java.games.input.Component.Identifier.Key.J,
         mvLeft2,
         IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
         
      RightAction mvRight2 = new RightAction(p2);
      im.associateAction(Keyboard,
         net.java.games.input.Component.Identifier.Key.L,
         mvRight2,
         IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);*/
         
      //Quit Action         
      QuitAction stop = new QuitAction(this);
      im.associateAction(Keyboard,
         net.java.games.input.Component.Identifier.Key.ESCAPE,
         stop,
         IInputManager.INPUT_ACTION_TYPE.ON_PRESS_AND_RELEASE);      
   }
   private void createScene() 
 	{ 
 		scene = new Group("Root Node"); 
 		 
 	   skybox = new SkyBox("SkyBox", 100.0f, 100.0f, 100.0f); 
 
 		Texture northTex = TextureManager.loadTexture2D("src/a3/images/heightMapTest.JPG"); 
 		Texture southTex = TextureManager.loadTexture2D("src/a3/images/heightMapTest.JPG");
        Texture eastTex = TextureManager.loadTexture2D("src/a3/images/lotTest.jpg"); 
 		Texture westTex = TextureManager.loadTexture2D("src/a3/images/lotTest.jpg");
        Texture upTex = TextureManager.loadTexture2D("src/a3/images/clouds.jpg"); 
 		Texture downTex = TextureManager.loadTexture2D("src/a3/images/lot_floor.jpg");  
 		Texture testTerr = TextureManager.loadTexture2D("src/a3/images/squaresquare.bmp");

 		Texture testMountain = TextureManager.loadTexture2D("src/a3/images/mountains512.jpg");
 		 
 	   skybox.setTexture(SkyBox.Face.North, northTex); 
 	   skybox.setTexture(SkyBox.Face.South, southTex);
      
       skybox.setTexture(SkyBox.Face.East, eastTex); 
 	   skybox.setTexture(SkyBox.Face.West, westTex); 
      
 	   skybox.setTexture(SkyBox.Face.Up, upTex); 
 	   skybox.setTexture(SkyBox.Face.Down, downTex);
      
 	   scene.addChild(skybox);
      

		addGameWorldObject(scene); 
 		 
 	} 

   private void createPlayers(){
	   try{
		      playerOne = objectLoader.loadModel("src/a3/kmap165Engine/external_models/albertTestMesh.obj");
		      Texture playerOneMeshT = TextureManager.loadTexture2D("src/a3/kmap165Engine/external_models/albertUV.jpg");
		      playerOne.setTexture(playerOneMeshT);
		      
		      
		      Matrix3D playerOneT = playerOne.getLocalTranslation();
		      playerOneT.translate(30, 0, 50);
		      playerOne.setLocalTranslation(playerOneT);
		      Matrix3D playerOneR = playerOne.getLocalRotation();
		      playerOneR.rotateY(180.0);
		      playerOne.setLocalRotation(playerOneR);
		      Matrix3D playerOneS = playerOne.getLocalScale();
		      playerOneS.scale(.3, 0.3, .3);
		      playerOne.setLocalScale(playerOneS);
		      
		      playerOne.updateWorldBound();
		      playerOne.updateLocalBound();
		      playerOne.updateGeometricState(0, true);
		      
		      addGameWorldObject(playerOne);
		   } catch (Exception e11)
		      {
			   e11.printStackTrace();
		      }
	      
	      camera1 = new JOGLCamera(renderer);
	      camera1.setPerspectiveFrustum(60, 1, 1, 1000);
	      camera1.setViewport(0.0, 1.0, 0.0, 1.0);
	      
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
      /*HUDString player2ID = new HUDString("Player2");
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
      camera2.addToHUD(player2ScoreString);*/
   }
   private void initGameObjects(){
      createScene();
      // configure game display      
      /*display.setTitle();
      //display.addKeyListener(this);
      
   camera = new JOGLCamera(renderer);
   camera.setPerspectiveFrustum(60, 2, 1, 1000);
   camera.setLocation(new Point3D(0,0,30));
   camera.setViewport(0.0, 1.0, 0.0, 0.45);*/
      
   //   origin = new Point3D();
      rng = new Random();
    
      lineNodes = (SceneNode) engine.get("lineNodes");
      addGameWorldObject(lineNodes);
      
  /*    // add some lines
      Line xLine = new Line(origin, new Point3D(100,0,0), new Color(255,0,0), 1);
      addGameWorldObject(xLine);
      
      Line yLine = new Line(origin, new Point3D(0,100,0), new Color(0,255,0), 1);
      addGameWorldObject(yLine);
      
      Line zLine = new Line(origin, new Point3D(0,0,100), new Color(0,0,255), 1);
      addGameWorldObject(zLine);
   */   
      // add a rectangle, and turn it into a plane
     /* Rectangle plane = new Rectangle(1000, 1000);
      plane.rotate(90, new Vector3D(1,0,0));
      plane.translate(0.0f,-2f,0.0f);
      plane.setColor(new Color(0,255,130));
      addGameWorldObject(plane);*/
      
      //add cube that will grow bigger later
      
      
      // adding objectloader
      try{

 
      
      createRing();

      
      // needs to fix the export for this object. doesn't look like objloader will take it. 
   
    //  fightingRingTriMesh = loader.loadModel("src/a3/kmap165Engine/external_models/fightingRing.obj");



   } catch (Exception e11)
      {
	   e11.printStackTrace();
      }    
      
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
      
      
      // physics
      powerUp = new Sphere();
      Matrix3D puT = powerUp.getLocalTranslation();
      puT.translate(30, 50, 40);
      powerUp.setLocalTranslation(puT);
      addGameWorldObject(powerUp);
      powerUp.updateGeometricState(1.0f, true);
      
      
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
	  
	   if (running)
	   {
		   Matrix3D mat;
		   Vector3D translateVec;
		   Vector3D rotateVec;
		   physicsEngine.update(20.0f);
		   for (SceneNode s : getGameWorld())
		   {
			   if (s.getPhysicsObject() != null)
			   {
				   mat = new Matrix3D(s.getPhysicsObject().getTransform());
				   translateVec = mat.getCol(3);
				   s.getLocalTranslation().setCol(3, translateVec);
			   }
		   }
	   }
	  
	   
      //Update skybox's location
      Point3D camLoc = c1c.getLocation();
      Matrix3D camTranslation = new Matrix3D();
      camTranslation.translate(camLoc.getX(), camLoc.getY(), camLoc.getZ());
      skybox.setLocalTranslation(camTranslation);
      
      //Update skybox2's location
      /*Point3D camLoc2 = c2c.getLocation();
      Matrix3D camTranslation2 = new Matrix3D();
      camTranslation2.translate(camLoc2.getX(), camLoc2.getY(), camLoc2.getZ());
      skybox2.setLocalTranslation(camTranslation2);*/
     // parkingLot.setLocalTranslation(camTranslation);
      //Player 1's crash events 
      if (tpt.getWorldBound().intersects(playerOne.getWorldBound()) && collidedWTeapot == false){

         collidedWTeapot = true;
         numCrashes++;
         score1 += 100;
         CrashEvent newCrash = new CrashEvent(numCrashes);
         removeGameWorldObject(tpt);
         eventMgr.triggerEvent(newCrash);
      }
      if (cyl.getWorldBound().intersects(playerOne.getWorldBound()) && collidedWCylinder == false){

         collidedWCylinder = true; 
         numCrashes++;
         score1 += 500;
         CrashEvent newCrash = new CrashEvent(numCrashes);
         removeGameWorldObject(cyl);
         eventMgr.triggerEvent(newCrash);
      }
      if (sph.getWorldBound().intersects(playerOne.getWorldBound()) && collidedWPyramid == false){

         collidedWPyramid = true;
         numCrashes++;
         score1 += 250;
         CrashEvent newCrash = new CrashEvent(numCrashes);
         removeGameWorldObject(sph);
         eventMgr.triggerEvent(newCrash);
      }
      if (jade.getWorldBound().intersects(playerOne.getWorldBound()) && collidedWDiamond == false){

         collidedWDiamond = true; 
         numCrashes++;
         score1 += 1000;
         CrashEvent newCrash = new CrashEvent(numCrashes);
         removeGameWorldObject(jade);
         eventMgr.triggerEvent(newCrash);
      }
      //Player 2's crash events
      /*if ((tpt.getWorldBound().intersects(p2.getWorldBound())) && collidedWTeapot == false){
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
      }*/
      // update player 1's HUD
      player1ScoreString.setText("Score = " + score1);
      time1 += elapsedTimeMS;
      DecimalFormat df1 = new DecimalFormat("0.0");
      player1TimeString.setText("Time = " + df1.format(time1/1000));
      
      // update player 2's HUD
      /*player2ScoreString.setText("Score = " + score2);
      time2 += elapsedTimeMS;
      DecimalFormat df2 = new DecimalFormat("0.0");
      player2TimeString.setText("Time = " + df2.format(time2/1000));*/
      
      // tell BaseGame to update game world state
      //c0c.update(elapsedTimeMS);
      c1c.update(elapsedTimeMS);
      //c2c.update(elapsedTimeMS);
      
      if (thisClient != null) thisClient.processPackets();
      
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
   private void createRing(){
	    //Creates the base of the fighting ring and uv-wraps it to a texure.
	     TriMesh fightingRing = objectLoader.loadModel("src/a3/kmap165Engine/external_models/fighting_ring/fightingRing_Pad.obj");
	     
		  Texture fightingRing_Filled = TextureManager.loadTexture2D("src/a3/kmap165Engine/external_models/texture/fightingRing_Pad_Filled.jpg");
		  fightingRing.setTexture(fightingRing_Filled);
	     
	     //Creates the four fighting ring posts and uv-wraps them to a texure.
	     Texture fightingRingPost_Filled = TextureManager.loadTexture2D("src/a3/kmap165Engine/external_models/texture/RingPole_Filled.jpg");
	     
	     TriMesh fightingRingPost1 = objectLoader.loadModel("src/a3/kmap165Engine/external_models/fighting_ring/fightingRingPost1.obj");
		  fightingRingPost1.setTexture(fightingRingPost_Filled);
	     
	     TriMesh fightingRingPost2 = objectLoader.loadModel("src/a3/kmap165Engine/external_models/fighting_ring/fightingRingPost2.obj");
		  fightingRingPost2.setTexture(fightingRingPost_Filled);
	     
	     TriMesh fightingRingPost3 = objectLoader.loadModel("src/a3/kmap165Engine/external_models/fighting_ring/fightingRingPost3.obj");
		  fightingRingPost3.setTexture(fightingRingPost_Filled);
	     
	     TriMesh fightingRingPost4 = objectLoader.loadModel("src/a3/kmap165Engine/external_models/fighting_ring/fightingRingPost4.obj");
		  fightingRingPost4.setTexture(fightingRingPost_Filled);
	     
	     //Creates the twelve fighting ring post strings and uv-wraps them to a texure.
	     Texture ringBoundaryString_Filled = TextureManager.loadTexture2D("src/a3/kmap165Engine/external_models/texture/String_Filled.jpg");
	     
	     TriMesh ringBoundaryString1 = objectLoader.loadModel("src/a3/kmap165Engine/external_models/fighting_ring/ringBoundaryString1.obj");
		  ringBoundaryString1.setTexture(ringBoundaryString_Filled);
	     
	     TriMesh ringBoundaryString2 = objectLoader.loadModel("src/a3/kmap165Engine/external_models/fighting_ring/ringBoundaryString2.obj");
		  ringBoundaryString2.setTexture(ringBoundaryString_Filled);
	     
	     TriMesh ringBoundaryString3 = objectLoader.loadModel("src/a3/kmap165Engine/external_models/fighting_ring/ringBoundaryString3.obj");
		  ringBoundaryString3.setTexture(ringBoundaryString_Filled);
	     
	     TriMesh ringBoundaryString4 = objectLoader.loadModel("src/a3/kmap165Engine/external_models/fighting_ring/ringBoundaryString4.obj");
		  ringBoundaryString4.setTexture(ringBoundaryString_Filled);
	     
	     TriMesh ringBoundaryString5 = objectLoader.loadModel("src/a3/kmap165Engine/external_models/fighting_ring/ringBoundaryString5.obj");
		  ringBoundaryString5.setTexture(ringBoundaryString_Filled);
	     
	     TriMesh ringBoundaryString6 = objectLoader.loadModel("src/a3/kmap165Engine/external_models/fighting_ring/ringBoundaryString6.obj");
		  ringBoundaryString6.setTexture(ringBoundaryString_Filled);
	     
	     TriMesh ringBoundaryString7 = objectLoader.loadModel("src/a3/kmap165Engine/external_models/fighting_ring/ringBoundaryString7.obj");
		  ringBoundaryString7.setTexture(ringBoundaryString_Filled);
	     
	     TriMesh ringBoundaryString8 = objectLoader.loadModel("src/a3/kmap165Engine/external_models/fighting_ring/ringBoundaryString8.obj");
		  ringBoundaryString8.setTexture(ringBoundaryString_Filled);
	     
	     TriMesh ringBoundaryString9 = objectLoader.loadModel("src/a3/kmap165Engine/external_models/fighting_ring/ringBoundaryString9.obj");
		  ringBoundaryString9.setTexture(ringBoundaryString_Filled);
	     
	     TriMesh ringBoundaryString10 = objectLoader.loadModel("src/a3/kmap165Engine/external_models/fighting_ring/ringBoundaryString10.obj");
		  ringBoundaryString10.setTexture(ringBoundaryString_Filled);
	     
	     TriMesh ringBoundaryString11 = objectLoader.loadModel("src/a3/kmap165Engine/external_models/fighting_ring/ringBoundaryString11.obj");
		  ringBoundaryString11.setTexture(ringBoundaryString_Filled);
	     
	     TriMesh ringBoundaryString12 = objectLoader.loadModel("src/a3/kmap165Engine/external_models/fighting_ring/ringBoundaryString12.obj");
		  ringBoundaryString12.setTexture(ringBoundaryString_Filled);
	      
	      Group ringGroup1 = new Group(); //fighting ring system position
	      Group ringGroup2 = new Group(); //ring boundary system position
	      Group ringGroup3 = new Group(); //first string system position
	      Group ringGroup4 = new Group(); //second string system position
	      Group ringGroup5 = new Group(); //third string system position
	      Group ringGroup6 = new Group(); //fourth string system position
	      
	      ringGroup1.addChild(fightingRing);
	      ringGroup1.addChild(ringGroup2);
	      
	      ringGroup2.addChild(fightingRingPost1);
	      ringGroup2.addChild(fightingRingPost2);
	      ringGroup2.addChild(fightingRingPost3);
	      ringGroup2.addChild(fightingRingPost4);
	      ringGroup2.addChild(ringGroup3);
	      ringGroup2.addChild(ringGroup4);
	      ringGroup2.addChild(ringGroup5);
	      ringGroup2.addChild(ringGroup6);
	      
	      ringGroup3.addChild(ringBoundaryString1);
	      ringGroup3.addChild(ringBoundaryString2);
	      ringGroup3.addChild(ringBoundaryString3);
	      
	      ringGroup4.addChild(ringBoundaryString4);
	      ringGroup4.addChild(ringBoundaryString5);
	      ringGroup4.addChild(ringBoundaryString6);
	      
	      ringGroup5.addChild(ringBoundaryString7);
	      ringGroup5.addChild(ringBoundaryString8);
	      ringGroup5.addChild(ringBoundaryString9);
	      
	      ringGroup6.addChild(ringBoundaryString10);
	      ringGroup6.addChild(ringBoundaryString11);
	      ringGroup6.addChild(ringBoundaryString12);
	      
	      ringGroup1.setIsTransformSpaceParent(true);
	      ringGroup2.setIsTransformSpaceParent(true);
	      ringGroup3.setIsTransformSpaceParent(true);
	      ringGroup4.setIsTransformSpaceParent(true);
	      ringGroup5.setIsTransformSpaceParent(true);
	      ringGroup6.setIsTransformSpaceParent(true);
	      
	      fightingRingPost1.setIsTransformSpaceParent(true);
	      fightingRingPost2.setIsTransformSpaceParent(true);
	      fightingRingPost3.setIsTransformSpaceParent(true);
	      fightingRingPost4.setIsTransformSpaceParent(true);
	      
	      ringBoundaryString1.setIsTransformSpaceParent(true);
	      ringBoundaryString2.setIsTransformSpaceParent(true);
	      ringBoundaryString3.setIsTransformSpaceParent(true);
	      ringBoundaryString4.setIsTransformSpaceParent(true);
	      ringBoundaryString5.setIsTransformSpaceParent(true);
	      ringBoundaryString6.setIsTransformSpaceParent(true);
	      ringBoundaryString7.setIsTransformSpaceParent(true);
	      ringBoundaryString8.setIsTransformSpaceParent(true);
	      ringBoundaryString9.setIsTransformSpaceParent(true);
	      ringBoundaryString10.setIsTransformSpaceParent(true);
	      ringBoundaryString11.setIsTransformSpaceParent(true);
	      ringBoundaryString12.setIsTransformSpaceParent(true);
	      
	      Matrix3D R1T = ringGroup1.getLocalTranslation();
		   R1T.translate(50, 0, 50);
		   ringGroup1.setLocalTranslation(R1T);
		   Matrix3D R1S = ringGroup1.getLocalScale();
		   R1S.scale(3, 3, 3);
	      ringGroup1.setLocalScale(R1S);
	     
	      ringGroup1.updateLocalBound();
		   ringGroup1.updateGeometricState(0,  true);
		   ringGroup1.updateWorldBound();
	     
	      addGameWorldObject(ringGroup1);
	   }

   protected void render(){
      renderer.setCamera(camera1);
      super.render();
      /*renderer.setCamera(camera2);
      super.render();*/
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
   protected void shutdown(){
      //display.close();

      super.shutdown();
      if(thisClient != null){ 
         thisClient.sendByeMessage();
         try{ 
            thisClient.shutdown(); // shutdown() is inherited
         } 
         catch (IOException e) {
            e.printStackTrace(); 
         }
      }
   }
   /*protected void initSystem(){ 
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
   public Vector3D getPlayerPosition()
   {
	  Vector3D position = playerOne.getLocalTranslation().getCol(3);

	  
      return position;
 
   }
   public boolean isConnected(){
      return isConnected();
   }
   public void setIsConnected(boolean x){
      isConnected = x;
   }
   public void keyPressed(KeyEvent e){}
   public void keyReleased(KeyEvent e){}
   public void keyTyped(KeyEvent e){}
   
   
   
   // more scripting stuff for players
   public class UpdatePlayerColor extends AbstractInputAction {
				
		public void performAction(float t, Event e)
		{
			Invocable invocable = (Invocable) engine;
			SceneNode testingobject = tpt;
			
			try
			{
				invocable.invokeFunction("updateCharacter", testingobject); // error 
			}
			catch (ScriptException t1)
			{
				t1.printStackTrace();
			}
			catch (NoSuchMethodException t2)
			{
				t2.printStackTrace();
			}
			catch (NullPointerException t3)
			{
				t3.printStackTrace();
			}
		}

	}
   

   private void runScript()
    { try
    	{ FileReader fileReader = new FileReader(scriptFile);
 		engine.eval(fileReader);
 		fileReader.close();
 	}
    catch (FileNotFoundException e1)
    { System.out.println(scriptFile + " not found " + e1); }
    catch (IOException e2)
    { System.out.println("IO problem with " + scriptFile + e2); }
 catch (ScriptException e3)
    { System.out.println("ScriptException in " + scriptFile + e3); }
 catch (NullPointerException e4)
    { System.out.println ("Null ptr exception reading " + scriptFile + e4); }
    }
   
   private TerrainBlock createTerBlock(AbstractHeightMap heightmap)
   {
	   float heightscale = .02f;
	   Vector3D terrainScale = new Vector3D(.5, heightscale, .5);
	   
	   int terrainsize = heightmap.getSize();
	   
	   float cornerheight = heightmap.getTrueHeightAtPoint(0,0)*heightscale;
	   Point3D terrainOrig = new Point3D(0,-cornerheight, 0);
	   
	   String name = "terrain" + heightmap.getClass().getSimpleName();
	   TerrainBlock tb = new TerrainBlock(name, terrainsize, terrainScale, heightmap.getHeightData(), terrainOrig);
	   return tb;
   }
   private void initTerrain()
   {
	   
	   
	   ImageBasedHeightMap myHeightMap = new ImageBasedHeightMap("src/a3/images/mountains512.jpg");
	   /*
	   HillHeightMap myHeightMap = new HillHeightMap(129, 2000, 5.0f, 20.0f, (byte)2, 12345);
	   myHeightMap.setHeightScale(0.1f);
	   
	   */
	   hillTerr = createTerBlock(myHeightMap);
	   TextureState groundState;
	   Texture floorTexture = TextureManager.loadTexture2D("src/a3/images/lot_floor.jpg");
	   floorTexture.setApplyMode(sage.texture.Texture.ApplyMode.Replace);
	   hillTerr.setTexture(floorTexture);
	   
	   
	   addGameWorldObject(hillTerr);
   }
public void addNode(GhostAvatar avatar) {
	// TODO Auto-generated method stub
	System.out.println("i'm being called!");
	this.addGameWorldObject(avatar);

}
public void removeNode(GhostAvatar avatar) {
	// TODO Auto-generated method stub
	System.out.println("removenode is being called!");
	this.removeGameWorldObject(avatar);
}
protected void initPhysicsSystem()
{
	String engine = "sage.physics.JBullet.JBulletPhysicsEngine";
	physicsEngine = PhysicsEngineFactory.createPhysicsEngine(engine);
	physicsEngine.initSystem();
	float[] gravity = {0, -1f, 0};
	physicsEngine.setGravity(gravity);
}


private void createSagePhysicsWorld()
{
	float mass = 1.0f;
	powerUpP  = physicsEngine.addSphereObject(physicsEngine.nextUID(), mass, powerUp.getWorldTransform().getValues(), 1.0f);
	powerUpP.setBounciness(1.0f);
	powerUp.setPhysicsObject(powerUpP);
	
	// terrain
	float up[] = {-.05f, .95f, 0};
	terrainP = physicsEngine.addStaticPlaneObject(physicsEngine.nextUID(), hillTerr.getWorldTransform().getValues(), up, 0.0f);
	terrainP.setBounciness(1.0f);
	hillTerr.setPhysicsObject(terrainP);
}
private class StartAction extends AbstractInputAction
{
	public void performAction(float time, Event ee)
	{
		running = true;
	}
}


}