package a3.kmap165Engine.npc;

import sage.ai.behaviortrees.BTAction;
import sage.ai.behaviortrees.BTStatus;

public class MopeAround extends BTAction {
	
	NPC npc;
	public MopeAround(NPC n)
	{
		npc = n;
	}
	protected BTStatus update(float time)
	{
		npc.mopeAround();
		return BTStatus.BH_SUCCESS;
	}

}
