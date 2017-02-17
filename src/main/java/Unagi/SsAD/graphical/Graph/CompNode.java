package Unagi.SsAD.graphical.Graph;

/**
 * Created by amitha on 12/8/16.
 */
public class CompNode extends GNode {

    private int compID;

    public CompNode(int compID){
        modifyNodeID(compID);
    }

    public void modifyNodeID(int compID) {
        this.compID = compID;
        String compName = "C" + compID;
        super.setNodeID(compName);
    }

    public int getCompID(){
        return compID;
    }
}
