package hearscreening.rcs.ei.tum.de.sound4all;

import junit.framework.Test;

import java.util.Date;

public class DPOAETestModel extends TestModel{

    //DP TESTS TABLE
    private Float ratio;
    private Float threshold;
    private Float maximum_duration;

    private Float noise;
    private Float DP_level;
    private Float f2;
    private Float l1;
    private Float l2;

    public DPOAETestModel(){
    }

    public void setDP_level(Float DP_level) {
        this.DP_level = DP_level;
    }

    public void setF2(Float f2) {
        this.f2 = f2;
    }

    public void setL1(Float l1) {
        this.l1 = l1;
    }

    public void setL2(Float l2) {
        this.l2 = l2;
    }

    public void setMaximum_duration(Float maximum_duration) {
        this.maximum_duration = maximum_duration;
    }

    public void setNoise(Float noise) {
        this.noise = noise;
    }

    public void setRatio(Float ratio) {
        this.ratio = ratio;
    }

    public void setThreshold(Float threshold) {
        this.threshold = threshold;
    }

    public Float getDP_level() {
        return DP_level;
    }

    public Float getF2() {
        return f2;
    }

    public Float getL1() {
        return l1;
    }

    public Float getL2() {
        return l2;
    }

    public Float getMaximumDuration() {
        return maximum_duration;
    }

    public Float getNoise() {
        return noise;
    }

    public Float getRatio() {
        return ratio;
    }

    public Float getThreshold() {
        return threshold;
    }
}
