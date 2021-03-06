package com.gongdian.weian.activity.baidu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.ab.activity.AbActivity;
import com.ab.util.AbLogUtil;
import com.ab.util.AbStrUtil;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;
import com.baidu.mapapi.utils.OpenClientUtil;
import com.baidu.mapapi.utils.route.BaiduMapRoutePlan;
import com.baidu.mapapi.utils.route.RouteParaOption;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.gongdian.weian.R;
import com.gongdian.weian.model.Project;
import com.gongdian.weian.model.Project_menu;
import com.gongdian.weian.utils.MyApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaiduMapShowTask extends AbActivity {

    private static final String TAG = "BaiduMapFragment";
    //    double mLat1 = 119.126559;
//    double mLon1 = 34.845686;
// 天安门坐标
    double mLat1 = 39.915291;
    double mLon1 = 116.403857;
    // 百度大厦坐标
    double mLat2 = 40.056858;
    double mLon2 = 116.308194;

    private MyApplication application;
    MapView mMapView;
    BaiduMap mBaiduMap;
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    private MyLocationConfiguration.LocationMode mCurrentMode;
    BitmapDescriptor mCurrentMarker;
    boolean isFirstLoc = true; // 是否首次定位
    private RadioButton r1, r2;
    private CheckBox checkTraffice, checkBaiduHeatMap;
    private ImageView location;
    private Marker[] mMarker = null;
    // 初始化全局 bitmap 信息，不用时及时 recycle
    List<Project> mList = new ArrayList<>();

    Map<Marker, Project> mLatLngMap = new HashMap<>();
    private InfoWindow mInfoWindow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAbContentView(R.layout.baidu_fragment);
        application = (MyApplication)getApplication();
        mMapView = (MapView)findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        location = (ImageView)findViewById(R.id.location);
        float level = mBaiduMap.getMaxZoomLevel();
        //   MapStatusUpdateFactory.zoomIn(level);
//        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomIn());
        MapStatusUpdate u = MapStatusUpdateFactory.zoomTo(level - 8);
        mBaiduMap.animateMapStatus(u);


        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();

        r1 = (RadioButton)findViewById(R.id.normal);
        r2 = (RadioButton)findViewById(R.id.statellite);
        r1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMapMode(v);
            }
        });
        r2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMapMode(v);
            }
        });

        checkTraffice = (CheckBox)findViewById(R.id.traffice);
        checkTraffice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTraffic(v);
            }
        });

        checkBaiduHeatMap = (CheckBox)findViewById(R.id.baiduHeatMap);
        checkBaiduHeatMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBaiduHeatMap(v);
            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //定位当前位置
                //设置为跟随模式
                mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING, true, mCurrentMarker));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, mCurrentMarker));
                    }
                }, 2000);
            }
        });

        Intent intent = getIntent();
        List<Project> temp = (List<Project>)intent.getSerializableExtra("project");
        initData(temp);


    }

    /**
     * 设置底图显示模式
     *
     * @param view
     */
    public void setMapMode(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.normal:
                if (checked) {
                    mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                }
                break;
            case R.id.statellite:
                if (checked) {
                    mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 设置是否显示交通图
     *
     * @param view
     */
    public void setTraffic(View view) {
        mBaiduMap.setTrafficEnabled(((CheckBox) view).isChecked());
    }

    /**
     * 设置是否显示百度热力图
     *
     * @param view
     */
    public void setBaiduHeatMap(View view) {
        mBaiduMap.setBaiduHeatMapEnabled(((CheckBox) view).isChecked());
    }


    @Override
    public void onResume() {
        mMapView.setVisibility(View.VISIBLE);
        mMapView.onResume();
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        mMapView.setVisibility(View.INVISIBLE);
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    public void onDestroy() {
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                            // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                mBaiduMap.animateMapStatus(u);
            }
        }
    }

    public void onReceivePoi(BDLocation poiLocation) {
    }

    private void initOverlay() {

        mMarker = new Marker[mList.size()];
        for (int i = 0; i < mList.size(); i++) {

            Double la = Double.parseDouble(mList.get(i).getLatitude());
            Double lo = Double.parseDouble(mList.get(i).getLontitude());

            //定义Maker坐标点
            LatLng point = new LatLng(la, lo);
            BitmapDescriptor bitmap;
            //构建Marker图标

            switch (mList.get(i).getMinMenu_id()){
                case 202:
                    bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_202);
                    break;
                case 203:
                    bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_203);
                    break;
                case 204:
                    bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_204);
                    break;
                case 205:
                    bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_205);
                    break;
                case 206:
                    bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_206);
                    break;
                default:
                    bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);
            }

            //构建MarkerOption，用于在地图上添加Marker
            OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
            //在地图上添加Marker，并显示
            mMarker[i] = (Marker) (mBaiduMap.addOverlay(option));
            mLatLngMap.put(mMarker[i], mList.get(i));
        }


        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                final Project project = mLatLngMap.get(marker);
                mBaiduMap.hideInfoWindow();
                Button button = new Button(BaiduMapShowTask.this);
                button.setBackgroundResource(R.drawable.btn_blue);
                button.setTextColor(getResources().getColor(R.color.white));
                InfoWindow.OnInfoWindowClickListener listener = null;
                button.setText(getInfo(project));

                listener = new InfoWindow.OnInfoWindowClickListener() {
                    public void onInfoWindowClick() {

                        new AlertView("提示", "是否开启导航?", "取消", new String[]{"确定"}, null, BaiduMapShowTask.this, AlertView.Style.Alert, new OnItemClickListener() {
                            @Override
                            public void onItemClick(Object o, int poi) {
                                if (poi==0) {
                                    LatLng end = marker.getPosition();
//                                    startRoutePlanDriving(end);
                                    startNavi(end,project);
                                }
                            }
                        }).show();


                    }
                };
                LatLng ll = marker.getPosition();
                mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(button), ll, -200, listener);
                mBaiduMap.showInfoWindow(mInfoWindow);

                return true;
            }
        });


    }

    public void initData(List<Project> projects) {
        if (projects==null) {
            return;
        }

        if (mMarker != null) {
            for (int i = 0; i < mMarker.length; i++) {
                mMarker[i].remove();
            }
        }
        mList.clear();
        mLatLngMap.clear();


        for (int i = 0; i < projects.size(); i++) {
            Project project = projects.get(i);
            if (!AbStrUtil.isEmpty(project.getLatitude())) {
                mList.add(project);
            }
        }

        if (mList.size() > 0) {
            mMarker = new Marker[mList.size()];
            initOverlay();
        }
    }

    private String getInfo(Project project) {
        String s = "", t = "";
        List<Project_menu> project_menus = project.getProject_menu();
        if (project_menus != null) {
            for (int i = 0; i < project_menus.size(); i++) {
                t = t + " " + project_menus.get(i).getMenu_bm();
            }
        }
        s = project.getId() + " " + project.getMc() + "\n" + "施工:"+project.getDw() +"(" + project.getFzrxm()+")"  + "\n" + "任务: " + t;
        return s;
    }


    /**
     * 启动百度地图驾车路线规划
     */
    /**
     * 启动百度地图驾车路线规划
     */
    public void startRoutePlanDriving(LatLng end) {
        AbLogUtil.d("xxxx", String.valueOf(application.mLocation.getLatitude()));
        LatLng pt_start = new LatLng(application.mLocation.getLatitude(), application.mLocation.getLontitude());
        if (application.mLocation.getLatitude()==0) {
            double mLat1 = 34.851482;
            double mLon1 = 119.140468;
            pt_start = new LatLng(mLat1, mLon1);
        }
        LatLng pt_end = end;
        // 构建 route搜索参
        RouteParaOption para = new RouteParaOption()
                .startPoint(pt_start)
                .startName("当前位置")
                .endPoint(pt_end)
                .endName("工程地点")
                .cityName("连云港");

//        RouteParaOption para = new RouteParaOption()
//                .startName("天安门").endName("百度大厦");

//        RouteParaOption para = new RouteParaOption().startPoint(pt_start).endPoint(pt_end);

        try {
            BaiduMapRoutePlan.openBaiduMapDrivingRoute(para, BaiduMapShowTask.this);
        } catch (Exception e) {
            e.printStackTrace();
            showDialog();
        }

    }

    /**
     * 提示未安装百度地图app或app版本过低
     */
    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(BaiduMapShowTask.this);
        builder.setMessage("您尚未安装百度地图app或app版本过低，点击确认安装？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                OpenClientUtil.getLatestBaiduMapApp(BaiduMapShowTask.this);
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    /**
     * 启动百度地图导航(Native)
     */
    public void startNavi(LatLng latLng, Project project) {
        LatLng pt_start = new LatLng(application.mLocation.getLatitude(), application.mLocation.getLontitude());
        if (application.mLocation.getLatitude() == 0) {
            double mLat1 = 34.851482;
            double mLon1 = 119.140468;
            pt_start = new LatLng(mLat1, mLon1);
        }
        LatLng pt2 = latLng;

        // 构建 导航参数
        NaviParaOption para = new NaviParaOption()
                .startPoint(pt_start).endPoint(pt2)
                .startName(application.mLocation.getAddress()).endName(project.getAddress());

        try {
            BaiduMapNavigation.openBaiduMapNavi(para, BaiduMapShowTask.this);
        } catch (BaiduMapAppNotSupportNaviException e) {
            e.printStackTrace();
            showDialog();
        }
    }

}