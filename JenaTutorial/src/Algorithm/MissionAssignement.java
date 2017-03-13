package Algorithm;

/** 
* This class is the link between a mission and a unit
* 
*
*/

public class MissionAssignement extends Assignement{

	private boolean notFound; //A special variable for special faulty assignment
	private Unit candidate; //Destination of the assignment
	private boolean isChosen; //if the assignment is chosen
	private boolean hasSym; //if the assignment has a symmetric (true unless it is faulty)
	private UnitAssignement symmetric; //symmetric assignment
	
	public MissionAssignement(){//This method creates a faulty assignment
		this.notFound=true;
		this.candidate=null;
		this.isChosen=true;
		this.hasSym=false;
		this.symmetric=null;
	}
	
	public MissionAssignement(Unit n){
		this.notFound=false;
		this.candidate=n;
		this.isChosen=false;
		this.hasSym=false;
		this.symmetric=null;
	}
	
	public boolean getIsNotFound(){
		return notFound;
	}
	
	public Unit getCandidate(){
		return candidate;
	}
	
	public void setCandidate(Unit n){
		this.candidate=n;
	}
	
	public boolean getIsChosen(){
		return isChosen;
	}
	
	public void setIsChosen(boolean b){
		this.isChosen=b;
	}
	
	public UnitAssignement getSymmetric(){
		return symmetric;
	}
	
	public void setSymmetric(UnitAssignement a){
		this.hasSym=true;
		this.symmetric=a;
	}
	
	public void getChosen(){//This method changes the variables accordingly when the assignment is chosen
		this.isChosen=true;
		this.candidate.setIsAssigned(true);
		this.candidate.setChoice(this.symmetric);
		this.symmetric.setIsChosen(true);
		this.symmetric.getMission().setIsAssigned(true);
		this.symmetric.getMission().setChoice(this);
	}
	
	public void getUnchosen(){//This method changes the variables accordingly when the assignment is not chosen
		this.isChosen=false;
		if (hasSym)
		{
			this.symmetric.setIsChosen(false);
		}
	}
	
	public void swap(){//This method changes the status of the assignment
		if (!notFound){
			if (isChosen){
				getUnchosen();
			}
			else{
				getChosen();
			}
		}
	}
	
	public String toString() {
	    return (isChosen ? "has chosen " : "has not chosen") + candidate; 
	}
}
