package Algorithm;

public class UnitAssignement extends Assignement{

	private boolean notFound;
	private Mission mission;
	private boolean isChosen;
	private boolean hasSym;
	private MissionAssignement symmetric;
	
	public UnitAssignement(){
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
	
	public void getChosen(){
		this.isChosen=true;
		this.symmetric.setIsChosen(true);
		this.mission.setIsAssigned(true);
		this.mission.setChoice(this.symmetric);
		this.symmetric.getCandidate().setIsAssigned(true);
		this.symmetric.getCandidate().setChoice(this);
	}
	
	public void getUnchosen(){
		this.isChosen=false;
		if (hasSym)
		{
			this.symmetric.setIsChosen(false);
		}
	}
	
	public void swap(){
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
