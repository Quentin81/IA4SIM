package Algorithm;

public abstract class Assignement {//This abstract class serves as union between MissionAssignement and UnitAssignement

	private boolean isChosen;
	
		
	public void getChosen(){
	}
	
	public void getUnchosen(){
	}
	
	public void swap(){
		if (isChosen){
			getUnchosen();
		}
		{
			getChosen();
		}
	}
	
	public String toString() {
	    return "Should not exist"; 
	}
}
