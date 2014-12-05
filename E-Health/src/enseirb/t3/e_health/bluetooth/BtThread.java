package enseirb.t3.e_health.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class BtThread extends Thread {

	private BluetoothSocket mmSocket = null;
	private InputStream mmInStream;
	private OutputStream mmOutStream;
	private Handler handler;
	private String TAG = "ConnectThread";

	public BtThread(BluetoothDevice device, Handler handler) {
		this.handler = handler;
		Log.d(TAG, device.getName());

		// get a BluetoothSocket to connect with the given BluetoothDevice
		try {
			mmSocket = (BluetoothSocket) device
					.getClass()
					.getMethod("createInsecureRfcommSocket",
							new Class[] { int.class }).invoke(device, 1);
			mmInStream = mmSocket.getInputStream();
			mmOutStream = mmSocket.getOutputStream();
		} catch (Exception e) {
		}
	}

	@Override
	public void run() {
		byte[] buffer = new byte[128]; // buffer store for the stream
		int bytes = 0;
		try {
			BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
			if (!mmSocket.isConnected()) {
				mmSocket.connect();
				Log.d(TAG, "Arduino connecté");
			} else
				Log.d(TAG, "Arduino déjà connecté");

			String value = "";
			while (true) {
				bytes = mmInStream.read(buffer, 0, 128);
				if (bytes > 0) {
					// On convertit les données en String
					byte rawdata[] = new byte[bytes];
					for (int i = 0; i < bytes; i++)
						rawdata[i] = buffer[i];

					value = value + new String(rawdata);

					if (value.contains("\n")) {
						Log.d(TAG, value);
						Message msg = handler.obtainMessage();
						Bundle bundle = new Bundle();
						bundle.putString("msg", value);
						msg.setData(bundle);
						handler.sendMessage(msg);
						value = "";
					}
				}
			}
		} catch (IOException e) {
			try {
				mmSocket.close();
			} catch (IOException closeException) {
				return;
			}
		}
	}

	/**
	 * Write to the connected OutStream.
	 * 
	 * @param buffer
	 *            The bytes to write
	 */
	public void write(byte[] buffer) {
		try {
			mmOutStream.write(buffer);
		} catch (IOException e) {
			Log.e(TAG, "Exception during write", e);
		}
	}

	public void cancel() {
		try {
			mmSocket.close();
		} catch (IOException e) {
		}
		;
	}
}
