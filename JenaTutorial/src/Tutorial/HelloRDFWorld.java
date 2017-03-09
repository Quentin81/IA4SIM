package Tutorial;

import java.io.*;
import java.util.*;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.vocabulary.*;
import org.apache.jena.*;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.apache.jena.query.* ;

public class HelloRDFWorld {
	


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Model m = ModelFactory.createDefaultModel();
		String NS = "http://example.com/test/";
		
		Resource r = m.createResource( NS + "r");
		Property p = m.createProperty( NS + "p");
		
		r.addProperty( p, "hello world", XSDDatatype.XSDstring );
		
		m.write(System.out, "Turtle");
		
		String url = "Bataille3.owl";
		ImportOnt im =new ImportOnt(); 
		
		OntModel test = im.ImportOnt(url);
		
		//Resource fpv = test.getResource("http://www.semanticweb.org/kentin/ontologies/2016/10/untitled-ontology-8#Fire_Power_Value");
		//String name = fpv.getLocalName();
		//System.out.println(name);

		//Resource toast = test.getResource("Blablabla#Jean_Michel_Toast");
		//String toastname = toast.getLocalName();
		//System.out.println(toastname);
		
		//for (Iterator<Resource> i = test.listResourcesWithProperty(test.getProperty("http://www.semanticweb.org/kentin/ontologies/2016/10/untitled-ontology-8#Has_Fire_Power")); i.hasNext(); ) {
		//    System.out.println( i.toString() + " is asserted in class " + i.next() );
		//}
		
		StmtIterator iter = test.listStatements();
		while (iter.hasNext()) {
		    Statement res = iter.nextStatement();
		    
		    String[] sstxt1 = res.asTriple().getSubject().toString().split("#");
		    String[] sstxt2 = res.asTriple().getPredicate().toString().split("#");
		    String[] sstxt3 = res.asTriple().getMatchObject().toString().split("#");

		    if (sstxt1.length==2 && sstxt2.length==2 && sstxt3.length==2) {
		    	String txt1 = sstxt1[1];
		    	String txt2 = sstxt2[1];
		    	String txt3 = sstxt3[1];
			    System.out.println(txt1+" "+txt2+" "+txt3);
		    }
		    	
		}    
		
		System.out.println(" =================================================== ");
		System.out.println(" =================================================== ");
		System.out.println(" =================================================== ");
		
		ExtendedIterator<ObjectProperty> objProp = test.listObjectProperties();
		while (objProp.hasNext()){
			ObjectProperty a = objProp.next();
			System.out.println(a);
		}
		
		System.out.println(" =================================================== ");
		System.out.println(" =================================================== ");
		System.out.println(" =================================================== ");
		
		ExtendedIterator<OntClass> clas = test.listClasses();
		while (clas.hasNext()){
			OntClass a = clas.next();
			System.out.println(a);
		}		

		System.out.println(" =================================================== ");
		System.out.println(" =================================================== ");
		System.out.println(" =================================================== ");
		
		String queryText = "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
				+ "\n"
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
				+ "\n"
				+ "PREFIX myOnto: <http://www.semanticweb.org/kentin/ontologies/2017/2/Bataille3#> "
				+ "\n"
				+ "PREFIX owl: <http://www.w3.org/2002/07/owl#>"
				+ "\n"
				+ "PREFIX xsd:  <http://www.w3.org/2001/XMLSchema#>"
				+ "\n"
				
				//Toutes les relations subClassOf Unite
				//+ "SELECT * WHERE {?subClass rdfs:subClassOf myOnto:Unite}"
				
				//Toutes les missions et sous-classes de mission (obsolète dans Bataille3.owl)
				//+ "SELECT ?mission ?subClass where {?subClass rdfs:subClassOf myOnto:Mission . ?mission rdf:type ?subClass}"

				//Toutes les ObjectProperty
				//+ "SELECT * WHERE {?a ?b owl:ObjectProperty}"
				
				//Toutes les Restrictions
				//+ "SELECT ?a ?b WHERE {?a ?b owl:Restriction } "
				
				//Tous les tanks de vitesse supérieure à 50
				//+ "SELECT ?tank ?speed WHERE {?tank rdf:type myOnto:Tank "
				//+ ". ?tank myOnto:MaxSpeed ?speed "
				//+ ". FILTER(?speed>50) }"
		
				//Toutes les unités capables d'agir à 20m
				//+ "SELECT ?regiment ?weapon WHERE {?regiment rdf:type myOnto:Infantry "
				//+ ". ?regiment myOnto:has ?weapon "
				//+ ". ?regiment myOnto:LineOfSight ?visionUnit"
				//+ ". OPTIONAL {?weapon myOnto:LineOfSight ?visionEquip}"
				//+ ". FILTER ((?visionEquip>20)||(?visionUnit>20))} "
				
				//En chantier, unités capables de détruire une infanterie
				+ "SELECT ?unit WHERE {myOnto:Target_Infantry myOnto:isDetroyedBy ?unit"
				+ "}"
				+ "";
		
		Model model = test ;
		String queryString = queryText ;
		Query query = QueryFactory.create(queryString) ;
		QueryExecution qexec = QueryExecutionFactory.create(query, model) ;
		try {
		  ResultSet results = qexec.execSelect() ;
		  ResultSetFormatter.out(System.out, results, query) ;
		  for ( ; results.hasNext() ;)
		  {
		    QuerySolution soln = results.nextSolution();
		    RDFNode x = soln.get("varName") ;       // Get a result variable by name.
		    Resource re = soln.getResource("VarR") ; // Get a result variable - must be a resource
		    Literal l = soln.getLiteral("VarL") ;   // Get a result variable - must be a literal
		    ResultSetFormatter.out(System.out, results, query) ;
		  }
		  
		    
		  
		  //IDEE A CREUSER ^ 
		} finally { qexec.close() ; }
	}
}
