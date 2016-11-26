package uzman.oguz.visualize;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;

import java.awt.geom.Rectangle2D;
import java.util.*;

import javax.swing.JApplet;

import org.jgraph.JGraph;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;

import org.jgrapht.ListenableGraph;
import org.jgrapht.ext.JGraphModelAdapter;
import org.jgrapht.graph.ListenableDirectedGraph;
import org.jgrapht.graph.DefaultEdge;



/**
 * Created by oguz on 25/11/2016.
 */
public class GraphAdapter extends JApplet {
    private static final Color     DEFAULT_BG_COLOR = Color.decode( "#FAFBFF" );
    private static final Dimension DEFAULT_SIZE = new Dimension( 530, 320 );
    public ListenableGraph g;
    //
    private JGraphModelAdapter m_jgAdapter;
    public JGraph jgraph;

    /**
     * @see java.applet.Applet#init().
     */
    public void init(  ) {
        // create a JGraphT graph
        g = new ListenableDirectedGraph( DefaultEdge.class );

        // create a visualization using JGraph, via an adapter
        m_jgAdapter = new JGraphModelAdapter( g );

        jgraph = new JGraph( m_jgAdapter );

        adjustDisplaySettings( jgraph );
        getContentPane(  ).add( jgraph );
        resize( DEFAULT_SIZE );
    }


    private void adjustDisplaySettings( JGraph jg ) {
        jg.setPreferredSize( DEFAULT_SIZE );

        Color  c        = DEFAULT_BG_COLOR;
        String colorStr = null;

        try {
            colorStr = getParameter( "bgcolor" );
        }
        catch( Exception e ) {}

        if( colorStr != null ) {
            c = Color.decode( colorStr );
        }

        jg.setBackground( c );
    }


    private void positionVertexAt( Object vertex, int x, int y ) {
        DefaultGraphCell cell = m_jgAdapter.getVertexCell( vertex );
        Map              attr = cell.getAttributes(  );
        Rectangle2D b    = GraphConstants.getBounds( attr );

        GraphConstants.setBounds( attr, new Rectangle( x, y, (int)b.getWidth(), (int)b.getHeight()) );

        Map cellAttr = new HashMap(  );
        cellAttr.put( cell, attr );
        m_jgAdapter.edit( cellAttr, null, null, null);
    }

    void layout(
            ListenableDirectedGraph graphModel,
            JGraphModelAdapter graphAdapter,
            JGraph graph)
    {
        List roots = new ArrayList();
        Iterator vertexIter = graphModel.vertexSet().iterator();
        while (vertexIter.hasNext()) {
            Object vertex = vertexIter.next();
            if (graphModel.inDegreeOf(vertex) == 0) {
                roots.add(graphAdapter.getVertexCell(vertex));
            }
        }

    }
}
