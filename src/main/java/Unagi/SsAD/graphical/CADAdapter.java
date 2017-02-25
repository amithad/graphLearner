package Unagi.SsAD.graphical;

import java.util.Observable;


/**
 * Created by amitha on 12/1/16.
 */
public class CADAdapter extends Observable {

    private long previousTimeStamp;
    private CADOutput CADOut;
    int noOfComponents;
    int noOfErrorTypes;

    public CADAdapter(int noOfComponents, int noOfErrorTypes, GraphGenerator graphGen) {
        CADOut = new CADOutput(noOfComponents, noOfErrorTypes);
        this.addObserver(graphGen);
        previousTimeStamp = System.currentTimeMillis();
        this.noOfComponents = noOfComponents;
        this.noOfErrorTypes = noOfErrorTypes;
    }

    public void setCADStatus(int compID, int errorID, int status) { //whenever a CAD State is changed, it's passed on to observers
        if ((System.currentTimeMillis() - previousTimeStamp) / 1000 > GraphGenerator.timeOut) {
            CADOut = new CADOutput(noOfComponents,noOfErrorTypes);
            previousTimeStamp = System.currentTimeMillis();
        }
        CADOut.setCADStatus(compID, errorID, status); //Possible edit: do a series of changes and then notify the observers
    }

    public void notifyGenerator() {
        setChanged();
        notifyObservers(CADOut);
    }

    public void printCADOut() {
        CADOut.displayCADOut();
    }
}
