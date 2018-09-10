package hearscreening.rcs.ei.tum.de.sound4all;

import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Image;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.NotDirectoryException;

import hearscreening.rcs.ei.tum.de.sound4all.TestModel.TestType;

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

        test = new TestModel();

        MyCustomAlertDialog();
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
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
            //read device to see if exsisting test exsists that does not exsist in database
            if(nfcHelper.rawMsgs != null){
                nfcHelper.msgs = new NdefMessage[nfcHelper.rawMsgs.length];
                for(int i = 0; i < nfcHelper.rawMsgs.length; i++){
                    nfcHelper.msgs[i] = (NdefMessage) nfcHelper.rawMsgs[i];
                }
                //TODO add checking test against DB to check
//                if(checkDifferentPatient(nfcHelper.msgs)){
//                    //new patient
//                    //check patient exsists
//                    nfcHelper.storeNewPatient(nfcHelper.msgs[0]);
//                }else{
//                    patient.sendPatient(this, nfcHelper.nfcTag);
//                }
                patient.sendPatient(this, nfcHelper.nfcTag);
            }
            //if not send patient data and test config to start new test
            else{
                patient.sendPatient(this, nfcHelper.nfcTag);
            }
            //send test config
            TestModel.TestType test_type = test.getTest_type();
            //compile active settings
            SettingsHelper settingsHelper = new SettingsHelper(this);
            settingsHelper.compileSettings();
            switch(test_type){
                case DPOAE:

                    break;
                case TEOAE:
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FormatException e) {
            e.printStackTrace();
        }
    }

    private boolean checkDifferentPatient(NdefMessage[] msgs){
        for(int i = 0; i < msgs.length; i++){
            for(int j = 0; j < msgs[i].getRecords().length; j++){
                byte record_id = 0;
                record_id = msgs[i].getRecords()[j].getId()[0];
                if(record_id != 0) {
                    if (msgs[i].getRecords()[j].getId()[0] == NFCHelper.RECORD_IDS.PATIENT_ID.getValue()) { //ndef contains a patient
                        byte[] payload = msgs[i].getRecords()[j].getPayload();
                        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
                        int languageCodeLength = payload[0] & 0063;
                        try {
                            //check this string length (-2)
                            String patient_ID_string = new String(payload, languageCodeLength + 1,
                                    payload.length - languageCodeLength - 2, textEncoding);
                            //is current patient?
                            String compare_string = patient.getID().toString();
                            if (patient_ID_string.equals(patient.getID().toString()))
                                //new patient
                                return false;
                            else
                                return true;
                        } catch (UnsupportedEncodingException e) {
                            Log.e("UnsupportedEncoding", e.toString());
                        }
                    }
                }
            }
        }
        return false;
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
