package com.kuangxf.baseappas.position;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kuangxf on 2017/9/4.
 */

public class PositioningUtil {
    /*
     * 创建位置监听器
     */
    private static LocationListener locationListener;
    private static LocationManager locationManager;
    private static String provider;
    private static Context context;
    private static final int MSG_QUERY_NEXT = 1;

    private static final int PollingInterval = 1000;//轮询间隔，毫秒
    private static final int outTime = 1000 * 10;//超时时间，毫秒
    private static long startTime = 0;

    private static Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d("Location", "handleMessage: " + msg);
            switch (msg.what){
                case MSG_QUERY_NEXT:
                    Location currentLocation = locationManager.getLastKnownLocation(provider);
                    if (currentLocation != null) {
                        Log.d("Location", "Latitude: " + currentLocation.getLatitude());
                        Log.d("Location", "location: " + currentLocation.getLongitude());
                        //长时间的监听位置更新可能导致耗电量急剧上升,一旦获取到位置后，就停止监听
                        double latitude = currentLocation.getLatitude();
                        double longitude = currentLocation.getLongitude();
                        parsePosition(context, latitude, longitude);
                        locationManager.removeUpdates(locationListener);
                        locationManager = null;
                        provider = null;
                        context = null;
                        locationListener = null;
                    } else if((System.currentTimeMillis() - startTime) >= outTime){
                        Log.d("Location", "outTime");
                        locationManager.removeUpdates(locationListener);
                        locationManager = null;
                        provider = null;
                        context = null;
                        locationListener = null;
                    } else {
                        Log.d("Location", "Latitude: " + 0);
                        Log.d("Location", "location: " + 0);
                        handler.sendEmptyMessageDelayed(MSG_QUERY_NEXT, PollingInterval);
                    }
                    break;
            }
        }
    };

    public static void getPosition(Context context, LocationListener locationListener) {
        PositioningUtil.context = context;
        PositioningUtil.locationListener = locationListener;
        startTime = System.currentTimeMillis();
        //获取到LocationManager对象
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        //如果没有开启位置源，转到‘设置’-‘位置和安全’里勾选使用无线网络，来激活NETWORK_PROVIDER 或 GPS_PROVIDER
        if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) &&
                !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(context, "请勾选位置源,无线网络或GPS!", Toast.LENGTH_LONG).show();
            context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            return;
        }

        //设置位置查询条件，通过criteria返回符合条件的provider,有可能是wifi provider,也有可能是gps provider
        Criteria criteria = new Criteria(); //创建一个Criteria对象
        criteria.setAccuracy(Criteria.ACCURACY_COARSE); //设置精度,模糊模式,对于DTV地区定位足够了；ACCURACY_FINE,精确模式
        criteria.setAltitudeRequired(false); //设置是否需要返回海拔信息,不要求海拔
        criteria.setBearingRequired(false); //设置是否需要返回方位信息，不要求方位
        criteria.setCostAllowed(true); //设置是否允许付费服
        criteria.setPowerRequirement(Criteria.POWER_LOW); //设置电量消耗等级
        criteria.setSpeedRequired(false); //设置是否需要返回速度信息
        //根据设置的Criteria对象，获取最符合此标准的provider对象
        provider = locationManager.getBestProvider(criteria, true);

        Log.d("Location", "provider: " + provider);
        //根据当前provider对象获取最后一次位置信息
        Location currentLocation = locationManager.getLastKnownLocation(provider);
        //如果位置信息为null，则请求更新位置信息
        if (currentLocation == null) {
            locationManager.requestLocationUpdates(provider, 0, 0, locationListener);
            handler.sendEmptyMessageDelayed(MSG_QUERY_NEXT, PollingInterval);
            return;
        }

        double latitude = currentLocation.getLatitude();
        double longitude = currentLocation.getLongitude();
        parsePosition(context, latitude, longitude);
    }

    public static ArrayList<String> parsePosition(Context context, double latitude, double longitude){
        ArrayList<String> ls = new ArrayList<>();
        //解析地址并显示
        Geocoder geoCoder = new Geocoder(context);
        try {
            List<Address> list = geoCoder.getFromLocation(latitude, longitude, 2);
            if (list != null && !list.isEmpty()) {
                //取第一个地址就可以
                Address address = list.get(0);
                //getCountryName 国家
                //getAdminArea 省份
                //getLocality 城市
                //getSubLocality 区
                //getFeatureName 街道
                Toast.makeText(context, address.getCountryName() + address.getAdminArea() + address.getLocality() + address.getSubLocality() + address.getFeatureName(), Toast.LENGTH_LONG).show();
                ls.add(address.getCountryName());
                ls.add(address.getAdminArea());
                ls.add(address.getLocality());
                ls.add(address.getSubLocality());
                ls.add(address.getFeatureName());
            }
        } catch (IOException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return ls;
    }
}
