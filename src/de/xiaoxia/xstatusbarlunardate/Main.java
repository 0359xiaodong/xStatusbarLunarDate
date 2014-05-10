package de.xiaoxia.xstatusbarlunardate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.widget.RelativeLayout;
import android.widget.TextView;

//����xposed������
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
//import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

/* Main */
public class Main implements IXposedHookLoadPackage{

    /* ��ʼ���� */
    private static String lunarText = "LUNAR"; //��¼������ʱ�������ַ���
    private static String breaklineText = "\n"; //�Ƿ��е��ı�
    private static String lDate = ""; //�ϴμ�¼������
    private static String nDate;
    private static String finalText; //��������ı�
    private static String year; //��¼���
    private static Boolean _layout_run = false; //�ж��Ƿ����ù�singleLine����
    private final static Pattern reg = Pattern.compile("\\n");
    private static TextView textview;
    private static String term;
    private static String fest;
    private static String custom;
    private static String sfest;
    private static String sfest_custom;

    /* ��ȡ���� */
    //ʹ��xposed�ṩ��XSharedPreferences��������ȡandroid���õ�SharedPreferences����
    private final static XSharedPreferences prefs = new XSharedPreferences(Main.class.getPackage().getName());

    //���ñ�����¼��ȡ����
    protected final static Boolean _remove = prefs.getBoolean("remove", true);
    protected final static Boolean _term = prefs.getBoolean("term", true);
    protected final static Boolean _fest = prefs.getBoolean("fest", true);
    protected final static Boolean _custom = prefs.getBoolean("custom", false);
    protected final static Boolean _solar = prefs.getBoolean("solar", true);
    protected final static Boolean _solar_custom = prefs.getBoolean("solar_cutom", true);
    protected final static Boolean _breakline = prefs.getBoolean("breakline", true);
    protected final static Boolean _layout_enable = prefs.getBoolean("layout_enable", false);
    protected final static Boolean _lockscreen = prefs.getBoolean("lockscreen", false);
    protected final static int _minor = Integer.valueOf(prefs.getString("minor", "1")).intValue();
    protected final static int _lang = Integer.valueOf(prefs.getString("lang", "1")).intValue();
    protected final static int _year = Integer.valueOf(prefs.getString("year", "1")).intValue();
    protected final static int _rom = Integer.valueOf(prefs.getString("rom", "1")).intValue();
    protected final static String[] _clf = {
    	prefs.getString("custom_lunar_item_0", "").trim(),
    	prefs.getString("custom_lunar_item_1", "").trim(),
    	prefs.getString("custom_lunar_item_2", "").trim(),
    	prefs.getString("custom_lunar_item_3", "").trim(),
    	prefs.getString("custom_lunar_item_4", "").trim(),
    	prefs.getString("custom_lunar_item_5", "").trim(),
    	prefs.getString("custom_lunar_item_6", "").trim(),
    	prefs.getString("custom_lunar_item_7", "").trim(),
    	prefs.getString("custom_lunar_item_8", "").trim(),
    	prefs.getString("custom_lunar_item_9", "").trim(),
    	prefs.getString("custom_lunar_item_10", "").trim(),
    	prefs.getString("custom_lunar_item_11", "").trim(),
    	prefs.getString("custom_lunar_item_12", "").trim(),
    	prefs.getString("custom_lunar_item_13", "").trim(),
    	prefs.getString("custom_lunar_item_14", "").trim(),
    	prefs.getString("custom_lunar_item_15", "").trim()
    };
    protected final static String[] _csf = {
    	prefs.getString("custom_solar_item_0", "").trim(),
    	prefs.getString("custom_solar_item_1", "").trim(),
    	prefs.getString("custom_solar_item_2", "").trim(),
    	prefs.getString("custom_solar_item_3", "").trim(),
    	prefs.getString("custom_solar_item_4", "").trim(),
    	prefs.getString("custom_solar_item_5", "").trim(),
    	prefs.getString("custom_solar_item_6", "").trim(),
    	prefs.getString("custom_solar_item_7", "").trim(),
    	prefs.getString("custom_solar_item_8", "").trim(),
    	prefs.getString("custom_solar_item_9", "").trim(),
    	prefs.getString("custom_solar_item_10", "").trim(),
    	prefs.getString("custom_solar_item_11", "").trim(),
    	prefs.getString("custom_solar_item_12", "").trim(),
    	prefs.getString("custom_solar_item_13", "").trim(),
    	prefs.getString("custom_solar_item_14", "").trim()
    };
    //��ʼ��Lunar��
    private static Lunar lunar = new Lunar(_lang);
    
