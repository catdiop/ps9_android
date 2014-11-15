package enseirb.t3.e_health.bluetooth;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

public class ConnectThread extends Thread {

	private UUID MY_UUID;
	private final BluetoothSocket mmSocket;
	private final BluetoothDevice mmDevice;
	
	public ConnectThread(BluetoothDevice device){
		Log.d("msg5", device.getName());
		BluetoothSocket tmp=null;
		mmDevice=device;
		//get a BluetoothSocket to connect with the given BluetoothDevice
		try{
			Method m=mmDevice.getClass().getMethod("createInsecureRfcommSocket", new Class[] {int.class});
			tmp=(BluetoothSocket)m.invoke(mmDevice, Integer.valueOf(1));
		}
		catch(Exception e){
		}
		mmSocket=tmp;
	}

	@Override
	public void run() {
		try{
			Log.d("msg6",String.valueOf(BluetoothAdapter.getDefaultAdapter().isDiscovering()));
			BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
			Log.d("msg6",String.valueOf(BluetoothAdapter.getDefaultAdapter().isDiscovering()));
			mmSocket.connect();
			Log.d("msg3", "Connexion réussie...");
		}
		catch(IOException e){
			try{
				Log.d("msg5", "cause : "+e.getLocalizedMessage());
				mmSocket.close();
			}
			catch(IOException closeException){return;}
		} 
		//Do work to manage the connection(in a separate thread)
		//manageConnectedSocket(mmSocket);
	}
	
	public void cancel(){
		try{
			mmSocket.close();
		} catch(IOException e) {};
	}
}
