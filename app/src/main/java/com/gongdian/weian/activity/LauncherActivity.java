package com.gongdian.weian.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ab.activity.AbActivity;
import com.ab.soap.AbSoapParams;
import com.ab.soap.AbSoapUtil;
import com.ab.util.AbAppUtil;
import com.ab.util.AbLogUtil;
import com.ab.util.AbToastUtil;
import com.ab.util.AbViewUtil;
import com.gongdian.weian.R;
import com.gongdian.weian.model.Users;
import com.gongdian.weian.parse.UsersPrase;
import com.gongdian.weian.utils.AppUtil;
import com.gongdian.weian.utils.Constant;
import com.gongdian.weian.utils.MyApplication;
import com.gongdian.weian.utils.WebServiceUntils;

public class LauncherActivity extends AbActivity {
    private LinearLayout launcherView;
    private Animation mFadeIn;
    private Animation mFadeInScale;
    private AbSoapUtil mAbSoapUtil = null;
    private MyApplication application;
    private String android_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        launcherView = (LinearLayout) this.findViewById(R.id.launcherView);
        AbViewUtil.scaleContentView(launcherView);
        application = (MyApplication)abApplication;
        TextView version = (TextView) findViewById(R.id.version);

        PackageInfo packageInfo = AbAppUtil.getPackageInfo(this);
        version.setText("V" + packageInfo.versionName);
        application.setVersion(packageInfo.versionName);
        mAbSoapUtil = AbSoapUtil.getInstance(this);
        mAbSoapUtil.setTimeout(15000);
        android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        AbLogUtil.d("xxxx",android_id);



        init();
        setListener();
    }

    private void setListener() {

        mFadeIn.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                launcherView.startAnimation(mFadeInScale);
            }
        });

        mFadeInScale.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (!AppUtil.isConn(getApplicationContext())) {
                    AppUtil.setNetworkMethod(LauncherActivity.this);
                } else {
                    gotoMain();
                }

            }

        });

    }

    private void init() {
        initAnim();
        launcherView.startAnimation(mFadeIn);
    }

    private void initAnim() {
        mFadeIn = AnimationUtils.loadAnimation(LauncherActivity.this,
                R.anim.welcome_fade_in);
        mFadeIn.setDuration(800);
        mFadeIn.setFillAfter(true);

        mFadeInScale = AnimationUtils.loadAnimation(LauncherActivity.this,
                R.anim.welcome_fade_in_scale);
        mFadeInScale.setDuration(2000);
        mFadeInScale.setFillAfter(true);
    }

//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        gotoMain();
//    }

    public void gotoMain() {


        if (!AppUtil.isConn(getApplicationContext())) {
            AbToastUtil.showToast(LauncherActivity.this, "检测网络不通,程序已关闭！");
            finish();
        } else {
            //获取IMEI
//            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//            String imei = telephonyManager.getDeviceId();

//             AbToastUtil.showToast(LauncherActivity.this,android_id);
            AbLogUtil.d("xxxxxxxxxx", android_id);
            if (Constant.DEBUG) {
                Users users = new Users();
                users.setId("100");
                users.setUname("钱峰测试");
                users.setVersion("1.0");
                users.setPname("安全监察质量部");
                users.setPcode("13951499090");
                users.setPid("1001");
                users.setUids("320721198205050011");
                users.setUrole("0");
                application.setUsers(users);
                application.setIsLogin(true);
                application.setToken(android_id+Constant.APPVERSION);
                Intent intent = new Intent();
                intent.setClass(LauncherActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }else {
                GetIMIE(android_id);
            }

        }
    }

    public void GetIMIE(final String imei) {
        AbSoapParams params = new AbSoapParams();
        params.put("imei", imei);
        WebServiceUntils.call(LauncherActivity.this, Constant.CheckIMEI, params, 10000, false, "", new WebServiceUntils.webServiceCallBack() {
            @Override
            public void callback(Boolean aBoolean, String result) {
                if (result.equals("0")||!aBoolean) {
                    Intent intent = new Intent();
                    Users users = new Users();
                    users.setUname("未登陆");
                    users.setVersion("1.0");
                    users.setPname("未分配");
                    users.setPcode("--");
                    users.setPid("--");
                    users.setUids("--");
                    users.setUrole("0");
                    application.setUsers(users);
                    application.setIsLogin(false);
                    intent.setClass(LauncherActivity.this, RegisterActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Users users = UsersPrase.parser(result).get(0);
                    AbToastUtil.showToast(LauncherActivity.this, "欢迎您" + users.getUname());
                    application.setUsers(users);
                    application.setIsLogin(true);
                    application.setToken(android_id + Constant.APPVERSION);
                    AppUtil.start_Activity(LauncherActivity.this, MainActivity.class);
                    finish();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0) {
            if (!AppUtil.isConn(getApplicationContext())) {
                AppUtil.setNetworkMethod(LauncherActivity.this);
            } else {
                gotoMain();
            }
        }
    }


}