    //��ȡũ���ַ����ӳ���
    private String returnDate(String nDate){
        /* �жϵ�ǰ�������Ƿ�����ϴθ��º�������ı�
         * 1 �������,��˵��ԭ��updateClock()û�б�ִ�У�������ȥ����
         * 2 �������������˵����Ҫ����д��TextView
         *  2.1 �����ǰ�����Ѿ��ı䣬��������¼���ũ��
         *  2.2 �����ǰ����δ�ı䣬��ֻ��Ҫ�������Ѿ�������ı�д��TextView */
        if(!nDate.contains(lunarText)){
            //�ж������Ƿ�ı䣬���ı��򲻸������ݣ��ı������¼���ũ��
            if (!nDate.equals(lDate)) {
                //��ȡʱ��
                lunar.init(System.currentTimeMillis());

                //����layout��singleLine����
                if(!_layout_run){
                    //ȥ��singleLine����
                    if(prefs.getBoolean("layout_line", false)){
                        textview.setSingleLine(false);
                    }
                    //ȥ��align_baseline������������Ϊcenter_vertical
                    if(prefs.getBoolean("layout_align", false)){
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)textview.getLayoutParams();
                        layoutParams.addRule(RelativeLayout.ALIGN_BASELINE,0);
                        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
                        textview.setLayoutParams(layoutParams); 
                    }
                    //���ÿ��Ϊfill_parent
                    if(prefs.getBoolean("layout_width", false)){
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)textview.getLayoutParams();
                        layoutParams.width = -1;
                        textview.setLayoutParams(layoutParams);
                    }
                    _layout_run = true;
                }
                
                //�ж��Ƿ��ǹ������
                if (_solar && (!"".equals(lunar.getSFestivalName()))){
                	sfest = " " + lunar.getSFestivalName();
                }else{
                    sfest = "";
                }
                
                //�ж��Ƿ���ũ������
                if (_fest && (!"".equals(lunar.getLFestivalName()))){
                	fest = " " + lunar.getLFestivalName();
                }else{
                    fest = "";
                }

                //�ж��Ƿ��Ƕ�ʮ�Ľ���
                if (_term && (!"".equals(lunar.getTermString()))){
                    term = " " + lunar.getTermString();
                }else{
                    term = "";
                }
                
                //�ж��Ƿ����Զ���ũ������
                if (_custom && (!"".equals(lunar.getCLFestivalName()))){
                	custom = "��" + lunar.getCLFestivalName();
                }else{
                    custom = "";
                }
                
                //�ж��Ƿ����Զ��幫������
                if (_solar_custom && (!"".equals(lunar.getCSFestivalName()))){
                	sfest_custom = "��" + lunar.getCSFestivalName();
                }else{
                    sfest_custom = "";
                }

                //���������������
                switch(_year){
                    case 1:  year = lunar.getAnimalString() + "��";
                        break;
                    case 2:  year = lunar.getLunarYearString() + "��";
                        break;
                    case 3:  year = "";
                        break;
                    case 4:  year = lunar.getLunarYearString() + lunar.getAnimalString() + "��";
                        break;               
                }

                //���ũ���ı�
                if(_lang != 3){
                	lunarText =  year + lunar.getLunarMonthString() + "��" + lunar.getLunarDayString() + term  + fest + custom + sfest + sfest_custom;
                }else{
                	lunarText = "[" + lunar.getLunarDay() + "/" + lunar.getLunarMonth() + "]";
                }
                
                //���¼�¼������
                lDate = nDate;
                //����������ַ���
                finalText = nDate + breaklineText + lunarText;
                //�����Ҫȥ����
                if(_remove){
                    Matcher mat = reg.matcher(finalText);
                    finalText = mat.replaceFirst(" ");
                }
            }
        }
        return finalText;
    }
  
    //�滻���ں���
    public void handleLoadPackage(final LoadPackageParam lpparam){
        if (!lpparam.packageName.equals("com.android.systemui"))
            return;

        //�������Ƿ��е��ı�������ַ�����
        if(!_breakline){
            breaklineText = " ";
        }
        
        //������˵������֣����������������ֲ���
        if(!_layout_enable){
            _layout_run = true;
        }
        
        
        switch(_rom){
        	case 1: 
	    	    try{
	    	    	//For most android roms
	    	        //����com.android.systemui.statusbar.policy.DateView�����updateClock()֮��
	    	        findAndHookMethod("com.android.systemui.statusbar.policy.DateView", lpparam.classLoader, "updateClock", new XC_MethodHook() {
	    	            @Override
	    	            protected void afterHookedMethod(MethodHookParam param){
	    	                //��ȡԭ����
	    	                textview = (TextView) param.thisObject;    
	    	                nDate = textview.getText().toString();
	    	                textview.setText(returnDate(nDate));
	    	            }
	    	        });
	    		}catch(Exception e){
	    			//Do nothing
	    		}
	    	    break;
        	case 2:
	        	try{
		        	//For Miui
		            findAndHookMethod("com.android.systemui.statusbar.policy.DateView", lpparam.classLoader, "a", new XC_MethodHook() {
		                @Override
		                protected void afterHookedMethod(MethodHookParam param){
		                    //��ȡԭ����
		                    textview = (TextView) param.thisObject;    
		                    nDate = textview.getText().toString();
		                    textview.setText(returnDate(nDate));
		                }
		            });
	        	}catch(Exception e){
	        		//Do nothing
	        	}
        	break;
        }
    }
}