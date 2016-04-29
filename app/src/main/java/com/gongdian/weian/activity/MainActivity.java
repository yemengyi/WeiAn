package com.gongdian.weian.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import com.ab.activity.AbActivity;
import com.ab.model.AbResult;
import com.ab.soap.AbSoapParams;
import com.ab.util.AbJsonUtil;
import com.ab.util.AbLogUtil;
import com.ab.util.AbToastUtil;
import com.ab.view.sliding.AbBottomTabView;
import com.ab.view.titlebar.AbTitleBar;
import com.gongdian.weian.R;
import com.gongdian.weian.activity.baidu.BaiduMapFragment;
import com.gongdian.weian.fragment.FragmentMenu;
import com.gongdian.weian.fragment.FragmentProfile;
import com.gongdian.weian.fragment.FragmentWeb;
import com.gongdian.weian.model.Menu;
import com.gongdian.weian.model.MenuListResult;
import com.gongdian.weian.model.Project;
import com.gongdian.weian.model.ProjectListResult;
import com.gongdian.weian.model.Users;
import com.gongdian.weian.permission.CheckPermission;
import com.gongdian.weian.permission.MainActivity2;
import com.gongdian.weian.utils.AppUtil;
import com.gongdian.weian.utils.Constant;
import com.gongdian.weian.utils.MyApplication;
import com.gongdian.weian.utils.WebServiceUntils;
import com.pgyersdk.feedback.PgyFeedbackShakeManager;
import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AbActivity {
    private Boolean isExit = false;
    private MyApplication application;
    private AbTitleBar mAbTitleBar = null;
    private AbBottomTabView mBottomTabView;
    private List<Drawable> tabDrawables = null;
    private Users users;
    private static int UPDATE_REQUEST_CODE = 1;
    List<Fragment> mFragments = null;
    List<String> tabTexts = null;

    BaiduMapFragment page2 = null;
    FragmentProfile page4 = null;
    FragmentMenu page3 = null;
    FragmentWeb page1 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAbContentView(R.layout.activity_main);
        application = (MyApplication) abApplication;
        users = application.getUsers();
        mAbTitleBar = this.getTitleBar();
        mAbTitleBar.setTitleText("欢迎您:" + users.getUname());
        mAbTitleBar.setLogo(R.drawable.title_logo);
        mAbTitleBar.setTitleBarBackground(R.color.colorPrimaryDark);
        mAbTitleBar.setTitleTextMargin(20, 0, 0, 0);
        //mAbTitleBar.setLogoLine(R.drawable.title_line);
        mAbTitleBar.setLogoOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });
