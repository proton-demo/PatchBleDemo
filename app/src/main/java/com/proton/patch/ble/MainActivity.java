package com.proton.patch.ble;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.proton.ecgpatch.connector.EcgPatchManager;
import com.proton.ecgpatch.connector.callback.DataListener;
import com.wms.ble.bean.ScanResult;
import com.wms.ble.callback.OnConnectListener;
import com.wms.ble.callback.OnScanListener;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EcgPatchManager.init(this);
        //需要获取定位权限，否则搜索不到设备
        PermissionUtils.getLocationPermission(this);

        findViewById(R.id.idText).setOnClickListener(v -> {
            testPatchConnect();
        });
    }

    private void testScanPatch() {
        EcgPatchManager.scanDevice(new OnScanListener() {
            @Override
            public void onScanStart() {
                super.onScanStart();
            }

            @Override
            public void onDeviceFound(ScanResult scanResult) {
                super.onDeviceFound(scanResult);
                Log.w(TAG, "扫描的设备:" + scanResult.getMacaddress());
            }

            @Override
            public void onScanStopped() {
                super.onScanStopped();
            }

            @Override
            public void onScanCanceled() {
                super.onScanCanceled();
            }
        });
    }

    private void testPatchConnect() {

        EcgPatchManager ecgPatchManager = EcgPatchManager.getInstance("C9:57:0B:99:0D:23");
        ecgPatchManager.setDataListener(new DataListener() {
            @Override
            public void receiveEcgRawData(byte[] bytes) {
                Log.e(TAG, "蓝牙数据:" + bytes.length);
            }

            @Override
            public void receivePackageNum(int packageNum) {
                Log.e(TAG, "包序: " + packageNum);
            }

            @Override
            public void receiveFallDown(boolean isFallDown) {
                Log.e(TAG, "是否跌倒: " + isFallDown);
            }
        });

        ecgPatchManager.connectEcgPatch(new OnConnectListener() {
            @Override
            public void onConnectSuccess(boolean isNewUUID) {
                super.onConnectSuccess(isNewUUID);
                Log.e(TAG, "连接成功");
            }

            @Override
            public void onConnectFaild() {
                super.onConnectFaild();
                Log.e(TAG, "连接失败");
            }

            @Override
            public void onDisconnect(boolean isManual) {
                super.onDisconnect(isManual);
                Log.e(TAG, "断开连接，是否手动断开:" + isManual);
            }
        });
    }
}