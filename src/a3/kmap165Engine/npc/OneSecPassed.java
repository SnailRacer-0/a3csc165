package a3.kmap165Engine.npc;

import sage.ai.behaviortrees.BTCondition;

public class OneSecPassed extends BTCondition {
	
	NPCcontroller npcc;
	NPC npc;
	long lastUpdateTime;
	
	public OneSecPassed(NPCcontroller c, NPC n, boolean toNegate)
	{
	super(toNegate);
	npcc = c;
	npc = n;
	lastUpdateTime = System.nanoTime();
	
	}
	
	protected boolean check()
	{
		float elapsedMiliSecs = (System.nanoTime()-lastUpdateTime)/(1000000.0f);
		if (elapsedMiliSecs >= 500.0f)
		{
			lastUpdateTime = System.nanoTime();
			npcc.setNearFlag();
			return true;
		}
		else return false;
	}
}
