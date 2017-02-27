package Unagi.SsAD.graphical;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Created by amitha on 2/26/17.
 */
public class GraphAnalyzer extends Thread {


    public static int DETAILED = 0;
    public static int HIGH_LEVEL = 1;

    public static String RULE_PATH = "/home/amitha/FYP_new/SsAD-alpha/Rules/";

    GraphGenerator gen;
    Vector<ArrayList<ArrayList<Integer>>> rules;

    public GraphAnalyzer(GraphGenerator gen) {
        this.gen = gen;
    }

    @Override
    public void run() {
        try {
            sleep(2000);
            if (gen.getGraphCount() == 2) {
                printMatrix(gen.getGraph(1).getDetailedMatrix());
            }
            System.out.println('\n');
            printMatrix(loadRule(RULE_PATH + "Rule0_Detailed.csv", DETAILED));
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }


    }

    public static void main(String[] args) throws IOException {
        GraphAnalyzer g1 = new GraphAnalyzer(null);
        g1.printMatrix(g1.loadRule(RULE_PATH + "Rule0_Detailed.csv", DETAILED));
        //g1.loadRule(RULE_PATH + "Rule0_Detailed.csv", DETAILED);
    }

    public ArrayList<ArrayList<Integer>> loadRule(String fileName, int type) throws IOException {

        List<String> lines = Files.readAllLines(Paths.get(fileName), Charset.forName("UTF-8"));

        StringTokenizer st = new StringTokenizer(lines.get(0), ","); //decode the header and find the effective rows
        int tokenCount = st.countTokens();
        Vector<Integer> multiplier = new Vector<>();
        //System.out.println(tokenCount);
        for (int i = 0; i < tokenCount; i++) {
            String t = st.nextToken();
            int a = Integer.parseInt(String.valueOf(t.charAt(1))) * Main.ERROR_COUNT + Integer.parseInt(String.valueOf(t.charAt(3)));
            multiplier.add(a);
            //System.out.println(a);
        }

        ArrayList<ArrayList<Integer>> rule = new ArrayList<>(); //create and populate the matrix
        if (type == DETAILED) {
            for (int i = 0; i < Main.COMP_COUNT * Main.ERROR_COUNT; i++) {
                ArrayList<Integer> line = new ArrayList<>();
                for (int j = 0; j < Main.COMP_COUNT * Main.ERROR_COUNT; j++) {
                    line.add(-1);
                }
                rule.add(line);
            }
        }

        for (int i = 0; i < multiplier.size(); i++) { //set the row values
            ArrayList<Integer> row = rule.get(multiplier.get(i));
            for (int j = 0; j < row.size(); j++) {
                row.set(j, 0);
            }
        }

        for (int i = 1; i < lines.size(); i++) { //add  error locations
            StringTokenizer st2 = new StringTokenizer(lines.get(i), ",");
            int locCount = st2.countTokens();
            for (int j = 0; j < locCount; j++) {
                int val = Integer.parseInt(st2.nextToken());
                int setRowLoc = multiplier.get(i - 1);
                int setColLoc = multiplier.get(j);
                rule.get(setRowLoc).set(setColLoc, val);
            }
        }
        return rule;
    }

    public void printMatrix(ArrayList<ArrayList<Integer>> matrix) {
        for (int i = 0; i < Main.COMP_COUNT * Main.ERROR_COUNT; i++) {
            for (int j = 0; j < Main.COMP_COUNT * Main.ERROR_COUNT; j++) {
                System.out.print(matrix.get(i).get(j) + " ");
            }
            System.out.println();
        }
    }
}
