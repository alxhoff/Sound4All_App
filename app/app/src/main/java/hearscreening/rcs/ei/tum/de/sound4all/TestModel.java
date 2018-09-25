package hearscreening.rcs.ei.tum.de.sound4all;

import android.content.Context;
import android.nfc.Tag;
import android.os.Parcel;
import android.os.Parcelable;

import junit.framework.Test;

import java.util.HashMap;
import java.util.Map;

public class TestModel implements Parcelable{

    public static final Creator<TestModel> CREATOR = new Creator<TestModel>() {
        @Override
        public TestModel createFromParcel(Parcel in) {
            return new TestModel(in);
        }

        @Override
        public TestModel[] newArray(int size) {
            return new TestModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (patient_ID == 0) {
            dest.writeInt(0);
        } else {
            dest.writeInt(patient_ID);
        }
        if (test_ID == 0) {
            dest.writeInt(0);
        } else {
            dest.writeInt(test_ID);
        }
        dest.writeString(comment);
        if (duration == null) {
            dest.writeInt(0);
        } else {
            dest.writeFloat(duration);
        }

    }

    public enum TestType {
        NOT_SET(0),
        DPOAE(1),
        TEOAE(2);

        private int value;
        private static Map map = new HashMap<>();

        TestType(){
            this.value = 0;
        }

        TestType(int value){
            this.value = value;
        }

        static{
            for(TestType testType : TestType.values()){
                map.put(testType.value, testType);
            }
        }

        public static TestType valueOf(int testType){
            return (TestType) map.get(testType);
        }

        public int getTestType(){
            return value;
        }
    }

    public enum Ear{
        NOT_SET, LEFT, RIGHT
    }

    public enum PassFail{
        NOT_SET, PASS, FAIL
    }

    private Context context;
    private int patient_ID;
    private int test_ID;
    private TestType test_type;
    private Ear ear;
    private PassFail pass_fail;
    private String comment;
    private Float duration;
    public SettingsModel settings;

    //constructors
    public TestModel(Context context){
        this.context = context;
        this.patient_ID = 0;
        this.test_ID = 0;
        this.test_type = TestType.NOT_SET;
        this.ear = Ear.NOT_SET;
        this.pass_fail = PassFail.NOT_SET;
        this.comment = new String();
        this.duration = new Float(0);
        this.settings =  new SettingsModel(context);
    }

    public TestModel(Parcel in) {
        //adding testmodel to settingshelpers stuff
        this.patient_ID = in.readInt();
        this.test_ID = in.readInt();
        this.test_type = TestType.valueOf(in.readString());
        this.ear = Ear.valueOf(in.readString());
        this.pass_fail = PassFail.valueOf(in.readString());
        this.duration = in.readFloat();
    }

    public TestModel(Integer patient_ID, Integer test_ID){
        this.patient_ID = patient_ID;
        this.test_ID = test_ID;
    }

    public TestModel(Integer patient_ID, Integer test_ID, TestType test_type, Ear ear,
                    PassFail pass_fail, String comment, Float duration){
        this.patient_ID = patient_ID;
        this.test_ID = test_ID;
        this.test_type = test_type;
        this.ear = ear;
        this.pass_fail = pass_fail;
        this.comment = comment;
        this.duration = duration;
    }

    //setters
    public void setPatient_ID(Integer patient_ID) {
        this.patient_ID = patient_ID;
    }

    public void setTest_ID(Integer test_ID) {
        this.test_ID = test_ID;
    }

    public void setTest_type(TestType test_type) {
        this.test_type = test_type;
    }

    public void setEar(Ear ear) {
        this.ear = ear;
    }

    public void setPass_fail(PassFail pass_fail) {
        this.pass_fail = pass_fail;
    }

    public void setComment(String comment) {
        if(this.comment == null) this.comment = new String();

        this.comment = comment;
    }

    public void setDuration(Float duration) {
        if(this.duration == null) this.duration = new Float(0);
        this.duration = duration;
    }

    //getters
    public Integer getPatient_ID() {
        return patient_ID;
    }

    public Integer getTest_ID() {
        return test_ID;
    }

    public TestType getTest_type() {
        return TestType.valueOf(test_type.value);
    }

    public Ear getEar() {
        return ear;
    }

    public PassFail getPass_fail() {
        return pass_fail;
    }

    public String getComment() {
        return comment;
    }

    public Float getDuration() {
        return duration;
    }

    public Context getContext() {
        return context;
    }

    public void sendTest(Context context, Tag tag){

    }
}
