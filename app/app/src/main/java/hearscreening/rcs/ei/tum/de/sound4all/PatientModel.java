package hearscreening.rcs.ei.tum.de.sound4all;

import android.content.Context;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.Tag;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class PatientModel implements Parcelable{
    private Integer ID;
    private String family_name;
    private String given_name;
    private String dob;
    private Integer _height;
    private Integer weight;
    private Context context;

//constructors
    public PatientModel(Context context){
        this.context = context;
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

    //parcel constructor
    public PatientModel(Parcel in){
        this.ID = in.readInt();
        this.family_name = in.readString();
        this.given_name = in.readString();
        this.dob = in.readString();
        this._height = in.readInt();
        this.weight = in.readInt();
    }

    //setters
    public static final Creator<PatientModel> CREATOR = new Creator<PatientModel>() {
        @Override
        public PatientModel createFromParcel(Parcel in) {
            return new PatientModel(in);
        }

        @Override
        public PatientModel[] newArray(int size) {
            return new PatientModel[size];
        }
    };

    public void setID(Integer ID){
        this.ID = ID;
    }

    public void setFamilyName(String family_name){
        this.family_name = family_name;
    }

    public void setGivenName(String given_name){
        this.given_name = given_name;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setHeight(Integer _height){
        this._height = _height;
    }

    public void setWeight(Integer weight){
        this.weight = weight;
    }

    //getters

    public Integer getID() {
        return ID;
    }

    public String getFamilyName() {
        return family_name;
    }

    public String getGivenName() {
        return given_name;
    }

    public String getDob() {
        return dob;
    }

    public Integer getHeight() {
        return _height;
    }

    public Integer getWeight() {
        return weight;
    }

    //NFC
    public void sendPatient(Context context, Tag tag) throws IOException, FormatException {
        NFCHelper nfcHelper = new NFCHelper(context);

        this.addPatient(context, tag);

        nfcHelper.writeStoredRecords(tag);
    }

    public void addPatient(Context context, Tag tag) throws UnsupportedEncodingException {
        NFCHelper nfcHelper = new NFCHelper(context);

        //add records
        if(this.ID != null)
            nfcHelper.addRecord(String.valueOf(this.ID),
                    NFCHelper.RECORD_IDS.PATIENT_ID);
        if(this.family_name != null && !this.family_name.isEmpty())
            nfcHelper.addRecord(this.family_name, NFCHelper.RECORD_IDS.FAMILY_NAME);
        if(this.given_name != null && !this.given_name.isEmpty())
            nfcHelper.addRecord(this.given_name, NFCHelper.RECORD_IDS.GIVEN_NAME);
        if(this.dob != null && !this.dob.isEmpty())
            nfcHelper.addRecord(this.dob, NFCHelper.RECORD_IDS.DOB);
        if(this._height != null)
            nfcHelper.addRecord(String.valueOf(this._height), NFCHelper.RECORD_IDS.HEIGHT);
        if(this.weight != null)
            nfcHelper.addRecord(String.valueOf(this.weight), NFCHelper.RECORD_IDS.WEIGHT);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (ID == null) {
            dest.writeInt(0);
        } else {
            dest.writeInt(ID);
        }
        dest.writeString(family_name);
        dest.writeString(given_name);
        dest.writeString(dob);
        if (_height == null) {
            dest.writeInt(0);
        } else {
            dest.writeInt(_height);
        }
        if (weight == null) {
            dest.writeInt(0);
        } else {
            dest.writeInt(weight);
        }
    }
}
