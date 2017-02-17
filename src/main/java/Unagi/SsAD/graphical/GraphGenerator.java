package Unagi.SsAD.graphical;

import Unagi.SsAD.graphical.Graph.CompErrorNode;
import Unagi.SsAD.graphical.Graph.CompNode;
import Unagi.SsAD.graphical.Graph.SSADGraph;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;


/**
 * Created by amitha on 12/4/16.
 */
public class GraphGenerator extends Thread implements Observer {

    private CADOutput previousCADOutState;
    private ArrayList<SSADGraph> graphList;
    private ArrayList<int[]> previousEdits; //0 - CompID , 1 - ErrorID
    private ArrayList<int[]> latestEdits; //0 - CompID , 1 - ErrorID
    private int graphCount;//Increment this by one to add a new graph

    private final int COMP_ID = 0;
    private final int ERROR_ID = 1;

    public GraphGenerator() {
        //g = new ListenableDirectedGraph<>(DefaultEdge.class); // create a JGraphT graph
        //initFlag = false;
        previousEdits = new ArrayList<>();
        latestEdits = new ArrayList<>();
        graphList = new ArrayList<>();
        graphCount = 1;

    }

    @Override
    public void update(Observable observable, Object o) {
        populateModel((CADOutput) o);
    }

    private void populateModel(CADOutput CADOut) { //modify this method to make changes to the entire model
        //add functionality here to support multiple graphs.
        if (graphCount > graphList.size()) {
            SSADGraph g = new SSADGraph(CADOut.getCompCount(), CADOut.getErrorCount());
            graphList.add(g);
        }
        updateGraph(CADOut, graphList.get(graphCount - 1)); // get the latest graph and update it
    }

    private void updateGraph(CADOutput CADOut, SSADGraph g) {
        if (!g.initialized()) {//if not initialized, initialize previous state
            previousCADOutState = new CADOutput(CADOut.getCompCount(), CADOut.getErrorCount());
            g.initialize();
        }
        CADOut.displayCADOut(); // temp
        findDiff(previousCADOutState, CADOut); //find differences and save in an array list
        addNodesToGraph(g, latestEdits); //generate new graph nodes
        copyCADOut(CADOut, previousCADOutState); //finally
    }


    private void findDiff(CADOutput previous, CADOutput current) {
        previousEdits.clear();
        latestEdits.forEach(coordinatePair-> previousEdits.add(coordinatePair));
        latestEdits.clear(); //clean previous data before modifications
        for (int i = 0; i < previous.getCompCount(); i++) {
            for (int j = 0; j < previous.getErrorCount(); j++) {
                if (previous.getCADStatus(i, j) != current.getCADStatus(i, j) && previous.getCADStatus(i, j) == 0) {
                    int[] coordinatePair = {i, j};
                    latestEdits.add(coordinatePair);
                }
            }
        }
    }

    private void addNodesToGraph(SSADGraph g, ArrayList<int[]> latestDiffs) {
        latestDiffs.forEach(coordinatePair -> {
                    System.out.print(coordinatePair[COMP_ID] + " " + coordinatePair[ERROR_ID]);
                    System.out.println();
                }
        );

        latestDiffs.forEach(LcoordinatePair -> {
                    CompErrorNode node = new CompErrorNode(LcoordinatePair[COMP_ID], LcoordinatePair[ERROR_ID]);
                    g.setDetailedGVertex(node, LcoordinatePair[COMP_ID], LcoordinatePair[ERROR_ID]);
                    previousEdits.forEach(PcoordinatePair-> g.getDetailedJGraph().addEdge(g.getDetailedGVertex(PcoordinatePair[COMP_ID],PcoordinatePair[ERROR_ID]).getNodeID(),node.getNodeID()));

                    if(g.getHighLevelGVertex(LcoordinatePair[COMP_ID])  == null){
                        CompNode node2 = new CompNode(LcoordinatePair[COMP_ID]);
                        g.setHighLevelGVertex(node2,LcoordinatePair[COMP_ID]);
                        ArrayList<Integer> previousComps = new ArrayList<>();

                        previousEdits.forEach(PCoordinatePair->{
                            if(!previousComps.contains(PCoordinatePair[COMP_ID])) previousComps.add(PCoordinatePair[COMP_ID]);
                        });
                        previousComps.forEach(PComp-> g.getHighLevelJGraph().addEdge(g.getHighLevelGVertex(PComp).getNodeID(),node2.getNodeID()));

                    }
                }

        );

        System.out.println(g.getDetailedJGraph());
        System.out.println(g.getHighLevelJGraph());

    }

    @Override
    public void run() {
        System.out.println("Thread started!");
    }

    private void copyCADOut(CADOutput source, CADOutput destination) {
        if (source.getCompCount() == destination.getCompCount() && source.getErrorCount() == destination.getErrorCount()) {
            for (int i = 0; i < source.getCompCount(); i++) {
                for (int j = 0; j < source.getErrorCount(); j++) {
                    destination.setCADStatus(i, j, source.getCADStatus(i, j));
                }
            }
        }
    }

    public SSADGraph getGraph(int i){
        if(graphList.size()>= i){
            return graphList.get(i);
        }
        else {
            System.out.println("hit");
            return null; //Handle exception
        }
    }
}
