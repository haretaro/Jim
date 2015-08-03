package jim.haretaro.com.github;

import org.aiwolf.client.base.player.AbstractRoleAssignPlayer;

public class JimRoleAssignPlayer extends AbstractRoleAssignPlayer {
	
	public JimRoleAssignPlayer(){
		setSeerPlayer(new JimSeer());
		setVillagerPlayer(new JimVillager());
		setWerewolfPlayer(new JimWerewolf());
		setPossessedPlayer(new JimPossessed());
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}
}
