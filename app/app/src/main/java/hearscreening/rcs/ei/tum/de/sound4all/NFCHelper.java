package hearscreening.rcs.ei.tum.de.sound4all;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
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

import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class NFCHelper {

    public enum RECORD_IDS {
        NONE(0),
        PATIENT_ID(1),
        FAMILY_NAME(2),
        GIVEN_NAME(3),
        DOB(4),
        HEIGHT(5),
        WEIGHT(6),
        COMPILED_CONFIG(7),
        TE_DATA(8),
        DP_DATA(9);

        private int value;
        private static Map map = new HashMap<>();

        RECORD_IDS(int value) {
            this.value = value;
        }

        static {
            for (RECORD_IDS recordId : RECORD_IDS.values()) {
                map.put(recordId.value, recordId);
            }
        }

        //enum hashmap not working!
        public static RECORD_IDS valueOf(int recordId) {
            RECORD_IDS ret_val = (RECORD_IDS) map.get(recordId);
            return ret_val;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value){
            this.value = value;
        }
//
//        public void setValue(RECORD_IDS value){
//            this.value = value;
//        }

    }

    private Context context;

    DatabaseHelper databaseHelper;

    //intent interrupt
    PendingIntent pendingIntent;

    //device level
    NfcAdapter nfcAdapter;
    Tag nfcTag;
    Parcelable[] rawMsgs;
    NdefMessage[] msgs;
    boolean writeMode;

    //packet level
    IntentFilter writeTagFilters[];
    List<NdefRecord> records;

    public NFCHelper(Context context){
        this.context = context;
        this.records = new ArrayList<NdefRecord>();
        databaseHelper = new DatabaseHelper(context);
    }

    public void addRecord(NdefRecord record){
        this.records.add(record);
    }

    public void addRecord(String record_contents){
        try {
            this.records.add(createTextRecord(record_contents));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void addRecord(String record_contents, RECORD_IDS record_id) throws UnsupportedEncodingException {
        this.records.add(createTextRecord(record_contents, (byte) record_id.getValue()));
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void addSettingsRecord(List<Byte> settings, RECORD_IDS record_id){
//        NdefRecord.createExternal("example.com", "mytype", byteArray)
        byte[] settings_bytes =
                ArrayUtils.toPrimitive(settings.toArray(new Byte[settings.size()]));
//        this.records.add(NdefRecord.createExternal("s4a.testSettings", "settings",
//                settings_bytes));
        this.records.add(createExternalRecord(settings, (byte)record_id.getValue(), "s4asettings",
                "compiledSettings"));
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void addPatientRecords(PatientModel patient) throws UnsupportedEncodingException {

        if(patient.getID() != 0)
            addRecord(String.valueOf(patient.getID()), RECORD_IDS.PATIENT_ID);
        if(patient.getFamilyName() != null)
            addRecord(patient.getFamilyName(), RECORD_IDS.FAMILY_NAME);
        if(patient.getGivenName() != null)
            addRecord(patient.getGivenName(), RECORD_IDS.GIVEN_NAME);
        if(patient.getDob() != null)
            addRecord(patient.getDob(), RECORD_IDS.DOB);
        if(patient.getWeight() != 0)
            addRecord(String.valueOf(patient.getWeight()), RECORD_IDS.WEIGHT);
        if(patient.getHeight() != 0)
            addRecord(String.valueOf(patient.getHeight()), RECORD_IDS.HEIGHT);
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
        try {
            Ndef ndef = Ndef.get(tag);
            if (ndef != null) {
                ndef.connect();
                if(!ndef.isWritable())
                    return;
                ndef.writeNdefMessage(message);
                ndef.close();
                //clear records from helper
                this.clearRecords();
            }
        }catch(Exception e){}

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
        NdefRecord[] records = { createTextRecord(text) };
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

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD_MR1)
    public void writeSingleRecordMessage(String text, byte ID, Tag tag) throws IOException, FormatException {
        NdefRecord[] records = { createTextRecord(text, ID) };
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
    private NdefRecord createTextRecord(String text) throws UnsupportedEncodingException {
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

        NdefRecord recordNFC = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,  NdefRecord.RTD_TEXT,
                new byte[0], payload);

        return recordNFC;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private NdefRecord createTextRecord(String text, byte ID) throws UnsupportedEncodingException {
        String lang       = "en";
        byte[] textBytes  = text.getBytes();
//        byte[] langBytes  = lang.getBytes("US-ASCII");
        byte[] langBytes  = lang.getBytes(StandardCharsets.UTF_8);
//        byte[] id = new byte[1];
//        id[0] = ID;
//        byte[] payload    = new byte[1 + langBytes.length + textBytes.length + id.length];
        byte[] payload    = new byte[1 + langBytes.length + textBytes.length];

        // set status byte (see NDEF spec for actual bits)
        payload[0] = (byte) lang.length();
        //set use id flag
//        payload[0] |= (1 << 3);

        // copy langbytes and textbytes into payload
        System.arraycopy(langBytes, 0, payload, 1, langBytes.length);
        System.arraycopy(textBytes, 0, payload, 1 + langBytes.length,
                textBytes.length);
//        System.arraycopy(id, 0, payload, 1 + langBytes.length + textBytes.length,
//                id.length);

//        NdefRecord recordNFC = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,  NdefRecord.RTD_TEXT,
//                id, payload);

        NdefRecord recordNFC = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,  NdefRecord.RTD_TEXT,
                new byte[]{ID}, payload);

        return recordNFC;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private NdefRecord createExternalRecord(List<Byte> data, byte ID, String domain,
                                            String type){
        byte[] data_array = ArrayUtils.toPrimitive(data.toArray(new Byte[data.size()]));

        if(domain == null) throw new NullPointerException("Please give a valid domain");
        if(type == null) throw new NullPointerException("Please give a valid type");

        domain = domain.trim().toLowerCase(Locale.ROOT);
        type = type.trim().toLowerCase(Locale.ROOT);

        if (domain.length() == 0) throw new IllegalArgumentException("domain is empty");
        if (type.length() == 0) throw new IllegalArgumentException("type is empty");

        byte[] byteDomain = domain.getBytes(StandardCharsets.UTF_8);
        byte[] byteType = type.getBytes(StandardCharsets.UTF_8);
        byte[] headerBytes = new byte[byteDomain.length + 1 + byteType.length];

        System.arraycopy(byteDomain, 0, headerBytes, 0, byteDomain.length);
        headerBytes[byteDomain.length] = ':';
        System.arraycopy(byteType, 0, headerBytes, byteDomain.length + 1, byteType.length);

        return new NdefRecord(NdefRecord.TNF_EXTERNAL_TYPE, headerBytes, new byte[]{ID}, data_array);
    }

    /******************************************************************************
     **************************************Read************************************
     ******************************************************************************/
//    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
//    public void readFromIntent(Intent intent){
//        String action = intent.getAction();
//        if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
//                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
//                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)){
//            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
//            NdefMessage[] msgs = null;
//            if(rawMsgs != null){
//                msgs = new NdefMessage[rawMsgs.length];
//                for(int i = 0; i < rawMsgs.length; i++){
//                    msgs[i] = (NdefMessage)rawMsgs[i];
//                }
//            }
//            buildTagViews(msgs);
//        }
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
//    private void buildTagViews(NdefMessage[] msgs) {
//        if (msgs == null || msgs.length == 0) return;
//        String text = "";
////        String tagId = new String(msgs[0].getRecords()[0].getType());
//        byte[] payload = msgs[0].getRecords()[0].getPayload();
//        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16"; // Get the Text Encoding
//        int languageCodeLength = payload[0] & 0063; // Get the Language Code, e.g. "en"
//        // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
//
//        try {
//            // Get the Text
//            text = new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
//        } catch (UnsupportedEncodingException e) {
//            Log.e("UnsupportedEncoding", e.toString());
//        }
//        //handle text here!
//        Toast.makeText((Activity)this.context, "Contents: " + text, Toast.LENGTH_LONG).show();
//    }

    public NFCHelper.RECORD_IDS getRecordID(NdefRecord record){
        return NFCHelper.RECORD_IDS.valueOf(record.getId()[0]);
    }

    public String getRecordText(NdefRecord record){
        String return_text = "";
        if(record != null){
            byte[] payload = record.getPayload();
            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16"; // Get the Text Encoding
            int languageCodeLength = payload[0] & 0063; // Get the Language Code, e.g. "en"
            // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
            try {
                // Get the Text
                return_text = new String(payload, languageCodeLength + 1,
                        payload.length - languageCodeLength - 2, textEncoding);
            } catch (UnsupportedEncodingException e) {
                Log.e("UnsupportedEncoding", e.toString());
            }
        }

        return return_text;
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

    /******************************************************************************
     **********************************Patient*************************************
     ******************************************************************************/
    public void storeNewPatient(NdefMessage msg){
        //construct patient and check they exist in db
        PatientModel tmp_patient = new PatientModel(this.context);
        if(msg.getRecords().length > 0){
            //handle records
            NFCHelper.RECORD_IDS tmp_id = RECORD_IDS.NONE;
            String tmp_value;
            NdefRecord[] records = msg.getRecords();
            if(records.length != 0){
                NFCHelper.RECORD_IDS id_check = getRecordID(records[0]);
            }
            for(int i = 0; i < msg.getRecords().length; i++){
                tmp_id = getRecordID(msg.getRecords()[i]);
                tmp_value = getRecordText(msg.getRecords()[i]);
                switch(tmp_id){
                    case PATIENT_ID:
                        tmp_patient.setID(Integer.parseInt(tmp_value));
                        break;
                    case FAMILY_NAME:
                        tmp_patient.setFamilyName(tmp_value);
                        break;
                    case GIVEN_NAME:
                        tmp_patient.setGivenName(tmp_value);
                        break;
                    case DOB:
                        tmp_patient.setDob(tmp_value);
                        break;
                    case HEIGHT:
                        tmp_patient.setHeight(Integer.parseInt(tmp_value));
                        break;
                    case WEIGHT:
                        tmp_patient.setWeight(Integer.parseInt(tmp_value));
                        break;
                    default:
                        break;
                }
            }
            //check patient doesn't already exist
            databaseHelper.createPatient(tmp_patient);
        }
    }

    /******************************************************************************
     ***********************************Test***************************************
     ******************************************************************************/
    public boolean containsTests(NdefMessage[] msgs){
        NFCHelper.RECORD_IDS record_id = RECORD_IDS.NONE;

        for(int i = 0; i < msgs.length; i++){
            NdefRecord[] records = msgs[i].getRecords();
            for(int j = 0; j < msgs[i].getRecords().length; j++){
                byte[] record_id_byte = null;

                if(msgs[i].getRecords().length > 0)
                    record_id_byte = records[j].getId();
                else break;

                if(record_id_byte.length > 0)
                    record_id = NFCHelper.RECORD_IDS.valueOf(msgs[i].getRecords()[j].getId()[0]);
                else break;

                if(record_id == RECORD_IDS.TE_DATA  || record_id == RECORD_IDS.DP_DATA)
                    return true;
            }
        }

        return false;
    }

    public boolean containsTestsConfig(NdefMessage[] msgs){
        RECORD_IDS record_id = RECORD_IDS.NONE;

        for(int i = 0; i < msgs.length; i++){
            NdefRecord[] records = msgs[i].getRecords();
            for(int j = 0; j < msgs[i].getRecords().length; j++){
                byte[] record_id_byte = null;

                if(msgs[i].getRecords().length > 0)
                    record_id_byte = records[j].getId();
                else break;

                if(record_id_byte.length > 0)
                    record_id = NFCHelper.RECORD_IDS.valueOf(msgs[i].getRecords()[j].getId()[0]);
                else break;

                if(record_id == RECORD_IDS.COMPILED_CONFIG)
                    return true;
            }
        }

        return false;
    }

    public void storeNewDPOAETest(NdefMessage msg){

    }

    public void soreNewTEOAETest(NdefMessage msg){

    }
}
