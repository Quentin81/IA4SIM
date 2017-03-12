

import java.io.*;
import java.util.*;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.vocabulary.*;
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
		
		//Resource res = model.getResource("http://www.semanticweb.org/kentin/ontologies/2017/2/Battleground#Mission5");
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
		System.out.println("========================");
		System.out.println("========================");
		System.out.println("========================");
		System.out.println("========================");
		
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
		f.saveGraph("Report.txt");
		
		
		
		
		
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
