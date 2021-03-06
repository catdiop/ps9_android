package enseirb.t3.e_health.bluetooth;

import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class Bluetooth {

	private final static int REQUEST_CODE_ENABLE_BLUETOOTH = 0;
	public static BluetoothDevice device = null;
	private Activity activity;
	private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
			.getDefaultAdapter();

	public Bluetooth(Activity activity) {
		this.activity = activity;
	}

	public void enableBluetooth() {

		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableBlueTooth = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			activity.startActivityForResult(enableBlueTooth,
					REQUEST_CODE_ENABLE_BLUETOOTH);
		}
	}

	public void selectDevice() {
		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter
				.getBondedDevices();
		// If there are paired devices
		if (pairedDevices.size() > 0) {
			// Loop through paired devices
			for (BluetoothDevice device : pairedDevices) {
				// Add the name and address to an array adapter to show in a
				// ListView

				if (device.getName().equals("Ehealth")) {
					Log.d("msg", "Arduino d�j� d�couvert....");
					Bluetooth.device = device;
				}
			}
		} else
			discoverDevices();
	} 

	private void discoverDevices() {
		mBluetoothAdapter.startDiscovery();
	}
}
