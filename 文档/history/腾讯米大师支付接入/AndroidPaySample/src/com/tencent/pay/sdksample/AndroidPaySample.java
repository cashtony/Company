package com.tencent.pay.sdksample;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.pay.AndroidPay;
import com.pay.api.APPayGameService;
import com.pay.api.APPayOpenService;
import com.pay.api.APPayResponseInfo;
import com.pay.api.IAPPayGameServiceCallBack;
import com.pay.api.IAPPayOpenServiceCallBack;
import com.pay.http.APBaseHttpAns;
import com.pay.http.IAPHttpAnsObserver;
import com.pay.network.modle.APMpAns;

public class AndroidPaySample extends Activity implements  IAPPayGameServiceCallBack, IAPPayOpenServiceCallBack
{
    private String userId   	= "";    
    private String userKey 		= "";  
    private String sessionId 	= "";   
    private String sessionType	= "";  
    private String zoneId  		= "";   
    private String saveValue 	= "";   
    private String pf 			= "";   
    private String pfKey		= "";  
    private String acctType     = "";  
    private String tokenUrl      = "";
    private int    resId        = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sdemo_index);

        Button payMonthWithoutSaveNumber = (Button)findViewById(R.id.MonthwithoutSaveNumberBtn);
        Button payMonthWithSaveNumberCanChange = (Button)findViewById(R.id.MonthwithSaveNumberBtn);
        Button paySubscribeWithoutSaveNumber = (Button)findViewById(R.id.SubScribewithoutSaveNumberBtn);
        Button paySubscribeWithSaveNumberCanChange = (Button)findViewById(R.id.SubScribewithSaveNumberBtn);
        Button payGameWithoutSaveNumber = (Button)findViewById(R.id.withoutSaveNumberBtn);
        Button payGameWithSaveNumberCanChange = (Button)findViewById(R.id.saveNumberCanChangeBtn);
        Button saveGoodsBtn = (Button)findViewById(R.id.saveGoodsBtn);
        Button saveGameBtn = (Button)findViewById(R.id.saveGameBtn);
        
        //全局初始化
        AndroidPay.Initialize(AndroidPaySample.this);

        //可以通过调用以下三个函数设置，具体参见文档
        AndroidPay.setOfferId("1450000766");
        AndroidPay.setEnv("test");
        AndroidPay.setLogEnable(true);

        //设置支付参数
        setParams();

        //调用包月接口
        payMonthWithoutSaveNumber.setOnClickListener(new Button.OnClickListener()
        {
        	public void onClick(View view)
        	{
        		//充值游戏币接口，充值默认值由支付SDK设置;
        		resId = R.drawable.sample_xxjzgw;
        		String remark = "业务透传";
        		//购买包月用的回调
                APPayOpenService.SetDelegate(AndroidPaySample.this);
                //拉起包月
        		APPayOpenService.LaunchOpenServiceView(userId, userKey, sessionId, sessionType, zoneId, pf, pfKey, "xxjzgw", "黄钻", resId, remark);
        	}
        });
        //调用包月接口
        
        payMonthWithSaveNumberCanChange.setOnClickListener(new Button.OnClickListener()
        {
        	public void onClick(View view)
        	{
        		resId = R.drawable.sample_xxjzgw;
        		String remark = "业务透传";
        		//购买包月用的回调
                APPayOpenService.SetDelegate(AndroidPaySample.this);
                //拉起包月
        		APPayOpenService.LaunchOpenServiceView(userId, userKey, sessionId, sessionType, zoneId, pf, pfKey, "xxjzgw", "黄钻", resId, "3", true, remark);
        	}
        });
        
        //调用订阅型接口,列表页面
        paySubscribeWithoutSaveNumber.setOnClickListener(new Button.OnClickListener()
        {
        	public void onClick(View view)
        	{
        		//充值游戏币接口，充值默认值由支付SDK设置;
        		resId = R.drawable.sample_xxjzgw;
        		String remark = "业务透传";
        		//设置回调
                APPayOpenService.SetDelegate(AndroidPaySample.this);
                APPayOpenService.LaunchOpenServiceView(userId,userKey,sessionId,sessionType,zoneId,pf,pfKey,"wemusic_vip_1","七天特权",resId,null,null,true, remark);
            	
        	}
        });
        //调用订阅型接口，定额不可改接口
        paySubscribeWithSaveNumberCanChange.setOnClickListener(new Button.OnClickListener()
        {
        	public void onClick(View view)
        	{
        		resId = R.drawable.sample_xxjzgw;
        		String remark = "业务透传";
        		saveValue = "1";
        		//设置回调
                APPayOpenService.SetDelegate(AndroidPaySample.this);
                APPayOpenService.LaunchOpenServiceView(userId,userKey,sessionId,sessionType,zoneId,pf,pfKey,"wemusic_vip_1","七天特权",resId,saveValue,"com.tencent.wemusic_vip_1_test",false, remark);
        	}
        });
  
        //调用游戏币游戏币接口
        payGameWithoutSaveNumber.setOnClickListener(new Button.OnClickListener()
        {
        	public void onClick(View view)
        	{
        		//充值游戏币接口，充值默认值由支付SDK设置;
        		resId = R.drawable.sample_yuanbao;
        		//购买游戏币、道具用的回调
                APPayGameService.SetDelegate(AndroidPaySample.this);
                //拉起购买游戏币
        		APPayGameService.LaunchSaveCurrencyView(userId,userKey,sessionId,sessionType,zoneId,pf,pfKey,acctType,resId);
        	}
        });

        //调用游戏币游戏币接口
        payGameWithSaveNumberCanChange.setOnClickListener(new Button.OnClickListener()
        {
        	public void onClick(View view)
        	{
        		resId = R.drawable.sample_yuanbao;
        		//购买游戏币、道具用的回调
                APPayGameService.SetDelegate(AndroidPaySample.this);
                //拉起购买游戏币
        		APPayGameService.LaunchSaveCurrencyView(userId,userKey,sessionId,sessionType,zoneId,pf,pfKey,acctType,saveValue,true, resId);
        	}
        });

        
        //指定渠道+营销活动购买游戏币接口
        saveGameBtn.setOnClickListener(new Button.OnClickListener()
        {
			@Override
			public void onClick(View v) 
			{
				resId = R.drawable.sample_xxjzgw;
				String discounttype = "InGame";
				String discountUrl = "http://imgcache.qq.com/bossweb/midas/unipay/androidPaySDK_1_3_3/act/actTip.html?_t=1";
				//购买游戏币、道具用的回调
                APPayGameService.SetDelegate(AndroidPaySample.this); 
				//拉起购买游戏币
				APPayGameService.LaunchMPSaveCurrencyView(userId, userKey, sessionId, sessionType, zoneId, pf, pfKey, acctType, saveValue, resId, 
    														APPayGameService.PAY_CHANNEL_BANK, discounttype, discountUrl, null);
			}
        });
        
        //调用道具接口
        saveGoodsBtn.setOnClickListener(new Button.OnClickListener()
        {
        	public void onClick(View view)
        	{
        		tokenUrl = "";
        		resId = R.drawable.sample_mofaquan;
        		saveValue = "10";
        		
        		//购买游戏币、道具用的回调
                APPayGameService.SetDelegate(AndroidPaySample.this); 
        		//拉取购买道具接口
        		APPayGameService.LaunchSaveGoodsView(userId, userKey, sessionId, sessionType, zoneId, pf, pfKey, tokenUrl, resId);
        	}
        });
    }
    
    public void getMpInfo()
    {
    	//购买游戏币、道具用的回调
        APPayGameService.SetDelegate(AndroidPaySample.this); 
    	APPayGameService.LaunchMp(userId, userKey, sessionId, sessionType, zoneId, pf, pfKey, new IAPHttpAnsObserver()
    	{
			@Override
			public void onError(APBaseHttpAns ans) 
			{
				
			}

			@Override
			public void onFinish(APBaseHttpAns ans) 
			{
				APMpAns  mpans= (APMpAns)ans;
				if(mpans.getResultCode() == 0)
				{
						String  begintime= mpans.getBeginTime();//营销活动的开始时间
						String  endtime  =mpans.getEndTime();  //营销活动的结束时间
						//营销活动的信息
						//mpValueList充值列表
						//mpPresentList赠送列表
						List<String>  mpValueList = mpans.getMpValueList();
						List<String>  mpPresentList = mpans.getMpPresentList();
						//rate汇率,汇率的概念是 1快钱买多少个
						//比如 rate为5的话 就是 1块钱 买5个游戏币
						String rate=mpans.getRate();
						//首充赠送值
						String firstsave=mpans. getFirstsave_present_count();
				}
			}

			@Override
			public void onStop(APBaseHttpAns ans) 
			{
				
			}
    	});
    }
    
    //设置支付参数
    private void setParams()
    {
    	Bundle bundle = this.getIntent().getExtras();
    	//用户ID,demo中调用即通登录模块获取，应用根据不同平台传递相应的登录id
    	userId      = bundle.getString("uin");

    	//用户登录态,demo中从即通登录模块获取，应用根据不同平台传递相应的登录票据
    	userKey     = bundle.getString("skey");		        
    	
    	//用户ID类型,应用根据自己的登录类型传递,如uin、openid、hy_gameid、uin
    	sessionId   = "uin";
    	//用户登录态类型,应用根据自己的登录类型传递,如seky、kp_actoken、wc_actoken、sid
    	sessionType = "skey";								
    	
    	//游戏分区id（若无分区， 默认传1）
    	zoneId      = "1";	
    	
    	//平台信息,格式平台-渠道-系统-自定义（详细见说明文档）
    	pf          = "huyu_m-2001-android-xxxx";
		//由平台下发，游戏传递给支付sdk,自研应用不校验，可以设置为pfKey
		pfKey       = "pfKey";
		
		//货币类型   ACCOUNT_TYPE_COMMON:基础货币； ACCOUNT_TYPE_SECURITY:安全货币
		acctType    = APPayGameService.ACCOUNT_TYPE_COMMON;
		//用户的充值数额（可选，调用相应充值接口即可）
	    saveValue   = "60";	
    }
	

    /**
     * 包月回调
     **/
	@Override
	public void PayOpenServiceNeedLogin()
	{
		Toast.makeText(this, "登录票据过期，请重新登录" , Toast.LENGTH_SHORT).show();
	}

	@Override
	public void PayOpenServiceCallBack(APPayResponseInfo payResponseInfo)
	{
		Toast.makeText(this, "包月支付sdk回调应用" , Toast.LENGTH_SHORT).show();
	}

	 /**
     * 游戏币或者道具回调
     **/
	@Override
	public void PayGameNeedLogin() 
	{
		Toast.makeText(this, "登录票据过期，请重新登录" , Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public void PayGameServiceCallBack(APPayResponseInfo payResponseInfo)
	{
		Toast.makeText(this, "支付sdk回调应用" , Toast.LENGTH_SHORT).show();
	}



}