//        initTitleRightLayout();

        mBottomTabView = (AbBottomTabView) findViewById(R.id.mBottomTabView);
        //缓存数量
        mBottomTabView.getViewPager().setOffscreenPageLimit(5);
        //禁止滑动
        mBottomTabView.getViewPager().setEnabled(false);
        mAbTitleBar.setVisibility(View.GONE);

        page1 = new FragmentWeb();
        page2 = new BaiduMapFragment();
        page3 = new FragmentMenu();
        page4 = new FragmentProfile();

        mFragments = new ArrayList<>();
        mFragments.add(page1);
        mFragments.add(page2);
        mFragments.add(page3);
        mFragments.add(page4);

        tabTexts = new ArrayList<>();
        tabTexts.add("首页");
        tabTexts.add("工作地图");
        tabTexts.add("功能");
        tabTexts.add("我的");

        //设置样式
        mBottomTabView.setTabTextColor(R.color.gray_dark);
        mBottomTabView.setTabSelectColor(R.color.colorPrimary);
        mBottomTabView.setTabBackgroundResource(R.color.white);
        mBottomTabView.setTabLayoutBackgroundResource(R.color.white);

        //注意图片的顺序
        tabDrawables = new ArrayList<>();
        tabDrawables.add(ContextCompat.getDrawable(this, R.drawable.tabbutton_sessential_icon));
        tabDrawables.add(ContextCompat.getDrawable(this, R.drawable.tabbutton_sessential_selected_icon));
        tabDrawables.add(ContextCompat.getDrawable(this, R.drawable.tabbutton_forum_icon));
        tabDrawables.add(ContextCompat.getDrawable(this, R.drawable.tabbutton_forum_selected_icon));
        tabDrawables.add(ContextCompat.getDrawable(this, R.drawable.tabbutton_wiki_icon));
        tabDrawables.add(ContextCompat.getDrawable(this, R.drawable.tabbutton_wiki_selected_icon));
        tabDrawables.add(ContextCompat.getDrawable(this, R.drawable.tabbutton_me_icon));
        tabDrawables.add(ContextCompat.getDrawable(this, R.drawable.tabbutton_me_selected_icon));
        mBottomTabView.setTabCompoundDrawablesBounds(0, 0, 40, 40);

        mBottomTabView.addItemViews(tabTexts, mFragments, tabDrawables);
        mBottomTabView.setTabPadding(10, 10, 10, 10);
        mBottomTabView.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        mAbTitleBar.setVisibility(View.GONE);
                        break;
                    case 1:
                        loadProject(2);
                        mAbTitleBar.setVisibility(View.GONE);
                        AbLogUtil.d("xxxx", "ccclikckk  222");
                        break;
                    case 2:
                        loadMenu(3);
                        mAbTitleBar.setVisibility(View.VISIBLE);
                        AbLogUtil.d("xxxx", "ccclikckk  444");
                        break;
                    case 3:
                        loadspNumber();
                        loadProject(4);
                        mAbTitleBar.setVisibility(View.VISIBLE);
                        AbLogUtil.d("xxxx", "ccclikckk  444");
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
//本地服务器更新apk
//        if (!AbStrUtil.isEquals(application.getUsers().getVersion(), application.getVersion())) {
//            AbDialogUtil.showAlertDialog(MainActivity.this, R.drawable.ic_help, "更新提示", "发现新版本,是否更新?", new AbAlertDialogFragment.AbDialogOnClickListener() {
//                @Override
//                public void onPositiveClick() {
//                    updateVersion();
//                }
//
//                @Override
//                public void onNegativeClick() {
//
//                }
//            });
//        }
        CheckPermission checkPermission = CheckPermission.newInstance(MainActivity.this);
        if (Build.VERSION.SDK_INT >= 23 ){
            if(!checkPermission.check(android.Manifest.permission.READ_EXTERNAL_STORAGE)||!checkPermission.check(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                AppUtil.start_Activity(this,MainActivity2.class);
            }
        }

        PGYupdate();


    }

    private void initTitleRightLayout() {
        mAbTitleBar.clearRightView();
        View rightViewMore = mInflater.inflate(R.layout.more_btn, null);
        mAbTitleBar.addRightView(rightViewMore);
        Button about = (Button) rightViewMore.findViewById(R.id.moreBtn);

        about.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                //mAbTitleBar.setTitleTextBackgroundResource(R.drawable.drop_down_title_btn);
//                View popView = mInflater.inflate(R.layout.list_pop, null);
//                ListView popListView = (ListView) popView.findViewById(R.id.pop_list);
//                List<AbMenuItem> list = new ArrayList<>();
//                list.add(new AbMenuItem("反馈"));
//                list.add(new AbMenuItem("关于"));
//                list.add(new AbMenuItem("版本更新"));
//                ListPopAdapter mListPopAdapter = new ListPopAdapter(MainActivity.this, list,R.layout.item_list_pop);
//                popListView.setAdapter(mListPopAdapter);
//                mAbTitleBar.setTitleTextDropDown(popView);
                Intent intent = new Intent(MainActivity.this,
                        AboutActivity.class);
                startActivity(intent);


            }

        });

    }


    @Override
    public void onBackPressed() {
        if (isExit.equals(false)) {
            isExit = true;
            AbToastUtil.showToast(MainActivity.this, "再按一次退出程序");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 2000);
        } else {
            super.onBackPressed();
        }

    }


    private void updateVersion() {

        CheckPermission checkPermission = CheckPermission.newInstance(MainActivity.this);
//        if (Build.VERSION.SDK_INT >= 23 &&
//                (!checkPermission.check(android.Manifest.permission.READ_EXTERNAL_STORAGE)
//                        || !checkPermission.check(Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
        if (Build.VERSION.SDK_INT >= 23) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, MainActivity2.class);
            startActivityForResult(intent, UPDATE_REQUEST_CODE);
        } else {
            //下载安装
            update();
        }
    }


    private void update() {
        //本地服务器下载更新
//        Intent intent2 = new Intent(MainActivity.this, UpdataService.class);
//        intent2.setAction("com.gongdian.weian.service.UpdataService");
//        ComponentName componentName = startService(intent2);
    }


    private void PGYupdate() {
        PgyUpdateManager.register(MainActivity.this,
                new UpdateManagerListener() {
                    @Override
                    public void onUpdateAvailable(final String result) {
                        final AppBean appBean = getAppBeanFromString(result);

                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("应用更新")
                                .setMessage("请更新至最新版本")
                                .setNegativeButton(
                                        "确定",
                                        new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int which) {
                                                startDownloadTask(
                                                        MainActivity.this,
                                                        appBean.getDownloadURL());
                                            }
                                        }).show();

                    }

                    @Override
                    public void onNoUpdateAvailable() {
                    }
                });
    }

    @Override
    protected void onPause() {
        PgyFeedbackShakeManager.unregister();
        super.onPause();
    }

    @Override
    protected void onResume() {
        PgyFeedbackShakeManager.register(MainActivity.this, false);
        super.onResume();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        PGYupdate();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.LoginResultCode && application.isLogin) {
            users = application.getUsers();
            mAbTitleBar.setTitleText("欢迎您:" + users.getUname());
        }

        if (requestCode == Constant.MineProjectResultCode) {
            loadProject(4);
        }

        if (requestCode == Constant.SpResultCode) {
            loadspNumber();
        }

        if (requestCode == Constant.ModifyProjectResultCode) {
            page3.refreshTask();
        }

