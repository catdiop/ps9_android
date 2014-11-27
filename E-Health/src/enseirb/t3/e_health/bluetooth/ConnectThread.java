package enseirb.t3.e_health.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class ConnectThread extends Thread {

	private static final int MESSAGE_READ = 999;
	private  UUID SERIAL_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
	private BluetoothSocket mmSocket = null;
	private final BluetoothDevice mmDevice;
	private InputStream mmInStream;
	private OutputStream mmOutStream;
	
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
		
		try {
			mmInStream=mmSocket.getInputStream();
			mmOutStream=mmSocket.getOutputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//		mmSocket=tmp;
	}

	@Override
	public void run() {
		byte[] buffer= new byte[32]; //buffer store for the stream
		int bytes = 0;
		try{
			BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
			if (!mmSocket.isConnected()) {
				mmSocket.connect();
				Log.d("Status", "Arduino connecté");
				while (true) {
					bytes=mmInStream.read(buffer);
					String value = new String(buffer, "UTF-8");
					Log.d("buffer", value);
					mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
	                .sendToTarget();
				}
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
		
//		ReadThread readthread = new ReadThread(mmSocket);
//		readthread.start();
	}
	
	public void cancel(){
		try{
			mmSocket.close();
		} catch(IOException e) {};
	}
	
	static Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			byte[] writeBuf = (byte[]) msg.obj;
			int begin = (int) msg.arg1;
			int end = (int) msg.arg2;

			switch (msg.what) {
			case MESSAGE_READ:
			      // your code goes here
				Log.d("handler", "message read");
				break;
			}
		}
	};
}
