
import java.io.*;
import java.util.*;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.vocabulary.*;

import Algorithm.*;

import org.apache.jena.*;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.rulesys.GenericRuleReasonerFactory;
import org.apache.jena.util.FileManager;
import org.apache.jena.util.PrintUtil;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.apache.jena.query.* ;

public class Battleground {

	static String urlModel = "Battleground2.owl";
	static String urlRules = "rulesBattleground1.txt";


	public static void main(String[] args) {
		
		Model rawModel = FileManager.get().loadModel(urlModel);
		InfModel infmodel = ModelFactory.createRDFSModel(rawModel);
		Model postRuleModel = processRules(urlRules, infmodel);
		
		String queryText = "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
				+ "\n"
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
				+ "\n"
				+ "PREFIX myOnto: <http://www.semanticweb.org/kentin/ontologies/2017/2/Battleground#> "
				+ "\n"
				+ "PREFIX owl: <http://www.w3.org/2002/07/owl#>"
				+ "\n"
				+ "PREFIX xsd:  <http://www.w3.org/2001/XMLSchema#>"
				+ "\n"
				+ "SELECT ?mission  WHERE {?mission rdf:type myOnto:Mission"
				+ "}"
				+ "";
		
		Model model = postRuleModel ;
		String queryString = queryText ;
		Query query = QueryFactory.create(queryString) ;
		QueryExecution qexec = QueryExecutionFactory.create(query, model) ;
		TreeSet<String> mission = new TreeSet<String>();
		try {
		  ResultSet results = qexec.execSelect() ;
		  for ( ; results.hasNext() ;)
		  {
		    QuerySolution soln = results.nextSolution();
		    RDFNode x = soln.get("mission");
		    mission.add(x.toString());
		  }
		} finally { qexec.close() ;}
		
		//Resource res = model.getResource("http://www.semanticweb.org/kentin/ontologies/2017/2/Battleground#IR2");
		//printStatements(model,res,null,null);
		
		String units = "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
				+ "\n"
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
				+ "\n"
				+ "PREFIX myOnto: <http://www.semanticweb.org/kentin/ontologies/2017/2/Battleground#> "
				+ "\n"
				+ "PREFIX owl: <http://www.w3.org/2002/07/owl#>"
				+ "\n"
				+ "PREFIX xsd:  <http://www.w3.org/2001/XMLSchema#>"
				+ "\n"
				+ "SELECT ?unit ?mission WHERE {?unit myOnto:can_accomplish ?mission"
				+ "}"
				+ ""; ;
		Query query_units = QueryFactory.create(units) ;
		QueryExecution qexecu = QueryExecutionFactory.create(query_units, model) ;
		HashSet<String> unit = new HashSet<String>();
		
		ArrayList<String> canDo = new ArrayList<String>();
		try {
		  ResultSet results = qexecu.execSelect() ;
		  
		  for ( ; results.hasNext() ;)
		  {
		    QuerySolution soln = results.nextSolution();
		    RDFNode u = soln.get("unit") ;       // Get a result variable by name
		    RDFNode m = soln.get("mission") ;
		    unit.add(u.toString());
		    canDo.add(u+" "+m);
		  }
		} finally { qexecu.close() ;}
		
		FordFulkerson f =new FordFulkerson();
		//System.out.println(mission);
		f.addMission(mission);
		f.addUnit(unit);
		f.addAssignement(canDo);
		List<Assignement> path = new LinkedList<Assignement>();
		MissionAssignement empty = new MissionAssignement();
		path.add(empty);
		//f.assignMissionToUnit(f.mission("http://www.semanticweb.org/kentin/ontologies/2017/2/Battleground#Mission1"), 0, path);
		f.assignAll();
		assignEquipment(f,model);
		
		f.saveGraph("Report.txt");
		
		
		
		
		
	}
	
	public static String myOnto(String uri){
		return "myOnto:"+uri.split("#")[1];
	}
	
	public static void assignEquipment(FordFulkerson f,Model model){
		List<Mission> missions =f.getMissions();
		for(Mission m : missions)
		{
			if(m.getIsAssigned())
			{
				String uriMission= m.getUri();
				Unit u=m.getChoice().getCandidate();
				String uriUnit=m.getChoice().getCandidate().getUri();
				String queryText =
						"PREFIX myOnto: <http://www.semanticweb.org/kentin/ontologies/2017/2/Battleground#> "
						+ "\n"
						+ "ASK { "+myOnto(uriUnit)+" myOnto:can_accomplish_without_equipment "+myOnto(uriMission)+"}";
				Query query = QueryFactory.create(queryText) ;
				QueryExecution qexec = QueryExecutionFactory.create(query, model) ;
				    
				boolean b = qexec.execAsk();
				
				qexec.close();
				if(!b){
					findEquipment(u,uriMission,model);
				}
			}
		}
	}
	
