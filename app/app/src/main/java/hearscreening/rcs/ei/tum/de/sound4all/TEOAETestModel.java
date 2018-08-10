package hearscreening.rcs.ei.tum.de.sound4all;

public class TEOAETestModel extends TestModel{

    private String file_name;
    private Float duration;
    private Integer stimulation_level;
    private Float maxiimum_duration;

    public TEOAETestModel(){
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

    public Float getMaximumDuration() {
        return maxiimum_duration;
    }

    public Integer getStimulationLevel() {
        return stimulation_level;
    }

    public String getFileName() {
        return file_name;
    }
}
