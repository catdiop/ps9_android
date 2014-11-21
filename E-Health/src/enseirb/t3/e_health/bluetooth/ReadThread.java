package enseirb.t3.e_health.bluetooth;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

public class ReadThread extends Thread {

	private InputStream mmInStream;
//	private OutputStream mmOutStream;
	
	public ReadThread(BluetoothSocket mmSocket) {
		
		try{
			mmInStream=mmSocket.getInputStream();
//			mmOutStream=mmSocket.getOutputStream();
		}
		catch(IOException e) {}
	}
	
	public void run() {
		byte[] buffer= new byte[1024]; //buffer store for the stream
		int bytes;  //bytes returned from read
		String data = null;
		
		//keep listening to the InputStream until an exceptions occurs
//		while(true){
			//read from InputStream
				try {
					bytes=mmInStream.read(buffer);
					 data = new String(buffer);
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
//				}
//				mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
//                .sendToTarget();
			Log.d("buffer", data);
		}
	}
	
//	public void write(byte[] bytes) {
//        try {
//            mmOutStream.write(bytes);
//        } catch (IOException e) { }
//    }
	
}