//        if (application.getUserChanged3()) {
//            users = application.getUsers();
//            mAbTitleBar.setTitleText("欢迎您:" + users.getUname());
//            application.setUserChanged3(false);
//        }
    }

    /**
     * 下载数据
     */
    public void loadMenu(final int page) {
        // 绑定参数
        AbSoapParams params = new AbSoapParams();
        params.put("uids", application.getUsers().getUids());
        WebServiceUntils.call(MainActivity.this, Constant.GetMenu, params, 10000, false, "", new WebServiceUntils.webServiceCallBack() {
            @Override
            public void callback(Boolean aBoolean, String rtn) {
                if (aBoolean) {
                    AbResult result = new AbResult(rtn);
                    if (result.getResultCode() > 0) {
                        //成功
                        MenuListResult menuListResult = AbJsonUtil.fromJson(rtn, MenuListResult.class);
                        List<Menu> menuList = menuListResult.getItems();
                        if (menuList != null && menuList.size() > 0) {
                            if (page == 3) {
                                page3.initData(menuList);
                            }
                        }
                    }
                }
            }
        });

    }


    public void loadProject(final int page) {
        AbSoapParams params = new AbSoapParams();
        params.put("user_id", application.getUsers().getId());
        WebServiceUntils.call(MainActivity.this, Constant.GetProject_menu, params, 10000, false, "", new WebServiceUntils.webServiceCallBack() {
            @Override
            public void callback(Boolean aBoolean, String rtn) {
                if (aBoolean) {
                    AbResult result = new AbResult(rtn);
                    if (result.getResultCode() > 0) {
                        //成功le
                        ProjectListResult projectListResult = AbJsonUtil.fromJson(rtn, ProjectListResult.class);
                        List<Project> projectList = projectListResult.getItems();
                        if (projectList != null && projectList.get(0).getId() != null) {
                            if (page == 2) {
                                page2.initData(projectList);
                            }
                        }
                        if (page == 4) {
                            page4.initData(projectList);
                        }

                    }
                }


            }
        });
    }


    public void loadspNumber() {
        AbSoapParams params = new AbSoapParams();
        WebServiceUntils.call(MainActivity.this, Constant.GetProject_sp_num, params, 10000, false, "", new WebServiceUntils.webServiceCallBack() {
            @Override
            public void callback(Boolean aBoolean, String result) {
                if (aBoolean) {
                    page4.initDataSp(result);
                }
            }
        });

    }


}
