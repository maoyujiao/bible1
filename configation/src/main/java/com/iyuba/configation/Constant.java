package com.iyuba.configation;

import android.os.Environment;

import com.iyuba.base.util.Util;
import com.iyuba.configation.type.IAPP;

public class Constant {

    public static IAPP APP_CONSTANT;

    public final static String PACKAGE_NAME = Util.getAppProcessName(RuntimeManager.getContext());
    public static final String AUTHORITY = ConstantManager.AUTHORITY;
    public static final String PERMISSION_ACCESS = ConstantManager.PERMISSION_ACCESS;
    public static final String PERMISSION_ACCESS_ADVANCED = ConstantManager.PERMISSION_ACCESS_ADVANCED;
    public static final String PERMISSION_SEND_INTENTS = ConstantManager.PERMISSION_SEND_INTENTS;

    public static String ADDAM_APPKEY;

    public static int VIP_STATUS;
    public static String mListen;
    // 程序存放在sd卡上地址
    public static String APP_DATA_PATH;

    public static String APP;
    public static String APPName;// 应用名称

    public static String AppName;// 爱语吧承认的英文缩写
    public static String APPID;// 爱语吧id
    public static String LANGUAGE;

    public static String HEAD;
    public final static String iconAddr = Environment.getExternalStorageDirectory() + "/" + "icon_icon.png";
    public static String SMSAPPID;
    public static String SMSAPPSECRET;


    public static String envir;// 文件夹路径
    public static String appUpdateUrl;// 升级地址
    public static String feedBackUrl;// 反馈


    public final static String basicReadInfoUrl = "http://cms.iyuba.cn/newsApi/getUserInfo.jsp?";
    public final static String recentInfoUrl = "http://cms.iyuba.cn/newsApi/getRecentRV.jsp?";
    public final static String imageUrl = "http://static.iyuba.cn/cms/news/image/";
    public final static String titleIdsUrl = "http://cms.iyuba.cn/cmsApi/getNews.jsp?";
    public final static String proportionUrl = "http://cms.iyuba.cn/newsApi/getCatRate.jsp?";
    public final static String logoUrl = "http://app.iyuba.cn/ios/images/bignews/bignews.png";
    public final static int normalWPM = 600;

    /**
     * 选择填空
     */
    public static final int ABILITY_TESTTYPE_BLANK_CHOSE = 3;

    public final static String APPID_DOWNLOAD = "27";// 托福听力在爱语吧官网下载的id

    public static final String[] WORD_TEST_ARR = {"中英力", "英中力", "音义力"};//英语四级练习题目的类型
    public static final String[] LISTEN_TEST_ARR = {"短篇新闻", "长对话", "听力篇章"};//英语四级练习题目的类型 听力
    public static final String[] READ_TEST_ARR = {"选词填空", "快速阅读", "仔细阅读"};//英语四级听力阅读 练习题目 300道
    public static final String[] WRITE_TEST_ARR = {"写作表达", "写作结构", "写作逻辑", "写作素材"};
    public static final String[] GRAM_TEST_ARR = {"实词", "虚词", "时态", "引语", "句子", "被动语态"};
    public static final String[] SPEAK_TEST_ARR = {"口语发音", "口语表达", "口语逻辑", "口语素材"};

