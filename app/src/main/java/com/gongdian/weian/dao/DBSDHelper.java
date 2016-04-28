package com.gongdian.weian.dao;

import android.content.Context;

import com.ab.db.orm.AbSDDBHelper;
import com.ab.util.AbFileUtil;
import com.gongdian.weian.model.Project;
import com.gongdian.weian.model.Project_dw;
import com.gongdian.weian.model.Project_jd;
import com.gongdian.weian.model.Project_photo;
import com.gongdian.weian.model.Project_ry;
import com.gongdian.weian.utils.Constant;

public class DBSDHelper extends AbSDDBHelper {
	// 数据库名
	private static final String DBNAME = "weian.db";
    
    // 当前数据库的版本
	private static final int DBVERSION = Constant.DBVERSION;

	// 要初始化的表
	private static final Class<?>[] clazz = { Project.class,Project_dw.class,Project_ry.class,Project_photo.class, Project_jd.class};

	public DBSDHelper(Context context) {
		super(context, AbFileUtil.getDbDownloadDir(context), DBNAME, null, DBVERSION, clazz);
	}

}



