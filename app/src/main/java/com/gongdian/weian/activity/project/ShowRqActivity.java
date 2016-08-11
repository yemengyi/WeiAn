package com.gongdian.weian.activity.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.ab.activity.AbActivity;
import com.ab.model.AbResult;
import com.ab.soap.AbSoapParams;
import com.ab.soap.AbSoapUtil;
import com.ab.util.AbJsonUtil;
import com.ab.view.titlebar.AbTitleBar;
import com.gongdian.weian.R;
import com.gongdian.weian.adapter.RqListAdapter;
import com.gongdian.weian.model.Rq;
import com.gongdian.weian.model.RqListResult;
import com.gongdian.weian.model.Users;
import com.gongdian.weian.utils.Constant;
import com.gongdian.weian.utils.MyApplication;
import com.gongdian.weian.utils.WebServiceUntils2;
import com.pgyersdk.feedback.PgyFeedbackShakeManager;

import java.util.ArrayList;
import java.util.List;

public class ShowRqActivity extends AbActivity {

    private MyApplication application;
    private Users user;
    private AbSoapUtil mAbSoapUtil = null;
    private List<Rq> mList=null;
    private ListView mListView;
    private RqListAdapter rqListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAbContentView(R.layout.activity_roll);
        application = (MyApplication) abApplication;

        mAbSoapUtil = AbSoapUtil.getInstance(this);
        mAbSoapUtil.setTimeout(10000);

        user = (Users) getIntent().getSerializableExtra("user");

        AbTitleBar mAbTitleBar = this.getTitleBar();
        mAbTitleBar.setTitleText("工作日期汇总");
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

        //获取ListView对象
        mListView = (ListView) findViewById(R.id.mListView);
        mList = new ArrayList<>();
        //够造SimpleAdapter对象，适配数据
        rqListAdapter = new RqListAdapter(ShowRqActivity.this, mList);
        mListView.setAdapter(rqListAdapter);


        //item被点击事件
        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RqListAdapter.ViewHolder viewHolder = (RqListAdapter.ViewHolder)view.getTag();
                String rq = mList.get(position).getRq();
                Intent intent = new Intent();
                intent.putExtra("rq",rq);
                intent.setClass(ShowRqActivity.this, ShowAllProject.class);
                startActivity(intent);
            }
        });

        getMenu();

    }



    public void getMenu() {
        AbSoapParams params = new AbSoapParams();
        WebServiceUntils2 webServiceUntils2 = WebServiceUntils2.newInstance(ShowRqActivity.this, Constant.GetProject_all_rq, params);
        webServiceUntils2.start(new WebServiceUntils2.webServiceCallBack() {
            @Override
            public void callback(Boolean aBoolean, String rtn) {
                mList.clear();
                if (aBoolean) {
                    AbResult result = new AbResult(rtn);
                    if (result.getResultCode() > 0) {
                        //成功le
                        RqListResult projectListResult = AbJsonUtil.fromJson(rtn, RqListResult.class);
                        List<Rq> projectList = projectListResult.getItems();
                        if (projectList != null ) {
                            mList.addAll(projectList);
                            rqListAdapter.notifyDataSetChanged();
                            projectList.clear();
                        }
//                        mAbPullToRefreshView.onHeaderRefreshFinish();
                    }
                }

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
        PgyFeedbackShakeManager.register(ShowRqActivity.this, false);
        super.onResume();
    }


}


