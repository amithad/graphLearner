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
    private GNode gDetailedVertexMat[][];
    private GNode gHighLevelVertexMat[];

    public boolean detailedGraphUpdated;
    public boolean highLevelGraphUpdated;
    private ArrayList<GNode> detailedLatest;
    private ArrayList<GNode> highLevelLatest;

    public SSADGraph(int noOfComponents, int noOfErrorTypes){
        initFlag = false;
        gDetailedVertexMat = new GNode[noOfComponents][noOfErrorTypes];
        gHighLevelVertexMat = new GNode[noOfComponents];
        gDetailed = new ListenableDirectedGraph<>(DefaultEdge.class);
        gHighLevel = new ListenableDirectedGraph<>(DefaultEdge.class);
        detailedLatest  = new ArrayList<>();
        highLevelLatest = new ArrayList<>();
    }

    public ListenableGraph<String,DefaultEdge> getDetailedJGraph(){
        return gDetailed;
    }

    public ListenableGraph<String,DefaultEdge> getHighLevelJGraph(){
        return gHighLevel;
    }

    public boolean initialized(){
        return  initFlag;
    }

    public void initialize(){
        initFlag = true;
    }

    public GNode getDetailedGVertex(int compID, int errorID){
        return gDetailedVertexMat[compID][errorID];
    }

    public GNode getHighLevelGVertex(int compID){
        return gHighLevelVertexMat[compID];
    }

    public void setDetailedGVertex(GNode node, int compLoc, int errLoc){
        gDetailedVertexMat[compLoc][errLoc] = node;
        gDetailed.addVertex(node.getNodeID());
        detailedGraphUpdated = true;
        getDetailedLatest().add(node);
    }

    public void setHighLevelGVertex(GNode node, int compLoc){
        gHighLevelVertexMat[compLoc]= node;
        gHighLevel.addVertex(node.getNodeID());
        highLevelGraphUpdated = true;
        getHighLevelLatest().add(node);
    }

    public synchronized ArrayList<GNode> getDetailedLatest(){
        return detailedLatest;
    }

    public synchronized ArrayList<GNode> getHighLevelLatest(){
        return highLevelLatest;
    }
}
