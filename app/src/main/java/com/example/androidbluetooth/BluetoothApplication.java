package com.example.androidbluetooth;


import android.app.Application;

import com.example.androidbluetooth.adapter.AdapterManager;
import com.example.androidbluetooth.entity.TouchObject;

public class BluetoothApplication extends Application {
	/**
	 * Application实例
	 */
	private static BluetoothApplication application;

	/**
	 * 
	 */
	private AdapterManager mAdapterManager;

	/**
	 * 当前操作的对象;
	 */
	private TouchObject mTouchObject;

	@Override
	public void onCreate() {
		super.onCreate();
		if (null == application) {
			application = this;
		}
		mTouchObject = new TouchObject();
	}

	/**
	 * 获取Application实例
	 * 
	 * @return
	 */
	public static BluetoothApplication getInstance() {
		return application;
	}

	public AdapterManager getAdapterManager() {
		return mAdapterManager;
	}

	public void setAdapterManager(AdapterManager adapterManager) {
		this.mAdapterManager = adapterManager;
	}

	public TouchObject getTouchObject() {
		return mTouchObject;
	}

	public void setTouchObject(TouchObject touchObject) {
		this.mTouchObject = touchObject;
	}

}
