package hearscreening.rcs.ei.tum.de.sound4all;

public class PatientModel {
    private Integer ID;
    private String family_name;
    private String given_name;
    private String dob;
    private Integer _height;
    private Integer weight;

//constructors
    public PatientModel(){
    }

    public PatientModel(Integer ID){
        this.ID = ID;
    }

    public PatientModel(Integer ID, String family_name, String given_name, String dob,
                        Integer height_, Integer weight){
        this.ID = ID;
        this.family_name = family_name;
        this.given_name = given_name;
        this.dob = dob;
        this._height = height_;
        this.weight = weight;
    }

    //setters

    public void setID(Integer ID){
        this.ID = ID;
    }

    public void setFamily_name(String family_name){
        this.family_name = family_name;
    }

    public void setGiven_name(String given_name){
        this.given_name = given_name;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void set_height(Integer _height){
        this._height = _height;
    }

    public void setWeight(Integer weight){
        this.weight = weight;
    }

    //getters

    public Integer getID() {
        return ID;
    }

    public String getFamily_name() {
        return family_name;
    }

    public String getGiven_name() {
        return given_name;
    }

    public String getDob() {
        return dob;
    }

    public Integer get_height() {
        return _height;
    }

    public Integer getWeight() {
        return weight;
    }

    //NFC
    public void sendPatient(){

    }
}
