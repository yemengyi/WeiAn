package com.gongdian.weian.utils;


public class Constant {
    public static final boolean DEBUG = false;

    public static final String APPVERSION = "101";
    public static final String VERSION_ERROR = "versionerror";
    public static final String IMEI_ERROR = "imeierror";

    //sqllite版本号,如果本地DB版本号与当前不同则删除本地db文件
    public static final int DBVERSION = 9;
    public static final int LoginResultCode = 0;
    public static final int AddProjectResultCode = 1;
    public static final int MineProjectResultCode = 2;
    public static final int ModifyProjectResultCode = 3;
    public static final int SpResultCode = 1004;

    public static final int imageCompress = 60;


    // 连接超时
    public static final int timeOut = 12000;
    // 建立连接
    public static final int connectOut = 12000;
    // 获取数据
    public static final int getOut = 60000;


    //1表示已下载完成
    public static final int downloadComplete = 1;
    //1表示未开始下载
    public static final int undownLoad = 0;
    //2表示已开始下载
    public static final int downInProgress = 2;
    //3表示下载暂停
    public static final int downLoadPause = 3;

    public static final String BASEURL = "http://m.wweian.con/";

    public static final String MSG_ALERT = "ALERT";
    public static final String MSG_CONFIRM = "CONFIRM";
    public static final String MSG_CUSTOM = "CUSTOM";
    public static final String MSG_STICKY = "STICKY";
    public static final String MSG_INFO = "INFO";

    //应用的key
    //1512528
    public final static String APPID = "1512528";

    //百度地图 jfa97P4HIhjxrAgfUdq1NoKC
    public final static String APIKEY = "jfa97P4HIhjxrAgfUdq1NoKC";

    //消息服务

    public final static String SOAPURL = "http://218.92.49.18:88/pb_webservice/n_webservice.asmx";

    public final static String SOAPNSP="http://gyyb.com/";

    public final static String APKURL2 = "http://218.92.49.18:88/app-debug-unaligned.apk";

    public final static String APKNAME2 = "app-debug-unaligned.apk";

    public final static String APKURL = "http://218.92.49.18:88/com.gongdian.weian-release-v1.0-1.apk";

    public final static String APKNAME = "com.gongdian.weian-release-v1.0-1.apk";

    public final static String UPLOADURL = "http://218.92.49.18:8080/upload/upload.do";

    public final static String UPLOADURLHEAD = "http://218.92.49.18:8080/";

    public final static String APPURL = "https://www.pgyer.com/gqYD";

    public final static String MENU1 ="部门管理";
    public final static String MENU2 ="人员管理";
    public final static String MENU3 ="生产计划";
    public final static String MENU4 ="现场勘查";
    public final static String MENU5 ="工作许可";
    public final static String MENU6 ="到岗到位";
    public final static String MENU7 ="现场督察";
    public final static String MENU8 ="工作终结";

    public final static String MENU9 ="工程一览";
    public final static String MENU10 ="短信通知";

    public final static String CheckIMEI = "uf_check_imei";
    public final static String Login_out = "uf_login_out";
    public final static String GetDepartMent = "get_department_json";
    public final static String getDepartmentFilter = "get_department_filter";
    public final static String GetUsers = "get_users_json";
    public final static String GetUsers2 = "get_users2";
    public final static String GET_users_by_menu = "get_users_by_menu";
    public final static String GET_users_by_pid = "get_user_by_pid_json";
    public final static String GET_department_users = "get_department_users_json";
    public final static String GetMenu = "get_menu_json";
//    public final static String GetMenu2 = "get_menu_json2";
    public final static String GetMenu3 = "get_menu_json3";
    public final static String GetMenuAll = "get_menu_all_json";
    public final static String Modify_department = "modify_department";
    public final static String Modify_Users = "modify_users";
    public final static String Modify_Users_Head = "modify_users_head";
    public final static String Modify_Rolls = "modify_rolls";
    public final static String Modify_Project = "modify_project_json";
    public final static String Modify_Project_jd = "modify_project_jd_json";

    public final static String GetProject = "get_project_json";
    public final static String GetProject_menu = "get_project_menu_json";
    public final static String GetProject_all = "get_project_all_json";
    public final static String GetProject_all_new = "get_project_all_new_json";
    public final static String GetProject_all_rq = "get_project_all_rq_json";
    public final static String GetProject_sp_num = "get_sp_number";
    public final static String GetProject_sp = "get_project_sp_json";
    public final static String Commit_sp = "uf_commit_sp";
    public final static String Send_msgs = "uf_send_msgs";
    public final static String Send_msgs_tz = "uf_send_msg_tz";
    public final static String Get_dxtz = "get_project_dxtz_json";

    public final static String Cf1 = "get_cf1_json";
    public final static String Cf2 = "get_cf2_json";



    public final static String FLAG1 = "1"; //生成计划
    public final static String FLAG2 = "2"; //勘查
    public final static String FLAG3 = "3"; //开工
    public final static String FLAG4 = "4";  //完工
    public final static String FLAG5 = "5";  //到岗
    public final static String FLAG6 = "6";  //督察



}

