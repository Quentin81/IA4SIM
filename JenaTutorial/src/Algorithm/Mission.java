package Algorithm;

import java.util.LinkedList;

/** Mission class
 * 
 * Units must be assigned to missions
 * 
 * 
 * 
 */

public class Mission extends Node {

	private String uri;
	private boolean isAssigned;
	private LinkedList<MissionAssignement> list;
	private MissionAssignement choice;
	
	/**
	 * 
	 * 
	 */
	public Mission(String uri){
		//System.out.println(uri);
		this.uri=uri;
		this.isAssigned=false;
		this.list=new LinkedList<MissionAssignement>();
		this.setChoice(null);
	}
	
	public String getUri(){
		return uri;
	}
	
	public void setUri(String uri){
		this.uri=uri;
	}
	
	public boolean getIsAssigned(){
		return isAssigned;
	}
	
	public void setIsAssigned(boolean b){
		this.isAssigned=b;
	}
	
	public LinkedList<MissionAssignement> getPotentialCandidate(){
		return list;
	}
	
	public void addCandidate(Unit u){
		MissionAssignement a = new MissionAssignement(u);
		UnitAssignement b = new UnitAssignement(this);
		a.setSymmetric(b);
		b.setSymmetric(a);
		list.add(a);
		u.getPotentialMission().add(b);
		

		//System.out.println(uri +" can be done by "+u.getUri());
	}
	
	public MissionAssignement getFreeAssignement(){
		for (int i=0;i<list.size();i++){
			MissionAssignement a = list.get(i);
			if(!a.getIsChosen())
			return a;
		}
		
		return new MissionAssignement();
	}

	public MissionAssignement getChoice() {
		return choice;
	}

	public void setChoice(MissionAssignement choice) {
		this.choice = choice;
	}
	
	public String toString() {
	    return uri.split("#")[1];
	}
}
