///*
// * 文件名
// * 包含类名列表
// * 版本信息，版本号
// * 创建日期
// * 版权声明
// */
//package com.iyuba.core.teacher.activity;
//import java.util.ArrayList;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.util.Log;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.AdapterView.OnItemLongClickListener;
//import android.widget.ImageView;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.iyuba.core.activity.CrashApplication;
//import com.iyuba.core.activity.Login;
//import com.iyuba.core.listener.ProtocolResponse;
//import com.iyuba.core.listener.ResultIntCallBack;
//import com.iyuba.core.manager.AccountManager;
//import com.iyuba.core.manager.QuestionManager;
//import com.iyuba.core.protocol.BaseHttpResponse;
//import com.iyuba.core.teacher.adapter.QuestionListAdapter;
//import com.iyuba.core.teacher.protocol.DeleteAnswerQuesRequest;
//import com.iyuba.core.teacher.protocol.DeleteAnswerQuesResponse;
//import com.iyuba.core.teacher.protocol.GetQuesListRequest;
//import com.iyuba.core.teacher.protocol.GetQuesListResponse;
//import com.iyuba.core.teacher.sqlite.mode.Question;
//import com.iyuba.core.util.ExeProtocol;
//import com.iyuba.core.util.ExeRefreshTime;
//import com.iyuba.core.widget.ContextMenu;
//import com.iyuba.core.widget.dialog.CustomToast;
//import com.iyuba.core.widget.pulltorefresh.PullToRefreshView;
//import com.iyuba.core.widget.pulltorefresh.PullToRefreshView.OnFooterRefreshListener;
//import com.iyuba.core.widget.pulltorefresh.PullToRefreshView.OnHeaderRefreshListener;
//import com.iyuba.biblelib.R;
//
///**
// * 类名
// *
// * @author 作者 <br/>
// *         实现的主要功能。 创建日期 修改者，修改日期，修改内容。
// */
//public class HomeForAt extends Activity implements
//		OnHeaderRefreshListener, OnFooterRefreshListener {
//	private Context mContext;
//	private TextView tvSelectQuesType,tvTitle;
//	private ImageView btnEditQues;
//	private PullToRefreshView refreshView;// 刷新列表
//	private ListView quesListview;
//	private QuestionListAdapter quesAdapter;
////	private View root;
//	private ArrayList<Question> quesList = new ArrayList<Question>();
//	public int pageNum=1;
//	boolean isLast=false;
//	ContextMenu contextMenu;
//
//	private static final String[] question_app_type_arr =
//			{"全部","VOA","BBC","听歌","CET4","CET6",
//			 "托福","N1","N2","微课","雅思","初中",
//			 "高中","考研","新概念","走遍美国"};
//
//	private static final String[] question_ability_type_arr =
//			{"全部","口语","听力","阅读","写作","翻译",
//		 	 "单词","语法","其他"};
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//
//		CrashApplication.getInstance().addActivity(this);
//	    setContentView(R.layout.lib_ques_list);
//
//		mContext = this;
//		initWidget();
//
//	}
//
//	@Override
//	public void onResume() {
//
//		super.onResume();
//	}
//
//
//
//	@Override
//	public void startActivityForResult(Intent intent, int requestCode) {
//
//		super.startActivityForResult(intent, requestCode);
//
//	}
//
//	@Override
//	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//		super.onActivityResult(requestCode, resultCode, data);
//		getHeaderData();
//	}
//
//
//
//  // 顶部今日头条事件监听器
//  private OnClickListener SelectQuesOnClickListener = new OnClickListener() {
//		@Override
//		public void onClick(View v) {
//			// 跳转到筛选Activity
//			Intent intent=new Intent();
//			intent.setClass(mContext, SelectQuestionType.class);
//			startActivityForResult(intent, 0);
//		}
//	};
//
//	public void initWidget() {
//		tvSelectQuesType = (TextView) findViewById(R.id.tv_select_ques_type);
//		tvTitle = (TextView) findViewById(R.id.tv_teacher_title);
//		btnEditQues = (ImageView) findViewById(R.id.tinsert);
//		contextMenu = (ContextMenu)  findViewById(R.id.context_menu);
//
//		quesListview = (ListView) findViewById(R.id.list);
//		refreshView = (PullToRefreshView) findViewById(R.id.ll_queslist_content);
//		refreshView.setOnHeaderRefreshListener(this);
//		refreshView.setOnFooterRefreshListener(this);
//
//		tvSelectQuesType.setOnClickListener(SelectQuesOnClickListener);
//
//		btnEditQues.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				//如果没登录则跳转登录
//				if(!AccountManager.Instance(mContext).checkUserLogin()){
//
//					Intent intent = new Intent();
//					intent.setClass(mContext, Login.class);
//					startActivity(intent);
//					return;
//				}
//
//
//				Intent intent = new Intent();
//				intent.setClass(mContext, QuezActivity.class);
//				startActivity(intent);
//			}
//		});
//
//		quesAdapter = new QuestionListAdapter(mContext);
//
//		quesAdapter.clearList();
//		quesListview.setOnItemLongClickListener(new OnItemLongClickListener() {
//
//			@Override
//			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
//					int arg2, long arg3) {
//
//				QuestionManager.getInstance().question = quesList.get(arg2);
//
//				if(quesList.get(arg2).uid.equals(AccountManager.Instance(mContext).userId)){
//
//					final int theqid=quesList.get(arg2).qid;
//					final int num=arg2;
//					contextMenu.setText(mContext.getResources().getStringArray(
//							R.array.choose_delete));
//					contextMenu.setCallback(new  ResultIntCallBack() {
//
//						@Override
//						public void setResult(int result) {
//
//							switch (result) {
//							case 0:
//								delAlertDialog(theqid+"",num);
//								break;
//							case 1:
//								Intent intent = new Intent();
//								intent.setClass(mContext, QuesDetailActivity.class);
//								intent.putExtra("qid",theqid+"");
//								startActivity(intent);
//								break;
//							default:
//								break;
//							}
//						}
//					});
//					contextMenu.show();
//					return true;
//				}else{
//					return false;
//
//				}
//
//			}
//		});
//
//		if(quesAdapter != null){
//			quesListview.setAdapter(quesAdapter);
//		}
//
//
//		quesListview.setOnItemClickListener(new OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3) {
//				QuestionManager.getInstance().question = quesList.get(arg2);
//				Intent intent = new Intent();
//				intent.setClass(mContext, QuesDetailActivity.class);
//				intent.putExtra("qid",quesList.get(arg2).qid+"");
//				startActivity(intent);
//			}
//		});
//
//		getHeaderData();
//	}
//
//	private void delAlertDialog(final String id,final int num) {
//		AlertDialog alert = new AlertDialog.Builder(mContext).create();
//		alert.setTitle(R.string.alert_title);
//		alert.setIcon(android.R.drawable.ic_dialog_alert);
//		alert.setMessage(mContext.getString(R.string.alert_delete));
//		alert.setButton(AlertDialog.BUTTON_POSITIVE,
//				getResources().getString(R.string.alert_btn_ok),
//				new DialogInterface.OnClickListener() {
//					@Override
//					public void onClick(final DialogInterface dialog, int which) {
//
//						ExeProtocol.exe(new DeleteAnswerQuesRequest("0", id, AccountManager.Instance(mContext).userId), new ProtocolResponse() {
//
//							@Override
//							public void finish(BaseHttpResponse bhr) {
//
//								DeleteAnswerQuesResponse tr = (DeleteAnswerQuesResponse) bhr;
//								if (tr.result.equals("1")) {
//									quesList.remove(num);
//
////									handler.sendEmptyMessage(1);
//									binderAdapterDataHandler.post(binderAdapterDataRunnable);
//									handler.sendEmptyMessage(8);
//
//								} else {
//									quesList.remove(num);
//
////									handler.sendEmptyMessage(1);
//									binderAdapterDataHandler.post(binderAdapterDataRunnable);
//									handler.sendEmptyMessage(8);
//								}
//							}
//
//							@Override
//							public void error() {
//							}
//						});
//
//
//					}
//				});
//		alert.setButton(AlertDialog.BUTTON_NEGATIVE,
//				getResources().getString(R.string.alert_btn_cancel),
//				new DialogInterface.OnClickListener() {
//					@Override
//					public void onClick(final DialogInterface dialog, int which) {
//					}
//				});
//		alert.show();
//	}
//
//	Handler handler = new Handler() {
//		@Override
//		public void handleMessage(Message msg) {
//
//			super.handleMessage(msg);
//			switch (msg.what) {
//			case 0:
//				break;
//			case 1:
//				quesAdapter.notifyDataSetChanged();
//				break;
//			case 2:
////				wettingDialog.show();
//				break;
//			case 3:
////				wettingDialog.dismiss();
//				break;
//			case 4:
//				refreshView.onHeaderRefreshComplete();
//				break;
//			case 5:
//				refreshView.onFooterRefreshComplete();
//				break;
//			case 6:
//				CustomToast.showToast(mContext, R.string.no_data);
//				break;
//			case 7:
//				CustomToast.showToast(mContext, "已是最后一页");
//				break;
//			case 8:
//				CustomToast.showToast(mContext, "删除成功!");
//				break;
//			}
//		}
//	};
//
//	private Handler binderAdapterDataHandler = new Handler();
//	private Runnable binderAdapterDataRunnable = new Runnable() {
//        public void run() {
//        	quesAdapter.clearList();
//        	quesAdapter.addList(quesList);
//        	quesAdapter.notifyDataSetChanged();
//        }
//    };
//
//	public void getHeaderData() {
//
//		handler.sendEmptyMessage(2);
//		ExeProtocol.exe(new GetQuesListRequest(1), new ProtocolResponse() {
//
//			@Override
//			public void finish(BaseHttpResponse bhr) {
//
//				GetQuesListResponse tr = (GetQuesListResponse) bhr;
//				if (tr.list != null && tr.list.size() != 0) {
//
//					quesList.clear();
//					quesList.addAll(tr.list);
//					binderAdapterDataHandler.post(binderAdapterDataRunnable);
//
//					pageNum=2;
////					handler.sendEmptyMessage(1);
//					handler.sendEmptyMessage(3);
//					handler.sendEmptyMessage(4);
//					if(tr.list.size()<20) isLast=true;
//					else  isLast=false;
//				} else {
//					handler.sendEmptyMessage(3);
//					handler.sendEmptyMessage(6);
//				}
//			}
//
//			@Override
//			public void error() {
//
//				handler.sendEmptyMessage(3);
//				handler.sendEmptyMessage(6);
//			}
//		});
//	}
//
//	public void getFooterData() {
//		if(isLast){
//			handler.sendEmptyMessage(5);
//			handler.sendEmptyMessage(7);
//			return;
//		}
//		handler.sendEmptyMessage(2);
//		ExeProtocol.exe(new GetQuesListRequest(pageNum), new ProtocolResponse() {
//
//			@Override
//			public void finish(BaseHttpResponse bhr) {
//
//				GetQuesListResponse tr = (GetQuesListResponse) bhr;
//				if (tr.list != null && tr.list.size() != 0) {
//					quesList.addAll(tr.list);
//					pageNum++;
//					binderAdapterDataHandler.post(binderAdapterDataRunnable);
//
////					handler.sendEmptyMessage(1);
//					handler.sendEmptyMessage(3);
//					handler.sendEmptyMessage(5);
//					if(tr.list.size()<20) isLast=true;
//					else  isLast=false;
//				} else {
////					handler.sendEmptyMessage(1);
//					binderAdapterDataHandler.post(binderAdapterDataRunnable);
//				}
//			}
//
//			@Override
//			public void error() {
//
////				handler.sendEmptyMessage(1);
//				binderAdapterDataHandler.post(binderAdapterDataRunnable);
//			}
//		});
//	}
//
//	private class GetHeaderDataTask extends AsyncTask<Void, Void, String[]> {
//
//		@Override
//		protected String[] doInBackground(Void... params) {
//			ExeRefreshTime.lastRefreshTime("NewPostListUpdateTime");
//			handler.sendEmptyMessage(2);
//			getHeaderData();
//			return null;
//		}
//	}
//
//	private class GetFooterDataTask extends AsyncTask<Void, Void, String[]> {
//
//		@Override
//		protected String[] doInBackground(Void... params) {
//			ExeRefreshTime.lastRefreshTime("NewPostListUpdateTime");
//			getFooterData();
//			return null;
//		}
//	}
//
//	@Override
//	public void onFooterRefresh(PullToRefreshView view) {
//
//		new GetFooterDataTask().execute();
//	}
//
//	@Override
//	public void onHeaderRefresh(PullToRefreshView view) {
//
//		Log.e("onHeaderRefresh", "onHeaderRefresh");
//		refreshView.setLastUpdated(ExeRefreshTime
//				.lastRefreshTime("NewPostListUpdateTime"));
//		new GetHeaderDataTask().execute();
//	}
//
//
//
//}
