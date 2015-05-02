package a3.kmap165Engine.npc;

import sage.ai.behaviortrees.BTAction;
import sage.ai.behaviortrees.BTStatus;

public class AttackAvatar extends BTAction {
	
	NPC npc;
	public AttackAvatar(NPC n)
	{
		npc = n;
	}
	protected BTStatus update(float time)
	{
		npc.attackAvatar();
		return BTStatus.BH_SUCCESS;
	}

}