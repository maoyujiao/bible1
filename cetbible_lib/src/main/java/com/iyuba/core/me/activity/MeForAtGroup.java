//package com.iyuba.core.me.activity;
//
//import android.app.AlertDialog;
//import android.content.ComponentName;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.graphics.drawable.Drawable;
//import android.media.AudioManager;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Looper;
//import android.os.Message;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.iyuba.configation.ConfigManager;
//import com.iyuba.core.activity.Base;
//import com.iyuba.core.activity.CrashApplication;
//import com.iyuba.core.activity.Login;
//import com.iyuba.core.listener.ProtocolResponse;
//import com.iyuba.core.manager.AccountManager;
//import com.iyuba.core.manager.SocialDataManager;
//import com.iyuba.core.network.ClientSession;
//import com.iyuba.core.network.IResponseReceiver;
//import com.iyuba.core.protocol.BaseHttpRequest;
//import com.iyuba.core.protocol.BaseHttpResponse;
//import com.iyuba.core.protocol.base.GradeRequest;
//import com.iyuba.core.protocol.base.GradeResponse;
//import com.iyuba.core.protocol.message.RequestBasicUserInfo;
//import com.iyuba.core.protocol.message.RequestNewInfo;
//import com.iyuba.core.protocol.message.ResponseBasicUserInfo;
//import com.iyuba.core.protocol.message.ResponseNewInfo;
//import com.iyuba.core.setting.SettingConfig;
//import com.iyuba.core.sqlite.mode.UserInfo;
//import com.iyuba.core.thread.GitHubImageLoader;
//import com.iyuba.core.util.CheckGrade;
//import com.iyuba.core.util.ExeProtocol;
//import com.iyuba.core.widget.dialog.CustomToast;
//import com.iyuba.biblelib.R;
//import com.umeng.analytics.MobclickAgent;
//
///**
// * 我界面 为activitygroup设计
// *
// * @author chentong
// * @version 1.0
// */
//@Deprecated
//public class MeForAtGroup extends Base {
//	private View noLogin, login; // 登录提示面板
//	private Button loginBtn, logout;
//	private Context mContext;
//	private ImageView photo;
//	private TextView name, state, fans, attention, notification, listem_time,
//			position, lv;
//	private View person,local_panel;
//	private View stateView, messageView, vipView;
//	private View local, favor, read, back;
//	private View attentionView, fansView, notificationView;
//	private UserInfo userInfo;
//	private boolean showLocal;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.me);
//		setVolumeControlStream(AudioManager.STREAM_MUSIC);
//		CrashApplication.getInstance().addActivity(this);
//		mContext = this;
//		showLocal = getIntent().getBooleanExtra("showLocal", false);
//		back = findViewById(R.id.button_back);
//		back.setVisibility(View.GONE);
//		noLogin = findViewById(R.id.noLogin);
//		login = findViewById(R.id.login);
//		logout = (Button) findViewById(R.id.logout);
////		local_panel = findViewById(R.id.local_panel);
////		if (!showLocal) {
////			local_panel.setVisibility(View.GONE);
////		}
//	}
//
//	@Override
//	protected void onResume() {
//
//		super.onResume();
//		MobclickAgent.onResume(this);
//		viewChange();
//	}
//
//	@Override
//	protected void onPause() {
//		super.onPause();
//		MobclickAgent.onPause(this);
//	}
//
//	private void viewChange() {
//		initLocal();
//		if (!AccountManager.Instance(mContext).checkUserLogin()) {
//			noLogin.setVisibility(View.VISIBLE);
//			login.setVisibility(View.GONE);
//			loginBtn = (Button) findViewById(R.id.button_to_login);
//			loginBtn.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//
//					Intent intent = new Intent();
//					intent.setClass(mContext, Login.class);
//					startActivity(intent);
//				}
//			});
//			logout.setVisibility(View.GONE);
//		} else {
//			noLogin.setVisibility(View.GONE);
//			login.setVisibility(View.VISIBLE);
//			logout.setVisibility(View.VISIBLE);
//			logout.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View arg0) {
//
//					AlertDialog dialog = new AlertDialog.Builder(mContext)
//							.setIcon(android.R.drawable.ic_dialog_alert)
//							.setTitle(
//									getResources().getString(
//											R.string.alert_title))
//							.setMessage(
//									getResources().getString(
//											R.string.logout_alert))
//							.setPositiveButton(
//									getResources().getString(
//											R.string.alert_btn_ok),
//									new DialogInterface.OnClickListener() {
//										public void onClick(
//												DialogInterface dialog,
//												int whichButton) {
//
//											handler.sendEmptyMessage(4);
//										}
//									})
//							.setNeutralButton(
//									getResources().getString(
//											R.string.alert_btn_cancel),
//									new DialogInterface.OnClickListener() {
//										public void onClick(
//												DialogInterface dialog,
//												int which) {
//										}
//									}).create();
//					dialog.show();
//				}
//			});
//			userInfo = AccountManager.Instance(mContext).userInfo;
//			ClientSession.Instance()
//					.asynGetResponse(
//							new RequestNewInfo(
//									AccountManager.Instance(mContext).userId),
//							new IResponseReceiver() {
//								@Override
//								public void onResponse(
//										BaseHttpResponse response,
//										BaseHttpRequest request, int rspCookie) {
//
//									ResponseNewInfo rs = (ResponseNewInfo) response;
//									if (rs.letter > 0) {
//										handler.sendEmptyMessage(2);
//									} else {
//										handler.sendEmptyMessage(5);
//									}
//								}
//
//
//							});
//			loadData();
//		}
//	}
//
//	/**
//	 *
//	 */
//	private void loadData() {
//
//		final String id = AccountManager.Instance(mContext).userId;
//		init();
//		ExeProtocol.exe(new RequestBasicUserInfo(id, id),
//				new ProtocolResponse() {
//
//					@Override
//					public void finish(BaseHttpResponse bhr) {
//
//						ResponseBasicUserInfo response = (ResponseBasicUserInfo) bhr;
//						userInfo = response.userInfo;
//						AccountManager.Instance(mContext).userInfo = userInfo;
//						handler.sendEmptyMessage(3);
//						Looper.prepare();
//						ExeProtocol.exe(new GradeRequest(id),
//								new ProtocolResponse() {
//
//									@Override
//									public void finish(BaseHttpResponse bhr) {
//
//										GradeResponse response = (GradeResponse) bhr;
//										userInfo.studytime = Integer
//												.parseInt(response.totalTime);
//										userInfo.position = response.positionByTime;
//										handler.sendEmptyMessage(3);
//									}
//
//									@Override
//									public void error() {
//
//										handler.sendEmptyMessage(0);
//									}
//								});
//						Looper.loop();
//					}
//
//					@Override
//					public void error() {
//
//					}
//				});
//	}
//
//	/**
//	 *
//	 */
//	private void init() {
//
//		person = findViewById(R.id.personalhome);
//		photo = (ImageView) findViewById(R.id.me_pic);
//		name = (TextView) findViewById(R.id.me_name);
//		state = (TextView) findViewById(R.id.me_state);
//		attention = (TextView) findViewById(R.id.me_attention);
//		listem_time = (TextView) findViewById(R.id.me_listem_time);
//		position = (TextView) findViewById(R.id.me_position);
//		lv = (TextView) findViewById(R.id.lv);
//		fans = (TextView) findViewById(R.id.me_fans);
//		notification = (TextView) findViewById(R.id.me_notification);
//		stateView = findViewById(R.id.me_state_change);
//		vipView = findViewById(R.id.me_vip);
//		messageView = findViewById(R.id.me_message);
//		attentionView = findViewById(R.id.attention_area);
//		fansView = findViewById(R.id.fans_area);
//		notificationView = findViewById(R.id.notification_area);
//		setViewClick();
//		if (userInfo != null) {
//			setTextViewContent();
//		}
//	}
//
//	private void initLocal() {
////		local = findViewById(R.id.me_local);
////		favor = findViewById(R.id.me_love);
////		read = findViewById(R.id.me_read);
////		local.setOnClickListener(ocl);
////		favor.setOnClickListener(ocl);
////		read.setOnClickListener(ocl);
//	}
//
//	/**
//	 *
//	 */
//	private void setViewClick() {
//
//		person.setOnClickListener(ocl);
//		stateView.setOnClickListener(ocl);
//		vipView.setOnClickListener(ocl);
//		messageView.setOnClickListener(ocl);
//		attentionView.setOnClickListener(ocl);
//		fansView.setOnClickListener(ocl);
//		stateView.setOnClickListener(ocl);
//		notificationView.setOnClickListener(ocl);
//	}
//
//	/**
//	 *
//	 */
//	private void setTextViewContent() {
//
//		GitHubImageLoader.Instance(mContext).setCirclePic(
//				AccountManager.Instance(mContext).userId, photo);
//		name.setText(AccountManager.Instance(mContext).userName);
//		if (ConfigManager.Instance().loadInt("isvip") == 1) {
//			Drawable img = mContext.getResources().getDrawable(R.drawable.vip);
//			img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
//			name.setCompoundDrawables(null, null, img, null);
//		} else {
//			Drawable img = mContext.getResources().getDrawable(
//					R.drawable.no_vip);
//			img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
//			name.setCompoundDrawables(null, null, img, null);
//		}
//		if (userInfo.text == null) {
//			state.setText(R.string.social_default_state);
//		} else {
//			state.setText(userInfo.text);
//		}
//		attention.setText(userInfo.following);
//		fans.setText(userInfo.follower);
//		listem_time.setText(exeStudyTime());
//		position.setText(exePosition());
//		lv.setText(exeIyuLevel());
//		notification.setText(userInfo.notification);
//	}
//
//	private String exeStudyTime() {
//		StringBuffer sb = new StringBuffer(
//				mContext.getString(R.string.me_study_time));
//		int time = userInfo.studytime;
//		int minus = time % 60;
//		int minute = time / 60 % 60;
//		int hour = time / 60 / 60;
//		if (hour > 0) {
//			sb.append(hour).append(mContext.getString(R.string.me_hour));
//		} else if (minute > 0) {
//			sb.append(minute).append(mContext.getString(R.string.me_minute));
//		} else {
//			sb.append(minus).append(mContext.getString(R.string.me_minus));
//		}
//		return sb.toString();
//	}
//
//	private String exePosition() {
//		StringBuffer sb = new StringBuffer(
//				mContext.getString(R.string.me_study_position));
//		int position = Integer.parseInt(userInfo.position);
//
//		if (position < 10000) {
//			sb.append(position);
//		} else {
//			sb.append(position / 10000 * 10000).append("+");
//		}
//		return sb.toString();
//	}
//
//	private String exeIyuLevel() {
//		StringBuffer sb = new StringBuffer("");
//		sb.append(mContext.getString(R.string.me_lv));
//		sb.append(CheckGrade.Check(userInfo.icoins));
//		sb.append(" ");
//		sb.append(CheckGrade.CheckLevelName(userInfo.icoins));
//		sb.append("   ");
//		sb.append(mContext.getString(R.string.me_score));
//		sb.append(userInfo.icoins);
//		return sb.toString();
//	}
//
//	private OnClickListener ocl = new OnClickListener() {
//
//		@Override
//		public void onClick(View arg0) {
//
//			Intent intent;
//			int id = arg0.getId();
//			if (id == R.id.personalhome) {
//				intent = new Intent(mContext, PersonalHome.class);
//				SocialDataManager.Instance().userid = AccountManager
//						.Instance(mContext).userId;
//				startActivity(intent);
//			} else if (id == R.id.me_state_change) {
//				intent = new Intent(mContext, WriteState.class);
//				startActivity(intent);
//			} else if (id == R.id.me_vip) {
//				intent = new Intent(mContext, VipCenter.class);
//				startActivity(intent);
//			} else if (id == R.id.me_message) {
//				intent = new Intent(mContext, MessageCenter.class);
//				startActivity(intent);
//			} else if (id == R.id.me_local) {
//				intent = new Intent();
//				intent.setComponent(new ComponentName("com.iyuba.music",
//						"com.iyuba.music.activity.LocalNews"));
//				intent.putExtra("localType", 0);
//				startActivity(intent);
//			} else if (id == R.id.me_love) {
//				intent = new Intent();
//				intent.setComponent(new ComponentName("com.iyuba.music",
//						"com.iyuba.music.activity.LocalNews"));
//				intent.putExtra("localType", 1);
//				startActivity(intent);
//			} else if (id == R.id.me_read) {
//				intent = new Intent();
//				intent.setComponent(new ComponentName("com.iyuba.music",
//						"com.iyuba.music.activity.LocalNews"));
//				intent.putExtra("localType", 2);
//				startActivity(intent);
//			} else if (id == R.id.attention_area) {
//				intent = new Intent(mContext, AttentionCenter.class);
//				intent.putExtra("userid",
//						AccountManager.Instance(mContext).userId);
//				startActivity(intent);
//			} else if (id == R.id.fans_area) {
//				intent = new Intent(mContext, FansCenter.class);
//				intent.putExtra("userid",
//						AccountManager.Instance(mContext).userId);
//				startActivity(intent);
//			} else if (id == R.id.notification_area) {
//				intent = new Intent(mContext, NoticeCenter.class);
//				intent.putExtra("userid",
//						AccountManager.Instance(mContext).userId);
//				startActivity(intent);
//			} else {
//			}
//		}
//	};
//	Handler handler = new Handler() {
//		@Override
//		public void handleMessage(final Message msg) {
//			super.handleMessage(msg);
//			switch (msg.what) {
//			case 0:
//				CustomToast.showToast(mContext, R.string.check_network);
//				break;
//			case 1:
//				CustomToast.showToast(mContext, R.string.action_fail);
//				break;
//			case 2:
//				findViewById(R.id.newletter).setVisibility(View.VISIBLE);
//				break;
//			case 3:
//				setTextViewContent();
//				break;
//			case 4:
//				AccountManager.Instance(mContext).loginOut();
//				CustomToast.showToast(mContext,
//						R.string.loginout_success);
//				SettingConfig.Instance().setHighSpeed(false);
//				onResume();
//				break;
//			case 5:
//				findViewById(R.id.newletter).setVisibility(View.GONE);
//				break;
//			}
//		}
//	};
//}
