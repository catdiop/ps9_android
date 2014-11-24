package enseirb.t3.e_health.bluetooth;

import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

public class ConnectThread extends Thread {

	private  UUID SERIAL_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
	private BluetoothSocket mmSocket = null;
	private final BluetoothDevice mmDevice;
	
	public ConnectThread(BluetoothDevice device){
		Log.d("device name", device.getName());
		BluetoothSocket tmp=null;
		mmDevice=device;
		//get a BluetoothSocket to connect with the given BluetoothDevice
		try{
//			mmSocket = device.createRfcommSocketToServiceRecord(SERIAL_UUID);
//			Method m=mmDevice.getClass().getMethod("createInsecureRfcommSocket", new Class[] {int.class});
//			tmp=(BluetoothSocket)m.invoke(mmDevice, Integer.valueOf(1));
			mmSocket = (BluetoothSocket) device.getClass().getMethod("createInsecureRfcommSocket", new Class[] {int.class}).invoke(device,1);
		}
		catch(Exception e){
		}
//		mmSocket=tmp;
	}

	@Override
	public void run() {
		try{
			BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
			if (!mmSocket.isConnected()) {
				mmSocket.connect();
				Log.d("Status", "Arduino connecté");
			}
			else
				Log.d("Status", "Arduino déjà connecté");
		}
		catch(IOException e){
			try{
				mmSocket.close();
			}
			catch(IOException closeException){return;}
		} 
		//Do work to manage the connection(in a separate thread)
		ReadThread readthread = new ReadThread(mmSocket);
		readthread.start();
	}
	
	public void cancel(){
		try{
			mmSocket.close();
		} catch(IOException e) {};
	}
}
