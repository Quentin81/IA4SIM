package Algorithm;

public abstract class Assignement {

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
