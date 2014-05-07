package de.xiaoxia.xstatusbarlunardate;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;

public class SettingCustomLunar extends PreferenceActivity implements OnSharedPreferenceChangeListener{

    EditTextPreference lp;

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting_custom_lunar);

        //�ҵ����ã�����������޸�Ϊ��ǰ����option_name
        for(int i = 0; i < 10; i++){
        	lp = (EditTextPreference)findPreference("custom_lunar_item_" + i);
        	if(!"".equals(lp.getText()) && lp.getText() != null){
        		lp.setSummary(lp.getText());
        	}
        	lp.setTitle(lp.getTitle().toString() + " " + (i + 1));
        }
        
        //����sharedPreferences�仯
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);
    }

	@SuppressWarnings("deprecation")
	@Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    	//���÷����仯ʱ������summaryΪoption_name
        for(int i = 0; i < 10; i++){
        	if(key.equals("custom_lunar_item_" + i)){
	        	lp = (EditTextPreference)findPreference("custom_lunar_item_" + i);
	        	if(!"".equals(lp.getText()))
	        		lp.setSummary(lp.getText());
	        	break;
        	}
        }
    }
}
