package Unagi.SsAD.graphical.Graph;

import org.jgrapht.Graph;
import org.jgrapht.ext.ExportException;
import org.jgrapht.ext.MatrixExporter;

import java.io.Writer;

/**
 * Created by amitha on 2/18/17.
 */
public class ExportMatrix {

    private static MatrixExporter matExport = new MatrixExporter();

    public ExportMatrix(){

    }

    public static MatrixExporter getMatExport(){
        return matExport;
    }
    public static void writeToFile(Graph g, Writer wr){

        try {
            matExport.exportGraph(g, wr);
        } catch (ExportException e) {
            e.printStackTrace();
        }

    }



}
