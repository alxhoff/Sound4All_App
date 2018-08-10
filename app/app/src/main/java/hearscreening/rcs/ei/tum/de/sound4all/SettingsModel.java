package hearscreening.rcs.ei.tum.de.sound4all;

import java.util.HashMap;
import java.util.Map;

public class SettingsModel {

    public enum TE_STIMULUS{
        OPTIMIZED, STANDARD
    }

    public enum DP_FREQS{
        _1K(0),
        _1K5(1),
        _2K(2),
        _3K(3),
        _4K(4),
        _5K(5),
        _6K(6),
        _8K(7);

        private int value;
        private static Map map = new HashMap<>();

        private DP_FREQS(int value) {
            this.value = value;
        }

        static {
            for (SettingsModel.DP_FREQS freqIndex : SettingsModel.DP_FREQS.values()) {
                map.put(freqIndex.value, freqIndex);
            }
        }

        public static SettingsModel.DP_FREQS valueOf(int freqIndex) {
            return (SettingsModel.DP_FREQS) map.get(freqIndex);
        }

        public int getValue() {
            return value;
        }
    }

    private int preset_ID;

    //TEOAE
    private float TE_max_duration;
    private TE_STIMULUS TE_stimulus;
    private Integer TE_num_of_passes;
    private Integer TE_stim_lvl;

    //DPOAE
    private boolean[] DP_freqs;
    private float DP_threshold;
    private Integer DP_f1;
    private Integer DP_f2;
    private Integer DP_l1;
    private Integer DL_l2;
    private float DP_max_duration;

    public SettingsModel(){}

    //setters
    public void setPreset_ID(Integer preset_ID) {
        this.preset_ID = preset_ID;
    }

    public void setDL_l2(Integer DL_l2) {
        this.DL_l2 = DL_l2;
    }

    public void setDP_f1(Integer DP_f1) {
        this.DP_f1 = DP_f1;
    }

    public void setDP_f2(Integer DP_f2) {
        this.DP_f2 = DP_f2;
    }

    public void setDP_freq(Integer index, boolean value){
        this.DP_freqs[index] = value;
    }

    public void setDP_freqs(boolean[] DP_freqs) {
        this.DP_freqs = DP_freqs;
    }

    public void setDP_l1(Integer DP_l1) {
        this.DP_l1 = DP_l1;
    }

    public void setDP_max_duration(float DP_max_duration) {
        this.DP_max_duration = DP_max_duration;
    }

    public void setDP_threshold(float DP_threshold) {
        this.DP_threshold = DP_threshold;
    }

    public void setTE_max_duration(float TE_max_duration) {
        this.TE_max_duration = TE_max_duration;
    }

    public void setTE_num_of_passes(Integer TE_num_of_passes) {
        this.TE_num_of_passes = TE_num_of_passes;
    }

    public void setTE_stim_lvl(Integer TE_stim_lvl) {
        this.TE_stim_lvl = TE_stim_lvl;
    }

    public void setTE_stimulus(TE_STIMULUS TE_stimulus) {
        this.TE_stimulus = TE_stimulus;
    }

    //getters
    public Integer getPreset_ID() {
        return preset_ID;
    }

    public float getDP_max_duration() {
        return DP_max_duration;
    }

    public float getDP_threshold() {
        return DP_threshold;
    }

    public float getTE_max_duration() {
        return TE_max_duration;
    }

    public Integer getDL_l2() {
        return DL_l2;
    }

    public Integer getDP_f1() {
        return DP_f1;
    }

    public Integer getDP_f2() {
        return DP_f2;
    }

    public Integer getDP_l1() {
        return DP_l1;
    }

    public Integer getTE_num_of_passes() {
        return TE_num_of_passes;
    }

    public Integer getTE_stim_lvl() {
        return TE_stim_lvl;
    }

    public boolean[] getDP_freqs() {
        return DP_freqs;
    }

    public boolean getDP_freq(DP_FREQS index){
        return DP_freqs[index.getValue()];
    }

    public TE_STIMULUS getTE_stimulus() {
        return TE_stimulus;
    }
}

