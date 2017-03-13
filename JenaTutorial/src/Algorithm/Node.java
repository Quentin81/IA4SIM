package Algorithm;
import java.util.LinkedList;

public abstract class Node //This abstract class serves as union between Unit & Mission

	private String uri;
	private boolean isAssigned;
	
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
	
	public String toString() {
	    return uri;
	}
}
