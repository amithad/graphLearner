package Unagi.SsAD.graphical.UI;/**
 * Created by amitha on 2/25/17.
 */


import Unagi.SsAD.graphical.CADAdapter;
import Unagi.SsAD.graphical.CADAgent;
import Unagi.SsAD.graphical.Graph.ExportMatrix;
import Unagi.SsAD.graphical.GraphGenerator;
import Unagi.SsAD.graphical.SsADVisualizer;
import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.jgraph.JGraph;

import javax.swing.*;
import java.applet.Applet;
import java.awt.Dimension;

import java.io.IOException;
import java.util.concurrent.*;

import static Unagi.SsAD.graphical.Main.DETAILED_GRAPH;
import static java.lang.Thread.sleep;

public class SsADMan extends Application{
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("SsAD Mananger");
        Button btn = new Button();
        btn.setText("Set Configuration");
        btn.setOnAction(event -> System.out.println("Hello World!")); //what to do when button is clicked
        StackPane root = new StackPane();
        root.getChildren().add(btn);
        Scene scene = new Scene(root, 300, 250);
        primaryStage.setScene(scene);
        scene.getStylesheets().add
                (SsADMan.class.getResource("/Sample.css").toExternalForm());
        primaryStage.show();

    }
}

/*public class SsADMan extends Application {


    private static JFrame f1 = null;
    private static JPanel p1 = null;
    private static JApplet a1 = null;
    private static JGraph j1 = null;
    public static final String CSSPath = "CSS/";

    public static void main(String[] args) {

        GraphGenerator gen1 = new GraphGenerator();
        //CSVMan csvMan1 = new CSVMan(gen1);
        CADAdapter adapter1 = new CADAdapter(5, 4, gen1);
        CADAgent agent1 = new CADAgent(adapter1);
        gen1.start();
        //csvMan1.start();
        agent1.start();

        try {
            sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        SsADVisualizer visualizer1 = new SsADVisualizer(gen1.getGraph(0), DETAILED_GRAPH, "Component-Error Graph");
        //SsADVisualizer visualizer2 = new SsADVisualizer(gen1.getGraph(0), HIGHLEVEL_GRAPH, "Components Graph");
        visualizer1.initialize();
        //j1 = visualizer1.launch();

        //f1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //f1.pack();
        //f1.setVisible(true);
        launch();
    }

    @Override
    public void start(Stage stage) {
        final SwingNode swingNode = new SwingNode();
        createSwingContent(swingNode);
        StackPane pane = new StackPane();
        pane.getChildren().add(swingNode);

        Scene scene = new Scene(pane, 600, 600);

        stage.setScene(scene);
        stage.setTitle("ButtonHtmlDemo Embedded in JavaFX");
        stage.show();

    }

    public void createSwingContent(final SwingNode swingNode) {
        SwingUtilities.invokeLater(() -> swingNode.setContent(j1));
    }




}*/
