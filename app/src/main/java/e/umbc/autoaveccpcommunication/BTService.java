package e.umbc.autoaveccpcommunication;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.ToggleButton;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by Jessica on 3/25/2018.
 */

public class BTService extends Service {
    //private BluetoothAdapter BTAdapter;
    private IBinder myBinder = new MyBinder();
    public class MyBinder extends Binder {
        BTService getService() {
            return BTService.this;
        }
    }

    public BluetoothAdapter BTAdapter = BluetoothAdapter.getDefaultAdapter();
    private static final String TAG = "CommsActivity";
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private OutputStream outStream = null;
    private InputStream inStream = null;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    // Constant for UUID
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    //final String address = "B8:27:EB:7A:B9:13";
    public final static String EXTRA_ADDRESS = "B8:27:EB:7A:B9:13";


    // MAKE BUTTON ON MAINACTIVITY AND MAKE ON bind for when button is pressed
    // binding service

    @Override
    public void onCreate() {
        super.onCreate();
        final String address = "B8:27:EB:7A:B9:13";
        final BluetoothDevice device = BTAdapter.getRemoteDevice(address);
        try {
            new ConnectThread(device).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public class ConnectThread extends Thread {
        private ConnectThread(BluetoothDevice device) throws IOException {
            /*if (mmSocket != null) {
                if(mmSocket.isConnected()) {
                    send();
                }
            }*/
            BluetoothSocket tmp = null;
            mmDevice = device;
            try {
                // This is the UUID that is in the Python Code on the RP3
                UUID uuid = UUID.fromString("94f39d29-7d6d-437d-973b-fba39e49d4ee");
                tmp = mmDevice.createRfcommSocketToServiceRecord(uuid);
            } catch (IOException e) {
                Log.e(TAG, "Socket's create() method failed", e);
            }
            mmSocket = tmp;
            BTAdapter.cancelDiscovery();
            try {
                mmSocket.connect();
            } catch (IOException connectException) {
                Log.v(TAG, "Connection exception!");
                try {
                    mmSocket.close();
                   /* mmSocket = (BluetoothSocket) mmDevice.getClass().getMethod("createRfcommSocket", new Class[]{int.class}).invoke(mmDevice, 1);
                    mmSocket.connect();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                */} catch (IOException closeException) {

                }
            }
            send();
        }
        // THIS IS WHAT SENDS THE VOLTAGE INFO
        // OutputStream: accepts output bytes and sends them to some sink
        public void send() throws IOException {
            String msg = "AVE test data!!";
            //byte[] msgBuffer = msg.getBytes();
            OutputStream mmOutputStream = mmSocket.getOutputStream();
            // writes bytes from the specified byte array to this output stream
            mmOutputStream.write(msg.getBytes());
            receive();
        }

        // InputStream: accepts an input stream of bytes
        // getInputStream(): called in order to retreive InputStream objects, which
        // are automatically connected to the socket
        public void receive() throws IOException {
            InputStream mmInputStream = mmSocket.getInputStream();
            byte[] buffer = new byte[256];
            int bytes;

            try {
                bytes = mmInputStream.read(buffer);
                String readMessage = new String(buffer, 0, bytes);
                Log.d(TAG, "Received: " + readMessage);
                // Sets TextView object to display the message
//                TextView test1 = (TextView) findViewById(R.id.Voltage);
//                test1.setText("Data from CCP\n" + readMessage);
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Problemsccurred!");
                return;
            }
        }
    }



//    @Override
//    public void onStartCommand(Intent intent ) {
//
//       /* BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//
//        //final Intent intent = getIntent();
//
//        //final String address = intent.getStringExtra(MainActivity.EXTRA_ADDRESS);
//        final String address = "B8:27:EB:7A:B9:13";
//
//        final BluetoothDevice device = BTAdapter.getRemoteDevice(address);
//        try {
//            // Starts connecting with the device
//            new ConnectThread(device).start();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        // Show a system activity that enables bluetooth
//        if (!mBluetoothAdapter.isEnabled()) {
//            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(enableBluetooth, 0);
//        }*/
//    }


/*    @Override
    public IBinder onBind(Intent intent) {
        // Used only in case of bound services.
        return null;
    }*/

}
