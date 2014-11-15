package enseirb.t3.e_health.bluetooth;

import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Intent;

public class Bluetooth {
	
	private final static int REQUEST_CODE_ENABLE_BLUETOOTH = 0;
	private Activity activity;
	private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	private Set<BluetoothDevice> pairedDevices;
	private BroadcastReceiver mReceiver = null;
    private Set<BluetoothDevice> mArrayAdapter;
	
	public Bluetooth (Activity activity) {
		this.activity = activity;
	}

	public void enableBluetooth () {

		if (!mBluetoothAdapter.isEnabled()) {
			   Intent enableBlueTooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			   activity.startActivityForResult(enableBlueTooth, REQUEST_CODE_ENABLE_BLUETOOTH);
		}
	}
	
	public void discoverDevices() {
	
		// Create a BroadcastReceiver for ACTION_FOUND
		mReceiver = new BluetoothBroadcastReceiver();
		
		// Register the BroadcastReceiver
		//IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		//this.activity.registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
		
		mBluetoothAdapter.startDiscovery();
		
	}
}
