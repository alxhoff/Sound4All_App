package hearscreening.rcs.ei.tum.de.sound4all;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    DatabaseHelper myDb;
    SettingsHelper settingsHelper;

    Button clear_db;
    Button btn_drop_patients;

    //settings views
    TextView TE_max_dur;
    Spinner TE_max_dur_spin;
    RadioGroup TE_stimulus;
    RadioGroup TE_num_passes;
    RadioGroup TE_stim_lvl;

    CheckBox DP_freq_1k;
    CheckBox DP_freq_1k5;
    CheckBox DP_freq_2k;
    CheckBox DP_freq_3k;
    CheckBox DP_freq_4k;
    CheckBox DP_freq_5k;
    CheckBox DP_freq_6k;
    CheckBox DP_freq_8k;
    TextView DP_thresh_tv;
    EditText DP_thresh_et;
    Button DP_thresh_btn;
    TextView DP_f1_tv;
    EditText DP_f1_et;
    Button DP_f1_btn;
    TextView DP_f2_tv;
    EditText DP_f2_et;
    Button DP_f2_btn;
    TextView DP_l1_tv;
    EditText DP_l1_et;
    Button DP_l1_btn;
    TextView DP_l2_tv;
    EditText DP_l2_et;
    Button DP_l2_btn;
    TextView DP_max_dur;
    Spinner DP_max_dur_spin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        myDb = new DatabaseHelper(this);

        clear_db = (Button) findViewById(R.id.btn_settings_clear_db);
        clear_db.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDb.removeAllPatients();

                Cursor all_data = myDb.getAllPatientData();

                if(all_data.getCount() == 0)
                    Toast.makeText(SettingsActivity.this, "Database cleared", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(SettingsActivity.this, "Database error", Toast.LENGTH_LONG).show();
            }
        });

        btn_drop_patients = (Button) findViewById(R.id.btn_settings_drop_patients);
        btn_drop_patients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDb.dropPatientable();
            }
        });

        TE_max_dur_spin = (Spinner) findViewById(R.id.sp_set_teoe_max_dur);
//        TE_stimulus = (RadioGroup) findViewById(R.id.rbg_set_teoe_stim);
        TE_num_passes = (RadioGroup) findViewById(R.id.rbg_set_teoe_no_pass);
