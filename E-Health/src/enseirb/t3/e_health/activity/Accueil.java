package enseirb.t3.e_health.activity;



import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;

import com.project.e_health.R;

public class Accueil extends Activity {
	protected static final String TAG = "BeaconActivity";

    private ListView listAnimation;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_accueil);
		
		listAnimation = (ListView) findViewById(R.id.list_device);
        
		final DatabaseHandler db = new DatabaseHandler(this);
        final ArrayList<MyBeacon> beacons = (ArrayList<MyBeacon>) db.getAllBeacons();
        
        final MyBeaconListBaseAdapter adapter = new MyBeaconListBaseAdapter(this, beacons);
        
        listAnimation.setAdapter(adapter);
        
        // Lorsque l'on clique sur un élément de la liste
        listAnimation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getBaseContext(), Accueil.class);
        		intent.putExtra("id", position+1);
        		startActivity(intent);
			}
		});
        
        // Lorsque l'on fait un clic long on peut afficher une liste permettant d'autres actions (comme par exemple supprimer)
//        listAnimation.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, final View view,
//                final int position, long id) {
              
				//Creating the instance of PopupMenu  
//				PopupMenu popup = new PopupMenu(getBaseContext(), view);  
				//Inflating the Popup using xml file  
//				popup.getMenuInflater().inflate(R.menu.popup_accueil, popup.getMenu());
				
				//registering popup with OnMenuItemClickListener  
//				popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {  
//			        @Override
//					public boolean onMenuItemClick(MenuItem item) {
//			        	Log.d("db", Integer.toString(db.getBeaconsCount()));
//			        	final MyBeacon beacon = db.getBeacon(position+1);
//			        	switch(item.getItemId()){
//			        	case R.id.remove:
//			        		/*view.animate().setDuration(2000).alpha(0)
//			        		.withEndAction(new Runnable() {
//			        			@Override
//			                    public void run() {
//			        				beacons.remove(position);
//			        				db.deleteBeacon(beacon);
//			        				adapter.notifyDataSetChanged();
//			        				view.setAlpha(1);
//			                    }
//			                  });*/
//			        		break;
//			         	}  
//				        return true;  
//			        }
//				});
//				popup.show();
//				return true;
//            }
//        });
//
	}
}
