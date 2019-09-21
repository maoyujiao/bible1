package com.iyuba.core.util;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * 获取地理位置
 *
 * @author 陈彤
 */
public class GetLocation {
    private static Context mContext;
    private static GetLocation instanceGetLocation;
    // 监听状态
    LocationListener locationListener = new LocationListener() {
        // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        // Provider被enable时触发此函数，比如GPS被打开
        @Override
        public void onProviderEnabled(String provider) {

        }

        // Provider被disable时触发此函数，比如GPS被关闭
        @Override
        public void onProviderDisabled(String provider) {

        }

        // 当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {

            }
        }
    };
    private double latitude = 0.0;
    private double longitude = 0.0;

    public static GetLocation getInstance(Context context) {
        mContext = context;
        if (instanceGetLocation == null) {
            instanceGetLocation = new GetLocation();
        }
        return instanceGetLocation;
    }

    /**
     * @return string[0]是latitude，string[1]是longitude
     */
    public String[] getLocation() {
//        LocationManager locationManager = (LocationManager) mContext
//                .getSystemService(Context.LOCATION_SERVICE);
//        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            Location location = locationManager
//                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
//            if (location != null) {
//                latitude = location.getLatitude();
//                longitude = location.getLongitude();
//            } else {
//                locationManager.requestLocationUpdates(
//                        LocationManager.NETWORK_PROVIDER, 1000, 0,
//                        locationListener);
//                Location location1 = locationManager
//                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//                if (location1 != null) {
//                    latitude = location1.getLatitude(); // 经度
//                    longitude = location1.getLongitude(); // 纬度
//                }
//            }
//        } else {
//            locationManager
//                    .requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
//                            1000, 0, locationListener);
//            Location location = locationManager
//                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//            if (location != null) {
//                latitude = location.getLatitude(); // 经度
//                longitude = location.getLongitude(); // 纬度
//
//            }
//        }
//        String[] strings = {String.valueOf(latitude),
//                String.valueOf(longitude)};
//        if (latitude != 0 || longitude != 0) {
//            locationManager.removeUpdates(locationListener);
//        }
        String [] s = {"0","0"};
        return s;

    }

//    public Location getPosition() {
//        Location location;
//        LocationManager locationManager = (LocationManager) mContext
//                .getSystemService(Context.LOCATION_SERVICE);
//        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            location = locationManager
//                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
//            if (location != null) {
//
//            } else {
//                locationManager.requestLocationUpdates(
//                        LocationManager.NETWORK_PROVIDER, 1000, 0,
//                        locationListener);
//                location = locationManager
//                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//            }
//        } else {
//            locationManager
//                    .requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
//                            1000, 0, locationListener);
//            location = locationManager
//                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//
//        }
//        if (location != null) {
//            locationManager.removeUpdates(locationListener);
//        }
//        return location;
//    }

}
