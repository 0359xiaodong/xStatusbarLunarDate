package de.xiaoxia.xstatusbarlunardate;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;

public class SettingCustomLunar extends PreferenceActivity implements OnSharedPreferenceChangeListener{

	//��ʼ������ lp
    EditTextPreference lp;

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting_custom_lunar);

        //���÷��ذ�ť
        getActionBar().setDisplayHomeAsUpEnabled(true);

        //�ҵ����ã�����������޸�Ϊ��ǰ����option_name
        for(int i = 0; i < 15; i++){
            lp = (EditTextPreference)findPreference("custom_lunar_item_" + i);
            if(!"".equals(lp.getText()) && lp.getText() != null)
                lp.setSummary(lp.getText());
            lp.setTitle(getString(R.string.custom_lunar) + " " + (i + 1));
        }

        //����sharedPreferences�仯
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);
    }

    @SuppressWarnings("deprecation")
    @Override
    //������sharedPreferences�仯��Ĵ���
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        //���÷����仯ʱ������summaryΪoption_name
        for(int i = 0; i < 15; i++){
            if(key.equals("custom_lunar_item_" + i)){
                lp = (EditTextPreference)findPreference("custom_lunar_item_" + i);
                if(!"".equals(lp.getText()) && lp.getText() != null){
                	//�����ѡ���ֵ��Ϊ���ַ������Ҳ�Ϊ�գ�����summary����Ϊ���������
                    lp.setSummary(lp.getText());
                }else{
                	//������ʾ��δ���á�
                    lp.setSummary(getString(R.string.custom_lunar_summary));
                }
                break;
            }
        }
    }
}
