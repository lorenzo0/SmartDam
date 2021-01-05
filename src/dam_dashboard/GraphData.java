import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.io.IOException;

public class GraphData extends JFrame{
	HTTPRequest httpRequest = new HTTPRequest();

	public GraphData() throws IOException{
		XYDataset dataset = createDataset();
	    JFreeChart chart = createChart(dataset);
	
	    ChartPanel chartPanel = new ChartPanel(chart);
	    chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
	    chartPanel.setBackground(Color.white);
	    add(chartPanel);
	
	    pack();
	    setTitle("Line chart");
	    setLocationRelativeTo(null);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
    
    private XYDataset createDataset() throws IOException {
        return httpRequest.sendGETArray();
    }
    
    private JFreeChart createChart(XYDataset dataset) {

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Last records from Service",
                "N° record",
                "Distance level",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        XYPlot plot = chart.getXYPlot();

        var renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.RED);
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));

        plot.setRenderer(renderer);
        plot.setBackgroundPaint(Color.white);

        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.BLACK);

        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.BLACK);

        chart.getLegend().setFrame(BlockBorder.NONE);

        chart.setTitle(new TextTitle("Last records from Service",
                        new Font("Serif", java.awt.Font.BOLD, 18)
                )
        );

        return chart;
    }
    
    public void generateNewGraph() {
    	GraphData ex;
		try {
			ex = new GraphData();
			ex.setVisible(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
}
