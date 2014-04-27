package de.xiaoxia.xstatusbarlunardate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.widget.TextView;

//����xposed������
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import de.xiaoxia.xstatusbarlunardate.Lunar;

/* Main */
public class Main implements IXposedHookLoadPackage {

    /* ��ʼ���� */
    private String lunarText = "LUNAR"; //��¼������ʱ�������ַ���
    private String breaklineText = "\n"; //�Ƿ��е��ı�
    private String lDate = ""; //�ϴμ�¼������
    private String finalText;
    private Pattern reg = Pattern.compile("\\n");

    /* ��ȡ���� */
    //ʹ��xposed�ṩ��XSharedPreferences��������ȡandroid���õ�SharedPreferences����
    private XSharedPreferences prefs = new XSharedPreferences(Main.class.getPackage().getName());

    //���ñ�����¼��ȡ����
    private final Boolean _remove = prefs.getBoolean("remove", true);
    private final Boolean _term = prefs.getBoolean("term", true);
    private final Boolean _fest = prefs.getBoolean("fest", true);
    private final String _minor = prefs.getString("minor", "1");

    //��ʼ��Lunar��
    private Lunar lunar = new Lunar();
    

    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
        if (!lpparam.packageName.equals("com.android.systemui"))
            return;

        //�������Ƿ��е��ı�������ַ�����
        if(prefs.getBoolean("breakline", true) == false){
            breaklineText = "  ";
        }
        
        //��������
        if(prefs.getString("lang", "1").equals("1")){
            lunar.changeLocale(false);
        }else{
        	lunar.changeLocale(true);
        }

        //����com.android.systemui.statusbar.policy.DateView�����updateClock()֮��
        findAndHookMethod("com.android.systemui.statusbar.policy.DateView", lpparam.classLoader, "updateClock", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                //��ȡԭ����
                TextView textview = (TextView) param.thisObject;
                String nDate = textview.getText().toString();

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

                        //�ж��Ƿ���ũ������
                        String fest = " " + lunar.getLFestivalName();
                        if ((_fest == true) && (!"".equals(fest))){
                            if(fest.equals(" С��")){
                                if((lunar.getLunarDay() == 23 && "1".equals(_minor)) || (lunar.getLunarDay() == 24 && "2".equals(_minor))  || (lunar.getLunarDay() == 25 && "3".equals(_minor))){
                                }else{
                                    fest = " ";
                                }
                            }
                        }else{
                            fest = " ";
                        }

                        //�ж��Ƿ��Ƕ�ʮ�Ľ���
                        String term = " " + lunar.getTermString();
                        if ((_term == true) && (!"".equals(term))){
                            term = " " + lunar.getTermString();
                        }else{
                            term = " ";
                        }

                        //���ũ���ı�
                        lunarText = lunar.getAnimalString() + "��" + lunar.getLunarMonthString() + "��" + lunar.getLunarDayString() + fest + term;
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
                    //����TextView
                    textview.setText(finalText);
                }
            }
        });
    }
}