    public final static Boolean DEBUG = true;
    public static int TOTALWORDS = 4500;
    public static String[] ABILITY_TYPE_ARR = {"写作", "单词", "语法", "听力", "口语", "阅读"};//雅思听力测试类型
    /**
     * 上传能力测评使用的url
     */
    public static final String url_updateExamRecord = "http://daxue.iyuba.cn/ecollege/updateExamRecord.jsp";
    public static final int ABILITY_TETYPE_WRITE = 0;
    public static final int ABILITY_TETYPE_WORD = 1;
    public static final int ABILITY_TETYPE_GRAMMAR = 2;
    public static final int ABILITY_TETYPE_LISTEN = 3;
    public static final int ABILITY_TETYPE_SPEAK = 4;
    public static final int ABILITY_TETYPE_READ = 5;
    public static final String ABILITY_WRITE = "X";
    public static final String ABILITY_WORD = "W";
    public static final String ABILITY_GRAMMAR = "G";
    public static final String ABILITY_LISTEN = "L";
    public static final String ABILITY_SPEAK = "S";
    public static final String ABILITY_READ = "R";
    public static String[] WRITE_ABILITY_ARR = {"写作表达", "写作结构", "写作逻辑", "写作素材"};
    public static String[] SPEAK_ABILITY_ARR = {"口语发音", "口语表达", "口语素材", "口语逻辑"};
    public static String[] WORD_ABILITY_ARR;
    public static String[] GRAM_ABILITY_ARR;
    public static String[] LIS_ABILITY_ARR;
    public static String[] READ_ABILITY_ARR;


    public static final String[] WORD_ABILITY_ARR_JP = {"汉字注假名", "假名注汉字", "单词运用", "单词词义", "近义词辨析", "单词写法", "单词读法"};
    public static final String[] GRAM_ABILITY_ARR_JP = {"语法"};
    public static final String[] LIS_ABILITY_ARR_JP = {"听力问答", "概要理解", "要点理解", "听力对话", "主题理解", "综合理解"};
    public static final String[] READ_ABILITY_ARR_JP = {"阅读理解"};

    // mp3文件存放sd卡上文件夹
    public static final String SDCARD_AUDIO_PATH = "audio";

    public static final String SDCARD_ATTACH_DIR = "abilityTest/";
    /**
     * 雅思能力测试 听力url前缀  http://static2.iyuba.cn/IELTS/sounds/16819.mp3
     */
    public static String ABILITY_AUDIO_URL_PRE;
    /**
     * 雅思能力测试 图片attach前缀  http://static2.iyuba.cn/IELTS/images/
     */
    public static String ABILITY_IMAGE_URL_PRE;
    /**
     * 雅思能力测试 附件attach前缀 http://static2.iyuba.cn/IELTS/attach/9081.txt
     */
    public static String ABILITY_ATTACH_URL_PRE;
    /**
     * 单选能力测试
     */
    public static final int ABILITY_TESTTYPE_SINGLE = 1;
    /**
     * 填空题
     */
    public static final int ABILITY_TESTTYPE_BLANK = 2;
    /**
     * 判断题目
     */
    public static final int ABILITY_TESTTYPE_JUDGE = 7;
    /**
     * 多选
     */
    public static final int ABILITY_TESTTYPE_MULTY = 6;
    /**
     * 图片选择
     */
    public static final int ABILITY_TESTTYPE_CHOSE_PIC = 4;
    /**
     * 语音评测
     */
    public static final int ABILITY_TESTTYPE_VOICE = 5;
    /**
     * 单词拼写
     */
    public static final int ABILITY_TESTTYPE_BLANK_WORD = 8;


    public static String getsimRecordAddr() {
        return simRecordAddr;
    }

    private static String recordTag = ".amr";// 录音（跟读

    // 所用）的位置
    public static String getrecordTag() {
        return recordTag;
    }

    // 播放音频时快进，前进的时间
    public static final int SEEK_NEXT = 5000;
    // 播放音频时快退，后退的时间
    public static final int SEEK_PRE = -5000;


    public static String simRecordAddr;

    public static String appfile = "music";// 更新时的前缀名
    public static String append = ".mp3";// 文件append
    public static String videoAddr;// 音频文件存储位置
    public static String microAddr;
    //    public static File picAddr = RuntimeManager.getContext()
//            .getExternalCacheDir();// imagedownloader默认缓存图片位置
//    public static String userAddr = envir + "/user.jpg";// 用户头像，已废弃
    public static String recordAddr;// 跟读音频
    public static String voiceCommentAddr;// 语音评论
    public static String screenShotAddr;// 截图位置
    public static int price = 1200;// 应用内终身价格
    public static int mode;// 播放模式
    public static int type;// 听歌播放模式
    public static int download = 1;// 是否下载
    public static int recordId;// 学习记录篇目id，用于主程序
    public static String recordStart;// 学习记录开始时间，用于主程序