    private static void findEquipment(Unit u, String uriMission,Model model) {
    	String uriUnit = u.getUri();
    	String queryText = "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
				+ "\n"
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
				+ "\n"
				+ "PREFIX myOnto: <http://www.semanticweb.org/kentin/ontologies/2017/2/Battleground#> "
				+ "\n"
				+ "PREFIX owl: <http://www.w3.org/2002/07/owl#>"
				+ "\n"
				+ "PREFIX xsd:  <http://www.w3.org/2001/XMLSchema#>"
				+ "\n"
				+ "ASK {"+myOnto(uriUnit)+" rdf:type myOnto:Infantry}";
		Query query = QueryFactory.create(queryText) ;
		QueryExecution qexec = QueryExecutionFactory.create(query, model) ;
		    
		boolean b = qexec.execAsk();
		    
		qexec.close();
		
		if(b)
		{
			boolean hasSight=false;
			int sight=0;
			boolean hasFirepower=false;
			String firepower="#";
			boolean hasArea=false;
			String area="#";
			
			
			//sight condition
			queryText = "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
					+ "\n"
					+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
					+ "\n"
					+ "PREFIX myOnto: <http://www.semanticweb.org/kentin/ontologies/2017/2/Battleground#> "
					+ "\n"
					+ "PREFIX owl: <http://www.w3.org/2002/07/owl#>"
					+ "\n"
					+ "PREFIX xsd:  <http://www.w3.org/2001/XMLSchema#>"
					+ "\n"
					+ "SELECT ?sight {"+myOnto(uriMission)+" myOnto:need_sight ?sight}";
			query = QueryFactory.create(queryText) ;
			QueryExecution qexecs = QueryExecutionFactory.create(query, model) ;
			    
			try {
				  ResultSet results = qexecs.execSelect() ;
				  
				  for ( ; results.hasNext() ;)
				  {
				    QuerySolution soln = results.nextSolution();
				    sight = soln.get("sight").asLiteral().getInt();
				    hasSight= (sight<=50);
				  }
				} finally { qexecs.close() ;}
			
			
			//firepower condition
			queryText = "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
					+ "\n"
					+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
					+ "\n"
					+ "PREFIX myOnto: <http://www.semanticweb.org/kentin/ontologies/2017/2/Battleground#> "
					+ "\n"
					+ "PREFIX owl: <http://www.w3.org/2002/07/owl#>"
					+ "\n"
					+ "PREFIX xsd:  <http://www.w3.org/2001/XMLSchema#>"
					+ "\n"
					+ "SELECT ?target {"+myOnto(uriMission)+" myOnto:has_target ?target}";
			query = QueryFactory.create(queryText) ;
			QueryExecution qexecf = QueryExecutionFactory.create(query, model) ;
			    
			try {
				  ResultSet results = qexecf.execSelect() ;
				  
				  for ( ; results.hasNext() ;)
				  {
				    QuerySolution soln = results.nextSolution();
				    firepower = soln.get("target").toString();
				    hasFirepower= (firepower.equals("http://www.semanticweb.org/kentin/ontologies/2017/2/Battleground#Weakest_target"));
				  }
				} finally { qexecf.close() ;}
			
			//environment condition
			queryText = "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
					+ "\n"
					+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
					+ "\n"
					+ "PREFIX myOnto: <http://www.semanticweb.org/kentin/ontologies/2017/2/Battleground#> "
					+ "\n"
					+ "PREFIX owl: <http://www.w3.org/2002/07/owl#>"
					+ "\n"
					+ "PREFIX xsd:  <http://www.w3.org/2001/XMLSchema#>"
					+ "\n"
					+ "SELECT ?area {"+myOnto(uriMission)+" myOnto:has_environment ?area}";
			query = QueryFactory.create(queryText) ;
			QueryExecution qexeca = QueryExecutionFactory.create(query, model) ;
			    
			try {
				  ResultSet results = qexeca.execSelect() ;
				  
				  for ( ; results.hasNext() ;)
				  {
				    QuerySolution soln = results.nextSolution();
				    area = soln.get("area").toString();
				    hasArea= (area.equals("http://www.semanticweb.org/kentin/ontologies/2017/2/Battleground#Ground")
				    		||area.equals("http://www.semanticweb.org/kentin/ontologies/2017/2/Battleground#Interior"));
				  }
				} finally { qexeca.close() ;}
			
			//select available equipment
			queryText = "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
					+ "\n"
					+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
					+ "\n"
					+ "PREFIX myOnto: <http://www.semanticweb.org/kentin/ontologies/2017/2/Battleground#> "
					+ "\n"
					+ "PREFIX owl: <http://www.w3.org/2002/07/owl#>"
					+ "\n"
					+ "PREFIX xsd:  <http://www.w3.org/2001/XMLSchema#>"
					+ "\n"
					+ "SELECT ?equip {"+myOnto(uriUnit)+" myOnto:has_equipment ?equip}";
			query = QueryFactory.create(queryText) ;
			QueryExecution qexece = QueryExecutionFactory.create(query, model) ;

		    HashSet<String> equipments = new HashSet<String>();
			    
			try {
				  ResultSet results = qexece.execSelect() ;
				  
				  for ( ; results.hasNext() ;)
				  {
				    QuerySolution soln = results.nextSolution();
				    equipments.add(soln.get("equip").toString());
				  }
				} finally { qexece.close() ;}
			
			Iterator<String> iter = equipments.iterator();
			while(!(hasArea && hasSight && hasFirepower)&& iter.hasNext()){
				String equipment = iter.next();
				String neededEquipment=equipment.split("#")[1];
				boolean testSight=false;
				boolean testArea=false;
				boolean testFirepower=false;
				
				if(!hasSight)
				{
					int equipSight=0;
					//sight condition
					queryText = "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
							+ "\n"
							+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
							+ "\n"
							+ "PREFIX myOnto: <http://www.semanticweb.org/kentin/ontologies/2017/2/Battleground#> "
							+ "\n"
							+ "PREFIX owl: <http://www.w3.org/2002/07/owl#>"
							+ "\n"
							+ "PREFIX xsd:  <http://www.w3.org/2001/XMLSchema#>"
							+ "\n"
							+ "SELECT ?sight {"+myOnto(equipment)+" myOnto:has_sight ?sight}";
					query = QueryFactory.create(queryText) ;
					QueryExecution querySight = QueryExecutionFactory.create(query, model) ;
					    
					try {
						  ResultSet results = querySight.execSelect() ;
						  
						  for ( ; results.hasNext() ;)
						  {
						    QuerySolution soln = results.nextSolution();
						    equipSight = soln.get("sight").asLiteral().getInt();
						    hasSight= (sight<=equipSight);
						  }
						} finally { querySight.close() ;}
					testSight=hasSight;
				}
				
				if(!hasArea)
				{
					String equipArea="";
					//environment condition
					queryText = "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
							+ "\n"
							+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
							+ "\n"
							+ "PREFIX myOnto: <http://www.semanticweb.org/kentin/ontologies/2017/2/Battleground#> "
							+ "\n"
							+ "PREFIX owl: <http://www.w3.org/2002/07/owl#>"
							+ "\n"
							+ "PREFIX xsd:  <http://www.w3.org/2001/XMLSchema#>"
							+ "\n"
							+ "SELECT ?area {"+myOnto(equipment)+" myOnto:can_operate ?area}";
					query = QueryFactory.create(queryText) ;
					QueryExecution querySight = QueryExecutionFactory.create(query, model) ;
					    
					try {
						  ResultSet results = querySight.execSelect() ;
						  
						  for ( ; results.hasNext() ;)
						  {
						    QuerySolution soln = results.nextSolution();
						    equipArea = soln.get("area").toString();
						    hasArea= (equipArea.equals(area));
						  }
						} finally { querySight.close() ;}
					testArea=hasArea;		
				}
				
				if(!hasFirepower)
				{
					//firepower condition
					queryText = "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
							+ "\n"
							+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
							+ "\n"
							+ "PREFIX myOnto: <http://www.semanticweb.org/kentin/ontologies/2017/2/Battleground#> "
							+ "\n"
							+ "PREFIX owl: <http://www.w3.org/2002/07/owl#>"
							+ "\n"
							+ "PREFIX xsd:  <http://www.w3.org/2001/XMLSchema#>"
							+ "\n"
							+ "ASK {"+myOnto(equipment)+" myOnto:can_destroy " +myOnto(firepower)+"}";
					query = QueryFactory.create(queryText) ;
					QueryExecution queryFP = QueryExecutionFactory.create(query, model) ;
					    
					hasFirepower = queryFP.execAsk();
				    
					queryFP.close();
					testFirepower=hasFirepower;
				}
				
				if(testSight||testArea||testFirepower)
				{
					u.addNeededEquipment(neededEquipment);
					u.setNeedEquipment(true);
				}
			}
			
		}
		else
		{
			boolean hasSight=false;
			int sight=0;
			boolean hasFirepower=false;
			String firepower="#";
			boolean hasArea=false;
			String area="#";
			
			
			//sight condition
			queryText = "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
					+ "\n"
					+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
					+ "\n"
					+ "PREFIX myOnto: <http://www.semanticweb.org/kentin/ontologies/2017/2/Battleground#> "
					+ "\n"
					+ "PREFIX owl: <http://www.w3.org/2002/07/owl#>"
					+ "\n"
					+ "PREFIX xsd:  <http://www.w3.org/2001/XMLSchema#>"
					+ "\n"
					+ "SELECT ?sight {"+myOnto(uriMission)+" myOnto:need_sight ?sight}";
			query = QueryFactory.create(queryText) ;
			QueryExecution qexecs = QueryExecutionFactory.create(query, model) ;
			    
			try {
				  ResultSet results = qexecs.execSelect() ;
				  
				  for ( ; results.hasNext() ;)
				  {
				    QuerySolution soln = results.nextSolution();
				    sight = soln.get("sight").asLiteral().getInt();
				  }
				} finally { qexecs.close() ;}
			

			queryText = "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
					+ "\n"
					+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
					+ "\n"
					+ "PREFIX myOnto: <http://www.semanticweb.org/kentin/ontologies/2017/2/Battleground#> "
					+ "\n"
					+ "PREFIX owl: <http://www.w3.org/2002/07/owl#>"
					+ "\n"
					+ "PREFIX xsd:  <http://www.w3.org/2001/XMLSchema#>"
					+ "\n"
					+ "SELECT ?sight {"+myOnto(uriUnit)+" myOnto:has_sight ?sight}";
			query = QueryFactory.create(queryText) ;
			QueryExecution qexecs1 = QueryExecutionFactory.create(query, model) ;
			int sighty=Integer.MAX_VALUE;   
			try {
				  ResultSet results = qexecs1.execSelect() ;
				  
				  for ( ; results.hasNext() ;)
				  {
				    QuerySolution soln = results.nextSolution();
				    sighty = Math.min(sighty,soln.get("sight").asLiteral().getInt());
				  }
				} finally { qexecs1.close() ;}
			hasSight=(sight<=sighty);
			
			//firepower condition
			queryText = "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
					+ "\n"
					+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
					+ "\n"
					+ "PREFIX myOnto: <http://www.semanticweb.org/kentin/ontologies/2017/2/Battleground#> "
					+ "\n"
					+ "PREFIX owl: <http://www.w3.org/2002/07/owl#>"
					+ "\n"
					+ "PREFIX xsd:  <http://www.w3.org/2001/XMLSchema#>"
					+ "\n"
					+ "SELECT ?target {"+myOnto(uriMission)+" myOnto:has_target ?target}";
			query = QueryFactory.create(queryText) ;
			QueryExecution qexecf = QueryExecutionFactory.create(query, model) ;
			    
			try {
				  ResultSet results = qexecf.execSelect() ;
				  
				  for ( ; results.hasNext() ;)
				  {
				    QuerySolution soln = results.nextSolution();
				    firepower = soln.get("target").toString();
				  }
				} finally { qexecf.close() ;}
			
			queryText = "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
					+ "\n"
					+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
					+ "\n"
					+ "PREFIX myOnto: <http://www.semanticweb.org/kentin/ontologies/2017/2/Battleground#> "
					+ "\n"
					+ "PREFIX owl: <http://www.w3.org/2002/07/owl#>"
					+ "\n"
					+ "PREFIX xsd:  <http://www.w3.org/2001/XMLSchema#>"
					+ "\n"
					+ "ASK {"+myOnto(uriUnit)+" myOnto:can_destroy "+ myOnto(firepower) + "}";
			query = QueryFactory.create(queryText) ;
			QueryExecution qexecf1 = QueryExecutionFactory.create(query, model) ;
			    
			hasFirepower = qexecf1.execAsk();
		    
			qexecf1.close();
			
			//environment condition
			queryText = "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
					+ "\n"
					+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
					+ "\n"
					+ "PREFIX myOnto: <http://www.semanticweb.org/kentin/ontologies/2017/2/Battleground#> "
					+ "\n"
					+ "PREFIX owl: <http://www.w3.org/2002/07/owl#>"
					+ "\n"
					+ "PREFIX xsd:  <http://www.w3.org/2001/XMLSchema#>"
					+ "\n"
					+ "SELECT ?area {"+myOnto(uriMission)+" myOnto:has_environment ?area}";
			query = QueryFactory.create(queryText) ;
			QueryExecution qexeca = QueryExecutionFactory.create(query, model) ;
			    
			try {
				  ResultSet results = qexeca.execSelect() ;
				  
				  for ( ; results.hasNext() ;)
				  {
				    QuerySolution soln = results.nextSolution();
				    area = soln.get("area").toString();
				  }
				} finally { qexeca.close() ;}
			
			queryText = "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
					+ "\n"
					+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
					+ "\n"
					+ "PREFIX myOnto: <http://www.semanticweb.org/kentin/ontologies/2017/2/Battleground#> "
					+ "\n"
					+ "PREFIX owl: <http://www.w3.org/2002/07/owl#>"
					+ "\n"
					+ "PREFIX xsd:  <http://www.w3.org/2001/XMLSchema#>"
					+ "\n"
					+ "ASK {"+myOnto(uriUnit)+" myOnto:can_operate "+ myOnto(area) + "}";
			query = QueryFactory.create(queryText) ;
			QueryExecution qexeca1 = QueryExecutionFactory.create(query, model) ;
			    
			hasArea = qexeca1.execAsk();
		    
			qexeca1.close();
			
			
			//select available equipment
			queryText = "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
					+ "\n"
					+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
					+ "\n"
					+ "PREFIX myOnto: <http://www.semanticweb.org/kentin/ontologies/2017/2/Battleground#> "
					+ "\n"
					+ "PREFIX owl: <http://www.w3.org/2002/07/owl#>"
					+ "\n"
					+ "PREFIX xsd:  <http://www.w3.org/2001/XMLSchema#>"
					+ "\n"
					+ "SELECT ?equip {"+myOnto(uriUnit)+" myOnto:has_equipment ?equip}";
			query = QueryFactory.create(queryText) ;
			QueryExecution qexece = QueryExecutionFactory.create(query, model) ;

		    HashSet<String> equipments = new HashSet<String>();
			    
			try {
				  ResultSet results = qexece.execSelect() ;
				  
				  for ( ; results.hasNext() ;)
				  {
				    QuerySolution soln = results.nextSolution();
				    equipments.add(soln.get("equip").toString());
				  }
				} finally { qexece.close() ;}
			
			boolean needsFirepowerAndArea=!hasFirepower && !hasArea;
			
			Iterator<String> iter = equipments.iterator();
			while(!(hasArea && hasSight && hasFirepower)&& iter.hasNext()){
				String equipment = iter.next();
				String neededEquipment=equipment.split("#")[1];
				boolean testSight=false;
				boolean testArea=false;
				boolean testFirepower=false;
				
				if(!hasSight)
				{
					int equipSight=0;
					//sight condition
					queryText = "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
							+ "\n"
							+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
							+ "\n"
							+ "PREFIX myOnto: <http://www.semanticweb.org/kentin/ontologies/2017/2/Battleground#> "
							+ "\n"
							+ "PREFIX owl: <http://www.w3.org/2002/07/owl#>"
							+ "\n"
							+ "PREFIX xsd:  <http://www.w3.org/2001/XMLSchema#>"
							+ "\n"
							+ "SELECT ?sight {"+myOnto(equipment)+" myOnto:has_sight ?sight}";
					query = QueryFactory.create(queryText) ;
					QueryExecution querySight = QueryExecutionFactory.create(query, model) ;
					    
					try {
						  ResultSet results = querySight.execSelect() ;
						  
						  for ( ; results.hasNext() ;)
						  {
						    QuerySolution soln = results.nextSolution();
						    equipSight = soln.get("sight").asLiteral().getInt();
						    hasSight= (sight<=equipSight);
						  }
						} finally { querySight.close() ;}
					testSight=hasSight;
				}
				if(needsFirepowerAndArea){
					//firepower condition
					queryText = "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
							+ "\n"
							+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
							+ "\n"
							+ "PREFIX myOnto: <http://www.semanticweb.org/kentin/ontologies/2017/2/Battleground#> "
							+ "\n"
							+ "PREFIX owl: <http://www.w3.org/2002/07/owl#>"
							+ "\n"
							+ "PREFIX xsd:  <http://www.w3.org/2001/XMLSchema#>"
							+ "\n"
							+ "ASK {"+myOnto(equipment)+" myOnto:can_destroy " +myOnto(firepower)+".\n"
									+myOnto(equipment)+" myOnto:can_operate "+ myOnto(area) + "}";
					query = QueryFactory.create(queryText) ;
					QueryExecution queryFP = QueryExecutionFactory.create(query, model) ;
				    
					hasFirepower = queryFP.execAsk();
					hasArea = hasFirepower;
			    
					queryFP.close();
					testFirepower=hasFirepower;
					testArea=hasArea;
				}
				else
				{
					if(!hasArea)
					{
						String equipArea="";
						//environment condition
						queryText = "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
								+ "\n"
								+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
								+ "\n"
								+ "PREFIX myOnto: <http://www.semanticweb.org/kentin/ontologies/2017/2/Battleground#> "
								+ "\n"
								+ "PREFIX owl: <http://www.w3.org/2002/07/owl#>"
								+ "\n"
								+ "PREFIX xsd:  <http://www.w3.org/2001/XMLSchema#>"
								+ "\n"
								+ "SELECT ?area {"+myOnto(equipment)+" myOnto:can_operate ?area}";
						query = QueryFactory.create(queryText) ;
						QueryExecution querySight = QueryExecutionFactory.create(query, model) ;
						
						try {
							ResultSet results = querySight.execSelect() ;
							
							for ( ; results.hasNext() ;)
							{
								QuerySolution soln = results.nextSolution();
								equipArea = soln.get("area").toString();
								hasArea= (equipArea.equals(area));
							}	
							} finally { querySight.close() ;}
						testArea=hasArea;		
					}
				
					if(!hasFirepower)
					{
						//firepower condition
						queryText = "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
								+ "\n"
								+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
								+ "\n"
								+ "PREFIX myOnto: <http://www.semanticweb.org/kentin/ontologies/2017/2/Battleground#> "
								+ "\n"
								+ "PREFIX owl: <http://www.w3.org/2002/07/owl#>"
								+ "\n"
								+ "PREFIX xsd:  <http://www.w3.org/2001/XMLSchema#>"
								+ "\n"
								+ "ASK {"+myOnto(equipment)+" myOnto:can_destroy " +myOnto(firepower)+"}";
						query = QueryFactory.create(queryText) ;
						QueryExecution queryFP = QueryExecutionFactory.create(query, model) ;
					    
						hasFirepower = queryFP.execAsk();
				    
						queryFP.close();
						testFirepower=hasFirepower;
					}
				}
				
				if(testSight||testArea||testFirepower)
				{
					u.addNeededEquipment(neededEquipment);
					u.setNeedEquipment(true);
				}
			}

		}
		
	}

