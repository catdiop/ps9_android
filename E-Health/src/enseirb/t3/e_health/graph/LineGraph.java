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
	private int window = 10;

	public LineGraph(String dataname) {
		String xTitle = "Time";
		String yTitle = null;
		String chartTitle = null;

		switch (dataname) {
		case "A":
			// 0 -> 1024 : 0 -> 8L/s
			yTitle = "L/s";
			chartTitle = "Airflow";
			break;
		case "B":
			yTitle = "Bpm";
			chartTitle = "Pouls";
			break;
		case "C":
			yTitle = "�S";
			chartTitle = "Conductance";
			break;
		case "O":
			yTitle = "%";
			chartTitle = "SPO2";
			break;
		case "P":
			yTitle = "Position";
			chartTitle = "Position";
			break;
		case "R":
			yTitle = "Ohm";
			chartTitle = "R�sistance";
			break;
		case "T":
			yTitle = "�C";
			chartTitle = "Temp�rature";
			break;
		default:
			break;
		}

		yTitle = "%";
		chartTitle = "SPO2";
		
		dataset = new TimeSeries(chartTitle);

		mDataset.addSeries(dataset);

		renderer.setColor(Color.RED);
		renderer.setPointStyle(PointStyle.CIRCLE);
		renderer.setFillPoints(true);
		renderer.setLineWidth(3);
		renderer.setDisplayChartValues(true);
		mRenderer.setXLabels(window);
		mRenderer.setChartTitle(chartTitle);
		mRenderer.setXTitle(xTitle);
		mRenderer.setYTitle(yTitle);
		mRenderer.setZoomButtonsVisible(true);
		mRenderer.setAxisTitleTextSize(textSize);
		mRenderer.setLabelsTextSize(textSize);
		mRenderer.setChartTitleTextSize(titleTextSize);

		mRenderer.addSeriesRenderer(renderer);
	}

	public GraphicalView getView(Context context) {
		view = ChartFactory.getTimeChartView(context, mDataset, mRenderer,
				"H:mm:ss");
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

	public void removePoint(int i) {
		dataset.remove(i);
	}

}