    public static String detailUrl = "http://apps.iyuba.cn/afterclass/getText.jsp?SongId=";// 原文地址
    public static String lrcUrl = "http://apps.iyuba.cn/afterclass/getLyrics.jsp?SongId=";// 原文地址，听歌专用
    public static String searchUrl = "http://apps.iyuba.cn/afterclass/searchApi.jsp?key=";// 查询
    public static String titleUrl = "http://apps.iyuba.cn/afterclass/getSongList.jsp?maxId=";// 新闻列表，主程序用
    public static String vipurl = "http://staticvip.iyuba.cn/sounds/song/";// vip地址
    public static String songurl = "http://static.iyuba.cn/sounds/song/";// 普通地址
    public static String soundurl = "http://static2.iyuba.cn/go/musichigh/";// 1000之前解析地址
    public static String userimage = "http://api.iyuba.com.cn/v2/api.iyuba?protocol=10005&uid=";// 用户头像获取地址
    public static String picSrcAddr;// 音频文件存储位置
    public final static String youdaoAdId = "b932187c3ec9f01c9ef45ad523510edd";

    // 移动课堂所需的相关API
    public final static String MOB_CLASS_DOWNLOAD_PATH = "http://static3.iyuba.cn/resource/";
    public final static String MOB_CLASS_PAYEDRECORD_PATH = "http://app.iyuba.cn/pay/apiGetPayRecord.jsp?";
    public final static String MOB_CLASS_PACK_IMAGE = "http://static3.iyuba.cn/resource/packIcon/";
    public final static String MOB_CLASS_PACK_TYPE_IMAGE = "http://static3.iyuba.cn/resource/nmicon/";
    public final static String MOB_CLASS_COURSE_IMAGE = "http://static3.iyuba.cn/resource/";
    public final static String MicroClassReqPackId = "-2";
    public final static String reqPackDesc = "class.all";
    public final static String PIC_BASE_URL = "http://dev.iyuba.cn/";
    public final static int IO_BUFFER_SIZE = 100 * 1024;
    public final static String MOB_CLASS_COURSE_RESOURCE_DIR = "http://static3.iyuba.cn/resource/package";
    public final static String MOB_CLASS_COURSE_RESOURCE_APPEND = ".zip";
    public final static String MOB_CLASS_PACK_BGPIC = "http://static3.iyuba.cn/resource/categoryIcon/";
    //日志音频地址 ，非VIP
    public final static String AUDIO_ADD = "http://static.iyuba.cn/sounds";
    //日志音频地址 ，VIP
    public final static String AUDIO_VIP_ADD = "http://staticvip.iyuba.cn/sounds";

    //日志视频地址 ，VIP
    public final static String VIDEO_VIP_ADD = "http://staticvip.iyuba.cn/video";
    //日志视频地址 ，非VIP
    public final static String VIDEO_ADD = "http://staticvip.iyuba.cn/video";
    public final static String IMAGE_DOWN_PATH = "http://api.iyuba.com.cn/v2/api.iyuba?protocol=10005&size=big&uid=";
    public final static String PIC_ABLUM__ADD = "http://static1.iyuba.cn/data/attachment/album/";

    public static void reLoadData() {
        envir = ConfigManager.Instance().loadString("envir");// 文件夹路径
        videoAddr = envir + "/audio/";// 音频文件存储位置
        recordAddr = envir + "/sound.amr";// 跟读音频
        voiceCommentAddr = envir + "/voicecomment.amr";// 语音评论
        screenShotAddr = envir + "/screenshot.jpg";// 截图位置
    }
}
