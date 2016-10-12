package com.gongdian.weian.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.ab.util.AbStrUtil;
import com.gongdian.weian.R;
import com.gongdian.weian.activity.project.AddProjectPhotoActivity;
import com.gongdian.weian.model.Project;
import com.gongdian.weian.model.Project_menu;
import com.gongdian.weian.utils.Constant;

import java.util.List;


public class MineProjectAdapter extends BaseAdapter {

    private Context mContext;
    //列表展现的数据
    private List<Project> mList;
    private Activity mActivity;

    /**
     * 构造方法
     *
     * @param context
     * @param list    列表展现的数据
     */
    public MineProjectAdapter(Context context,Activity activity, List<Project> list) {
        mContext = context;
        mList = list;
        mActivity = activity;
    }

    @Override
    public int getCount() {
        if (mList == null) {
            return 0;
        }
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            //使用自定义的list_items作为Layout
            convertView = LayoutInflater.from(mContext).inflate(R.layout.project_mine_item_list, parent, false);
            //使用减少findView的次数
            holder = new ViewHolder();
            holder.pro_id = ((TextView) convertView.findViewById(R.id.pro_id));
            holder.nr = ((TextView) convertView.findViewById(R.id.nr));
            holder.mc = ((TextView) convertView.findViewById(R.id.mc));
            holder.kssj = ((TextView) convertView.findViewById(R.id.kssj));
            holder.dz = ((TextView) convertView.findViewById(R.id.dz));
            holder.createuername = ((TextView) convertView.findViewById(R.id.createuername));
            holder.dw = ((TextView) convertView.findViewById(R.id.dw));
            holder.ry = ((TextView) convertView.findViewById(R.id.ry));
            holder.xk = ((TextView) convertView.findViewById(R.id.xk));
            holder.xznr = ((TextView) convertView.findViewById(R.id.xznr));
            holder.btn_202 = ((Button) convertView.findViewById(R.id.btn202));
            holder.btn_203 = ((Button) convertView.findViewById(R.id.btn203));
            holder.btn_204 = ((Button) convertView.findViewById(R.id.btn204));
            holder.btn_205 = ((Button) convertView.findViewById(R.id.btn205));
            holder.btn_206 = ((Button) convertView.findViewById(R.id.btn206));
            //设置标记
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //监听


        //获取该行数据
        final Project mProject = mList.get(position);
        final List<Project_menu> mProject_menu= mProject.getProject_menu();
        String menu_id;

        holder.btn_202.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPhoto("202", Constant.MENU4,mProject);
            }
        });
        holder.btn_203.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPhoto("203", Constant.MENU5,mProject);
            }
        });
        holder.btn_204.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPhoto("204", Constant.MENU6,mProject);
            }
        });
        holder.btn_205.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPhoto("205", Constant.MENU7,mProject);
            }
        });
        holder.btn_206.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPhoto("206", Constant.MENU8,mProject);
            }
        });


        holder.pro_id.setText(mProject.getId() + " (" + mProject.getYf() + ")");
        holder.nr.setText(mProject.getNr());
        holder.mc.setText(mProject.getMc());
        String jssj = mProject.getJssj();
        String kssj = mProject.getKssj();
        String s = kssj.substring(kssj.indexOf("-") + 1) + " ~ " + jssj.substring(jssj.indexOf("-") + 1);
        holder.kssj.setText(s);
        holder.dz.setText(mProject.getDz());
        String cn = mProject.getCreateusername() + " " +  mProject.getCreatetime();
        holder.createuername.setText(cn);
        holder.dw.setText(mProject.getDw());
        holder.ry.setText(mProject.getRy());
        holder.xk.setText(mProject.getXkdw());
        holder.xznr.setText(mProject.getXznr());
        if (!AbStrUtil.isEmpty(mProject.getFzrxm())) {
            holder.dw.setText(mProject.getDw() + " (" + mProject.getFzrxm() + ")");
        } else {
            holder.dw.setText(mProject.getDw());
        }
        holder.btn_202.setVisibility(View.GONE);
        holder.btn_203.setVisibility(View.GONE);
        holder.btn_204.setVisibility(View.GONE);
        holder.btn_205.setVisibility(View.GONE);
        holder.btn_206.setVisibility(View.GONE);

        if(mProject_menu!=null) {
            for (int i = 0; i < mProject_menu.size(); i++) {
                menu_id = mProject_menu.get(i).getMenu_id();
                switch (menu_id) {
                    case "202":
                        holder.btn_202.setVisibility(View.VISIBLE);
                        break;
                    case "203":
                        holder.btn_203.setVisibility(View.VISIBLE);
                        break;
                    case "204":
                        holder.btn_204.setVisibility(View.VISIBLE);
                        break;
                    case "205":
                        holder.btn_205.setVisibility(View.VISIBLE);
                        break;
                    case "206":
                        holder.btn_206.setVisibility(View.VISIBLE);
                        break;
                }
            }
        }

        return convertView;
    }


    /**
     * ViewHolder类
     */
    static class ViewHolder {
        TextView pro_id;
        TextView nr;
        TextView mc;
        TextView kssj;
        TextView dz;
        TextView createuername;
        TextView dw;
        TextView ry;
        TextView xk;
        TextView xznr;
        Button btn_202;
        Button btn_203;
        Button btn_204;
        Button btn_205;
        Button btn_206;
    }

    private void addPhoto(String menu_id,String menu,Project project){
        Intent intent = new Intent();
        intent.setClass(mContext, AddProjectPhotoActivity.class);
        intent.putExtra("menu_id", menu_id);
        intent.putExtra("menu", menu);
        intent.putExtra("project", project);
        mActivity.startActivityForResult(intent, Constant.ModifyProjectResultCode);
    }
}
