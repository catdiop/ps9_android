package enseirb.t3.e_health.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import enseirb.t3.e_health.DAO.DatabaseHandler;
import enseirb.t3.e_health.entity.ArduinoData;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class ConnectThread extends Thread {

	Context context;
	private DatabaseHandler dbHandler = DatabaseHandler.getInstance(context);
	private static final int MESSAGE_READ = 999;
	private  UUID SERIAL_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
	private BluetoothSocket mmSocket = null;
	private final BluetoothDevice mmDevice;
	private InputStream mmInStream;
	private OutputStream mmOutStream;
	
	public ConnectThread(BluetoothDevice device, Context context){
		this.context = context;
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
	}

	@Override
	public void run() {
		byte[] buffer= new byte[128]; //buffer store for the stream
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
					ArduinoData arduinoData = new ArduinoData(value);
					arduinoData.getAndStoreData(dbHandler);
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
				Log.d("handler", "message read");
				//Enregistrement des mesure réceptionnées dans la BDD
				
				break;
			}
		}
	};
}
