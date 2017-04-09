package com.sleepingbear.english;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;

public class SettingsActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener{

    private static final String TAG = "PreSettingsActivity";

    private static final String USE_USER_NAME = "key_useUserName";
    private static final String USER_NAME = "key_userName";
    private static final String USE_BACKGROUND_COLOR = "key_backgroundcolor";
    private static final String BACKGROUND_COLOR = "key_dialog_backgroundcolor";
    private static final String TEXT_COLOR = "key_textcolor";
    private static final String ALL_REMOVE_MEMO = "key_all_memo_clear";

    private PreferenceScreen screen;
    private ListPreference mFontSize;
    private CheckBoxPreference mUseUsername;
    private EditTextPreference mUsername;
    private ListPreference mbackgroundcolor;
    private ListPreference mTextcolor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_settings);

        screen = getPreferenceScreen();

        mFontSize = (ListPreference) screen.findPreference("key_fontSize");

        //인자로 전달되는 Key값을 가지는 Preference 항목의 인스턴스를 가져옴
        //굳이 여러곳에서 사용하지 않는 이상에는 이런식으로 객체화 시킬필요는 없는듯
        mUsername = (EditTextPreference) screen.findPreference(USER_NAME);
        mbackgroundcolor = (ListPreference) screen.findPreference(BACKGROUND_COLOR);
        mTextcolor = (ListPreference) screen.findPreference(TEXT_COLOR);

        //변화 이벤트가 일어났을 시 동작
        mFontSize.setOnPreferenceChangeListener(this);

        mUsername.setOnPreferenceChangeListener(this);
        mbackgroundcolor.setOnPreferenceChangeListener(this);
        mTextcolor.setOnPreferenceChangeListener(this);
    }

    @Override
    public void onResume(){

        super.onResume();

        updateSummary();
        DicUtils.dicLog("onResume");
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
                                         Preference preference) {
        DicUtils.dicLog("onPreferenceTreeClick : " + preference.getKey());
        if ( preference.getKey().equals("key_backup") ) {
        } else if ( preference.getKey().equals("key_recovery") ) {
        } else if ( preference.getKey().equals("key_voc_clear") ) {
        } else if ( preference.getKey().equals("key_conv_clear") ) {

        }
        return false;
    }


    public boolean onPreferenceChange(Preference preference, Object newValue) {
        DicUtils.dicLog("preference : " + preference +", newValue : "+ newValue);

        String value = (String) newValue;
        if ( preference == mFontSize ) {
            ListPreference listPreference = (ListPreference) preference;
            int index = listPreference.findIndexOfValue(value);
            mFontSize.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);
        } else if(preference == mUsername){
            DicUtils.dicLog("mUsername onPreferenceChange");
            mUsername.setSummary(value);
        }else if(preference == mbackgroundcolor){
            ListPreference listPreference = (ListPreference) preference;
            int index = listPreference.findIndexOfValue(value);
            mbackgroundcolor.setSummary(index >= 0 ? listPreference.getEntries()[index]
                    : null);    // entries 값 대신 이에 해당하는 entryValues값 set
        }else if(preference == mTextcolor){
            ListPreference listPreference = (ListPreference) preference;
            int index = listPreference.findIndexOfValue(value);
            mTextcolor.setSummary(index >= 0 ? listPreference.getEntries()[index]
                    : null);    // entries 값 대신 이에 해당하는 entryValues값 set
        }
        return true;
    }


    private void updateSummary(){
        mFontSize.setSummary(mFontSize.getEntry());
        mUsername.setSummary(mUsername.getText());
        mbackgroundcolor.setSummary(mbackgroundcolor.getEntry());
        mTextcolor.setSummary(mTextcolor.getEntry());
        DicUtils.dicLog("mbackgroundcolor="+mbackgroundcolor +", mUsername :" + mUsername);
    }
}