//        TE_stim_lvl = (RadioGroup) findViewById(R.id.rbg_set_teoe_stim_lvl);

        DP_freq_1k = (CheckBox) findViewById(R.id.cb_set_dpoae_freq_1k);
        DP_freq_1k5 = (CheckBox) findViewById(R.id.cb_set_dpoae_freq_1k5);
        DP_freq_2k = (CheckBox) findViewById(R.id.cb_set_dpoae_freq_2k);
        DP_freq_3k = (CheckBox) findViewById(R.id.cb_set_dpoae_freq_3k);
        DP_freq_4k = (CheckBox) findViewById(R.id.cb_set_dpoae_freq_4k);
        DP_freq_5k = (CheckBox) findViewById(R.id.cb_set_dpoae_freq_5k);
        DP_freq_6k = (CheckBox) findViewById(R.id.cb_set_dpoae_freq_6k);
        DP_freq_8k = (CheckBox) findViewById(R.id.cb_set_dpoae_freq_8k);
        DP_thresh_tv = (TextView) findViewById(R.id.tv_set_dpoae_thresh_val);
        DP_thresh_et = (EditText) findViewById(R.id.et_set_dpoae_thresh_val);
        DP_f1_tv = (TextView) findViewById(R.id.tv_set_dpoae_f1_val);
        DP_f1_et = (EditText) findViewById(R.id.et_set_dpoae_f1_val);
        DP_f2_tv = (TextView) findViewById(R.id.tv_set_dpoae_f1_val);
        DP_f2_et = (EditText) findViewById(R.id.et_set_dpoae_f1_val);
        DP_l1_tv = (TextView) findViewById(R.id.tv_set_dpoae_l1_val);
        DP_l1_et = (EditText) findViewById(R.id.et_set_dpoae_l1_val);
        DP_l2_tv = (TextView) findViewById(R.id.tv_set_dpoae_l2_val);
        DP_l2_et = (EditText) findViewById(R.id.et_set_dpoae_l2_val);
        DP_max_dur_spin = (Spinner) findViewById(R.id.sp_set_dpoae_max_dur);
        DP_thresh_btn = (Button) findViewById(R.id.btn_set_dpoae_thresh_val);
        DP_f1_btn = (Button) findViewById(R.id.btn_set_dpoae_f1_val);
        DP_f2_btn = (Button) findViewById(R.id.btn_set_dpoae_f2_val);
        DP_l1_btn = (Button) findViewById(R.id.btn_set_dpoae_l1_val);
        DP_l2_btn = (Button) findViewById(R.id.btn_set_dpoae_l2_val);

        //get current settings
        settingsHelper = new SettingsHelper(this);
        settingsHelper.actualizeSettingPreset();

        //set listeners
        TE_max_dur_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                settingsHelper.settings.setTE_max_duration(Float.parseFloat(selectedItem));
                settingsHelper.updateSettingPreset();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        DP_max_dur_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                settingsHelper.settings.setDP_max_duration(Float.parseFloat(selectedItem));
                settingsHelper.updateSettingPreset();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        TE_num_passes.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.rb_set_teoe_no_pass_3:
                        settingsHelper.settings.setTE_num_of_passes(3);
                        break;
                    case R.id.rb_set_teoe_no_pass_4:
                        settingsHelper.settings.setTE_num_of_passes(4);
                        break;
                    case R.id.rb_set_teoe_no_pass_5:
                        settingsHelper.settings.setTE_num_of_passes(5);
                        break;
                }

                settingsHelper.updateSettingPreset();
            }
        });

        DP_freq_1k.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true)
                    settingsHelper.settings.setDP_freq(SettingsModel.DP_FREQS._1K.getValue(), 1);
                else
                    settingsHelper.settings.setDP_freq(SettingsModel.DP_FREQS._1K.getValue(), 0);
                settingsHelper.updateSettingPreset();
            }
        });

        DP_freq_1k5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true)
                    settingsHelper.settings.setDP_freq(SettingsModel.DP_FREQS._1K5.getValue(), 1);
                else
                    settingsHelper.settings.setDP_freq(SettingsModel.DP_FREQS._1K5.getValue(), 0);
                settingsHelper.updateSettingPreset();
            }
        });

        DP_freq_2k.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true)
                    settingsHelper.settings.setDP_freq(SettingsModel.DP_FREQS._2K.getValue(), 1);
                else
                    settingsHelper.settings.setDP_freq(SettingsModel.DP_FREQS._2K.getValue(), 0);
                settingsHelper.updateSettingPreset();
            }
        });

        DP_freq_3k.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true)
                    settingsHelper.settings.setDP_freq(SettingsModel.DP_FREQS._3K.getValue(), 1);
                else
                    settingsHelper.settings.setDP_freq(SettingsModel.DP_FREQS._3K.getValue(), 0);
                settingsHelper.updateSettingPreset();
            }
        });

        DP_freq_4k.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true)
                    settingsHelper.settings.setDP_freq(SettingsModel.DP_FREQS._4K.getValue(), 1);
                else
                    settingsHelper.settings.setDP_freq(SettingsModel.DP_FREQS._4K.getValue(), 0);
                settingsHelper.updateSettingPreset();
            }
        });

        DP_freq_5k.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true)
                    settingsHelper.settings.setDP_freq(SettingsModel.DP_FREQS._5K.getValue(), 1);
                else
                    settingsHelper.settings.setDP_freq(SettingsModel.DP_FREQS._5K.getValue(), 0);
                settingsHelper.updateSettingPreset();
            }
        });

        DP_freq_6k.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true)
                    settingsHelper.settings.setDP_freq(SettingsModel.DP_FREQS._6K.getValue(), 1);
                else
                    settingsHelper.settings.setDP_freq(SettingsModel.DP_FREQS._6K.getValue(), 0);
                settingsHelper.updateSettingPreset();
            }
        });

        DP_freq_8k.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true)
                    settingsHelper.settings.setDP_freq(SettingsModel.DP_FREQS._8K.getValue(), 1);
                else
                    settingsHelper.settings.setDP_freq(SettingsModel.DP_FREQS._8K.getValue(), 0);
                settingsHelper.updateSettingPreset();
            }
        });

        DP_thresh_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Float setValue = Float.parseFloat(DP_thresh_et.getText().toString());
                DP_thresh_tv.setText(setValue.toString());
                settingsHelper.settings.setDP_threshold(setValue);
                settingsHelper.updateSettingPreset();
            }
        });

        DP_f1_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Float setValue = Float.parseFloat(DP_f1_et.getText().toString());
                settingsHelper.settings.setDP_f1(setValue);
                settingsHelper.updateSettingPreset();
            }
        });

        DP_f2_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Float setValue = Float.parseFloat(DP_f2_et.getText().toString());
                settingsHelper.settings.setDP_f2(setValue);
                settingsHelper.updateSettingPreset();
            }
        });

        DP_l1_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Float setValue = Float.parseFloat(DP_l1_et.getText().toString());
                settingsHelper.settings.setDP_l1(setValue);
                settingsHelper.updateSettingPreset();
            }
        });

        DP_l2_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Float setValue = Float.parseFloat(DP_l2_et.getText().toString());
                settingsHelper.settings.setDP_l2(setValue);
                settingsHelper.updateSettingPreset();
            }
        });

        loadSettingsPreset(settingsHelper.getSettings());
    }

    void loadSettingsPreset(SettingsModel settings){

        switch((int)settings.getTE_max_duration()){
            case 15:
                TE_max_dur_spin.setSelection(0);
                break;
            case 30:
                TE_max_dur_spin.setSelection(1);
                break;
            case 45:
                TE_max_dur_spin.setSelection(2);
                break;
            default:
                TE_max_dur_spin.setSelection(1);
                break;
        }

        if(settings.getTE_stimulus() == SettingsModel.TE_STIMULUS.STANDARD)
            ((RadioButton) findViewById(R.id.rb_set_teoe_stim_std)).setChecked(true);
        else
            ((RadioButton) findViewById(R.id.rb_set_teoe_stim_opt)).setChecked(true);

        if(settings.getTE_num_of_passes() == 3)
            ((RadioButton) findViewById(R.id.rb_set_teoe_no_pass_3)).setChecked(true);
        else if(settings.getTE_num_of_passes() == 4)
            ((RadioButton) findViewById(R.id.rb_set_teoe_no_pass_4)).setChecked(true);
        else
            ((RadioButton) findViewById(R.id.rb_set_teoe_no_pass_5)).setChecked(true);

        switch (settings.getTE_stim_lvl()){
            case 60:
                ((RadioButton) findViewById(R.id.rb_set_teoe_stim_lvl_60)).setChecked(true);
                break;
            case 65:
                ((RadioButton) findViewById(R.id.rb_set_teoe_stim_lvl_65)).setChecked(true);
                break;
            case 70:
                ((RadioButton) findViewById(R.id.rb_set_teoe_stim_lvl_70)).setChecked(true);
                break;
            case 75:
                ((RadioButton) findViewById(R.id.rb_set_teoe_stim_lvl_75)).setChecked(true);
                break;
            case 80:
                ((RadioButton) findViewById(R.id.rb_set_teoe_stim_lvl_80)).setChecked(true);
                break;
            case 85:
                ((RadioButton) findViewById(R.id.rb_set_teoe_stim_lvl_85)).setChecked(true);
                break;
            default:
                ((RadioButton) findViewById(R.id.rb_set_teoe_stim_lvl_60)).setChecked(true);
                break;
        }

        if(settings.getDP_freq(SettingsModel.DP_FREQS._1K) == true)
            DP_freq_1k.setChecked(true);
        if(settings.getDP_freq(SettingsModel.DP_FREQS._1K5) == true)
            DP_freq_1k5.setChecked(true);
        if(settings.getDP_freq(SettingsModel.DP_FREQS._2K) == true)
            DP_freq_2k.setChecked(true);
        if(settings.getDP_freq(SettingsModel.DP_FREQS._3K) == true)
            DP_freq_3k.setChecked(true);
        if(settings.getDP_freq(SettingsModel.DP_FREQS._4K) == true)
            DP_freq_4k.setChecked(true);
        if(settings.getDP_freq(SettingsModel.DP_FREQS._5K) == true)
            DP_freq_5k.setChecked(true);
        if(settings.getDP_freq(SettingsModel.DP_FREQS._6K) == true)
            DP_freq_6k.setChecked(true);
        if(settings.getDP_freq(SettingsModel.DP_FREQS._8K) == true)
            DP_freq_8k.setChecked(true);

        DP_thresh_tv.setText(Float.toString(settings.getDP_threshold()));
        DP_f1_tv.setText(Float.toString(settings.getDP_f1()));
        DP_f2_tv.setText(Float.toString(settings.getDP_f2()));
        DP_l1_tv.setText(Float.toString(settings.getDP_l1()));
        DP_l2_tv.setText(Float.toString(settings.getDP_l2()));

        switch((int)settings.getDP_max_duration()){
            case 15:
                DP_max_dur_spin.setSelection(0);
                break;
            case 30:
                DP_max_dur_spin.setSelection(1);
                break;
            case 45:
                DP_max_dur_spin.setSelection(2);
                break;
            default:
                DP_max_dur_spin.setSelection(1);
                break;
        }
    }
}
