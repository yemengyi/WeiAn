package com.gongdian.weian.activity.project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.ab.activity.AbActivity;
import com.ab.model.AbResult;
import com.ab.soap.AbSoapParams;
import com.ab.util.AbJsonUtil;
import com.ab.util.AbStrUtil;
import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.titlebar.AbTitleBar;
import com.gongdian.weian.R;
import com.gongdian.weian.activity.baidu.BaiduMapShowTask;
import com.gongdian.weian.adapter.MineProjectAdapter;
import com.gongdian.weian.model.Project;
import com.gongdian.weian.model.ProjectListResult;
import com.gongdian.weian.model.Project_dw;
import com.gongdian.weian.model.Users;
import com.gongdian.weian.utils.Constant;
import com.gongdian.weian.utils.MyApplication;
import com.gongdian.weian.utils.WebServiceUntils2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MineProject extends AbActivity {

    private MyApplication application;
    private AbTitleBar mAbTitleBar = null;
    private Users users;
    private List<Project> mList = null;
    private AbPullToRefreshView mAbPullToRefreshView = null;
    private ListView mListView = null;
    private MineProjectAdapter myListViewAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAbContentView(R.layout.activity_mine_project);
        application = (MyApplication) abApplication;
        application.setIsAddProject(false);
        users = application.getUsers();

        mAbTitleBar = this.getTitleBar();
        mAbTitleBar.setTitleText("我的工程");
        mAbTitleBar.setLogo(R.drawable.title_back_n);
        mAbTitleBar.setTitleBarBackground(R.color.colorPrimaryDark);
        mAbTitleBar.setTitleTextMargin(20, 0, 0, 0);
        mAbTitleBar.setLogoOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });
        initTitleRightLayout();


        mAbPullToRefreshView = (AbPullToRefreshView) findViewById(R.id.mPullRefreshView);
        mListView = (ListView) findViewById(R.id.mListView);

        //设置监听器
        mAbPullToRefreshView.setOnHeaderRefreshListener(new AbPullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(AbPullToRefreshView view) {
                refreshTask();
            }
        });
        mAbPullToRefreshView.setOnFooterLoadListener(new AbPullToRefreshView.OnFooterLoadListener() {
            @Override
            public void onFooterLoad(AbPullToRefreshView view) {
                loadMoreTask();
            }
        });

        //设置进度条的样式
        mAbPullToRefreshView.getHeaderView().setHeaderProgressBarDrawable(ContextCompat.getDrawable(this, R.drawable.progress_circular));
        mAbPullToRefreshView.getFooterView().setFooterProgressBarDrawable(ContextCompat.getDrawable(this, R.drawable.progress_circular));

        //ListView数据
        mList = new ArrayList<>();

        //使用自定义的Adapter
        myListViewAdapter = new MineProjectAdapter(this, MineProject.this,mList);
        mListView.setAdapter(myListViewAdapter);
        //item被点击事件

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemClick(position);
            }
        });

        //第一次下载数据
        refreshTask();
    }

    private void initTitleRightLayout(){
            mAbTitleBar.clearRightView();
            View rightViewMore = mInflater.inflate(R.layout.title_map_btn, null);
            mAbTitleBar.addRightView(rightViewMore);
            Button about = (Button) rightViewMore.findViewById(R.id.mapBtn);
            about.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(MineProject.this, BaiduMapShowTask.class);
                    intent.putExtra("project", (Serializable) mList);
                    startActivity(intent);
                }

            });

    }

    /**
     * 下载数据
     */
    public void refreshTask() {
        AbSoapParams params = new AbSoapParams();
        params.put("user_id", application.getUsers().getId());
        WebServiceUntils2 webServiceUntils2 = WebServiceUntils2.newInstance(MineProject.this, Constant.GetProject_menu, params);
        webServiceUntils2.start(new WebServiceUntils2.webServiceCallBack() {
            @Override
            public void callback(Boolean aBoolean, String rtn) {
                mList.clear();
                if (aBoolean) {
                    AbResult result = new AbResult(rtn);
                    if (result.getResultCode() > 0) {
                        //成功le
                        ProjectListResult projectListResult = AbJsonUtil.fromJson(rtn, ProjectListResult.class);
                        List<Project> projectList = projectListResult.getItems();
                        if (projectList != null && projectList.get(0).getId() != null) {
                            mList.addAll(projectList);
                            myListViewAdapter.notifyDataSetChanged();
                            projectList.clear();
                        }
                        mAbPullToRefreshView.onHeaderRefreshFinish();
                    }
                }

            }
        });

    }

    public void loadMoreTask() {
        AbSoapParams params = new AbSoapParams();
        params.put("user_id", application.getUsers().getId());
        WebServiceUntils2 webServiceUntils2 = WebServiceUntils2.newInstance(MineProject.this, Constant.GetProject_menu, params);
        webServiceUntils2.start(new WebServiceUntils2.webServiceCallBack() {
            @Override
            public void callback(Boolean aBoolean, String rtn) {
                mList.clear();
                if (aBoolean) {
                    AbResult result = new AbResult(rtn);
                    if (result.getResultCode() > 0) {
                        //成功
                        ProjectListResult projectListResult = AbJsonUtil.fromJson(rtn, ProjectListResult.class);
                        List<Project> projectList = projectListResult.getItems();
                        if (projectList != null && projectList.get(0).getId() != null) {
                            mList.addAll(projectList);
                            myListViewAdapter.notifyDataSetChanged();
                            projectList.clear();
                        }
                        mAbPullToRefreshView.onFooterLoadFinish();
                    }
                }

            }
        });

    }


    private void itemClick(int position) {
    }

    private Boolean checkProject(Project project){
        List<Project_dw> project_dws = project.getProject_dw();
        for (int i= 0;i<project_dws.size();i++) {
            if(AbStrUtil.isEquals(project_dws.get(i).getFlag(),"3")||AbStrUtil.isEquals(project_dws.get(i).getFlag(),"4")){
                return false;
            }
        }
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constant.ModifyProjectResultCode) {
            refreshTask();
        }
    }


}
