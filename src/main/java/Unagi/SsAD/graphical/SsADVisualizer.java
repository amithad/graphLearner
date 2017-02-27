package Unagi.SsAD.graphical;

import Unagi.SsAD.graphical.Graph.SSADGraph;
import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.swing.mxGraphComponent;
import org.jgraph.JGraph;
import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;
import org.jgrapht.ext.JGraphModelAdapter;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultEdge;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Created by amitha on 12/7/16.
 */
public class SsADVisualizer extends JApplet {

    public static final int DETAILED_GRAPH = 0;
    public static final int HIGHLEVEL_GRAPH = 1;

    private static final long serialVersionUID = 2202072534703043194L;
    private static final Dimension DEFAULT_SIZE = new Dimension(530, 320);
    private static final Color DEFAULT_BG_COLOR = Color.decode("#FAFBFF");

    private SSADGraph generatedGraph;
    private JGraphXAdapter<String, DefaultEdge> jgxAdapter;
    private JGraphModelAdapter<String, DefaultEdge> jgAdapter; //new
    private String title;

    private int graphType;
    private JGraph j1;

    public SsADVisualizer(SSADGraph generatedGraph, int graphType, String title) {
        this.generatedGraph = generatedGraph;
        this.graphType = graphType;
        this.title = title;
    }


    public void init2() {
        if (generatedGraph != null) {

            if (graphType == 0) jgxAdapter = new JGraphXAdapter<>(generatedGraph.getDetailedJGraph());
            if (graphType == 1) jgxAdapter = new JGraphXAdapter<>(generatedGraph.getHighLevelJGraph());
            getContentPane().add(new mxGraphComponent(jgxAdapter));

            resize(DEFAULT_SIZE);

            // positioning via jgraphx layouts
            mxCircleLayout layout = new mxCircleLayout(jgxAdapter);
            layout.execute(jgxAdapter.getDefaultParent());
        }
    }

    public void launch() {
        JFrame frame = new JFrame();
        frame.getContentPane().add(this);
        //frame.getRootPane().add(this);
        //JPanel panel = new JPanel();
        //panel.getRootPane().add(this);
        //panel.add(this);

        frame.setTitle(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        //return j1;
    }

    public void initialize() {

        if (generatedGraph != null) {
            // create a visualization using JGraph, via an adapter
            if (graphType == DETAILED_GRAPH) jgAdapter = new JGraphModelAdapter<>(generatedGraph.getDetailedJGraph());
            if (graphType == HIGHLEVEL_GRAPH) jgAdapter = new JGraphModelAdapter<>(generatedGraph.getHighLevelJGraph());

            JGraph jgraph = new JGraph(jgAdapter);
            //j1 = jgraph;
            adjustDisplaySettings(jgraph);
            getContentPane().add(jgraph);
            resize(DEFAULT_SIZE);

            //positionVertexAt(v1, 130, 40);
            
        }

    }

    private void adjustDisplaySettings(JGraph jg) {
        jg.setPreferredSize(DEFAULT_SIZE);

        Color c = DEFAULT_BG_COLOR;
        String colorStr = null;

        try {
            colorStr = getParameter("bgcolor");
        } catch (Exception e) {
        }

        if (colorStr != null) {
            c = Color.decode(colorStr);
        }

        jg.setBackground(c);
    }

    @SuppressWarnings("unchecked")
    private void positionVertexAt(Object vertex, int x, int y) {
        DefaultGraphCell cell = jgAdapter.getVertexCell(vertex);
        AttributeMap attr = cell.getAttributes();
        Rectangle2D bounds = GraphConstants.getBounds(attr);

        Rectangle2D newBounds = new Rectangle2D.Double(x, y, bounds.getWidth(), bounds.getHeight());

        GraphConstants.setBounds(attr, newBounds);

        // TODO: Clean up generics once JGraph goes generic
        AttributeMap cellAttr = new AttributeMap();
        cellAttr.put(cell, attr);
        jgAdapter.edit(cellAttr, null, null, null);
    }

}

