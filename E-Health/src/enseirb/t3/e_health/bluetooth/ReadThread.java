package enseirb.t3.e_health.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

public class ReadThread extends Thread {

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
		byte[] buffer= new byte[1024]; //buffer store for the stream
		int bytes;  //bytes returned from read
		
		//keep listening to the InputStream until an exceptions occurs
		while(true){
			//read from InputStream
				try {
					bytes=mmInStream.read(buffer);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//			write(buffer);
//				mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
//                .sendToTarget();
			Log.d("buffer", buffer.toString());
		}
	}
	
	public void write(byte[] bytes) {
        try {
            mmOutStream.write(bytes);
        } catch (IOException e) { }
    }
	
}


