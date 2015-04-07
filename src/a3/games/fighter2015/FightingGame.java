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
import java.io.File; 
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException; 
import java.net.UnknownHostException;
import java.net.URL; 

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
   private Group rootNode;
   private SceneNode lineNodes; 
   private SkyBox skybox, skybox2; 
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
      
      initGameObjects();
 //     initTerrain();
      createPlayers();
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
      

      
      // scriptTestInput
      UpdatePlayerColor updateColor = new UpdatePlayerColor();
      im.associateAction(Keyboard, net.java.games.input.Component.Identifier.Key.SPACE, updateColor, 
    		  IInputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
      
      
      c1c = new Camera3Pcontroller(camera1,p1,im,mouseName);
      //c2c = new Camera3Pcontroller(camera2,p2,im,mouseName);
      //c2c = new Camera3Pcontroller(camera2,p2,im,gpName);
      //Controls for P1
      ForwardAction mvForward = new ForwardAction(p1, thisClient);
      im.associateAction(Keyboard,
         net.java.games.input.Component.Identifier.Key.S,
         mvForward,
         IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
         
      BackwardAction mvBackward = new BackwardAction(p1, thisClient);
      im.associateAction(Keyboard,
         net.java.games.input.Component.Identifier.Key.W,
         mvBackward,
         IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
         
      LeftAction mvLeft = new LeftAction(p1, thisClient);
      im.associateAction(Keyboard,
         net.java.games.input.Component.Identifier.Key.A,
         mvLeft,
         IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
         
      RightAction mvRight = new RightAction(p1, thisClient);
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
      
      //skybox2 = skybox; 
      //scene.addChild(skybox2); 

 	 //  scene.addChild(parkingLot);
 	   
 		 
 		AbstractHeightMap heightmap = null; 
 
 
 		heightmap = new ImageBasedHeightMap(testMountain.getImage()); 
 		heightmap.load(); 
 
 
 		Vector3D scaleFactor = new Vector3D(new Point3D(1, 1, 1)); 
 		 
 		try 
 		{ 
 		parkingLot1 = new TerrainBlock("tblock", 512, scaleFactor, heightmap.getHeightData(), new Point3D( 0, 0, 0));
 		parkingLot1.setTexture(downTex);
 		Matrix3D p1LotT = parkingLot1.getLocalTranslation();
 		p1LotT.translate(0.0f, -0.5f, 0.0f);
 		parkingLot1.setLocalTranslation(p1LotT);
 		Matrix3D p1Scale = parkingLot1.getLocalScale();
 		p1Scale.scale(10f, 10f, 0);
 		parkingLot1.setLocalScale(p1Scale);
 		Matrix3D p1Rot = new Matrix3D();
 		p1Rot.rotateX(-90);
 		parkingLot1.setLocalRotation(p1Rot);
 		
 		
 		parkingLot2 = new TerrainBlock("tblock2", 512, scaleFactor, heightmap.getHeightData(), new Point3D( 0, 0, 0));
 		parkingLot2.setTexture(upTex);
 		Matrix3D p2LotT = parkingLot2.getLocalTranslation();
 		p2LotT.translate(0.0f, -0.5f, 0.0f);
 		parkingLot2.setLocalTranslation(p2LotT);
 		Matrix3D p2Scale = parkingLot2.getLocalScale();
 		p2Scale.scale(10f, 10f, 0);
 		parkingLot2.setLocalScale(p2Scale);
 		Matrix3D p2Rot = new Matrix3D();
 		p2Rot.rotateZ(90);
 		p2Rot.rotateY(90);
 		parkingLot2.setLocalRotation(p2Rot);
 		
 		parkingLot3 = new TerrainBlock("tblock3", 512, scaleFactor, heightmap.getHeightData(), new Point3D( 0, 0, 0));
 		parkingLot3.setTexture(eastTex);
 		Matrix3D p3LotT = parkingLot3.getLocalTranslation();
 		p3LotT.translate(0.0f, -0.5f, 0.0f);
 		parkingLot3.setLocalTranslation(p3LotT);
 		Matrix3D p3Scale = parkingLot3.getLocalScale();
 		p3Scale.scale(100f, 100f, 0);
 		parkingLot3.setLocalScale(p3Scale);
 		Matrix3D p3Rot = new Matrix3D();
 		p3Rot.rotateX(90);
 		parkingLot3.setLocalRotation(p3Rot);
 		
 		parkingLot4 = new TerrainBlock("tblock4", 512, scaleFactor, heightmap.getHeightData(), new Point3D( 0, 0, 0));
 		parkingLot4.setTexture(upTex);
 		Matrix3D p4LotT = parkingLot4.getLocalTranslation();
 		p4LotT.translate(0.0f, -0.5f, 0.0f);
 		parkingLot2.setLocalTranslation(p2LotT);
 		Matrix3D p4Scale = parkingLot4.getLocalScale();
 		p4Scale.scale(10f, 10f, 0);
 		parkingLot4.setLocalScale(p4Scale);
 		Matrix3D p4Rot = new Matrix3D();
 		p4Rot.rotateZ(90);
 		p4Rot.rotateY(-90);
 		parkingLot4.setLocalRotation(p4Rot);
 		
 		} catch (Exception e) 
 		{ 
 			e.printStackTrace(); 
 		} 
 	//	scene.addChild(parkingLotT); 
 		scene.addChild(parkingLot1);
 		scene.addChild(parkingLot2);
 		scene.addChild(parkingLot3);
 		scene.addChild(parkingLot4);
 		 
		addGameWorldObject(scene); 
 		 
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
      
      /*p2 = new Cube("PLAYER2");
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
      camera2.setViewport(0.0, 1.0, 0.55, 1.0);*/
      
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
   public Matrix3D getPlayerPosition(){
      return p1.getLocalTranslation();
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
	   float heightscale = .005f;
	   Vector3D terrainScale = new Vector3D(100, heightscale, 100);
	   
	   int terrainsize = heightmap.getSize();
	   
	   float cornerheight = heightmap.getTrueHeightAtPoint(0,0)*heightscale;
	   Point3D terrainOrig = new Point3D(0,-cornerheight, 0);
	   
	   String name = "terrain" + heightmap.getClass().getSimpleName();
	   TerrainBlock tb = new TerrainBlock(name, terrainsize, terrainScale, heightmap.getHeightData(), terrainOrig);
	   return tb;
   }
   private void initTerrain()
   {
	   
	   
	   HillHeightMap myHeightMap = new HillHeightMap(129, 2000, 5.0f, 20.0f, (byte)2, 12345);
	   myHeightMap.setHeightScale(0.1f);
	   TerrainBlock hillTerr = createTerBlock(myHeightMap);
	   TextureState groundState;
	   Texture floorTexture = TextureManager.loadTexture2D("lot_floor.jpg");
	   floorTexture.setApplyMode(sage.texture.Texture.ApplyMode.Replace);
	//   groundState = (TextureState)
	//		   display.getRenderer().createRenderState(RenderState.RenderStateType.Texture);
	//   groundState.setTexture(floorTexture, 0);
	//   groundState.setEnabled(true);
	   
	 //  hillTerr.setRenderState(groundState);
	   hillTerr.setTexture(floorTexture);
	   addGameWorldObject(hillTerr);
   }
}