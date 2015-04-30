package a3.kmap165Engine.npc;

import java.util.Random;

import sage.ai.behaviortrees.*;
import a3.kmap165Engine.network.*;



public class NPCcontroller {

	private Random rn;
	private BehaviorTree bt;
	private long lastUpdateTime;
	private long startTime;
	private GameServerTCP server;
	private NPC npc;
	private boolean nearFlag;
	
	public void startNPCControl()
	{
		bt = new BehaviorTree(BTCompositeType.SELECTOR);
		startTime = System.nanoTime();
		lastUpdateTime = startTime;
		setupNPC();
		setupBehaviorTree();
		npcLoop();
	}
	
	public void setupNPC()
	{
		npc = new NPC();
		npc.randomizeLocation(rn.nextInt(100), rn.nextInt(100));
	}
	public void npcLoop()
	{
		while (true)
		{
			long frameStartTime = System.nanoTime();
			float elapsedMiliSecs = (frameStartTime-lastUpdateTime)/(1000000.0f);

			if (elapsedMiliSecs >= 50.0f)
			{
				lastUpdateTime = frameStartTime;
				npc.updateLocation();
				server.sendNPCinfo();
				bt.update(elapsedMiliSecs);
			}
			Thread.yield();
		}
		
	}
	public void setupBehaviorTree()
	{
		bt.insertAtRoot(new BTSequence(10));
		bt.insertAtRoot(new BTSequence(20));
		bt.insert(10, new OneSecPassed(this, npc, false));
		bt.insert(10,  new MopeAround(npc));
		bt.insert(20, new AvatarNear(server, this, npc, false));
		bt.insert(20, new AttackAvatar(npc));
	}

	public boolean getNearFlag() {
		// TODO Auto-generated method stub
		return nearFlag;
	}
	public void setNearFlag()
	{
		nearFlag = true; // test
	}
	
	
}
