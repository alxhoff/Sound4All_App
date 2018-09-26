package hearscreening.rcs.ei.tum.de.sound4all;

import android.content.Context;

import junit.framework.Test;

import java.util.Date;

public class DPOAETestModel extends TestModel{

    //DP TESTS TABLE
    private Float ratio;
    private Float threshold;
    private int maximum_duration;

    private Float noise;
    private Float DP_level;
    private Float f1;
    private Float l1;
    private Float l2;

    private int data_length;

    public DPOAETestModel(Context context){
        super(context);
        this.ratio = new Float(0);
        this.threshold = new Float(0);
        this.noise = new Float(0);
        this.DP_level = new Float(0);
        this.f1 = new Float(0);
        this.l1 = new Float(0);
        this.l2 = new Float(0);
    }

    public void setDP_level(Float DP_level) {
        this.DP_level = DP_level;
    }

    public void setF1(Float f1) {
        this.f1 = f1;
    }

    public void setL1(Float l1) {
        this.l1 = l1;
    }

    public void setL2(Float l2) {
        this.l2 = l2;
    }

    public void setMaximum_duration(int maximum_duration) {
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

    public void setData_length(int data_length){
        this.data_length = data_length;
    }

    public int getData_length() {
        return this.data_length;
    }

    public Float getDP_level() {
        return DP_level;
    }

    public Float getF1() {
        return f1;
    }

    public Float getL1() {
        return l1;
    }

    public Float getL2() {
        return l2;
    }

    public int getMaximumDuration() {
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
