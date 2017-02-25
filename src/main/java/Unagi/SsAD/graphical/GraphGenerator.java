package Unagi.SsAD.graphical;

import Unagi.SsAD.graphical.Graph.CompErrorNode;
import Unagi.SsAD.graphical.Graph.CompNode;
import Unagi.SsAD.graphical.Graph.ExportMatrix;
import Unagi.SsAD.graphical.Graph.SSADGraph;
import org.jgrapht.ext.CSVBaseListener;

import java.io.IOException;
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
    public static final int timeOut = 3;

    private long previousTimeStamp;
    private boolean setChanged = false;

    public GraphGenerator() {
        //g = new ListenableDirectedGraph<>(DefaultEdge.class); // create a JGraphT graph
        //initFlag = false;
        previousEdits = new ArrayList<>();
        latestEdits = new ArrayList<>();
        graphList = new ArrayList<>();
        graphCount = 1;
        previousTimeStamp = System.currentTimeMillis();

    }

    @Override
    public void update(Observable observable, Object o) {
        populateModel((CADOutput) o);
    }

    private void populateModel(CADOutput CADOut) { //modify this method to make changes to the entire model
        //add functionality here to support multiple graphs
        //System.out.println(("Time: "+(System.currentTimeMillis() - previousTimeStamp) / 1000));
        if (graphCount > graphList.size()) {
            SSADGraph g = new SSADGraph(CADOut.getCompCount(), CADOut.getErrorCount());
            //initGraph(g,CADOut.getCompCount(),CADOut.getErrorCount());
            graphList.add(g);
            //System.out.println(("Time: "+(System.currentTimeMillis() - previousTimeStamp) / 1000));
        } else if ((System.currentTimeMillis() - previousTimeStamp) / 1000 > timeOut) {
            //System.out.println("hit2");
            previousTimeStamp = System.currentTimeMillis();
            saveGraph();
            graphCount++;
            SSADGraph g = new SSADGraph(CADOut.getCompCount(), CADOut.getErrorCount());
            graphList.add(g);
            previousEdits.clear();
            latestEdits.clear();
            previousCADOutState = null;
        }
        updateGraph(CADOut, graphList.get(graphCount - 1)); // get the latest graph and update it
        setChanged();
        saveGraph();
    }

    private void setChanged(){
        setChanged = true;
    }

    public boolean isSetChanged(){
        return setChanged;
    }

    public void resetSetChanged(){
        setChanged = false;
    }

    public void saveGraph(){
        try {
            ExportMatrix.writeToFile(getGraph(graphCount - 1), "Seq" + (graphCount - 1));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateGraph(CADOutput CADOut, SSADGraph g) {
        if (!g.initialized()) {//if not initialized, initialize previous state
            previousCADOutState = new CADOutput(CADOut.getCompCount(), CADOut.getErrorCount());
            g.initialize();
        }
        //CADOut.displayCADOut(); // temp
        findDiff(previousCADOutState, CADOut); //find differences and save in an array list
        addNodesToGraph(g, latestEdits); //generate new graph nodes
        copyCADOut(CADOut, previousCADOutState); //finally
    }


    private void findDiff(CADOutput previous, CADOutput current) {
        previousEdits.clear();
        latestEdits.forEach(coordinatePair -> previousEdits.add(coordinatePair));
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
                    g.addDetailedVertexOnMatrix(LcoordinatePair[COMP_ID], LcoordinatePair[ERROR_ID]);
                    previousEdits.forEach(PcoordinatePair -> g.getDetailedJGraph().addEdge(g.getDetailedGVertex(PcoordinatePair[COMP_ID], PcoordinatePair[ERROR_ID]).getNodeID(), node.getNodeID()));
                    previousEdits.forEach(PcoordinatePair -> g.addDetailedEdgeOnMatrix(PcoordinatePair[COMP_ID], PcoordinatePair[ERROR_ID], LcoordinatePair[COMP_ID], LcoordinatePair[ERROR_ID]));

                    if (g.getHighLevelGVertex(LcoordinatePair[COMP_ID]) == null) {
                        CompNode node2 = new CompNode(LcoordinatePair[COMP_ID]);
                        g.setHighLevelGVertex(node2, LcoordinatePair[COMP_ID]);
                        g.addHighLevelVertexOnMatrix(LcoordinatePair[COMP_ID]);
                        ArrayList<Integer> previousComps = new ArrayList<>();

                        previousEdits.forEach(PCoordinatePair -> {
                            if (!previousComps.contains(PCoordinatePair[COMP_ID])) previousComps.add(PCoordinatePair[COMP_ID]);
                        });
                        previousComps.forEach(PComp -> g.getHighLevelJGraph().addEdge(g.getHighLevelGVertex(PComp).getNodeID(), node2.getNodeID()));
                        previousComps.forEach(PComp -> g.addHighLevelEdgeOnMatrix(PComp, LcoordinatePair[COMP_ID]));

                    }
                }
        );

        System.out.println(g.getDetailedJGraph());
        System.out.println(g.getHighLevelJGraph());

    }

    @Override
    public void run() {
        System.out.println("Observer Thread started!");
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

    public void initGraph(SSADGraph g, int noComps, int noErrors) {
        for (int i = 0; i < noComps; i++) {
            CompNode n1 = new CompNode(i);
            g.setHighLevelGVertex(n1, i);
            for (int j = 0; j < noErrors; j++) {
                CompErrorNode node = new CompErrorNode(i, j);
                g.setDetailedGVertex(node, i, j);
            }
        }
    }

    public SSADGraph getGraph(int i) {
        if (graphList.size() >= i) {
            return graphList.get(i);
        } else {
            System.out.println("hit");
            return null; //Handle exception
        }
    }

    public int getGraphCount(){
        return graphCount;
    }
}
