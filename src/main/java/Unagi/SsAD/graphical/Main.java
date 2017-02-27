package Unagi.SsAD.graphical;

import Unagi.SsAD.graphical.Graph.ExportMatrix;

import java.io.*;

import static java.lang.Thread.sleep;

public class Main {

    public static final int DETAILED_GRAPH = 0;
    public static final int HIGHLEVEL_GRAPH = 1;

    public static final int COMP_COUNT = 5;
    public static final int ERROR_COUNT = 4;

    public static void main(String[] args) {

        GraphGenerator gen1 = new GraphGenerator();
        //CSVMan csvMan1 = new CSVMan(gen1);
        CADAdapter adapter1 = new CADAdapter(COMP_COUNT, ERROR_COUNT, gen1);
        CADAgent agent1 = new CADAgent(adapter1);
        gen1.start();
        //csvMan1.start();
        agent1.start();

        try {
            sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        SsADVisualizer visualizer1 = new SsADVisualizer(gen1.getGraph(0), DETAILED_GRAPH, "Component-Error Graph");
        SsADVisualizer visualizer2 = new SsADVisualizer(gen1.getGraph(0), HIGHLEVEL_GRAPH, "Components Graph");
        visualizer1.initialize();
        visualizer2.initialize();
        visualizer1.launch();
        visualizer2.launch();

        try {
            ExportMatrix.writeToFile(gen1.getGraph(gen1.getGraphCount() - 1), "Seq" + (gen1.getGraphCount() - 1));
        } catch (IOException e) {
            e.printStackTrace();
        }

        GraphAnalyzer ga1 = new GraphAnalyzer(gen1);
        ga1.start();
    }
}
