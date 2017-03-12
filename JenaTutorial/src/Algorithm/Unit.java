package Algorithm;
import java.util.LinkedList;

public class Unit extends Node {
	
	private String uri;
	private boolean isAssigned;
	private LinkedList<UnitAssignement> list;
	private UnitAssignement choice;
	private Mission mission;
	private boolean needEquipment;
	private String neededEquipment;
	
	public Unit(String uri){
		//System.out.println(uri);
		this.uri=uri;
		this.isAssigned=false;
		this.list=new LinkedList<UnitAssignement>();
		this.setChoice(null);
		this.mission=null;
		this.setNeedEquipment(false);
		this.neededEquipment="";
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
	
	public LinkedList<UnitAssignement> getPotentialMission(){
		return list;
	}

	public UnitAssignement getChoice() {
		return choice;
	}

	public void setChoice(UnitAssignement choice) {
		this.choice = choice;
	}
	
	public Mission getMission() {
		return mission;
	}

	public void setMission(Mission mission) {
		this.mission = mission;
	}
	
	public String toString() {
	    return uri.split("#")[1];
	}

	public boolean isNeedEquipment() {
		return needEquipment;
	}

	public void setNeedEquipment(boolean needEquipment) {
		this.needEquipment = needEquipment;
	}

	public String getNeededEquipment() {
		return neededEquipment;
	}

	public void addNeededEquipment(String neededEquipment) {
		this.neededEquipment=this.neededEquipment+ "needs "+neededEquipment+"\n";
	}
}
