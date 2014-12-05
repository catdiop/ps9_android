package enseirb.t3.e_health.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class ConnectThread extends Thread {

	private Context context;
	private static final int MESSAGE_READ = 999;
	private  UUID SERIAL_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
	private BluetoothSocket mmSocket = null;
	private final BluetoothDevice mmDevice;
	private InputStream mmInStream;
	private OutputStream mmOutStream;
	private Handler handler;
	
	private String TAG = "ConnectThread";
	
	public ConnectThread(BluetoothDevice device, Context context, Handler handler){
		this.context = context;
		this.handler = handler;
		Log.d(TAG, device.getName());
		BluetoothSocket tmp = null;
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
				Log.d(TAG, "Arduino connecté");
			}
			else
				Log.d(TAG, "Arduino déjà connecté");
			
			while (true) {
				bytes=mmInStream.read(buffer, 0, 100);
				if(bytes > 20) {
                    // On convertit les données en String
                    byte rawdata[] = new byte[bytes];
                    for(int i=0;i<bytes;i++)
                        rawdata[i] = buffer[i];
                     
                    String value = new String(rawdata);
                    
                    Log.d(TAG, value);
					
					Message msg = handler.obtainMessage();
	    			Bundle bundle = new Bundle();
	    			SimpleDateFormat dateformat = 
	                             new SimpleDateFormat("HH:mm:ss MM/dd/yyyy");
	    			String dateString = dateformat.format(new Date());
	    			bundle.putString("date", dateString);
	    			bundle.putString("msg", value);
	                msg.setData(bundle);
	                handler.sendMessage(msg);
                   
                }
				
				sleep(900);
			}
		} catch(IOException e){
			try{
				mmSocket.close();
			}
			catch(IOException closeException){return;}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public void cancel(){
		try{
			mmSocket.close();
		} catch(IOException e) {};
	}
	
}
