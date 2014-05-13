package de.xiaoxia.xstatusbarlunardate;

import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;

public class Setting extends PreferenceActivity implements OnSharedPreferenceChangeListener{

    ListPreference lp;
	ListPreference _lp;

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting);

        //�ҵ����ã�����������޸�Ϊ��ǰ����option_name
        
        
        lp = (ListPreference)findPreference("minor");
        lp.setSummary(lp.getEntry());

        lp = (ListPreference)findPreference("lang");
        lp.setSummary(lp.getEntry());

        lp = (ListPreference)findPreference("year");
        lp.setSummary(lp.getEntry());

        lp = (ListPreference)findPreference("rom");
        lp.setSummary(lp.getEntry());

        lp = (ListPreference)findPreference("lockscreen_layout");
        lp.setSummary(lp.getEntry());

    	_lp = (ListPreference)findPreference("lockscreen_alignment");
        if(Build.VERSION.SDK_INT < 17){
        	_lp.setSummary(getString(R.string.lockscreen_alignment_disable));
        	_lp.setEnabled(false);
        }else{
            if(lp.getValue().toString().equals("1")){
            	_lp.setEnabled(false);
            }else{
            	_lp.setEnabled(true);
            }
        	_lp.setSummary(_lp.getEntry());
        }

        //����sharedPreferences�仯
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    	
        //���÷����仯ʱ������summaryΪoption_name
        if(key.equals("minor")){
            lp = (ListPreference)findPreference("minor");
            lp.setSummary(lp.getEntry());
            return;
        }
        if(key.equals("lang")){
            lp = (ListPreference)findPreference("lang");
            lp.setSummary(lp.getEntry());
            return;
        }
        if(key.equals("year")){
            lp = (ListPreference)findPreference("year");
            lp.setSummary(lp.getEntry());
            return;
        }
        if(key.equals("rom")){
            lp = (ListPreference)findPreference("rom");
            lp.setSummary(lp.getEntry());
            return;
        }
        if(key.equals("lockscreen_alignment")){
            lp = (ListPreference)findPreference("lockscreen_alignment");
            lp.setSummary(lp.getEntry());
            return;
        }
        if(key.equals("lockscreen_layout")){
            lp = (ListPreference)findPreference("lockscreen_layout");
            lp.setSummary(lp.getEntry());
        	_lp = (ListPreference)findPreference("lockscreen_alignment");
            if(Build.VERSION.SDK_INT < 17){
            	_lp.setSummary(getString(R.string.lockscreen_alignment_disable));
            	_lp.setEnabled(false);
            }else{
	            if(lp.getValue().toString().equals("1")){
	            	_lp.setEnabled(false);
	            }else{
	            	_lp.setEnabled(true);
	            }
            }
            return;
        }
    }
}
