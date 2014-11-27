package enseirb.t3.e_health.graph;

import java.util.Date;
import java.util.List;

import org.achartengine.GraphicalView;

import enseirb.t3.e_health.entity.Data;

public class GraphThread extends Thread {
	private List<Data> datas;
	private LineGraph line;
	private GraphicalView view;
	
	public GraphThread(List<Data> datas, GraphicalView view, LineGraph line) {
		this.datas = datas;
		this.view = view;
		this.line = line;
	}
	
	public void run() {
		int j = 0;
		for (int i = 0; i < datas.size(); i++) {
			try {
				Thread.sleep(1000);
				Date date = new Date(Long.parseLong(datas.get(i).getDate()));
				Point p = new Point(date, Integer.parseInt(datas.get(i).getValue()));
				line.addNewPoint(p);
				j++;
				if (j > 9) {
					line.removePoint(j - 10);
					j--;
				}
			}
			catch (Exception e) {
				break;
			}
			view.repaint();
		}
	}

}
