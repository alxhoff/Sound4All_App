package hearscreening.rcs.ei.tum.de.sound4all;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import hearscreening.rcs.ei.tum.de.sound4all.TestModel.TestType;

import static hearscreening.rcs.ei.tum.de.sound4all.NFCHelper.RECORD_IDS.PATIENT_ID;

public class TestActivity extends AppCompatActivity {

    Dialog MyDialog;
    Button hello;
    Button close;

    TestModel test;
    DPOAETestModel DPOAEtest;
    TEOAETestModel TEOAEtest;

    DatabaseHelper databaseHelper;

    PatientModel patient;
    NFCHelper nfcHelper;

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nfcHelper = new NFCHelper(TestActivity.this);

        test = new TestModel();

        databaseHelper = new DatabaseHelper(this);

        nfcHelper.nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if(nfcHelper.nfcAdapter == null){
            Toast.makeText(this, getResources().getString(R.string.NFC_error),
                    Toast.LENGTH_LONG).show();
            finish();
        }
        nfcHelper.pendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),0);
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        tagDetected.addCategory(Intent.CATEGORY_DEFAULT);
        nfcHelper.writeTagFilters = new IntentFilter[]{tagDetected};

        setContentView(R.layout.activity_test);

        Integer test_type = getIntent().getIntExtra("TEST_TYPE", 0);
        patient = (PatientModel) getIntent().getParcelableExtra("PATIENT");

        switch(test_type){
            case 1:
                getSupportActionBar().setTitle(getResources().getString(R.string.d_test) + " Test");
                test.setTest_type(TestType.DPOAE);
                DPOAEtest = new DPOAETestModel();
                break;
            case 2:
                getSupportActionBar().setTitle(getResources().getString(R.string.t_test) + " Test");
                test.setTest_type(TestType.TEOAE);
                TEOAEtest = new TEOAETestModel();
                break;
        }

        MyCustomAlertDialog();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        //tag found
