package com.example.mybluetooth;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private static final int REQUEST_ENABLE_BT = 1;

    private BluetoothAdapter adapter;
    private Button saomiao;
    private Button stop;
    private Button delete;
    private ListView listview;
    private TextView title;
    private List<Device> list = new ArrayList<>();
    private NewAdapter newadapter;
    private OutputStream os;
    //  private final UUID MY_UUID = UUID.fromString("00001106-0000-1000-8000-00805F9B34FB");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            adapter = bluetoothManager.getAdapter();
        }
        initBlueE();
        newadapter = new NewAdapter(this, list);
        listview.setAdapter(newadapter);
        listview.setOnItemClickListener(this);
    }

    private void initBlueE() {
        if (adapter == null) {
            Log.d("MainActivity", "adapter为空");
        }
        if (adapter == null || !adapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        String name = adapter.getName();
        String address = adapter.getAddress();
        Log.d("MainActivity", "bluetooth name =" + name + " address =" + address);
        Set<BluetoothDevice> devices = adapter.getBondedDevices();
        title.setText(name + "         " + address);
        Log.d("MainActivity", "bonded device size =" + devices.size());
        for (BluetoothDevice bonddevice : devices) {
            Log.d("MainActivity", "bonded device name =" + bonddevice.getName() + " address" + bonddevice.getAddress());
//            list.clear();
//            list.add(new Device(bonddevice.getName(), bonddevice.getAddress()));
        }
    }

    private String path;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            Log.d("MainActivity", "蓝牙启动成功");
        }
        if (resultCode == 8) {
            Uri uri = data.getData();
            if ("file".equalsIgnoreCase(uri.getScheme())) {//使用第三方应用打开
                path = uri.getPath();
                Log.e("--------->",path);
                Toast.makeText(this, path + "11111", Toast.LENGTH_SHORT).show();
                return;
            }
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {//4.4以后
                path = getPath(this, uri);
                Toast.makeText(this, path, Toast.LENGTH_SHORT).show();
            } else {//4.4以下下系统调用方法
                path = getRealPathFromURI(uri);

                Toast.makeText(MainActivity.this, path + "222222", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initView() {
        saomiao = (Button) findViewById(R.id.saomiao);
        stop = (Button) findViewById(R.id.stop);
        delete = (Button) findViewById(R.id.delete);
        listview = (ListView) findViewById(R.id.listview);
        title = (TextView) findViewById(R.id.title);

        saomiao.setOnClickListener(this);
        stop.setOnClickListener(this);
        delete.setOnClickListener(this);


    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.saomiao:
                list.clear();
                adapter.startDiscovery();
                IntentFilter filter = new IntentFilter();
                //发现设备
                filter.addAction(BluetoothDevice.ACTION_FOUND);
                //设备连接状态改变
                filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
                //蓝牙设备状态改变
                filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
                registerReceiver(mBluetoothReceiver, filter);
                break;
            case R.id.stop:
                adapter.cancelDiscovery();
                break;
            case R.id.delete:
                adapter.disable();
                break;
        }
    }

    private BroadcastReceiver mBluetoothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d("MainActivity", "mBluetoothReceiver action =" + action);
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {//每扫描到一个设备，系统都会发送此广播。
                //获取蓝牙设备
                BluetoothDevice scanDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (scanDevice == null || scanDevice.getName() == null) return;
                Log.d("MainActivity", "name=" + scanDevice.getName() + "address=" + scanDevice.getAddress());
                //蓝牙设备名称
                String name = scanDevice.getName();
                ////蓝牙设备MAC
                String address = scanDevice.getAddress();
                if (scanDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
                    list.add(new Device(name, address, "已配对"));
                } else {
                    list.add(new Device(name, address, "未配对"));
                }
                newadapter.notifyDataSetChanged();
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                Log.d("MainActivity", "搜索完成");
            }
        }

    };
    private BluetoothSocket mmSocket;
    private BluetoothDevice mmDevice;

    @Override
    public void onItemClick(final AdapterView<?> adapterView, View view, final int i, final long l) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.RESOURCE).setMessage(R.string.RESOURCE2).setPositiveButton(R.string.RESOURCE3, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                BluetoothDevice device = adapter.getRemoteDevice(list.get(i).getAddress());
                // Use a temporary object that is later assigned to mmSocket,
                // because mmSocket is final
                BluetoothSocket tmp = null;
                mmDevice = device;
                Method m = null;
                try {
                    m = mmDevice.getClass().getMethod(
                            "createRfcommSocket", new Class[]{int.class});
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                try {
                    tmp = (BluetoothSocket) m.invoke(mmDevice, 1);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                mmSocket = tmp;
                try {
                    mmSocket.connect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }

        }).setNegativeButton(R.string.RESOURCE4, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");//无类型限制
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 8);
                dialog.dismiss();
            }
        }).show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            mmSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private final BluetoothGattCallback bluetoothGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                gatt.readRemoteRssi();
                gatt.discoverServices();
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                //    BluetoothGattService service = gatt.getService(BluetoothConstant.UUID_SERVICE);
            }

        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            super.onReadRemoteRssi(gatt, rssi, status);
        }
    };

    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (null != cursor && cursor.moveToFirst()) {
            ;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
            cursor.close();
        }
        return res;
    }

    public static String getPath(Context context, Uri uri) {

        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }
}
