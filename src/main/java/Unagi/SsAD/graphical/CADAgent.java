package Unagi.SsAD.graphical;


//NOTE: make the necessary changes by calling set CAD status and then call notifyGenerator to inform the change to the generator to draw the graph.

/**
 * Created by amitha on 12/4/16.
 */
public class CADAgent extends Thread {

    private CADAdapter adapter;

    public CADAgent(CADAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void run() {
        adapter.setCADStatus(3,2,1);
        adapter.notifyGenerator();

        adapter.setCADStatus(4,3,1);
        adapter.setCADStatus(1,2,1);
        adapter.notifyGenerator();

        try{
            sleep(20000);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        adapter.setCADStatus(2,1,1);
        adapter.notifyGenerator();
    }
}
