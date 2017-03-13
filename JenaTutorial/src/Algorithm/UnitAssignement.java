package Algorithm;

/** 
* This class is the link between a unit and a mission
* 
*
*/

public class UnitAssignement extends Assignement{

	private boolean notFound; //A special variable for special faulty assignment
	private Mission mission; //Destination of the assignment
	private boolean isChosen; //if the assignment is chosen
	private boolean hasSym; //if the assignment has a symmetric (true unless it is faulty)
	private MissionAssignement symmetric; //symmetric assignment
	
	public UnitAssignement(){//This method creates a faulty assignment
		this.notFound=true;
		this.mission=null;
		this.isChosen=true;
		this.hasSym=false;
		this.symmetric=null;
	}
	
	public UnitAssignement(Mission m){
		this.notFound=false;
		this.mission=m;
		this.isChosen=false;
		this.hasSym=false;
		this.symmetric=null;
	}
	
	public boolean getIsNotFound(){
		return notFound;
	}
	
	public Mission getMission(){
		return mission;
	}
	
	public void setMission(Mission m){
		this.mission=m;
	}
	
	public boolean getIsChosen(){
		return isChosen;
	}
	
	public void setIsChosen(boolean b){
		this.isChosen=b;
	}
	
	public MissionAssignement getSymmetric(){
		return symmetric;
	}
	
	public void setSymmetric(MissionAssignement a){
		this.hasSym=true;
		this.symmetric=a;
	}
	
	public void getChosen(){//This method changes the variables accordingly when the assignment is chosen
		this.isChosen=true;
		this.symmetric.setIsChosen(true);
		this.mission.setIsAssigned(true);
		this.mission.setChoice(this.symmetric);
		this.symmetric.getCandidate().setIsAssigned(true);
		this.symmetric.getCandidate().setChoice(this);
	}
	
	public void getUnchosen(){//This method changes the variables accordingly when the assignment is not chosen
		this.isChosen=false;
		if (hasSym)
		{
			this.symmetric.setIsChosen(false);
		}
	}
	
	public void swap(){//This method changes the status of the assignment
		if (isChosen||notFound){
			getUnchosen();
		}
		{
			getChosen();
		}
	}
	
	public String toString() {
	    return (isChosen ? "was chosen for  " : "was not chosen for") + mission; 
	}
}