//        nfcHelper.readFromIntent(intent);
        if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())){
            nfcHelper.nfcTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            nfcHelper.rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        }
        try {
            //read device to see if existing test exists that does not exist in database
            if(nfcHelper.rawMsgs != null){
                nfcHelper.msgs = new NdefMessage[nfcHelper.rawMsgs.length];
                for(int i = 0; i < nfcHelper.rawMsgs.length; i++){
                    nfcHelper.msgs[i] = (NdefMessage) nfcHelper.rawMsgs[i];
                }
                //check if patient exists
                int patient_ID_in_DB = isDifferentPatient(nfcHelper.msgs);
                if(patient_ID_in_DB == 0) {
                    //different patient
                    //check patient exists and store if not
                    nfcHelper.storeNewPatient(nfcHelper.msgs[0]);
                }else if(patient_ID_in_DB == -1){
                    //empty tag/no patient
                    patient_ID_in_DB = 0;
                }else{
                    //same patient - update local ID
                    patient.setID(patient_ID_in_DB);
                }
                //check if tag contains test data
                if(nfcHelper.containsTests(nfcHelper.msgs)){
                    //store data etc
                }
                if(nfcHelper.containsTestsConfig(nfcHelper.msgs)){
                    //handle stored test configs
                }
            }
            //send test config
            TestModel.TestType test_type = test.getTest_type();
            //compile active settings
            SettingsHelper settingsHelper = new SettingsHelper(this);
            settingsHelper.actualizeSettingPreset();
            List<Byte> compiled_settings = null;
            switch(test_type){
                case DPOAE:
                    compiled_settings = settingsHelper.compileSettings(
                            TestType.DPOAE);
                    break;
                case TEOAE:
                    compiled_settings = settingsHelper.compileSettings(
                            TestType.TEOAE);
                    break;
                default:
                    break;
            }
            //prepare data for tag
            //add patient
            if(patient != null)
                patient.addPatient(this, nfcHelper.nfcTag);
            //send compiled settings
            //HERE
            if(compiled_settings != null)
                nfcHelper.addSettingsRecord(compiled_settings);

            nfcHelper.writeStoredRecords(nfcHelper.nfcTag);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (FormatException e) {
            e.printStackTrace();
        }
    }

    //return 0 if the same else returns ID of patient in DB
    private int isDifferentPatient(NdefMessage[] msgs){
        //reconstruct patient in TAG
        PatientModel patient_in_tag = new PatientModel(this);
        //check all messages
        for(int i = 0; i < msgs.length; i++){
            //get all records
            NFCHelper.RECORD_IDS record_id = NFCHelper.RECORD_IDS.NONE;
            NdefRecord[] records = msgs[i].getRecords(); //HERE using zero'd out tag. Fix null records etc
            for(int j = 0; j < msgs[i].getRecords().length; j++){
                //current record's ID
                byte[] record_id_byte = null;
                if(msgs[i].getRecords().length > 0 )
                    record_id_byte = records[j].getId();
                else
                    return -1;
                if(record_id_byte.length > 0)
                    record_id = NFCHelper.RECORD_IDS.valueOf(msgs[i].getRecords()[j].getId()[0]);
                else
                    //empty tags
                    return -1;
                if(record_id != NFCHelper.RECORD_IDS.NONE) {
                    //populate patient information
                    switch(record_id){
                        case PATIENT_ID:{
                            byte[] payload = msgs[i].getRecords()[j].getPayload();
                            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
                            int languageCodeLength = payload[0] & 0063;
                            try {
                                patient_in_tag.setID(Integer.parseInt(new String(payload, languageCodeLength + 1,
                                        payload.length - languageCodeLength - 2, textEncoding)));
                            } catch (UnsupportedEncodingException e) {
                                Log.e("UnsupportedEncoding", e.toString());
                            }
                        }
                            break;
                        case FAMILY_NAME:{
                            byte[] payload = msgs[i].getRecords()[j].getPayload();
                            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
                            int languageCodeLength = payload[0] & 0063;
                            try {
                                patient_in_tag.setFamilyName(new String(payload, languageCodeLength + 1,
                                        payload.length - languageCodeLength - 2, textEncoding));
                            } catch (UnsupportedEncodingException e) {
                                Log.e("UnsupportedEncoding", e.toString());
                            }
                        }
                            break;
                        case GIVEN_NAME:{
                            byte[] payload = msgs[i].getRecords()[j].getPayload();
                            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
                            int languageCodeLength = payload[0] & 0063;
                            try {
                                patient_in_tag.setGivenName(new String(payload, languageCodeLength + 1,
                                        payload.length - languageCodeLength - 2, textEncoding));
                            } catch (UnsupportedEncodingException e) {
                                Log.e("UnsupportedEncoding", e.toString());
                            }
                        }
                            break;
                        case DOB:{
                            byte[] payload = msgs[i].getRecords()[j].getPayload();
                            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
                            int languageCodeLength = payload[0] & 0063;
                            try {
                                patient_in_tag.setDob(new String(payload, languageCodeLength + 1,
                                        payload.length - languageCodeLength - 2, textEncoding));
                            } catch (UnsupportedEncodingException e) {
                                Log.e("UnsupportedEncoding", e.toString());
                            }
                        }
                            break;
                        case HEIGHT:{
                            byte[] payload = msgs[i].getRecords()[j].getPayload();
                            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
                            int languageCodeLength = payload[0] & 0063;
                            try {
                                //ID of patient on TAG
                                patient_in_tag.setHeight(Integer.parseInt(new String(payload, languageCodeLength + 1,
                                        payload.length - languageCodeLength - 2, textEncoding)));
                            } catch (UnsupportedEncodingException e) {
                                Log.e("UnsupportedEncoding", e.toString());
                            }
                        }
                            break;
                        case WEIGHT:{
                            byte[] payload = msgs[i].getRecords()[j].getPayload();
                            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
                            int languageCodeLength = payload[0] & 0063;
                            try {
                                //ID of patient on TAG
                                patient_in_tag.setWeight(Integer.parseInt(new String(payload, languageCodeLength + 1,
                                        payload.length - languageCodeLength - 2, textEncoding)));
                            } catch (UnsupportedEncodingException e) {
                                Log.e("UnsupportedEncoding", e.toString());
                            }
                        }
                            break;
                        default:
                            break;
                    }
//                    if (msgs[i].getRecords()[j].getId()[0] == PATIENT_ID.getValue()) { //ndef contains a patient
//                        byte[] payload = msgs[i].getRecords()[j].getPayload();
//                        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
//                        int languageCodeLength = payload[0] & 0063;
//                        try {
//                            //ID of patient on TAG
//                            String patient_ID_string = new String(payload, languageCodeLength + 1,
//                                    payload.length - languageCodeLength - 2, textEncoding);
//                            String compare_string = patient.getID().toString();
//
//                            //patient has same ID?
//                            if (patient_ID_string.equals(patient.getID().toString())) {
//                                //same ID
//                                DatabaseHelper databaseHelper = new DatabaseHelper(this);
//                                //get current patient from DB with ID of patient on tag
//                                PatientModel patient_at_tag_ID =
//                                        databaseHelper.getPatientByID(patient_ID_string);
//
//                                //check patient is the same as the one stored in DB at tag ID
//                                if(databaseHelper.samePatient(patient, patient_at_tag_ID))
//                                    return false;
//
//                                //check patient doesn't exist with different ID
//                                if(databaseHelper.checkPatientExists(patient_at_tag_ID))
//                            }
//                            else
//                                //different ID
//                                //check patient doesn't exist under different ID
//
//                                return true;
//                        } catch (UnsupportedEncodingException e) {
//                            Log.e("UnsupportedEncoding", e.toString());
//                        }
//                    }
                }
            }
        }
        //get patient stored at same id in DB
        PatientModel patient_in_DB = databaseHelper.getPatientByID(patient_in_tag.getID());

        //are they the same?
        if(databaseHelper.samePatient(patient_in_tag, patient_in_DB))
            return 0;

        //check if patient exists in DB with different ID
        int tag_patient_DB_ID = 0;
        tag_patient_DB_ID = databaseHelper.checkPatientExists(patient_in_tag);
        if(tag_patient_DB_ID != 0)
            return tag_patient_DB_ID;

        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD_MR1)
    @Override
    public void onPause(){
        super.onPause();
        nfcHelper.WriteModeOff();
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD_MR1)
    @Override
    public void onResume(){
        super.onResume();
        nfcHelper.WriteModeOn();
    }

    private void MyCustomAlertDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onBackPressed();
            }
        });

        final AlertDialog dialog = builder.create();
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.nfc_dialog, null);
        dialog.setView(dialogLayout);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.show();
    }
}
