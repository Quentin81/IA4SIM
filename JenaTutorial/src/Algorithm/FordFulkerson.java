package Algorithm;
import java.io.*;
import java.util.*;

public class FordFulkerson {

	private List<Mission> missions;
	private int m;
	private List<Unit> units;
	
	public FordFulkerson(){
		missions=new LinkedList<Mission>();
		m=0;
		units=new LinkedList<Unit>();
		
	}

	public List<Mission> getMissions(){
		return missions;
	}
	
	public Mission mission(String uri){
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
	
	public void addMission(TreeSet<String> a){
		for (String str : a)
		{
			addMission(str);
		}
	}
	
	public Unit unit(String uri){
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

	public void addUnit(HashSet<String> a){
		for (String str : a)
		{
			addUnit(str);
		}
	}
	
	public void addAssignement(Mission m,Unit u){
		m.addCandidate(u);
	}
	
	public void addAssignement(ArrayList<String> a){
		for (int i=0;i<a.size();i++)
		{
			String unit = a.get(i).split(" ")[0];
			String mission = a.get(i).split(" ")[1];
			
			addAssignement(mission(mission),unit(unit));
		}
	}
	
	public boolean canAssignMission(Mission m){
		MissionAssignement a = m.getFreeAssignement();
		return !a.getIsNotFound();
	}
	
	public void assignMissionToUnit(Mission m, int k, List<Assignement> path){
		if(path.isEmpty()||k>=this.m){
			if(k>=this.m){
				System.out.println(m+" needs an other unit.");
			}
			path.clear();
		}
		else
		{
			MissionAssignement a = m.getFreeAssignement();
			if(!a.getIsNotFound()){
				path.add(a);
				for (Assignement assign : path){
					assign.swap();
				}
				path.clear();
			}
			else{
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
	
	public void assignAll(){
		for(Mission m : missions){
			int k=0;
			List<Assignement> path = new LinkedList<Assignement>();
			MissionAssignement empty = new MissionAssignement();
			path.add(empty);
			assignMissionToUnit(m, k, path);
		}
	}
	
	public void saveGraph(String filename) {
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
	
	public static void main(String[] args){
		
		
		
	}
}
