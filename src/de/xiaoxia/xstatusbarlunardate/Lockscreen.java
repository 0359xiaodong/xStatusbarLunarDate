package de.xiaoxia.xstatusbarlunardate;

import android.annotation.SuppressLint;
import android.os.Build;
import android.widget.TextClock;
import android.widget.TextView;

//����xposed������
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
//import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

/* Main */
public class Lockscreen implements IXposedHookLoadPackage{

    /* ��ʼ���� */
    private String lunarText; //��¼������ʱ�������ַ���
    private String year; //��¼���
    private String lDate = "LastDate";
    private String nDate;
    private Lunar lunar = new Lunar(Main._lang);
    private TextClock textclock;
    private TextView textview;
 
    //��ȡũ���ַ����ӳ���
    private String returnDate(String nDate){
    	//�ж������Ƿ��������û�б����ֱ�ӷ��ػ���
    	if(!nDate.equals(lDate)){
	    	lunar.init(System.currentTimeMillis());
	        //�ж��Ƿ���ũ������
	        String fest = " " + lunar.getLFestivalName();
	        if ((Main._fest == true) && (!"".equals(fest))){
	            if(fest.equals(" С��")){
	                if((lunar.getLunarDay() == 23 && "1".equals(Main._minor)) || (lunar.getLunarDay() == 24 && "2".equals(Main._minor))  || (lunar.getLunarDay() == 25 && "3".equals(Main._minor))){
	                }else{
	                    fest = " ";
	                }
	            }
	        }else{
	            fest = " ";
	        }
	
	        //�ж��Ƿ��Ƕ�ʮ�Ľ���
	        String term = " " + lunar.getTermString();
	        if ((Main._term == true) && (!"".equals(term))){
	            term = " " + lunar.getTermString();
	        }else{
	            term = " ";
	        }
	
	        //���������������
	        switch(Main._year){
	            case 1:  year = lunar.getAnimalString() + "��";
	                break;
	            case 2:  year = lunar.getLunarYearString() + "��";
	                break;
	            case 3:  year = "";
	                break;
	            case 4:  year = lunar.getLunarYearString() + lunar.getAnimalString() + "��";
	                break;
	            default: year = lunar.getAnimalString() + "��";
	        
	        }
	
	        //���ũ���ı�
	        if(Main._lang != 3){
	            lunarText =  year + lunar.getLunarMonthString() + "��" + lunar.getLunarDayString() + fest + term;
	        }else{
	            lunarText = "[" + lunar.getLunarDay() + "/" + lunar.getLunarMonth() + "]";
	        }
	        lunarText = lunarText.trim();
	        lDate = nDate;
	        //XposedBridge.log("Calculating lunar date: @" + System.currentTimeMillis());
    	}
		return lunarText;
    }

    //�滻���ں���
    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
    	if(Main._lockscreen){
    		//XposedBridge.log(lpparam.packageName);
	        if(lpparam.packageName.equals("android")){
	        	try{
			        if(Build.VERSION.SDK_INT <= 16) {
			        	//XposedBridge.log("SDK 15-16");
				        Class<?> kgStatusViewManagerClass = XposedHelpers.findClass("com.android.internal.policy.impl.KeyguardStatusViewManager", null);
				        XposedHelpers.findAndHookMethod(kgStatusViewManagerClass, "refreshDate", new XC_MethodHook() {
				        	//XposedBridge.log("Found 15-16");
				
				        	@Override
				            protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
				        		textview = (TextView) XposedHelpers.getObjectField(param.thisObject, "mDateView");
				        		nDate = (String) textview.getText().toString();
			 	                textview.setText(nDate + " - " + returnDate(nDate));
			 	                //XposedBridge.log("Hooking lunar date: @" + System.currentTimeMillis());
				            }
				        });
			        }else if(Build.VERSION.SDK_INT <= 18){
			        	//XposedBridge.log("SDK 17-18");
			        	Class<?> kgStatusViewManagerClass = XposedHelpers.findClass("com.android.internal.policy.impl.keyguard.KeyguardStatusView", null);
			 	        XposedHelpers.findAndHookMethod(kgStatusViewManagerClass, "refreshDate", new XC_MethodHook() {
			 	        	//XposedBridge.log("Found 17-18");
			 	
			 	        	@Override
			 	            protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
				        		textview = (TextView) XposedHelpers.getObjectField(param.thisObject, "mDateView");
				        		nDate = (String) textview.getText().toString();
			 	                textview.setText(nDate + " - " + returnDate(nDate));
			 	                //XposedBridge.log("Hooking lunar date: @" + System.currentTimeMillis());
			 	            }
			 	        });
			        }
	        	}catch(Exception e){
	        		//Do nothing
	        	}
	        }else if(lpparam.packageName.equals("com.android.keyguard")){
	        	try{
	        		//XposedBridge.log("SDK 19");
		        	findAndHookMethod("com.android.keyguard.KeyguardStatusView", lpparam.classLoader, "refresh", new XC_MethodHook() {
		        		//XposedBridge.log("Found 19");
		
		        		@SuppressLint("NewApi")
		                @Override
		                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
			        		textclock = (TextClock) XposedHelpers.getObjectField(param.thisObject, "mDateView");
			        		nDate = (String) textclock.getText().toString();
			        		textclock.setText(nDate + " - " + returnDate(nDate));
			        		//XposedBridge.log("Hooking lunar date: @" + System.currentTimeMillis());
		                }
		        	});
	        	}catch(Exception e){
	        		//Do nothing
	        	}
	        }
    	}
    }
}