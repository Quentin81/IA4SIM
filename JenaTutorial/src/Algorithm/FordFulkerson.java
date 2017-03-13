package Algorithm;
import java.io.*;
import java.util.*;

public class FordFulkerson {
	//This is the class that will execute the algorithm
	private List<Mission> missions; //This is the list of all missions that must be assigned
	private int m; //This is the number of missions to be assigned
	private List<Unit> units; //This is the list of all units that can be assigned to the missions
	
	public FordFulkerson(){
		missions=new LinkedList<Mission>();
		m=0;
		units=new LinkedList<Unit>();
		
	}

	public List<Mission> getMissions(){
		return missions;
	}
	
	public Mission mission(String uri){ //This method returns the mission corresponding to the uri if it exists
		for (Mission m : missions){
			if(m.getUri().equals(uri)){
				return m;
			}
		}
		return null;
	}
	
	public void addMission(String uri){
		addMission(new Mission(uri));
	}
	
	public void addMission(Mission m){
		missions.add(m);
		this.m++;
	}
	
	public void addMission(TreeSet<String> a){ //This method creates all the missions given in a
		for (String str : a)
		{
			addMission(str);
		}
	}
	
	public Unit unit(String uri){//This method returns the unit corresponding to the uri if it exists
		for (Unit u : units){
			if(u.getUri().equals(uri)){
				return u;
			}
		}
		return null;
	}
	
	public void addUnit(String uri){
		addUnit(new Unit(uri));
	}
	
	public void addUnit(Unit u){
		units.add(u);
	}

	public void addUnit(HashSet<String> a){ //This method creates all the units given in a
		for (String str : a)
		{
			addUnit(str);
		}
	}
	
	public void addAssignement(Mission m,Unit u){ //This method creates the assignment needed between m and u
		m.addCandidate(u);
	}
	
	public void addAssignement(ArrayList<String> a){ //This method creates all the assignments contained in a
		for (int i=0;i<a.size();i++)
		{
			String unit = a.get(i).split(" ")[0];
			String mission = a.get(i).split(" ")[1];
			
			addAssignement(mission(mission),unit(unit));
		}
	}
	
	public boolean canAssignMission(Mission m){  //This method returns if the mission is assignable
		MissionAssignement a = m.getFreeAssignement();
		return !a.getIsNotFound();
	}
	
	public void assignMissionToUnit(Mission m, int k, List<Assignement> path){//This is the core of the algorithm
		//k is the depth of the search, if k >= m, all missions have been checked and there is no point to continue
		//path is the list of assignments checked before, it is initialized with a special fake path to function correctly
		if(path.isEmpty()||k>=this.m){
			if(k>=this.m){
				System.out.println(m+" needs an other unit.");
			}
			path.clear();
		}
		else
		{
			//if the search continues, check if it is possible to assign the mission
			MissionAssignement a = m.getFreeAssignement();
			if(!a.getIsNotFound()){ //if so do it
				path.add(a);
				for (Assignement assign : path){
					assign.swap(); //swapping all assignments in the path makes it so the number of assignments is strictly increased
				}
				path.clear();
			}
			else{
				//if there is no possible assignment, check if there is one the assigned missions of the accessible units
				for (MissionAssignement link : m.getPotentialCandidate()){
					path.add(link);
					UnitAssignement b = link.getCandidate().getChoice();
					path.add(b);
					assignMissionToUnit(b.getMission(),k+1,path);
					path.remove(link);
					path.remove(b);
					
				}
			}
		}
	}
	
	public void assignAll(){//this method assign all the missions if possible
		for(Mission m : missions){
			int k=0;
			List<Assignement> path = new LinkedList<Assignement>();
			MissionAssignement empty = new MissionAssignement();
			path.add(empty);
			assignMissionToUnit(m, k, path);
		}
	}
	
	public void saveGraph(String filename) { //this method logs the results of the algorithm in a file named filename
		try {
			PrintStream ps = new PrintStream(filename);
			ps.println("Report");
			for (Mission m : missions) {
				ps.println("mission [");
				ps.println("uri " + m);
				if (m.getIsAssigned()){
					ps.println("is assigned to " + m.getChoice().getCandidate()+ ".");
					if(m.getChoice().getCandidate().isNeedEquipment())
					{
						ps.println(m.getChoice().getCandidate().getNeededEquipment());
					}
				}
				else{
					ps.println("was not assigned.");
				}
				ps.println("]");
			}
			

			ps.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
