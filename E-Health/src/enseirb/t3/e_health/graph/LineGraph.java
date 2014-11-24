package enseirb.t3.e_health.graph;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.graphics.Color;

public class LineGraph {
	
	private GraphicalView view;
	
	private TimeSeries dataset;
	private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
	
	private XYSeriesRenderer renderer = new XYSeriesRenderer();
	private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
	
	private float textSize = 22;
	private float titleTextSize = 24;
	
	public LineGraph(String dataname) {
		dataset = new TimeSeries(dataname);
		mDataset.addSeries(dataset);
		
		renderer.setColor(Color.RED);
		renderer.setPointStyle(PointStyle.CIRCLE);
		renderer.setFillPoints(true);
		renderer.setLineWidth(3);
		renderer.setDisplayChartValues(true);
		
		mRenderer.setXLabels(0);
		mRenderer.setChartTitle("Oxygen rate of client X");
		mRenderer.setXTitle("Arduino timestamps");
		mRenderer.setYTitle("Heart beats");
		mRenderer.setZoomButtonsVisible(true);
		mRenderer.setAxisTitleTextSize(textSize);
		mRenderer.setLabelsTextSize(textSize);
		mRenderer.setChartTitleTextSize(titleTextSize);
		
		mRenderer.addSeriesRenderer(renderer);
	}
	
	public GraphicalView getView(Context context) {
		view = ChartFactory.getLineChartView(context, mDataset, mRenderer);
		return view;
	}
	
	public XYMultipleSeriesRenderer getmRenderer() {
		return mRenderer;
	}
	
	public XYMultipleSeriesDataset getDataset() {
		return mDataset;
	}
	
	public void addNewPoint(Point p) {
		dataset.add(p.getX(), p.getY());
	}

}
