package enseirb.t3.e_health.graph;

import java.util.List;

import org.achartengine.GraphicalView;

import enseirb.t3.e_health.DAO.DatabaseHandler;
import enseirb.t3.e_health.entity.Data;

public class GraphThread extends Thread {
	private DatabaseHandler dbHandler;
	private LineGraph line;
	private GraphicalView view;
	private String dataname;
	
	public GraphThread(DatabaseHandler dbHandler, GraphicalView view, LineGraph line, String dataname) {
		this.dbHandler = dbHandler;
		this.view = view;
		this.line = line;
		this.dataname = dataname;
	}
	
	public void run() {
		int i = 0;
		while (true) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			List<Data> datas= dbHandler.retrieveDataList(dataname);
			Data dataTmp = new Data();
			try {
				dataTmp = datas.get(i);
			}
			catch (Exception e) {
				break;
			}

			Point p = new Point(Integer.parseInt(dataTmp.getDate()), Integer.parseInt(dataTmp.getValue()));
			line.addNewPoint(p);
			view.repaint();
			i++;
		}
	}

}
