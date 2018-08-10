package hearscreening.rcs.ei.tum.de.sound4all;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.Toast;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NFCHelper {

    public enum RECORD_IDS {
        PATIENT_ID(1),
        PATIENT_NAME(2),
        FAMILY_NAME(3),
        GIVEN_NAME(4),
        DOB(5),
        HEIGHT(6),
        WEIGHT(7);

        private byte value;
        private static Map map = new HashMap<>();

        private RECORD_IDS(int value) {
            this.value = (byte) value;
        }

        static {
            for (RECORD_IDS recordId : RECORD_IDS.values()) {
                map.put(recordId.value, recordId);
            }
        }

        public static RECORD_IDS valueOf(int recordId) {
            return (RECORD_IDS) map.get(recordId);
        }

        public int getValue() {
            return value;
        }

    }

    private Context context;

    //intent interrupt
    PendingIntent pendingIntent;

    //device level
    NfcAdapter nfcAdapter;
    Tag nfcTag;
    boolean writeMode;

    //packet level
    IntentFilter writeTagFilters[];
    List<NdefRecord> records;

    public NFCHelper(Context context){
        this.context = context;
        this.records = new ArrayList<NdefRecord>();
    }

    public void addRecord(NdefRecord record){
        this.records.add(record);
    }

    public void addRecord(String record_contents){
        try {
            this.records.add(createRecord(record_contents));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void addRecord(String record_contents, RECORD_IDS record_id) throws UnsupportedEncodingException {
        this.records.add(createRecord(record_contents, (byte) record_id.getValue()));

    }

    public void clearRecords(){
        records.clear();
    }


    /******************************************************************************
     *************************************Write************************************
     ******************************************************************************/
    public void writeStoredRecords(Tag tag) throws IOException, FormatException {
        NdefRecord[] final_records = (NdefRecord[]) this.records.toArray(new NdefRecord[0]);
        NdefMessage message = new NdefMessage(final_records);
        Ndef ndef = Ndef.get(tag);
        ndef.connect();
        ndef.writeNdefMessage(message);
        ndef.close();

    }

    public void writeMessage(List<NdefRecord> records, Tag tag) throws IOException, FormatException {
        NdefRecord[] final_records = (NdefRecord[]) records.toArray();
        NdefMessage message = new NdefMessage(final_records);
        Ndef ndef = Ndef.get(tag);
        ndef.connect();
        ndef.writeNdefMessage(message);
        ndef.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD_MR1)
    public void writeSingleRecordMessage(String text, Tag tag) throws IOException, FormatException {
        NdefRecord[] records = { createRecord(text) };
        NdefMessage message = new NdefMessage(records);
        // Get an instance of Ndef for the tag.
        Ndef ndef = Ndef.get(tag);
        // Enable I/O
        ndef.connect();
        // Write the message
        ndef.writeNdefMessage(message);
        // Close the connection
        ndef.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD_MR1)
    public void writeSingleRecordMessage(String text, byte ID, Tag tag) throws IOException, FormatException {
        NdefRecord[] records = { createRecord(text, ID) };
        NdefMessage message = new NdefMessage(records);
        // Get an instance of Ndef for the tag.
        Ndef ndef = Ndef.get(tag);
        // Enable I/O
        ndef.connect();
        // Write the message
        ndef.writeNdefMessage(message);
        // Close the connection
        ndef.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    private NdefRecord createRecord(String text) throws UnsupportedEncodingException {
        String lang       = "en";
        byte[] textBytes  = text.getBytes();
        byte[] langBytes  = lang.getBytes("US-ASCII");
        int    langLength = langBytes.length;
        int    textLength = textBytes.length;
        byte[] payload    = new byte[1 + langLength + textLength];

        // set status byte (see NDEF spec for actual bits)
        payload[0] = (byte) langLength;

        // copy langbytes and textbytes into payload
        System.arraycopy(langBytes, 0, payload, 1,              langLength);
        System.arraycopy(textBytes, 0, payload, 1 + langLength, textLength);

        NdefRecord recordNFC = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,  NdefRecord.RTD_TEXT,  new byte[0], payload);

        return recordNFC;
    }

    private NdefRecord createRecord(String text, byte ID) throws UnsupportedEncodingException {
        String lang       = "en";
        byte[] textBytes  = text.getBytes();
        byte[] langBytes  = lang.getBytes("US-ASCII");
        int    langLength = langBytes.length;
        int    textLength = textBytes.length;
        byte[] payload    = new byte[1 + langLength + textLength];
        byte[] id = new byte[1];
        id[0] = ID;

        // set status byte (see NDEF spec for actual bits)
        payload[0] = (byte) langLength;

        // copy langbytes and textbytes into payload
        System.arraycopy(langBytes, 0, payload, 1,              langLength);
        System.arraycopy(textBytes, 0, payload, 1 + langLength, textLength);

        NdefRecord recordNFC = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,  NdefRecord.RTD_TEXT,  id, payload);

        return recordNFC;
    }

    /******************************************************************************
     **************************************Read************************************
     ******************************************************************************/
    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    public void readFromIntent(Intent intent){
        String action = intent.getAction();
        if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)){
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs = null;
            if(rawMsgs != null){
                msgs = new NdefMessage[rawMsgs.length];
                for(int i = 0; i < rawMsgs.length; i++){
                    msgs[i] = (NdefMessage)rawMsgs[i];
                }
            }
            buildTagViews(msgs);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    private void buildTagViews(NdefMessage[] msgs) {
        if (msgs == null || msgs.length == 0) return;
        String text = "";
//        String tagId = new String(msgs[0].getRecords()[0].getType());
        byte[] payload = msgs[0].getRecords()[0].getPayload();
        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16"; // Get the Text Encoding
        int languageCodeLength = payload[0] & 0063; // Get the Language Code, e.g. "en"
        // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");

        try {
            // Get the Text
            text = new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
        } catch (UnsupportedEncodingException e) {
            Log.e("UnsupportedEncoding", e.toString());
        }
        //handle text here!
        Toast.makeText((Activity)this.context, "Contents: " + text, Toast.LENGTH_LONG).show();
    }

    /******************************************************************************
     **********************************Enable Write********************************
     ******************************************************************************/
    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD_MR1)
    public void WriteModeOn(){
        writeMode = true;
        nfcAdapter.enableForegroundDispatch((Activity)this.context, pendingIntent, writeTagFilters, null);
    }

    /******************************************************************************
     **********************************Disable Write*******************************
     ******************************************************************************/
    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD_MR1)
    public void WriteModeOff(){
        writeMode = false;
        nfcAdapter.disableForegroundDispatch((Activity)this.context);
    }
}