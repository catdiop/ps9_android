package enseirb.t3.e_health.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.bluetooth.BluetoothSocket;

public class ReadThread  {

	private final BluetoothSocket mmSocket;
	private final InputStream mmInStream;
	private final OutputStream mmOutStream;
	
	public ReadThread(BluetoothSocket socket) {
		mmSocket=socket;
		InputStream tmpIn=null;
		OutputStream tmpOut=null;
		
		try{
			tmpIn=socket.getInputStream();
			tmpOut=socket.getOutputStream();
		}catch(IOException e) {}
		
		mmInStream=tmpIn;
		mmOutStream=tmpOut;
	}
	
	public void run() {
		byte[] buffer=new byte[1024]; //buffer store for the stream
		int bytes;  //bytes returned from read
		
		//keep listening to the InputStream until an exceptions occurs
		while(true){
			try{
				//read from InputStream
				bytes=mmInStream.read(buffer);
			} catch(IOException e){
				break;
			}
		}
	}
}


