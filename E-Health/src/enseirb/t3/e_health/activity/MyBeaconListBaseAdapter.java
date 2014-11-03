package enseirb.t3.e_health.activity;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.project.e_health.R;

public class MyBeaconListBaseAdapter extends BaseAdapter {
	private static ArrayList<MyBeacon> itemDetailsrrayList;
 
	private LayoutInflater l_Inflater;

	public MyBeaconListBaseAdapter(Context context, ArrayList<MyBeacon> results) {
		itemDetailsrrayList = results;
		l_Inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return itemDetailsrrayList.size();
	}

	@Override
	public Object getItem(int position) {
		return itemDetailsrrayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = l_Inflater.inflate(R.layout.item_device, null);
			holder = new ViewHolder();
			holder.txt_itemName = (TextView) convertView.findViewById(R.id.name_device);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
  
		holder.txt_itemName.setText(itemDetailsrrayList.get(position).getName());


		return convertView;
	}

	static class ViewHolder {
		TextView txt_itemName;
	}
}