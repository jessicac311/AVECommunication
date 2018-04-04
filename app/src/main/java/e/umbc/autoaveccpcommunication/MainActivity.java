package e.umbc.autoaveccpcommunication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.ParcelUuid;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.ToggleButton;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.UUID;
import e.umbc.autoaveccpcommunication.BTService.MyBinder;

public class MainActivity extends AppCompatActivity {
    BTService mBTService;
    boolean mServiceBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Button AVEbutton = (Button) findViewById(R.id.AVEbutton);
        ToggleButton AVEToggle = (ToggleButton) findViewById(R.id.AVEtoggle);
        AVEToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // AVEToggle is enabled
                    Intent intent = new Intent(MainActivity.this, BTService.class);
                    //startService(intent);
                    bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
                } else {
                    // AVEToggle is disabled
                    //Intent stopIntent = new Intent(MainActivity.this, BTService.class);
                    unbindService(mServiceConnection);
                    //startService(stopIntent);
                }


            }
        });
    }

        private ServiceConnection mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                MyBinder myBinder = (MyBinder) service;
                mBTService = myBinder.getService();
                mServiceBound = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mServiceBound = false;
            }
        };

}
