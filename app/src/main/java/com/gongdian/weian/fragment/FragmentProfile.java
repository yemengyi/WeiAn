package com.gongdian.weian.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ab.image.AbImageLoader;
import com.ab.util.AbStrUtil;
import com.gongdian.weian.R;
import com.gongdian.weian.activity.AboutActivity;
import com.gongdian.weian.activity.RegisterActivity;
import com.gongdian.weian.activity.SendMsgActivity;
import com.gongdian.weian.activity.profile.MineActivity;
import com.gongdian.weian.activity.profile.PublicityActivity;
import com.gongdian.weian.activity.project.MineProject;
import com.gongdian.weian.activity.project.ShowSpProject;
import com.gongdian.weian.model.Project;
import com.gongdian.weian.model.Users;
import com.gongdian.weian.utils.AppUtil;
import com.gongdian.weian.utils.Constant;
import com.gongdian.weian.utils.MyApplication;
import com.gongdian.weian.utils.ShareUtil;
import com.makeramen.roundedimageview.RoundedImageView;
import com.pgyersdk.feedback.PgyFeedback;

import java.util.ArrayList;
import java.util.List;


//我
public class FragmentProfile extends Fragment implements OnClickListener {
    private View layout;
    private TextView tvname, tv_accout, tvdebug, txt_photo, project_task,sp_task,txt_dx;
    private RelativeLayout lay03;
    private MyApplication application;
    private Activity mActivity = null;
    private RoundedImageView mImageHead = null;
    private AbImageLoader mAbImageLoader = null;
    private Users user;
    private List<Project> mList = new ArrayList<>();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mActivity = this.getActivity();
        application = (MyApplication) mActivity.getApplication();
        user = ShareUtil.getSharedUser(mActivity);
        mAbImageLoader = new AbImageLoader(mActivity);
        if (layout == null) {
            layout = mActivity.getLayoutInflater().inflate(R.layout.fragment_profile,
                    null);
            initViews();
            setOnListener();
        } else {
            ViewGroup parent = (ViewGroup) layout.getParent();
            if (parent != null) {
                parent.removeView(layout);
            }
        }
        checkLogin();
        return layout;
    }

    private void initViews() {
        mImageHead = (RoundedImageView) layout.findViewById(R.id.head);
        /**100为圆,<100为椭圆*/
        mImageHead.setCornerRadius(100);
        mImageHead.setOval(true);
        tvname = (TextView) layout.findViewById(R.id.tvname);
        tv_accout = (TextView) layout.findViewById(R.id.tvmsg);
        tvdebug = (TextView) layout.findViewById(R.id.debug);
        txt_photo = (TextView) layout.findViewById(R.id.txt_photo);
        project_task = (TextView) layout.findViewById(R.id.project_task);
        sp_task = (TextView) layout.findViewById(R.id.sp_task);
        txt_dx = (TextView) layout.findViewById(R.id.txt_dx);
        lay03 = (RelativeLayout) layout.findViewById(R.id.lay03);
        if (AbStrUtil.isEquals(user.getUrole(), "0")) {
            txt_photo.setVisibility(View.VISIBLE);
            txt_dx.setVisibility(View.VISIBLE);
        } else {
            lay03.setVisibility(View.GONE);
            txt_dx.setVisibility(View.GONE);
        }
    }

    private void setOnListener() {
        layout.findViewById(R.id.view_user).setOnClickListener(this);
        layout.findViewById(R.id.txt_photo).setOnClickListener(this);
        layout.findViewById(R.id.txt_cast).setOnClickListener(this);
        layout.findViewById(R.id.txt_project).setOnClickListener(this);
        layout.findViewById(R.id.text_fk).setOnClickListener(this);
        layout.findViewById(R.id.text_about).setOnClickListener(this);
        layout.findViewById(R.id.head).setOnClickListener(this);
        layout.findViewById(R.id.txt_dx).setOnClickListener(this);
    }

    private void checkLogin() {
        if (application.isLogin) {
            user =  ShareUtil.getSharedUser(mActivity);
            if (AbStrUtil.isEmpty(user.getHeadurl())) {
                mImageHead.setBackgroundResource(R.drawable.icon_login);
//                mImageHead.setImageResource(R.drawable.ic_sex_male);
            } else {
                mAbImageLoader.display(mImageHead, user.getHeadurl());
            }

            tvname.setText(user.getUname());
            tv_accout.setText(user.getPname());
        } else {
            mImageHead.setBackgroundResource(R.drawable.icon_addfriend);
            tvname.setText("未登陆");
            tv_accout.setText("手机号：--");
        }
    }

    public void initData(List<Project> projects) {
        mList.clear();
        if (projects != null && projects.get(0).getId() != null) {
            mList.addAll(projects);
        }
        if (mList.size() > 0) {
            project_task.setVisibility(View.VISIBLE);
            project_task.setText(String.valueOf(mList.size()));
        } else {
            project_task.setVisibility(View.GONE);
        }
    }

    public void initDataSp(String spnumber) {

        if (Integer.parseInt(spnumber)>0) {
            sp_task.setVisibility(View.VISIBLE);
            sp_task.setText(spnumber);
        }else {
            sp_task.setVisibility(View.GONE);
        }

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_dx:
                AppUtil.start_Activity(mActivity, SendMsgActivity.class);
                break;
            case R.id.view_user:
                AppUtil.start_Activity(mActivity, MineActivity.class);
                break;
            case R.id.txt_photo://照片审核
                AppUtil.start_ResultActivity(mActivity, ShowSpProject.class, Constant.SpResultCode);
                break;
            case R.id.txt_cast: //公示
                AppUtil.start_Activity(mActivity,PublicityActivity.class );
                break;
            case R.id.txt_project:
                AppUtil.start_ResultActivity(mActivity, MineProject.class, Constant.MineProjectResultCode);
                break;
            case R.id.text_fk:
                PgyFeedback.getInstance().showDialog(mActivity);
                break;
            case R.id.text_about:
                AppUtil.start_Activity(mActivity, AboutActivity.class);
                break;
            case R.id.head:
                if (application.isLogin) {
                    AppUtil.start_Activity(mActivity, MineActivity.class);
                } else {
                    Intent intent = new Intent();
                    intent.setClass(mActivity, RegisterActivity.class);
                    getActivity().startActivityForResult(intent, Constant.LoginResultCode);
                }
                break;
            default:
                break;
        }
    }



    @Override
    public void onResume() {
        super.onResume();
        if (application.getUserChanged2()) {
        checkLogin();
            application.setUserChanged2(false);
        }
    }
}