package hearscreening.rcs.ei.tum.de.sound4all;

public class TestModel {

    public enum TestType {
        DPOAE, TEOAE
    }

    public enum Ear{
        LEFT, RIGHT
    }

    public enum PassFail{
        PASS, FAIL
    }

    Integer patient_ID;
    Integer test_ID;
    TestType test_type;
    Ear ear;
    PassFail pass_fail;
    String comment;
    Float duration;

    //constructors
    public TestModel(){
    }

    public TestModel(Integer patient_ID){
        this.patient_ID = patient_ID;
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
        this.comment = comment;
    }

    public void setDuration(Float duration) {
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
        return test_type;
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
}
