package com.example.androidbluetooth.receiver;


import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.androidbluetooth.BluetoothApplication;
import com.example.androidbluetooth.adapter.AdapterManager;
import com.example.androidbluetooth.entity.TouchObject;

/**
 * 配对状态改变监听器
 * @author 210001001427
 *
 */
public class PairStateChangeReceiver extends BroadcastReceiver {
	private BluetoothApplication mApplication;
	private Activity mActivity;
	private AdapterManager mAdapterManager;
	private TouchObject mTouchObject;
	
	public PairStateChangeReceiver(Activity activity){
		this.mApplication = BluetoothApplication.getInstance();
		this.mActivity = activity;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if(intent.getAction().equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)){
			//状态改变
			if(null == mAdapterManager){
				mAdapterManager = mApplication.getAdapterManager();
			}
			if(null == mTouchObject){
				mTouchObject = mApplication.getTouchObject();
			}
			//取得状态改变的设备，更新设备列表信息 （配对状态）
			BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			mAdapterManager.changeDevice(mTouchObject.clickDeviceItemId, device);
			mAdapterManager.updateDeviceAdapter();
			mActivity.unregisterReceiver(this);
		}
	}

}
