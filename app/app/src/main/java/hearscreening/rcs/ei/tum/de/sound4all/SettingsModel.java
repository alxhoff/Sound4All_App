package hearscreening.rcs.ei.tum.de.sound4all;

import android.content.Context;
import android.content.SharedPreferences;
import android.nfc.Tag;

import java.util.HashMap;
import java.util.Map;

public class SettingsModel {

    private Context context;

    public enum TE_STIMULUS{
        OPTIMIZED, STANDARD
    }

    public enum SNR_dBs{
        _3(3),
        _6(6),
        _9(9),
        _12(12);

        private int value;
        private static Map map = new HashMap<>();

        SNR_dBs(int value){
            this.value = value;
        }

        static{
            for(SettingsModel.SNR_dBs dBIndex : SettingsModel.SNR_dBs.values()){
                map.put(dBIndex.value, dBIndex);
            }
        }

        public static SettingsModel.SNR_dBs valueOf(int dBIndex) {
            return (SettingsModel.SNR_dBs) map.get(dBIndex);
        }

        public int getValue() {
            return value;
        }
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

        DP_FREQS(int value) {
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
    private SNR_dBs TE_SNR;
    private int TE_num_of_passes;
    private int TE_stim_lvl;

    //DPOAE
    private int DP_num_of_passes;
    private boolean[] DP_freqs;
    private SNR_dBs DP_SNR;
    private float DP_threshold;
    private float DP_f1;
    private float DP_l1;
    private float DL_l2;
    private float DP_max_duration;

    //compiled config bits
    // | 8 bit: max dur(sec) | 1 bit: Stimulus- 1=optimized, 0=standard | \
    // |3 bit: stim level- 60=1, 65=2, 70=3. 75=4, 80=5, 85=6

    //app preferences
    SharedPreferences sharedPreferences;

    public SettingsModel(Context context){
        this.DP_freqs = new boolean[8];
        this.DP_SNR = SNR_dBs._3;
        this.TE_SNR = SNR_dBs._3;
        this.TE_stimulus = TE_STIMULUS.STANDARD;
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(
                context.getResources().getString(R.string.preferences_key), Context.MODE_PRIVATE);
    }

    //setters
    public void setPreset_ID(int preset_ID) {
        this.preset_ID = preset_ID;
    }

    public void setDP_l2(float DL_l2) {
        this.DL_l2 = DL_l2;
    }

    public void setDP_f1(float DP_f1) {
        this.DP_f1 = DP_f1;
    }

    public void setDP_freq(int index, int value){
        this.DP_freqs[index] = (value != 0);
    }

    public void setDP_freqs(boolean[] DP_freqs) {
        this.DP_freqs = DP_freqs;
    }

    public void setDP_SNR(int DP_SNR) {
        this.DP_SNR.value = DP_SNR;
    }

    public void setDP_l1(float DP_l1) {
        this.DP_l1 = DP_l1;
    }

    public void setDP_max_duration(float DP_max_duration) {
        this.DP_max_duration = DP_max_duration;
    }

    public void setDP_num_of_passes(Integer DP_num_of_passes) {
        this.DP_num_of_passes = DP_num_of_passes;
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

    public void setTE_SNR(int DP_SNR) {
        this.TE_SNR.value = DP_SNR;
    }

    //getters
    public int getPreset_ID() {
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

    public float getDP_l2() {
        return DL_l2;
    }

    public float getDP_f1() {
        return DP_f1;
    }

    public float getDP_l1() {
        return DP_l1;
    }

    public Integer getDP_num_of_passes() {
        return DP_num_of_passes;
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

    public int getDP_SNR() {
        return DP_SNR.value;
    }

    public int getTE_SNR() {
        return TE_SNR.value;
    }

    public boolean getDP_freq(DP_FREQS index){
        return DP_freqs[index.getValue()];
    }

    public TE_STIMULUS getTE_stimulus() {
        return TE_stimulus;
    }

    public void sendConfig(Context context, Tag tag, TestModel.TestType testType){
        NFCHelper nfcHelper = new NFCHelper(context);

        switch(testType){
            case DPOAE:

                break;
            case TEOAE:
                //compile data
                break;
            default:
                break;
        }
    }
}

