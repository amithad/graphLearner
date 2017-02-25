package Unagi.SsAD.graphical;

/**
 * Created by amitha on 2/22/17.
 */
public class CSVMan extends Thread {

    private GraphGenerator gen;

    public CSVMan(GraphGenerator gen){
        this.gen = gen;
    }

    @Override
    public void run(){
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while(true){
            if(gen.isSetChanged()){
                gen.saveGraph();
                gen.resetSetChanged();
            }
        }
    }


}
