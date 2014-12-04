package enseirb.t3.e_health.graph;

import java.util.Date;

import org.achartengine.GraphicalView;

import enseirb.t3.e_health.DAO.DatabaseHandler;
import enseirb.t3.e_health.entity.Data;

public class GraphThread extends Thread {
	private Data data;
	private LineGraph line;
	private GraphicalView view;
	private DatabaseHandler dbHandler;
	private String dataname;
	private Date lastDate;
	
	public GraphThread(DatabaseHandler dbHandler, GraphicalView view, LineGraph line, String dataname) {
		this.dbHandler = dbHandler;
		this.view = view;
		this.line = line;
		this.dataname = dataname;
	}
	
	public void run() {
		int i = 0;
		
		while(true) {
			data = dbHandler.retrieveLastData(dataname);
			
			if (data.getDate() != lastDate) {
				Point p = new Point(data.getDate(), Integer.parseInt(data.getValue()));
				line.addNewPoint(p);
				i++;
				if (i > 9) {
					line.removePoint(i - 10);
					i--;
				}
				view.repaint();
			}
			
		}
	}
}
