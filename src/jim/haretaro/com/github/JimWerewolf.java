package jim.haretaro.com.github;

import java.util.List;
import java.util.Random;

import org.aiwolf.client.base.player.AbstractWerewolf;
import org.aiwolf.common.data.Agent;

public class JimWerewolf extends AbstractWerewolf {

	@Override
	public Agent attack() {
		List<Agent> candidates = getLatestDayGameInfo().getAliveAgentList();
		candidates.remove(getMe());
		for(Agent wolf : getWolfList()){
			if(candidates.contains(wolf)){
				candidates.remove(wolf);
			}
		}
		return randomSelect(candidates);
	}

	@Override
	public void dayStart() {
		// TODO Auto-generated method stub∂
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}

	@Override
	public String talk() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Agent vote() {

		List<Agent> candidates = getLatestDayGameInfo().getAliveAgentList();
		candidates.remove(getMe());
		for(Agent wolf : getWolfList()){
			if(candidates.contains(wolf)){
				candidates.remove(wolf);
			}
		}
		return randomSelect(candidates);
	}

	@Override
	public String whisper() {
		// TODO Auto-generated method stub
		return null;
	}
	

	private Agent randomSelect(List<Agent> agentList){
		int num = new Random().nextInt(agentList.size());
		return agentList.get(num);
	}

}
