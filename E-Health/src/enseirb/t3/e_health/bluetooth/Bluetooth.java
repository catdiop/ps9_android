package enseirb.t3.e_health.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;

public class Bluetooth {
	
	private final static int REQUEST_CODE_ENABLE_BLUETOOTH = 0;
	private Activity activity;
	private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	
	public Bluetooth (Activity activity) {
		this.activity = activity;
	}

	public void enableBluetooth () {

		if (!mBluetoothAdapter.isEnabled()) {
			   Intent enableBlueTooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			   activity.startActivityForResult(enableBlueTooth, REQUEST_CODE_ENABLE_BLUETOOTH);
		}
	}

}
