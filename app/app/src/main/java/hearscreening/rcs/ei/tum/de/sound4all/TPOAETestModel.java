package hearscreening.rcs.ei.tum.de.sound4all;

public class TPOAETestModel extends TestModel{

    private String file_name;
    private Float duration;
    private Integer stimulation_level;
    private Float maxiimum_duration;

    public TPOAETestModel(){
    }

    public void setDuration(Float duration) {
        this.duration = duration;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public void setMaxiimum_duration(Float maxiimum_duration) {
        this.maxiimum_duration = maxiimum_duration;
    }

    public void setStimulation_level(Integer stimulation_level) {
        this.stimulation_level = stimulation_level;
    }

    public Float getDuration() {
        return duration;
    }

    public Float getMaxiimum_duration() {
        return maxiimum_duration;
    }

    public Integer getStimulation_level() {
        return stimulation_level;
    }

    public String getFile_name() {
        return file_name;
    }
}
