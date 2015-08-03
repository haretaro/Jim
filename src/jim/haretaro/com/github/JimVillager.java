package jim.haretaro.com.github;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.aiwolf.client.base.player.AbstractVillager;
import org.aiwolf.client.lib.Utterance;
import org.aiwolf.common.data.Agent;
import org.aiwolf.common.data.Role;
import org.aiwolf.common.data.Species;
import org.aiwolf.common.data.Talk;
import org.aiwolf.common.net.GameInfo;

public class JimVillager extends AbstractVillager {
	
	private List<Agent> SeerCOAgents;
	private List<Agent> blackList;
	private List<Agent> whiteList;
	private int readTalkNum;
	
	public JimVillager(){
		SeerCOAgents = new ArrayList<Agent>();
		blackList = new ArrayList<Agent>();
		whiteList = new ArrayList<Agent>();
		readTalkNum = 1;
	}

	@Override
	public void dayStart() {
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void update(GameInfo gameInfo){
		super.update(gameInfo);
		if(SeerCOAgents.contains(gameInfo.getExecutedAgent())){
			SeerCOAgents.remove(gameInfo.getExecutedAgent());
		}
		if(SeerCOAgents.contains(gameInfo.getAttackedAgent())){
			blackList.addAll(SeerCOAgents);
			SeerCOAgents = new ArrayList<Agent>();
		}
		
		if(blackList.contains(gameInfo.getExecutedAgent())){
			blackList.remove(gameInfo.getExecutedAgent());
		}
		if(blackList.contains(gameInfo.getAttackedAgent())){
			blackList.remove(gameInfo.getAttackedAgent());
		}
		

		if(whiteList.contains(gameInfo.getExecutedAgent())){
			whiteList.remove(gameInfo.getExecutedAgent());
		}
		if(whiteList.contains(gameInfo.getAttackedAgent())){
			whiteList.remove(gameInfo.getAttackedAgent());
		}
		
		
		List<Talk> talkList = gameInfo.getTalkList();
		for(int i = readTalkNum; i < talkList.size(); i++){
			Talk talk = talkList.get(i);
			Utterance utterance = new Utterance(talk.getContent());
			switch(utterance.getTopic()){
			case COMINGOUT:
				if(utterance.getRole() == Role.SEER && SeerCOAgents.contains(utterance.getTarget())){
					SeerCOAgents.add(utterance.getTarget());
			}
				break;
			case DIVINED:
				if(SeerCOAgents.size() == 1 && utterance.getResult() == Species.WEREWOLF){
					blackList.add(utterance.getTarget());
				}else if(utterance.getResult() == Species.WEREWOLF){
					whiteList.add(utterance.getTarget());
				}
			default:
				break;
			}
			readTalkNum++;
		}
	}

	@Override
	public String talk() {
		return Talk.OVER;
	}

	@Override
	public Agent vote() {
		
		List<Agent> voteCandidates = new ArrayList<Agent>();
		voteCandidates.addAll(getLatestDayGameInfo().getAliveAgentList());
		voteCandidates.remove(getMe());
		voteCandidates.removeAll(whiteList);
		if(blackList.size() > 0){
			return randomSelect(blackList);
		}else{
			return randomSelect(voteCandidates);
		}
	}
	
	private Agent randomSelect(List<Agent> agentList){
		int num = new Random().nextInt(agentList.size());
		return agentList.get(num);
	}

}
