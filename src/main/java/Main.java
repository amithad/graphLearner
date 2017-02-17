import Unagi.SsAD.graphical.CADAdapter;
import Unagi.SsAD.graphical.CADAgent;
import Unagi.SsAD.graphical.GraphGenerator;
import Unagi.SsAD.graphical.SsADVisualizer;

import static java.lang.Thread.sleep;

public class Main {

    public static final int DETAILED_GRAPH = 0;
    public static final int HIGHLEVEL_GRAPH = 1;

    public static void main(String[] args) {

        GraphGenerator gen1 = new GraphGenerator();
        CADAdapter adapter1 = new CADAdapter(5,4, gen1);
        CADAgent agent1 = new CADAgent(adapter1);
        gen1.start();
        agent1.start();

        try{
            sleep(2000);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        SsADVisualizer visualizer1 = new SsADVisualizer(gen1.getGraph(0), DETAILED_GRAPH);
        SsADVisualizer visualizer2 = new SsADVisualizer(gen1.getGraph(0), HIGHLEVEL_GRAPH);
        visualizer1.init();
        visualizer1.launch();
        visualizer2.init();
        visualizer2.launch();

    }
}
