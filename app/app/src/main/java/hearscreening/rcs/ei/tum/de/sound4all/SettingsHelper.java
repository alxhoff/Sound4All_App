package hearscreening.rcs.ei.tum.de.sound4all;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class SettingsHelper {

    private Context context;
    private int current_preset_id;
    public SettingsModel settings;

    public SettingsHelper(Context context){
        this.context = context;
        this.settings = new SettingsModel(context);
    }

    public void setCurrent_preset_id(int current_preset_id) {
        this.current_preset_id = current_preset_id;
    }

    public int getCurrent_preset_id() {
        return current_preset_id;
    }

    public SettingsModel getSettings() {
        return settings;
    }

    public void setActiveSettingPreset(int preset_id){
        SharedPreferences.Editor editor = this.settings.sharedPreferences.edit();
        editor.putInt(this.context.getResources().getString(R.string.active_settings_preset_key),
                preset_id);
        editor.commit();
    }

    public int getActiveSettingPreset(){
        return this.settings.sharedPreferences.getInt(
                this.context.getResources().getString(R.string.active_settings_preset_key), 1);
    }

    public void actualizeSettingPreset(){
        DatabaseHelper myDb = new DatabaseHelper(this.context);
        this.settings =  myDb.getSettings(this.getActiveSettingPreset());
    }

    public void updateSettingPreset(){
        DatabaseHelper myDb = new DatabaseHelper(this.context);
        myDb.updateSettings(this.settings);
    }

    private byte[] splitFloat(float to_split){
        byte[] ret = new byte[4];
        int tmp = Float.floatToRawIntBits(to_split);
        ret[3] = (byte)((Float.floatToRawIntBits(to_split) >> 24) & 0xFF);
        ret[2] = (byte)((Float.floatToRawIntBits(to_split) >> 16) & 0xFF);
        ret[1] = (byte)((Float.floatToRawIntBits(to_split) >> 8) & 0xFF);
        ret[0] = (byte)(Float.floatToRawIntBits(to_split) & 0xFF);

        return ret;
    }

    private void addByteArray(List<Byte> list, byte[] array){
        for(byte i = 0; i < array.length; i++)
            list.add(array[i]);
    }

    public List<Byte> compileSettings(TestModel.TestType testType){
        List<Byte> settings_bytes = new ArrayList<Byte>();
        //test type
        if(testType == TestModel.TestType.TEOAE)
            settings_bytes.add((byte)1);
        else
            settings_bytes.add((byte)0);
        //TE settings
        //max dur (seconds)
        settings_bytes.add(settings.getTE_max_duration());
        //compile stimulus, SNR and num of passes into byte
        byte tmp = (byte)(settings.getTE_stimulus() << 5);
        tmp |= (byte)(settings.getTE_SNR() << 3);
        tmp |= (byte)settings.getTE_num_of_passes();
        settings_bytes.add(tmp);
        //stim level
        settings_bytes.add(settings.getTE_stim_lvl());

        //DP settings
        settings_bytes.add(settings.getDP_max_duration());
        tmp = (byte)(settings.getDP_num_of_passes() << 2);
        tmp |= settings.getDP_SNR();
        settings_bytes.add(tmp);

        //DP threshold
        byte[] tmp_array = splitFloat(settings.getDP_threshold());
        addByteArray(settings_bytes, tmp_array);

        tmp_array = splitFloat(settings.getDP_f1());
        addByteArray(settings_bytes, tmp_array);

        tmp_array = splitFloat(settings.getDP_l1());
        addByteArray(settings_bytes, tmp_array);

        tmp_array = splitFloat(settings.getDP_l2());
        addByteArray(settings_bytes, tmp_array);

        return  settings_bytes;
    }
}
