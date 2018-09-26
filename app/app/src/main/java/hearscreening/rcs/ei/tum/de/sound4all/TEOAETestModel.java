package hearscreening.rcs.ei.tum.de.sound4all;

import android.content.Context;

public class TEOAETestModel extends TestModel{

    private String file_name;
    private Float duration;
    private Integer stimulation_level;
    private int maximum_duration;
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

    public void setMaximum_duration(int maximum_duration) {
        this.maximum_duration = maximum_duration;
    }

    public void setStimulation_level(Integer stimulation_level) {
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

    public int getMaximumDuration() {
        return maximum_duration;
    }

    public Integer getStimulationLevel() {
        return stimulation_level;
    }

    public String getFileName() {
        return file_name;
    }
}
