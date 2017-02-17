package Unagi.SsAD.graphical.Graph;

/**
 * Created by amitha on 12/4/16.
 */
public class CompErrorNode extends GNode {

    private int compID, errorID;

    public CompErrorNode(int compID, int errorID) {
        modifyNodeID(compID, errorID);
    }

    public void modifyNodeID(int compID, int errorID) {
        this.compID = compID;
        this.errorID = errorID;
        String compName = "C" + compID + "E" + errorID;
        super.setNodeID(compName);
    }

    public int getCompID(){
        return compID;
    }

    public int getErrorID(){
        return errorID;
    }


}
