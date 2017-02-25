package Unagi.SsAD.graphical.Graph;

import org.jgrapht.Graph;
import org.jgrapht.ext.ExportException;
import org.jgrapht.ext.MatrixExporter;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by amitha on 2/18/17.
 */
public class ExportMatrix {

    private static MatrixExporter matExport = new MatrixExporter();

    public static void writeToFile(Graph g, Writer wr) {

        try {
            matExport.exportGraph(g, wr);
        } catch (ExportException e) {
            e.printStackTrace();
        }

    }

    public static void writeToFile(SSADGraph g, String filePrefix) throws IOException {
        WriteAdjacency(g, GraphType.DETAILED_GRAPH, filePrefix);
        WriteAdjacency(g, GraphType.HIGH_LEVEL_GRAPH, filePrefix);
    }

    public static void WriteAdjacency(SSADGraph g, GraphType gType, String filePrefix) throws IOException {

        Writer wr1 = null;
        ArrayList<ArrayList<Integer>> writeMatrix = null;
        ArrayList<Integer> linesToIgnore = new ArrayList<>();
        if (gType == GraphType.HIGH_LEVEL_GRAPH) {
            wr1 = new PrintWriter("Graphs/" + filePrefix + "_HighLevel.csv", "UTF-8");
            writeMatrix = g.getHighLevelMatrix();
            String columnNames = "";
            for (int i = 0; i < writeMatrix.size(); i++) {
                if (writeMatrix.get(i).get(0) == -1) //if vertices do not exist
                    linesToIgnore.add(i);
                else {
                    columnNames +=(!columnNames.equals("") ? "," : "") + "C"+i;
                }
            }
            wr1.append(columnNames).append("\n");
        }
        if (gType == GraphType.DETAILED_GRAPH) {
            wr1 = new PrintWriter("Graphs/" + filePrefix + "_Detailed.csv", "UTF-8");
            writeMatrix = g.getDetailedMatrix();
            String columnNames = "";
            for (int i = 0; i < writeMatrix.size(); i++) {
                if (writeMatrix.get(i).get(0) == -1) //if vertices do not exist
                    linesToIgnore.add(i);
                else {
                    columnNames +=(!columnNames.equals("") ? "," : "") + "C"+i/g.getErrorTypeCount()+"E"+i%g.getErrorTypeCount();
                }
            }
            wr1.append(columnNames).append("\n");
        }

        assert writeMatrix != null;
        for (int i = 0; i < writeMatrix.size(); i++) {
            if (!linesToIgnore.contains(i)) {
                String str = "";
                for (int j = 0; j < writeMatrix.get(0).size(); j++) {
                    if (!linesToIgnore.contains(j)) {
                            str += (!str.equals("") ? "," : "") + writeMatrix.get(i).get(j);
                    }
                }
                wr1.append(str).append("\n");
            }
        }
        wr1.close();
    }
}
