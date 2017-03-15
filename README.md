# IA4SIM

By Tithnara SUN & Quentin AUBERTOT

Keywords: Inference engine, ontology, OWL, SPARQL

Military officers must train themselves in taking real-time decisions in a battlefield. To do so, the military uses computer simulation tools, as it would be too complicated and not very cost-effective to make a mock battlefield using real soldiers every time an officer must train.

To make a realistic simulation environment, the simulation tool has to recreate the behavior of real soldiers, this is why it is crucial to design a proper artificial intelligence system. One of the ways of making an artificial intelligence system is to design an ontology and a reasoner based on such ontology. This is the goal of the project IA4SIM.

After some intensive research, we reached the conclusion to use OpenSource ways to reach our goal as it was the most cost-effective ways, the ontology was written using Protégé and the reasoner was written using the Jena plugin in the Eclipse environment. Also we decided to use Operations Research means to complete our reasoner because it seemed like the natural evolution for our project.

Ultimately, we were able to design an ontology including many military concepts such as Unit, Mission, Target, Environment & Equipment. We could create a reasoner that supports 35 rules and we completed it with an implemented adapted Ford-Fulkerson algorithm to assign missions to units.

The project is currently fully functional. It can be further developed to complexify the ontology or optimize the algorithm. Also rules can be added for a sharper artificial intelligence. 
