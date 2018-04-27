package com.tencent.pay.sdksample;
import oicq.wlogin_sdk.request.TransReqContext;
import oicq.wlogin_sdk.request.WUserSigInfo;
import oicq.wlogin_sdk.request.WloginLastLoginInfo;
import oicq.wlogin_sdk.sharemem.WloginSimpleInfo;
import oicq.wlogin_sdk.request.WtloginListener;
import oicq.wlogin_sdk.tools.util;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.content.Intent;

public class wtlogin_sdk_demo extends Activity {
 
	public static LoginHelper mLoginHelper = null;
	public static long mAppid = 0x1;
	public static String mAppName = "wtlogin_sdk_demo";
	public static String mAppVersion = "1.0";
 
	long mUin = 0;
	int mChg = 0;
	byte[] mSuperSig = new byte[0];
	Button mRegisterButton;
	Button mLoginButton;
	Button mClearButton;
	EditText mAccount;
	EditText mPasswd;  
	ProgressDialog mPDialog;

	ImageView mImage;
	EditText mImageCode;
	Dialog mChkImgDlg;
 
	/** Called when the activity is first created. */
	@Override 
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sdemo_login);		
		
		Intent intent = this.getIntent();
		mUin = intent.getLongExtra("UIN", 0);
		mSuperSig = intent.getByteArrayExtra("SUPERSIG");

		mAccount = (EditText) findViewById(R.id.sample_login_edit_account);
		mPasswd = (EditText) findViewById(R.id.sample_login_edit_password);

		mLoginButton = (Button) findViewById(R.id.sample_button_login);
		mLoginButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
					 			       
 
				// 登录 
				WUserSigInfo sigInfo = new WUserSigInfo();
				WloginLastLoginInfo info = mLoginHelper.GetLastLoginInfo(); 
				
				if (mLoginHelper.IsNeedLoginWithPasswd(mAccount.getText().toString(), 0x1)) {
					sigInfo._userPasswdSig = mLoginHelper.GetA1ByAccount(mAccount.getText().toString(), 0x1);
					if (info != null && mChg == 0 && sigInfo._userPasswdSig != null) {
						mLoginHelper.GetStWithPasswd(mAccount.getText().toString(), 0x1, "", sigInfo, 0);
						//mLoginHelper.GetOpenKeyWithPasswd(mAccount.getText().toString(), 0x1, "", sigInfo, 0);
						mPDialog = ProgressDialog.show(wtlogin_sdk_demo.this,"请稍等...", "保存密码登录...", true);
					} else {
						mLoginHelper.GetStWithPasswd(mAccount.getText().toString(), 0x1, mPasswd.getText().toString(),sigInfo, 0);
						//mLoginHelper.GetOpenKeyWithPasswd(mAccount.getText().toString(), 0x1, mPasswd.getText().toString(), sigInfo, 0);
						mPDialog = ProgressDialog.show(wtlogin_sdk_demo.this,"请稍等...", "正在验证...", true);
					}
				} else {
					mLoginHelper.GetStWithoutPasswd(mAccount.getText().toString(), 0x1, 0x1, sigInfo, 0);
					//mLoginHelper.GetOpenKeyWithoutPasswd(mAccount.getText().toString(), 0x1, 0x1, sigInfo, 0);
					mPDialog = ProgressDialog.show(wtlogin_sdk_demo.this,"请稍等...", "正在更新票据...", true);				
				}		
				
				/*
				TransReqContext req_con = new TransReqContext(); 
				req_con._body = new byte[100];
				mLoginHelper.RequestTransportMsf(0,1, "18603029139",0x1,112,req_con);				
				return ;
				*/      				
			}
		});    

		// 初始化登录&透传对象   
		mLoginHelper = new LoginHelper(this.getApplicationContext()); 
		//打开调试日志
		//设置测试IP
		//mLoginHelper.SetTestHost(1, "121.14.101.81");
	    //初始化共享票据服务 
		//mLoginHelper.InitShareService();
		
		
		WloginLastLoginInfo info = mLoginHelper.GetLastLoginInfo();
		if (info != null && mChg == 0) {
			mAccount.setText(info.mAccount);
			if(info.mAccount.length() > 0){
				if(mLoginHelper.GetA1ByAccount(info.mAccount, 0x1) != null)
					mPasswd.setText("123456");
				else  
					mPasswd.setText("");
			}
		}
		mLoginHelper.SetTkTimeOut(30*60);	//设置保存登录态时长
		//AddAccountEditEvent();		
	}

	@SuppressLint("UseValueOf")
	@Override 
	protected void onStart() {
		super.onStart();
		
		// 注册回调
		wtlogin_sdk_demo.mLoginHelper.SetListener(mListener);
		mChg = 0;

		if (mUin != 0) {
			mAccount.setText(new Long(mUin).toString());
			mPasswd.setText("");
			mChg = 1;
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		mUin = intent.getLongExtra("UIN", 0);
		mSuperSig = intent.getByteArrayExtra("SUPERSIG");
	}
	
	  @Override
	  public void onDestroy() { 
		  super.onDestroy();
	  }	    

	public void LoginSucess(String userAccount, WUserSigInfo userSigInfo) {
		WloginSimpleInfo info = new WloginSimpleInfo();
		mLoginHelper.GetBasicUserInfo(userAccount, info);
		
		byte[] byteskey = userSigInfo._sKey;
		String strskey = new String (byteskey);
		
		Intent intent = new Intent();

		intent.setClass(wtlogin_sdk_demo.this, AndroidPaySample.class);
		
		intent.putExtra("uin", userAccount);
		intent.putExtra("skey", strskey);
		
		startActivity(intent);
	}

	@SuppressLint("ShowToast")
	public void LoginFail(String userAccount, int ret) {
		mPasswd.setText("");

		WloginSimpleInfo info = new WloginSimpleInfo();
		mLoginHelper.GetBasicUserInfo(userAccount, info);
		
		Toast.makeText(wtlogin_sdk_demo.this, mLoginHelper.GetLastErrMsg().getMessage(), Toast.LENGTH_LONG);

	}

	public void AddAccountEditEvent() {
		mAccount.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				mChg = 1;
			}
		});
		mPasswd.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				mChg = 1;
			}
		});
	}

	WtloginListener mListener = new WtloginListener() {
		public void OnInit(int ret)
		{
			
		}

		public void OnGetStWithPasswd(String userAccount, long dwAppid,int dwMainSigMap,long dwSubDstAppid,
				String userPasswd, WUserSigInfo userSigInfo, int ret) {
			util.LOGD("OnGetStWithPasswd:" + ret);
			if (mPDialog != null)
				mPDialog.dismiss();
			if (ret == util.S_SUCCESS) {
				LoginSucess(userAccount, userSigInfo);
			}

			else if (ret == util.S_GET_IMAGE) {
				//ShowChkImgDlg(userAccount);
			} else {
				LoginFail(userAccount, ret);
			}
		}

		public void OnCheckPictureAndGetSt(String userAccount,
				byte[] userInput, WUserSigInfo userSigInfo, int ret) {
			util.LOGD("OnCheckPictureAndGetSt:" + ret);
			if (mPDialog != null)
				mPDialog.dismiss();
			if (ret == util.S_SUCCESS) {
				LoginSucess(userAccount, userSigInfo);
			}

			else if (ret == util.S_GET_IMAGE) {
				//ShowChkImgDlg(userAccount);
			} else {
				LoginFail(userAccount, ret);
			}
		}

		public void OnRefreshPictureData(String userAccount,
				byte[] pictureData, int ret) {
			if (mPDialog != null)
				mPDialog.dismiss();
			util.LOGD("OnRefreshPictureData:" + ret);
			//ShowChkImgDlg(userAccount);
		}
				
		public void OnGetStWithoutPasswd(String userAccount, long dwSrcAppid, long dwDstAppid,int dwMainSigMap,long dwSubDstAppid, WUserSigInfo userSigInfo, int ret)
		{
			if (mPDialog != null)
				mPDialog.dismiss();			
			if (ret == util.S_SUCCESS) {
				LoginSucess(userAccount, userSigInfo);
			}
			else {
				LoginFail(userAccount, ret);
			}			 
		}	
		 
		public void OnRequestTransport(String userAccount, long appid, long role, TransReqContext req_context, int ret)
		{
			util.LOGD(util.buf_to_string(req_context._body));
			Log.e("util.LOGD", util.buf_to_string(req_context._body));
		}				
	};

}