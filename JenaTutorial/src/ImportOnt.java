import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;

public class ImportOnt {

	public OntModel ImportOnt(String url)
	{
	OntModel m = ModelFactory.createOntologyModel();
	m.read(url);
	return m;
	}
	}
