package Algorithm;

public abstract class Assignement {

	private boolean notFound;
	private boolean isChosen;
	
	public boolean getIsNotFound(){
		return notFound;
	}
	
	public void getChosen(){
	}
	
	public void getUnchosen(){
	}
	
	public void swap(){
		if (isChosen){
			getUnchosen();
		}
		else{
			getChosen();
		}
	}
	
	public String toString() {
	    return "Should not exist"; 
	}
}
