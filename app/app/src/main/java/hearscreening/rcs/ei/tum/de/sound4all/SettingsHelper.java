package hearscreening.rcs.ei.tum.de.sound4all;

import android.content.Context;
import android.content.SharedPreferences;

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

    public void compileSettings(){

    }

}
