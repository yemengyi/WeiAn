
package com.gongdian.weian.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.ab.model.AbResult;
import com.ab.soap.AbSoapParams;
import com.ab.soap.AbSoapUtil;
import com.ab.util.AbJsonUtil;
import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.gongdian.weian.R;
import com.gongdian.weian.activity.RegisterActivity;
import com.gongdian.weian.activity.admin.DepartmentActivity;
import com.gongdian.weian.activity.admin.UsersActivity;
import com.gongdian.weian.activity.project.ShowAllProject;
import com.gongdian.weian.activity.project.ShowProjectByMenuActivity;
import com.gongdian.weian.adapter.MenuListAdapter;
import com.gongdian.weian.model.Menu;
import com.gongdian.weian.model.MenuListResult;
import com.gongdian.weian.utils.Constant;
import com.gongdian.weian.utils.MyApplication;
import com.gongdian.weian.utils.WebServiceUntils;

import java.util.ArrayList;
import java.util.List;


public class FragmentMenu extends Fragment {

    private MyApplication application;
    private Activity mActivity = null;
    private List<Menu> mList = null;
    private AbPullToRefreshView mAbPullToRefreshView = null;
    private ListView mListView = null;
    private MenuListAdapter myListViewAdapter = null;
    private int currentPage = 1;
    private int total = 50;
    private int pageSize = 10;
    private AbSoapUtil mAbSoapUtil = null;
    private LinearLayout mLoginLayout = null;
    private ImageView mLoginButton = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mActivity = this.getActivity();
        application = (MyApplication) mActivity.getApplication();

        View view = inflater.inflate(R.layout.pull_to_refresh_list, null);
        //获取ListView对象
        mAbPullToRefreshView = (AbPullToRefreshView) view.findViewById(R.id.mPullRefreshView);
        mListView = (ListView) view.findViewById(R.id.mListView);
        mLoginLayout = (LinearLayout) view.findViewById(R.id.login);
        mLoginButton = (ImageView) view.findViewById(R.id.head);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), RegisterActivity.class);
                getActivity().startActivityForResult(intent, Constant.LoginResultCode);
            }
        });

        //设置监听器
        mAbPullToRefreshView.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {

            @Override
            public void onHeaderRefresh(AbPullToRefreshView view) {
                refreshTask();
            }
        });
        mAbPullToRefreshView.setOnFooterLoadListener(new OnFooterLoadListener() {

            @Override
            public void onFooterLoad(AbPullToRefreshView view) {
                loadMoreTask();

            }
        });

        //设置进度条的样式
        mAbPullToRefreshView.getHeaderView().setHeaderProgressBarDrawable(ContextCompat.getDrawable(mActivity, R.drawable.progress_circular));
        mAbPullToRefreshView.getFooterView().setFooterProgressBarDrawable(ContextCompat.getDrawable(mActivity, R.drawable.progress_circular));

        //ListView数据
        mList = new ArrayList<>();

        //使用自定义的Adapter
        myListViewAdapter = new MenuListAdapter(mActivity, mList);
        mListView.setAdapter(myListViewAdapter);
        //item被点击事件

        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //// TODO: 12/26/15
                Menu menu = (Menu) parent.getItemAtPosition(position);
                if (menu == null || menu.getMenu() == null) {
                    return;
                }
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("menu", menu);
                intent.putExtras(bundle);
                switch (menu.getMenu()) {
                    case Constant.MENU1: //部门
                        intent.setClass(mActivity, DepartmentActivity.class);
                        mActivity.startActivity(intent);
                        break;
                    case Constant.MENU2: //人员
                        intent.setClass(mActivity, UsersActivity.class);
                        mActivity.startActivity(intent);
                        break;
                    case Constant.MENU3: //计划
                    case Constant.MENU4: //勘查
                    case Constant.MENU5: //开工
                    case Constant.MENU6: //到岗到位
                    case Constant.MENU7: //现场督察
                    case Constant.MENU8: //完工
                        intent.setClass(mActivity, ShowProjectByMenuActivity.class);
                        mActivity.startActivityForResult(intent,Constant.ModifyProjectResultCode);
                        break;
                    case Constant.MENU9: //一览
                        intent.setClass(mActivity, ShowAllProject.class);
                        mActivity.startActivityForResult(intent, Constant.ModifyProjectResultCode);
                        break;
                    default:
                        break;
                }

            }
        });


        mAbSoapUtil = AbSoapUtil.getInstance(mActivity);
        mAbSoapUtil.setTimeout(10000);

        refreshTask();
        checkLogin();
        return view;
    }

    private void checkLogin() {
        if (application.isLogin) {
            mAbPullToRefreshView.setVisibility(View.VISIBLE);
            mLoginLayout.setVisibility(View.INVISIBLE);
        } else {
            mAbPullToRefreshView.setVisibility(View.INVISIBLE);
            mLoginLayout.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 下载数据
     */
    public void refreshTask() {
        currentPage = 1;
        // 绑定参数
        AbSoapParams params = new AbSoapParams();
        params.put("uids", application.getUsers().getUids());
        WebServiceUntils.call(mActivity, Constant.GetMenu, params, 10000, false, "", new WebServiceUntils.webServiceCallBack() {
            @Override
            public void callback(Boolean aBoolean, String rtn) {
                mList.clear();
                if (aBoolean) {
                    AbResult result = new AbResult(rtn);
                    if (result.getResultCode() > 0) {
                        //成功
                        MenuListResult menuListResult = AbJsonUtil.fromJson(rtn, MenuListResult.class);
                        List<Menu> menuList = menuListResult.getItems();
                        if (menuList != null && menuList.size() > 0) {
                            mList.addAll(menuList);
                            application.setMenuList(mList);
                            myListViewAdapter.notifyDataSetChanged();
                            menuList.clear();
                        }
                        mAbPullToRefreshView.onHeaderRefreshFinish();
                    }
                }
            }
        });

    }

    public void loadMoreTask() {
        currentPage++;
        AbSoapParams params = new AbSoapParams();
        params.put("uids", application.getUsers().getUids());
        WebServiceUntils.call(mActivity, Constant.GetMenu, params, 10000, false, "", new WebServiceUntils.webServiceCallBack() {
            @Override
            public void callback(Boolean aBoolean, String rtn) {
                mList.clear();
                AbResult result = new AbResult(rtn);
                if (result.getResultCode() > 0) {
                    //成功
                    MenuListResult menuListResult = AbJsonUtil.fromJson(rtn, MenuListResult.class);
                    List<Menu> menuList = menuListResult.getItems();
                    if (menuList != null && menuList.size() > 0) {
                        mList.addAll(menuList);
                        myListViewAdapter.notifyDataSetChanged();
                        menuList.clear();
                    }
                    mAbPullToRefreshView.onFooterLoadFinish();
                }
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
//        if (application.getUserChanged1()) {
            checkLogin();
//            application.setUserChanged1(false);
//        }
    }

    public void initData(List<Menu> projects){
        mList.clear();
        mList.addAll(projects);
        myListViewAdapter.notifyDataSetChanged();
    }


}

