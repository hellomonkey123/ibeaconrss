package com.lyw.tracker.activity;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.lyw.tracker.R;
import com.lyw.tracker.adapter.BLEListViewAdapter;
import com.lyw.tracker.bean.BLEObj;
import com.lyw.tracker.bean.SettingInfoBean;
import com.lyw.tracker.comm.SP;
import com.lyw.tracker.service.RssiService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class MainActivity extends AppCompatActivity {

    public long exitTime;
    public static final int SERVICE_RSSI = 100;

    private FloatingActionButton btn_scan;

    private BluetoothManager btManager;
    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning = false;//扫描标志位
    private boolean mWarning = false;//b报警标志位
    private int SCAN_PERIOD = 30000;//扫描周期1秒

    private TextView tv_count;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private BLEListViewAdapter mAdapter;
    private Map<String, BLEObj> mDeviceMap = new HashMap<>();
    //声明AMapLocationClient类对象
    public AMapLocationClient mlocationClient = null;
    public AMapLocation aMapLocation;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            MainActivity.this.aMapLocation = aMapLocation;
        }
    };

    //Service相关
    private RssiService mService = null;
    private RssiService.MyBinder mBinder;
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            mBinder = (RssiService.MyBinder) binder;
            mService = mBinder.getService();
            mBinder.setHandler(mHandler);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }
    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SERVICE_RSSI: {
//                    Toast.makeText(MainActivity.this, "SERVICE_RSSI", Toast.LENGTH_SHORT).show();
                    //得到连接设备列表
                    Map<String, BLEObj> mConnectedMap = mBinder.getConnectedMap();
                    tv_count.setText("连接设备：" + mConnectedMap.size() + "/5");

                    String strName = "";
                    mWarning = false;//重置报警标记位
                    for (String key : mDeviceMap.keySet()) {
                        mDeviceMap.get(key).setIsConnected(false);
                    }

                    for (String key : mConnectedMap.keySet()) {
                        //后台Service管理的数据
                        final BLEObj objService = mConnectedMap.get(key);
                        //当前UI所显示的数据
                        BLEObj objActivity = mDeviceMap.get(key);
                        //设置信号强度
                        int rssi = Math.abs(objService.getRssi());
                        objActivity.setRssi(rssi);
                        if (rssi > 66) {
                            mWarning = true;
                            strName = strName + " " + objService.getDevice().getName();
                            BmobQuery<SettingInfoBean> bmobQuery = new BmobQuery<SettingInfoBean>();
                            bmobQuery.addWhereEqualTo("Address", objService.getDevice().getAddress());
                            bmobQuery.findObjects(new FindListener<SettingInfoBean>() {
                                @Override
                                public void done(List<SettingInfoBean> list, BmobException e) {
                                    if (null!=e){
                                        return;
                                    }
                                    if (list.size() != 0) {
                                        SettingInfoBean settingInfoBean = new SettingInfoBean();
                                        settingInfoBean.setName(objService.getDevice().getName());
                                        settingInfoBean.setAddress(objService.getDevice().getAddress());
                                        settingInfoBean.setType(objService.getDevice().getType() + "");
                                        settingInfoBean.setPhone((String) SP.get(MainActivity.this, "phone",""));
                                        if (null != aMapLocation) {
                                            settingInfoBean.setLongitude(aMapLocation.getLongitude() + "");
                                            settingInfoBean.setLatitude(aMapLocation.getLatitude() + "");
                                        }
                                        settingInfoBean.update(list.get(0).getObjectId(), new UpdateListener() {
                                            @Override
                                            public void done(BmobException e) {
                                                if (null!=e){
                                                    return;
                                                }
                                            }
                                        });
                                    } else {
                                        SettingInfoBean settingInfoBean = new SettingInfoBean();
                                        settingInfoBean.setName(objService.getDevice().getName());
                                        settingInfoBean.setAddress(objService.getDevice().getAddress());
                                        settingInfoBean.setType(objService.getDevice().getType() + "");
                                        settingInfoBean.setPhone((String) SP.get(MainActivity.this, "phone",""));
                                        if (null != aMapLocation) {
                                            settingInfoBean.setLongitude(aMapLocation.getLongitude() + "");
                                            settingInfoBean.setLatitude(aMapLocation.getLatitude() + "");
                                        }
                                        settingInfoBean.save(new SaveListener<String>() {
                                            @Override
                                            public void done(String s, BmobException e) {
                                                if (null != e) {

                                                }
                                            }
                                        });
                                    }
                                }
                            });
//                            objService.getDevice().get

                        }
                        //设置时间戳
                        objActivity.setTimestamp(objService.getTimestamp());
                        //设置连接状态
                        objActivity.setIsConnected(true);

                    }
                    //根据mWarning标志位判断是否发送广播
                    if (mWarning == true) {
                        Intent intent = new Intent("com.lyw.tracker.broadcast.RssiReceiver");
                        intent.putExtra("msg", strName);
                        sendBroadcast(intent);
                    }
                    mWarning = false;
                    //更新UI
                    if (mAdapter != null) {
                        mAdapter.setDatas(mDeviceMap);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                }
                break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //工具栏、侧滑菜单初始化
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(toolbar);

        exitTime = System.currentTimeMillis();

        //初始化Service
        Intent serviceIntent = new Intent(MainActivity.this, RssiService.class);
        bindService(serviceIntent, conn, Service.BIND_AUTO_CREATE);

        //初始化蓝牙设备
        initBluetooth();
        //初始化页面
        initView();
        //初始化高德
        initLoction();
    }

    private void initLoction() {

        mlocationClient = new AMapLocationClient(this);
        //初始化定位参数
        AMapLocationClientOption   mLocationOption = new AMapLocationClientOption();
        //设置定位监听
        mlocationClient.setLocationListener(mLocationListener);
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(true);
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。
        //如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会。
        mLocationOption.setOnceLocationLatest(true);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        //启动定位
        mlocationClient.startLocation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
    }

    private void initBluetooth() {
        btManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        mBluetoothAdapter = btManager.getAdapter();
    }

    private void initView() {

        btn_scan = (FloatingActionButton) findViewById(R.id.btn_scan);
        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mService.disconnectBLE();
                mDeviceMap.clear();
                mAdapter.setDatas(mDeviceMap);
                scanLeDevice(true);
            }
        });

        //textview连接设备数量
        tv_count = (TextView) findViewById(R.id.tv_count);

        //初始化RecyclerView相关
        mRecyclerView = (RecyclerView) findViewById(R.id.listview);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mAdapter = new BLEListViewAdapter();
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new BLEListViewAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position, BLEObj obj) {
                if (obj.isConnected() == false) {
                    boolean test = mService.connectBLE(obj);
                    if (test == false) {
                        Toast.makeText(MainActivity.this, "连接失败！请检查设备状态或是否超过连接最大数！", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    mService.disconnectBLE(obj);
                }
            }
        });
    }

    /**
     * 扫描附近蓝牙设备
     */
    private void scanLeDevice(boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    Toast.makeText(MainActivity.this, "搜索结束！", Toast.LENGTH_SHORT).show();
                }
            }, SCAN_PERIOD);
            if (!mBluetoothAdapter.isEnabled()) {
                mBluetoothAdapter.enable();
            }
            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }

    /**
     * 扫描设备的回调
     */
    BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice bluetoothDevice, int rssi, byte[] scanRecord) {
            BLEObj obj = new BLEObj();
            obj.setAddress(bluetoothDevice.getAddress());
            obj.setDevice(bluetoothDevice);
            obj.setRssi(rssi);
            obj.setTimestamp(System.currentTimeMillis());
            if (!mDeviceMap.containsKey(bluetoothDevice.getAddress())) {
                mDeviceMap.put(obj.getAddress(), obj);
                mAdapter.setDatas(mDeviceMap);
            } else {
                mDeviceMap.get(bluetoothDevice.getAddress()).setRssi(rssi);//刷新信号强度
                mDeviceMap.get(bluetoothDevice.getAddress()).setTimestamp(System.currentTimeMillis());//刷新扫描时间
                mAdapter.setDatas(mDeviceMap);
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdapter.notifyDataSetChanged();
                }
            });


        }
    };

    @Override
    protected void onResume() {
        super.onResume();
//        if (null!=mBluetoothAdapter){
//            if (mBluetoothAdapter.getState()>0){
//                mScanning = false;
//                mBluetoothAdapter.stopLeScan(mLeScanCallback);
//            }else {
//                scanLeDevice(true);
//            }
//        }
    }

    private long touchTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN
                && KeyEvent.KEYCODE_BACK == keyCode) {
            long currentTime = System.currentTimeMillis();
            if ((currentTime - touchTime) >= 2000) {
                Toast.makeText(this, "再点一次退出", Toast.LENGTH_SHORT).show();
                touchTime = currentTime;
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            //设置
            case R.id.action_settings: {
                mScanning = false;
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
                startActivity(new Intent(this, SetActivity.class));
            }
            break;

            //关于
            case R.id.action_about: {
                mScanning = false;
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
                startActivity(new Intent(this, LoseAct.class));
            }
            break;
        }

        return super.onOptionsItemSelected(item);
    }


}





