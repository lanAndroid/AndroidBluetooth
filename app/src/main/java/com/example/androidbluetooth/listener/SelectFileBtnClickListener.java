package com.example.androidbluetooth.listener;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.androidbluetooth.activity.BluetoothDemoActivity;
import com.example.androidbluetooth.activity.SelectFileActivity;

/**
 * 选择文件按钮监听器
 * @author 210001001427
 *
 */
public class SelectFileBtnClickListener implements OnClickListener {
	private Activity mActivity;
	
	public SelectFileBtnClickListener(Activity activity){
		this.mActivity = activity;
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(mActivity, SelectFileActivity.class);
		mActivity.startActivityForResult(intent, BluetoothDemoActivity.RESULT_CODE);
	}

}
