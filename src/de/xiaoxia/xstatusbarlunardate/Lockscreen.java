package de.xiaoxia.xstatusbarlunardate;

import android.annotation.SuppressLint;
import android.os.Build;
import android.widget.TextClock;
import android.widget.TextView;

//����xposed������
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

/* Main */
public class Lockscreen implements IXposedHookLoadPackage{

    /* ��ʼ���� */
    private String lunarText; //��¼������ʱ�������ַ���
    private String year; //��¼���
    private Lunar lunar = new Lunar(Main._lang);
 
    private String returnText(){
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
            case 4:  year = lunar.getLunarYearString() + "��" + lunar.getAnimalString() + "����";
                break;
            default: year = lunar.getAnimalString() + "��";
        
        }

        //���ũ���ı�
        if(Main._lang != 3){
            lunarText =  year + lunar.getLunarMonthString() + "��" + lunar.getLunarDayString() + fest + term;
        }else{
            lunarText = "[" + lunar.getLunarDay() + "/" + lunar.getLunarMonth() + "]";
        }
		return lunarText.trim();
    }

    //�滻���ں���
    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
        if(!lpparam.packageName.equals("android") || !Main._lockscreen)
            return;

        if(Build.VERSION.SDK_INT <= 16) {
	        Class<?> kgStatusViewManagerClass = XposedHelpers.findClass("com.android.internal.policy.impl.KeyguardStatusViewManager", null);
	        XposedHelpers.findAndHookMethod(kgStatusViewManagerClass, "refreshDate", new XC_MethodHook() {
	
	        	@Override
	            protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
	        		TextView textview = (TextView) XposedHelpers.getObjectField(param.thisObject, "mDateView");
 	                textview.setText((String) textview.getText().toString() + " - " + returnText());
	            }
	        });
        }else if(Build.VERSION.SDK_INT <= 18){
        	Class<?> kgStatusViewManagerClass = XposedHelpers.findClass("com.android.internal.policy.impl.keyguard.KeyguardStatusView", null);
 	        XposedHelpers.findAndHookMethod(kgStatusViewManagerClass, "refreshDate", new XC_MethodHook() {
 	
 	        	@Override
 	            protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
 	                TextView textview = (TextView) XposedHelpers.getObjectField(param.thisObject, "mDateView");
 	                textview.setText((String) textview.getText().toString() + " - " + returnText());
 	            }
 	        });
        }else if(Build.VERSION.SDK_INT == 19){
        	Class<?> kgStatusViewManagerClass = XposedHelpers.findClass("com.android.keyguard.KeyguardStatusView", null);
 	        XposedHelpers.findAndHookMethod(kgStatusViewManagerClass, "refresh", new XC_MethodHook() {
 	
 	        	@SuppressLint("NewApi")
 	        	@Override
 	            protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
 	        		TextClock textclock = (TextClock) XposedHelpers.getObjectField(param.thisObject, "mDateView");
 	        		textclock.setText((String) textclock.getText().toString() + " - " + returnText());
 	            }
 	        });        	
        }
    }
}