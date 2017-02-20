package Unagi.SsAD.graphical.Graph;

import org.jgrapht.ListenableGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.ListenableDirectedGraph;

import java.util.ArrayList;


/**
 * Created by amitha on 12/7/16.
 */
public class SSADGraph {

    boolean initFlag; //set to true when graph has at least one node.
    private ListenableGraph<String, DefaultEdge> gDetailed;
    private ListenableGraph<String, DefaultEdge> gHighLevel;

    private int noOfComponents;
    private int noOfErrorTypes;

    private GNode gDetailedVertexMat[][];
    private GNode gHighLevelVertexMat[];

    public boolean detailedGraphUpdated;
    public boolean highLevelGraphUpdated;
    private ArrayList<GNode> detailedLatest;
    private ArrayList<GNode> highLevelLatest;

    //adjacency matrices
    private ArrayList<ArrayList<Integer>> highLevelMatrix;
    private ArrayList<ArrayList<Integer>> detailedMatrix;

    public SSADGraph(int noOfComponents, int noOfErrorTypes) {
        initFlag = false;
        this.noOfComponents = noOfComponents;
        this.noOfErrorTypes = noOfErrorTypes;
        gDetailedVertexMat = new GNode[noOfComponents][noOfErrorTypes];
        gHighLevelVertexMat = new GNode[noOfComponents];
        gDetailed = new ListenableDirectedGraph<>(DefaultEdge.class);
        gHighLevel = new ListenableDirectedGraph<>(DefaultEdge.class);
        detailedLatest = new ArrayList<>();
        highLevelLatest = new ArrayList<>();

        highLevelMatrix = new ArrayList<>();
        for (int i = 0; i < noOfComponents; i++) {
            highLevelMatrix.add(new ArrayList<>());
            for (int j = 0; j < noOfComponents; j++) {
                highLevelMatrix.get(i).add(-1);
            }
        }

        detailedMatrix = new ArrayList<>();
        for (int i = 0; i < noOfComponents * noOfErrorTypes; i++) {
            detailedMatrix.add(new ArrayList<>());
            for (int j = 0; j < noOfComponents * noOfErrorTypes; j++) {
                detailedMatrix.get(i).add(-1);
            }
        }
    }

    public ListenableGraph<String, DefaultEdge> getDetailedJGraph() {
        return gDetailed;
    }

    public ListenableGraph<String, DefaultEdge> getHighLevelJGraph() {
        return gHighLevel;
    }

    public boolean initialized() {
        return initFlag;
    }

    public void initialize() {
        initFlag = true;
    }

    public GNode getDetailedGVertex(int compID, int errorID) {
        return gDetailedVertexMat[compID][errorID];
    }

    public GNode getHighLevelGVertex(int compID) {
        return gHighLevelVertexMat[compID];
    }

    public void setDetailedGVertex(GNode node, int compLoc, int errLoc) {
        gDetailedVertexMat[compLoc][errLoc] = node;
        gDetailed.addVertex(node.getNodeID());
        detailedGraphUpdated = true;
        getDetailedLatest().add(node);
    }

    public void setHighLevelGVertex(GNode node, int compLoc) {
        gHighLevelVertexMat[compLoc] = node;
        gHighLevel.addVertex(node.getNodeID());
        highLevelGraphUpdated = true;
        getHighLevelLatest().add(node);
    }

    public void addHighLevelVertexOnMatrix(int compID) {
        if (highLevelMatrix.get(compID).get(0) == -1) { //if not initialized earlier
            for (int i = 0; i < noOfComponents; i++) {
                highLevelMatrix.get(compID).set(i, 0);
            }
        }
    }

    public void addDetailedVertexOnMatrix(int compID, int errorID) {
        if (detailedMatrix.get(compID * noOfErrorTypes + errorID).get(0) == -1) { //if not initialized earlier
            for (int i = 0; i < noOfComponents * noOfErrorTypes; i++) {
                detailedMatrix.get(compID * noOfErrorTypes + errorID).set(i, 0);
            }
        }
    }

    public void addHighLevelEdgeOnMatrix(int comp1ID, int comp2ID) {
        highLevelMatrix.get(comp1ID).set(comp2ID, 1);
    }

    public void addDetailedEdgeOnMatrix(int comp1ID, int err1ID, int comp2ID, int err2ID) {
        detailedMatrix.get(comp1ID * noOfErrorTypes + err1ID).set(comp2ID * noOfErrorTypes + err2ID, 1);
    }

    public void exportAdjacency() {
        for (int i = 0; i < noOfComponents; i++) {
            highLevelMatrix.add(new ArrayList<>());
            for (int j = 0; j < noOfComponents; j++) {
                System.out.print(highLevelMatrix.get(i).get(j) + " ");
            }
            System.out.println();
        }

        System.out.println("\n");

        for (int i = 0; i < noOfComponents * noOfErrorTypes; i++) {
            for (int j = 0; j < noOfComponents * noOfErrorTypes; j++) {
                System.out.print(detailedMatrix.get(i).get(j) + " ");
            }
            System.out.println();
        }
    }

    public ArrayList<ArrayList<Integer>> getHighLevelMatrix() {
        return highLevelMatrix;
    }

    public ArrayList<ArrayList<Integer>> getDetailedMatrix() {
        return detailedMatrix;
    }

    public int getCompCount(){
        return noOfComponents;
    }

    public int getErrorTypeCount(){
        return noOfErrorTypes;
    }

    public synchronized ArrayList<GNode> getDetailedLatest() {
        return detailedLatest;
    }

    public synchronized ArrayList<GNode> getHighLevelLatest() {
        return highLevelLatest;
    }
}
