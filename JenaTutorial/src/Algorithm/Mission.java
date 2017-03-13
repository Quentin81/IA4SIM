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

	private String uri; //uri of the mission so that it can be found in the ontology
	private boolean isAssigned; //if the mission is assigned to a unit
	private LinkedList<MissionAssignement> list; //list of possible assignments
	private MissionAssignement choice; //chosen assignment if any
	
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
	
	public void addCandidate(Unit u){//to add a new possible unit is to add a canditate
		//to do so, we create a MissionAssignement and a UnitAssignement and we set the needed variables
		MissionAssignement a = new MissionAssignement(u);
		UnitAssignement b = new UnitAssignement(this);
		a.setSymmetric(b);
		b.setSymmetric(a);
		list.add(a);
		u.getPotentialMission().add(b);
		

		//System.out.println(uri +" can be done by "+u.getUri());
	}
	
	public MissionAssignement getFreeAssignement(){ //return a possible assignment if any
		for (int i=0;i<list.size();i++){
			MissionAssignement a = list.get(i);
			if(!a.getIsChosen())
			return a;
		}
		//if not possible, return an empty assignment that is identifiable as empty
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
