package de.xiaoxia.xstatusbarlunardate;

import android.annotation.SuppressLint;
import android.os.Build;
import android.widget.TextClock;
import android.widget.TextView;

//导入xposed基本类
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

/* Main */
public class Lockscreen implements IXposedHookLoadPackage{

    /* 初始变量 */
    private String lunarText; //记录最后更新时的文字字符串
    private String year; //记录年份
    private String lDate = "LastDate";
    private String nDate;
    private Lunar lunar = new Lunar(Main._lang);
    private TextClock textclock;
    private TextView textview;
 
    //获取农历字符串子程序
    private String returnDate(String nDate){
    	//判断日期是否发生变更，没有变更则直接返回缓存
    	if(!nDate.equals(lDate)){
	    	lunar.init(System.currentTimeMillis());
	        //判断是否是农历节日
	        String fest = " " + lunar.getLFestivalName();
	        if ((Main._fest == true) && (!"".equals(fest))){
	            if(fest.equals(" 小年")){
	                if((lunar.getLunarDay() == 23 && "1".equals(Main._minor)) || (lunar.getLunarDay() == 24 && "2".equals(Main._minor))  || (lunar.getLunarDay() == 25 && "3".equals(Main._minor))){
	                }else{
	                    fest = " ";
	                }
	            }
	        }else{
	            fest = " ";
	        }
	
	        //判断是否是二十四节气
	        String term = " " + lunar.getTermString();
	        if ((Main._term == true) && (!"".equals(term))){
	            term = " " + lunar.getTermString();
	        }else{
	            term = " ";
	        }
	
	        //根据设置设置年份
	        switch(Main._year){
	            case 1:  year = lunar.getAnimalString() + "年";
	                break;
	            case 2:  year = lunar.getLunarYearString() + "年";
	                break;
	            case 3:  year = "";
	                break;
	            case 4:  year = lunar.getLunarYearString() + "（" + lunar.getAnimalString() + "）年";
	                break;
	            default: year = lunar.getAnimalString() + "年";
	        
	        }
	
	        //组合农历文本
	        if(Main._lang != 3){
	            lunarText =  year + lunar.getLunarMonthString() + "月" + lunar.getLunarDayString() + fest + term;
	        }else{
	            lunarText = "[" + lunar.getLunarDay() + "/" + lunar.getLunarMonth() + "]";
	        }
	        lunarText = lunarText.trim();
	        lDate = nDate;
    	}
		return lunarText;
    }

    //替换日期函数
    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
    	if(Main._lockscreen){
    		XposedBridge.log(lpparam.packageName);
	        if(lpparam.packageName.equals("android")){
	        	try{
			        if(Build.VERSION.SDK_INT <= 16) {
			        	XposedBridge.log("SDK 1");
				        Class<?> kgStatusViewManagerClass = XposedHelpers.findClass("com.android.internal.policy.impl.KeyguardStatusViewManager", null);
				        XposedHelpers.findAndHookMethod(kgStatusViewManagerClass, "refreshDate", new XC_MethodHook() {
				
				        	@Override
				            protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
				        		textview = (TextView) XposedHelpers.getObjectField(param.thisObject, "mDateView");
				        		nDate = (String) textview.getText().toString();
			 	                textview.setText(nDate + " - " + returnDate(nDate));
				            }
				        });
			        }else if(Build.VERSION.SDK_INT <= 18){
			        	XposedBridge.log("SDK 2");
			        	Class<?> kgStatusViewManagerClass = XposedHelpers.findClass("com.android.internal.policy.impl.keyguard.KeyguardStatusView", null);
			 	        XposedHelpers.findAndHookMethod(kgStatusViewManagerClass, "refreshDate", new XC_MethodHook() {
			 	
			 	        	@Override
			 	            protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
				        		textview = (TextView) XposedHelpers.getObjectField(param.thisObject, "mDateView");
				        		nDate = (String) textview.getText().toString();
			 	                textview.setText(nDate + " - " + returnDate(nDate));
			 	            }
			 	        });
			        }
	        	}catch(Exception e){
	        		//Do nothing
	        	}
	        }else if(lpparam.packageName.equals("com.android.keyguard")){
	        	try{
	        		XposedBridge.log("SDK 3");
		        	findAndHookMethod("com.android.keyguard.KeyguardStatusView", lpparam.classLoader, "refresh", new XC_MethodHook() {
		
		        		@SuppressLint("NewApi")
		                @Override
		                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
			        		textclock = (TextClock) XposedHelpers.getObjectField(param.thisObject, "mDateView");
			        		nDate = (String) textclock.getText().toString();
			        		textclock.setText(nDate + " - " + returnDate(nDate));
		                }
		        	});
	        	}catch(Exception e){
	        		//Do nothing
	        	}
	        }
    	}
    }
}