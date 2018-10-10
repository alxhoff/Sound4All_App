package hearscreening.rcs.ei.tum.de.sound4all;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TEOAETestModel extends TestModel{

    private String file_name;
    private Float duration;
    private byte stimulation_level;
    private byte maximum_duration;
    private int data_length;

    public TEOAETestModel(Context context){
        super(context);
        this.file_name = new String();
        this.duration = new Float(0);
    }

    public void setDuration(Float duration) {
        this.duration = duration;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public void setMaximum_duration(byte maximum_duration) {
        this.maximum_duration = maximum_duration;
    }

    public void setStimulation_level(byte stimulation_level) {
        this.stimulation_level = stimulation_level;
    }

    public void setData_length(int data_length){
        this.data_length = data_length;
    }

    public int getData_length() {
        return this.data_length;
    }

    public Float getDuration() {
        return duration;
    }

    public byte getMaximumDuration() {
        return maximum_duration;
    }

    public byte getStimulationLevel() {
        return stimulation_level;
    }

    public String getFileName() {
        return file_name;
    }

    public List<Byte> compileSettings(){
        SettingsHelper settingsHelper = new SettingsHelper(this.getContext());

        List<Byte> compiled_settings = new ArrayList<Byte>();

        settingsHelper.addByteArray(compiled_settings, this.getFileName().getBytes());

        byte[] tmp_array = settingsHelper.splitFloat(this.getDuration());

        settingsHelper.addByteArray(compiled_settings, tmp_array);

        compiled_settings.add(this.getStimulationLevel());
        compiled_settings.add(this.getMaximumDuration());

        settingsHelper.addByteArray(compiled_settings,
                settingsHelper.splitInt(this.getData_length()));

        return compiled_settings;
    }
}
