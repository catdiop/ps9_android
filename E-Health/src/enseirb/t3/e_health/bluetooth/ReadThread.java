package enseirb.t3.e_health.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

public class ReadThread extends Thread {

	private static final int MESSAGE_READ = 999;
	private InputStream mmInStream;
	private OutputStream mmOutStream;
	
	public ReadThread(BluetoothSocket mmSocket) {
		
		try{
			mmInStream=mmSocket.getInputStream();
			mmOutStream=mmSocket.getOutputStream();
		}
		catch(IOException e) {}
	}
	
	public void run() {
		byte[] buffer= new byte[1024]; //buffer store for the stream
		int bytes = 0;  //bytes returned from read
		
		//keep listening to the InputStream until an exceptions occurs
		while(true){
			//read from InputStream
				try {
					bytes=mmInStream.read(buffer);
					Log.d("buffer", buffer.toString());
					
				} catch (IOException e) {
					e.printStackTrace();
				}
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
	
//	Handler mHandler = new Handler() {
//		@Override
//		public void handleMessage(Message msg) {
//			byte[] writeBuf = (byte[]) msg.obj;
//			int begin = (int) msg.arg1;
//			int end = (int) msg.arg2;
//
//			switch (msg.what) {
//			case 1:
//				String writeMessage = new String(writeBuf);
//				writeMessage = writeMessage.substring(begin, end);
//				break;
//			}
//		}
//	};
	
}


