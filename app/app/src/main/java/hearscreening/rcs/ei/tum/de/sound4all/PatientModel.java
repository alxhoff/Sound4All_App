package hearscreening.rcs.ei.tum.de.sound4all;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.Tag;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class PatientModel implements Parcelable{
    private int ID;
    private String family_name;
    private String given_name;
    private String dob;
    private int _height;
    private int weight;
    private Context context;

//constructors
    public PatientModel(Context context){
        this.context = context;
    }

    public PatientModel(int ID){
        this.ID = ID;
    }

    public PatientModel(int ID, String family_name, String given_name, String dob,
                        int height_, int weight){
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

    public void setID(int ID){
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

    public void setHeight(int _height){
        this._height = _height;
    }

    public void setWeight(int weight){
        this.weight = weight;
    }

    //getters

    public int getID() {
        if(ID != 0) return ID;
        else return 0;
    }

    public String getFamilyName() {
        if(family_name != null) return family_name;
        else return null;
    }

    public String getGivenName() {
        if(given_name != null) return given_name;
        else return null;
    }

    public String getDob() {
        if(dob != null) return dob;
        else return null;
    }

    public int getHeight() {
        return _height;
    }

    public int getWeight() {
        return weight;
    }

    //NFC
//    @TargetApi(Build.VERSION_CODES.KITKAT)
//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    public void sendPatient(Context context, Tag tag) throws IOException, FormatException {
//        NFCHelper nfcHelper = new NFCHelper(context);
//
//        this.addPatient(context, tag);
//
//        nfcHelper.writeStoredRecords(tag);
//    }

//    @TargetApi(Build.VERSION_CODES.KITKAT)
//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    public void addPatient(Context context, Tag tag) throws UnsupportedEncodingException {
//        NFCHelper nfcHelper = new NFCHelper(context);
//
//        //add records
//        if(this.ID != null)
//            nfcHelper.addRecord(String.valueOf(this.ID),
//                    NFCHelper.RECORD_IDS.PATIENT_ID);
//        if(this.family_name != null && !this.family_name.isEmpty())
//            nfcHelper.addRecord(this.family_name, NFCHelper.RECORD_IDS.FAMILY_NAME);
//        if(this.given_name != null && !this.given_name.isEmpty())
//            nfcHelper.addRecord(this.given_name, NFCHelper.RECORD_IDS.GIVEN_NAME);
//        if(this.dob != null && !this.dob.isEmpty())
//            nfcHelper.addRecord(this.dob, NFCHelper.RECORD_IDS.DOB);
//        if(this._height != null)
//            nfcHelper.addRecord(String.valueOf(this._height), NFCHelper.RECORD_IDS.HEIGHT);
//        if(this.weight != null)
//            nfcHelper.addRecord(String.valueOf(this.weight), NFCHelper.RECORD_IDS.WEIGHT);
//    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (ID == 0) {
            dest.writeInt(0);
        } else {
            dest.writeInt(ID);
        }
        dest.writeString(family_name);
        dest.writeString(given_name);
        dest.writeString(dob);
        if (_height == 0) {
            dest.writeInt(0);
        } else {
            dest.writeInt(_height);
        }
        if (weight == 0) {
            dest.writeInt(0);
        } else {
            dest.writeInt(weight);
        }
    }
}
