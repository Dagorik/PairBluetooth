package com.pairbluetooth.mariachiio.dagorik.pairbluetooth.test_bluetooth.test_bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pairbluetooth.mariachiio.dagorik.pairbluetooth.R;
import com.pairbluetooth.mariachiio.dagorik.pairbluetooth.test_bluetooth.test_bluetooth.utils.BlueDroid;
import com.pairbluetooth.mariachiio.dagorik.pairbluetooth.test_bluetooth.test_bluetooth.utils.Device;
import com.pairbluetooth.mariachiio.dagorik.pairbluetooth.test_bluetooth.test_bluetooth.utils.BlueDiscoveryDialog;
import com.pairbluetooth.mariachiio.dagorik.pairbluetooth.test_bluetooth.test_bluetooth.utils.ConnectionDevice;
import com.pairbluetooth.mariachiio.dagorik.pairbluetooth.test_bluetooth.test_bluetooth.utils.ConnectionSecure;
import com.pairbluetooth.mariachiio.dagorik.pairbluetooth.test_bluetooth.test_bluetooth.utils.LineBreakType;

import java.nio.charset.Charset;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 1234;

    private BlueDroid bt;
    private StringBuilder textoRecebido = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt = new BlueDroid(this, ConnectionDevice.OTHER, ConnectionSecure.SECURE);

        if (!bt.isAvailable()) {
            finish();
            return;
        }

        bt.addDiscoveryListener(new BlueDroid.DiscoveryListener() {
            @Override
            public void onDiscoveryStarted() {
                Toast.makeText(MainActivity.this, "Busqueda iniciada", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDiscoveryFinished() {
                Toast.makeText(MainActivity.this, "Busqueda finalizada", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNoDevicesFound() {
                Toast.makeText(MainActivity.this, "Ningún dispositivo encontrado", Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onDeviceFound(Device device) {

                Toast.makeText(MainActivity.this, "Encontrado: " + device.getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDiscoveryFailed() {
                Toast.makeText(MainActivity.this, "La busca fallo", Toast.LENGTH_SHORT).show();
            }
        });

        bt.addConnectionListener(new BlueDroid.ConnectionListener() {
            @Override
            public void onDeviceConnecting() {
                Toast.makeText(MainActivity.this, "Conectando...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDeviceConnected() {
                Toast.makeText(MainActivity.this, "Conectado", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDeviceDisconnected() {
                Toast.makeText(MainActivity.this, "Desconectado", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDeviceConnectionFailed() {
                Toast.makeText(MainActivity.this, "Falla al conectar", Toast.LENGTH_SHORT).show();
            }
        });

        bt.addDataReceivedListener(new BlueDroid.DataReceivedListener() {
            @Override
            public void onDataReceived(byte data) {
                textoRecebido.append((char) data);
                ((TextView) findViewById(R.id.received_text)).setText(textoRecebido.toString());
            }
        });

        findViewById(R.id.btnProcurar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BlueDiscoveryDialog(MainActivity.this, bt).show();
            }
        });

        findViewById(R.id.btnDesconectar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bt.disconnect();
            }
        });

        findViewById(R.id.btnEnviar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = ((EditText) findViewById(R.id.send_text)).getText().toString();
                if (text.length() > 0) {
                    bt.send(text.getBytes(Charset.forName("US-ASCII")), LineBreakType.UNIX);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!bt.isEnabled()) {
            Toast.makeText(MainActivity.this, "Bluetooth desabilitado", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(i, REQUEST_ENABLE_BT);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bt.stop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        bt.checkDiscoveryPermissionRequest(requestCode, permissions, grantResults);
    }
}
