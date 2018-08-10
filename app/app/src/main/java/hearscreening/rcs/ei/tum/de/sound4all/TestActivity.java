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

public class TestActivity extends AppCompatActivity {

    Dialog MyDialog;
    Button hello;
    Button close;

    TestModel test;
    DPOAETestModel DPOAEtest;
    TEOAETestModel TEOAEtest;

    PatientModel patient;
    NFCHelper nfcHelper;

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nfcHelper = new NFCHelper(TestActivity.this);

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
                DPOAEtest = new DPOAETestModel();
                break;
            case 2:
                getSupportActionBar().setTitle(getResources().getString(R.string.t_test) + " Test");
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
        nfcHelper.readFromIntent(intent);
        if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())){
            nfcHelper.nfcTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        }
        try {
            patient.sendPatient(this, nfcHelper.nfcTag);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FormatException e) {
            e.printStackTrace();
        }
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
