package de.xiaoxia.xstatusbarlunardate;

import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.app.AlertDialog;
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
            //Android SDK �汾С��4.2ʱ����ʾsummaryΪ�����ã���������Ϊ������
            _lp.setSummary(getString(R.string.lockscreen_alignment_disable));
            _lp.setEnabled(false);
        }else{
            //����...
            if(lp.getValue().toString().equals("1")){
                //���lockscreen_layoutֵ��Ϊ��1��������Ϊ���������֣������ѡ����Ϊ������
                _lp.setEnabled(false);
            }else{
                //������Ϊ����
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

    //����ActionBar���Ͻǰ�ť
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.about, menu);
        return true;
    }

    //��ť�����Ϊ����Ϊû�ж�����ť������Ҫ�жϵ������
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        LayoutInflater inflater = LayoutInflater.from(this);
        final View textEntryView = inflater.inflate(R.layout.about, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_launcher);
        builder.setTitle(R.string.about);
        builder.setView(textEntryView);
        builder.setPositiveButton(R.string.ok, null);
        builder.show(); 
        return true;
    }
}
