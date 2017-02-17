package Unagi.SsAD.graphical;

import java.util.ArrayList;

/**
 * Created by amitha on 12/4/16.
 */
public class CADOutput {

    private ArrayList<ArrayList<Integer>> CADOut;
    private int noOfComponents;
    private int noOfErrorTypes;

    public CADOutput(int noOfComponents, int noOfErrorTypes) {
        this.noOfComponents = noOfComponents;
        this.noOfErrorTypes = noOfErrorTypes;
        CADOut = new ArrayList<>();
        createCADMat(this.noOfComponents, this.noOfErrorTypes);
    }

    private void createCADMat(int noOfComponents, int noOfErrorTypes) {
        for (int i = 0; i < noOfComponents; i++) {
            ArrayList<Integer> compStatus = new ArrayList<>();
            CADOut.add(compStatus);
            for (int j = 0; j < noOfErrorTypes; j++) {
                compStatus.add(new Integer(0)); // set all values to zero first
            }
        }
    }

    public void setCADStatus(int compID, int errorID, int status) {
        CADOut.get(compID).set(errorID, status);
    }

    public int getCADStatus(int compID, int errorID){
        return CADOut.get(compID).get(errorID);
    }

    public void displayCADOut() {
        for (int i = 0; i < noOfComponents; i++) {
            for (int j = 0; j < noOfErrorTypes; j++) {
                System.out.print(CADOut.get(i).get(j) + " ");
            }
            System.out.println();
        }
        System.out.println(CADOut.size());
        System.out.println(CADOut.get(0).size());
        System.out.println();
    }


    public int getCompCount(){
        return this.noOfComponents;
    }

    public int getErrorCount(){
        return this.noOfErrorTypes;
    }


}