	public static void printStatements(Model m, Resource s, Property p, Resource o) { 
        PrintUtil.registerPrefix(s.getLocalName(), "http://www.semanticweb.org/kentin/ontologies/2017/2/Battleground#Tank4"); 
        for (StmtIterator i = m.listStatements(s,p,o); i.hasNext(); ) { 
            Statement stmt = i.nextStatement(); 
            System.out.println(" - " + PrintUtil.print(stmt)); 
        } 
    } 
	

	public static Model processRules(String fileloc, InfModel modelIn) { 
 
        // create a simple model; create a resource  and add rules from a file 
        Model m = ModelFactory.createDefaultModel(); 
        Resource configuration =  m.createResource(); 
        configuration.addProperty(ReasonerVocabulary.PROPruleSet, fileloc  ); 
 
 
        // Create an instance of a reasoner 
        Reasoner reasoner = GenericRuleReasonerFactory.theInstance().create(configuration); 
 
 
        // Now with the rawdata model & the reasoner, create an InfModel 
        InfModel infmodel = ModelFactory.createInfModel(reasoner, modelIn); 
 
        // use the following to show everything that can be deduced based on the rules 
        //infmodel.setNsPrefix("drc", "http://www.codesupreme.com/#"); 
        //infmodel.write(System.out, "N3");      
        return infmodel;     
   }
 
}
