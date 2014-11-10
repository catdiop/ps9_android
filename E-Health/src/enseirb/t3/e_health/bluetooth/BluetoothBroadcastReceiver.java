package enseirb.t3.e_health.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BluetoothBroadcastReceiver extends BroadcastReceiver {
    
	@Override
	 public void onReceive(Context context, Intent intent) {
	        String action = intent.getAction();
	        if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)){
	        	Toast.makeText(context, "Discovery started...", Toast.LENGTH_LONG).show();
	        }
	        // When discovery finds a device
	        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
	            // Get the BluetoothDevice object from the Intent
	            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				// Add the name and address to an array adapter to show in a ListView
	           // mArrayAdapter.add(device);
	            Toast.makeText(context, device.getName(), Toast.LENGTH_SHORT).show();
	        }
	    }
}
