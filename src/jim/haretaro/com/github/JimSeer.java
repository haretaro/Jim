package jim.haretaro.com.github;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.aiwolf.client.base.player.AbstractSeer;
import org.aiwolf.client.lib.TemplateTalkFactory;
import org.aiwolf.client.lib.Utterance;
import org.aiwolf.common.data.Agent;
import org.aiwolf.common.data.Judge;
import org.aiwolf.common.data.Role;
import org.aiwolf.common.data.Species;
import org.aiwolf.common.data.Talk;
import org.aiwolf.common.net.GameInfo;

public class JimSeer extends AbstractSeer {
	
	private boolean isComingOut = false;
	private List<Judge> myToldJudgeList;
	private int readTalkNum;
	private List<Agent> fakeSeerCOAgent;
	
	@Override
	public void dayStart(){

		myToldJudgeList = new ArrayList<Judge>();
		fakeSeerCOAgent = new ArrayList<Agent>();
		super.dayStart();
		readTalkNum = 0;
	}
	
	
	
	@Override
	public void update(GameInfo gameInfo){
		super.update(gameInfo);
		List<Talk> talkList = gameInfo.getTalkList();
		for(int i = readTalkNum; i < talkList.size(); i++){
			Talk talk = talkList.get(i);
			Utterance utterance = new Utterance(talk.getContent());
			switch(utterance.getTopic()){
			case COMINGOUT:
				if(utterance.getRole() == Role.SEER && !talk.getAgent().equals(getMe())){
					fakeSeerCOAgent.add(utterance.getTarget());
			}
				break;
			case DIVINED:
				break;
			default:
				break;
			}
			readTalkNum++;
		}
	}

	@Override
	public Agent divine() {
		List<Agent> divineCandidates = new ArrayList<Agent>();
		divineCandidates.addAll(getLatestDayGameInfo().getAliveAgentList());
		divineCandidates.remove(getMe());
		for(Judge judge: getMyJudgeList()){
			if(divineCandidates.contains(judge.getTarget())){
				divineCandidates.remove(judge.getTarget());
			}
		}
		if(divineCandidates.size() > 0){
			return randomSelect(divineCandidates);
		}else{
			return getMe();
		}
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}

	@Override
	public String talk() {
		if(!isComingOut){
			for(Judge judge: getMyJudgeList()){
				if(judge.getResult() == Species.WEREWOLF){
					String comingoutTalk = TemplateTalkFactory.comingout(getMe(),getMyRole());
					isComingOut = true;
					return comingoutTalk;
				}
			}
		}else{
			for(Judge judge : getMyJudgeList()){
				if(!myToldJudgeList.contains(judge)){
					String resultTalk = TemplateTalkFactory.divined(judge.getTarget(),judge.getResult());
					myToldJudgeList.add(judge);
					return resultTalk;
				}
			}
		}
		return Talk.OVER;
	}

	@Override
	public Agent vote() {
		List<Agent> whiteAgent = new ArrayList<Agent>(),
				blackAgent = new ArrayList<Agent>();
		
		for(Judge judge: getMyJudgeList()){
			if(getLatestDayGameInfo().getAliveAgentList().contains(judge.getTarget())){
				switch(judge.getResult()){
				case HUMAN:
					whiteAgent.add(judge.getTarget());
					break;
				case WEREWOLF:
					blackAgent.add(judge.getTarget());
				}
			}
		}
		
		if(blackAgent.size() > 0){
			return randomSelect(blackAgent);
		}else{
			List<Agent> voteCandidates = new ArrayList<Agent>();
			voteCandidates.addAll(getLatestDayGameInfo().getAliveAgentList());
			voteCandidates.remove(getMe());
			voteCandidates.removeAll(whiteAgent);
			return randomSelect(voteCandidates);
		}
	}
	
	private Agent randomSelect(List<Agent> agentList){
		int num = new Random().nextInt(agentList.size());
		return agentList.get(num);
	}

}
