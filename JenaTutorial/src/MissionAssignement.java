
public class MissionAssignement extends Assignement{

	private boolean notFound;
	private Unit candidate;
	private boolean isChosen;
	private boolean hasSym;
	private UnitAssignement symmetric;
	
	public MissionAssignement(){
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
	
	public void getChosen(){
		this.isChosen=true;
		this.candidate.setIsAssigned(true);
		this.candidate.setChoice(this.symmetric);
		this.symmetric.setIsChosen(true);
		this.symmetric.getMission().setIsAssigned(true);
		this.symmetric.getMission().setChoice(this);
	}
	
	public void getUnchosen(){
		this.isChosen=false;
		if (hasSym)
		{
			this.symmetric.setIsChosen(false);
		}
	}
	
	public void swap(){
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
