package enseirb.t3.e_health.activity;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.project.e_health.R;

import enseirb.t3.e_health.entity.Alert;

public class ListAlertAdapter extends ArrayAdapter<Alert>{
	@Override
	public boolean areAllItemsEnabled() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean isEnabled(int position) {
		// TODO Auto-generated method stub
		return super.isEnabled(position);
	}
	private LayoutInflater inflater;
	private List<Alert> alerts;
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View row=inflater.inflate(R.layout.activity_alert_row, null);
		Alert alert=alerts.get(position);
		if(alert!=null) {
			TextView firstname=(TextView)row.findViewById(R.id.firstname);
			firstname.setText(alert.getPatient().getFirstname());

			TextView lastname=(TextView)row.findViewById(R.id.lastname);
			lastname.setText(alert.getPatient().getLastname());
		}
		return row;
	}
	public ListAlertAdapter(Context context, List<Alert> alerts) {
		super(context, R.layout.activity_alert_row, alerts);
		this.inflater = LayoutInflater.from(context);
		this.alerts = alerts;
	}
}