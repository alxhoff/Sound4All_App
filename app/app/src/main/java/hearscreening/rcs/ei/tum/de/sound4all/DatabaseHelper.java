package hearscreening.rcs.ei.tum.de.sound4all;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    private static final int Version = 1;

    private static final String TAG = "DB Helper";

    //******************DATABASES******************//
    public final static String DATABASE_NAME = "sound4all.db";


    //********************TABLES********************//
    public final static String PATIENTS_TABLE = "patients_table";
    public final static String TESTS_TABLE = "patient_tests_table";
    public final static String DP_TESTS_TABLE = "dpoae_tests_table";
    public final static String DP_RESULTS_TABLE = "dpoae_results_table";
    public final static String TE_TESTS_TABLE = "teoae_tests_table";
    public final static String SETTINGS_TABLE = "settings_table";


    //***************PATIENTS TABLE****************//
    public final static String PAT_FN = "FAMILY_NAME";
    public final static String PAT_GN = "GIVEN_NAME";
    public final static String PAT_DOB = "DOB";
    public final static String PAT_HT = "HEIGHT";
    public final static String PAT_WT = "WEIGHT";


    //*****************TESTS TABLE*****************//
    //test ID
    //patient ID
    public final static String TEST_TIME = "TEST_TIME";
    public final static String TEST_TYPE = "TEST_TYPE";
    public final static String TEST_EAR = "TEST_EAR";
    public final static String TEST_PASS_FAIL = "TEST_PASS_FAIL";
    public final static String TEST_COMMENT = "TEST_COMMENT";
    public final static String TEST_DUR = "TEST_DUR";


    //*****************COMMON COLS****************//
    public final static String PAT_ID = "PAT_ID";


    //***************DP TESTS TABLE***************//
    //test ID
    public final static String DP_T_RATIO = "DP_TEST_RATIO";
    public final static String DP_T_THRESH = "DP_TEST_THRESH";
    public final static String DP_T_MAX_DUR = "DP_TEST_MAX_DUR";


    //**************DP RESULTS TABLE**************//
    //test ID
    public final static String DP_R_NOISE = "DP_RES_NOISE";
    public final static String DP_R_LEVEL = "DP_RES_LEVEL";
    public final static String DP_R_F1 = "DP_RES_F1";
    public final static String DP_R_L1 = "DP_RES_L1";
    public final static String DP_R_L2 = "DP_RES_L2";


    //***************TE TESTS TABLE***************//
    //test ID
    public final static String TE_FILE_NAME = "TE_FILE_NAME";
    public final static String TE_DUR = "TE_DUR";
    public final static String TE_STIMULUS_LVL = "TE_STIM_LVL";
    public final static String TE_MAX_DUR = "TE_MAX_DUR";
    public final static String TE_T_SNR = "TE_TEST_SNR";


    //*****************COMMON COLS****************//
    public final static String TEST_ID = "TEST_ID";


    //***************SETTINGS TABLE***************//
    public final static String SET_PRESET = "SETTING_PRESET";
    public final static String TE_STIMULUS = "TE_STIMULUS";
    public final static String TE_NO_PASSES = "TE_NUM_OF_PASSES";
    public final static String DP_NO_PASSES = "DE_NUM_OF_PASSES";
    public final static String DP_FREQ_1K = "DP_FREQ_1K";
    public final static String DP_FREQ_1K5 = "DP_FREQ_1K5";
    public final static String DP_FREQ_2K = "DP_FREQ_2K";
    public final static String DP_FREQ_3K = "DP_FREQ_3K";
    public final static String DP_FREQ_4K = "DP_FREQ_4K";
    public final static String DP_FREQ_5K = "DP_FREQ_5K";
    public final static String DP_FREQ_6K = "DP_FREQ_6K";
    public final static String DP_FREQ_8K = "DP_FREQ_8K";
    public final static String DP_T_SNR = "DP_TEST_SNR";


    //***************PATIENTS TABLE****************//
    private static final String CREATE_TABLE_PATIENTS = "CREATE TABLE IF NOT EXISTS "
            + PATIENTS_TABLE + "("
            + PAT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + PAT_FN + " TEXT, "
            + PAT_GN + " TEXT, "
            + PAT_DOB + " TEXT, "
            + PAT_HT + " INTEGER, "
            + PAT_WT + " INTEGER)";

    //*****************TESTS TABLE*****************//
    private static final String CREATE_TABLE_TESTS = "CREATE TABLE IF NOT EXISTS "
            + TESTS_TABLE + "("
            + PAT_ID + " INTEGER NOT NULL, "
            + TEST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TEST_TIME + " TEXT, "
            + TEST_EAR + " TEXT, "
            + TEST_PASS_FAIL + " TEXT, "
            + TEST_COMMENT + " TEXT, "
            + TEST_DUR + " REAL)";

    //***************DP TESTS TABLE***************//
    private static final String CREATE_TABLE_DP_TESTS = "CREATE TABLE IF NOT EXISTS "
            + DP_TESTS_TABLE + "("
            + TEST_ID + " INTEGER PRIMARY KEY, "
            + DP_T_RATIO + " REAL, "
            + DP_T_THRESH + " REAL, "
            + DP_T_MAX_DUR + " REAL)";


    //**************DP RESULTS TABLE**************//
    private static final String CREATE_TABLE_DP_RESULTS = "CREATE TABLE IF NOT EXISTS "
            + DP_RESULTS_TABLE + "("
            + TEST_ID + " INTEGER PRIMARY KEY, "
            + DP_R_NOISE + " REAL, "
            + DP_R_LEVEL + " REAL, "
            + DP_R_F1 + " INTEGER, "
            + DP_R_L1 + " INTEGER, "
            + DP_R_L2 + " INTEGER)";


    //***************TE TESTS TABLE***************//
    private static final String CREATE_TABLE_TE_TESTS = "CREATE TABLE IF NOT EXISTS "
            + TE_TESTS_TABLE + "("
            + TEST_ID + " INTEGER PRIMARY KEY, "
            + TE_FILE_NAME + " TEXT, "
            + TE_DUR + " REAL, "
            + TE_MAX_DUR + " REAL, "
            + TE_STIMULUS_LVL + " REAL, "
            + TE_T_SNR + " INTEGER)";


    //***************TE TESTS TABLE***************//
    private static final String CREATE_TABLE_SETTINGS = "CREATE TABLE IF NOT EXISTS "
            + SETTINGS_TABLE + "("
            + SET_PRESET + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TE_MAX_DUR + " REAL, "
            + TE_STIMULUS + " TEXT, "
            + TE_NO_PASSES + " INTEGER, "
            + TE_STIMULUS_LVL + " INTEGER, "
            + TE_T_SNR + " INTEGER, "
            + DP_NO_PASSES + " INTEGER, "
            + DP_FREQ_1K + " BOOLEAN, "
            + DP_FREQ_1K5 + " BOOLEAN, "
            + DP_FREQ_2K + " BOOLEAN, "
            + DP_FREQ_3K + " BOOLEAN, "
            + DP_FREQ_4K + " BOOLEAN, "
            + DP_FREQ_5K + " BOOLEAN, "
            + DP_FREQ_6K + " BOOLEAN, "
            + DP_FREQ_8K + " BOOLEAN, "
            + DP_T_THRESH + " INTEGER, "
            + DP_R_F1 + " REAL, "
            + DP_R_L1 + " REAL, "
            + DP_R_L2 + " REAL, "
            + DP_T_MAX_DUR + " REAL, "
            + DP_T_SNR + " INTEGER)";

    /******************************************************************************
     **********************************CONSTRUCTORS********************************
     ******************************************************************************/
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, Version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PATIENTS);
        db.execSQL(CREATE_TABLE_TESTS);
        db.execSQL(CREATE_TABLE_DP_TESTS);
        db.execSQL(CREATE_TABLE_DP_RESULTS);
        db.execSQL(CREATE_TABLE_TE_TESTS);
        db.execSQL(CREATE_TABLE_SETTINGS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PATIENTS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TESTS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DP_RESULTS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DP_TESTS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TE_TESTS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SETTINGS_TABLE);
        onCreate(db);
    }

    public boolean settingsDefaults(){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(TE_MAX_DUR, 30.0);
        contentValues.put(TE_STIMULUS, SettingsModel.TE_STIMULUS.STANDARD.name());
        contentValues.put(TE_STIMULUS_LVL, 60);
        contentValues.put(TE_NO_PASSES, 1);
        contentValues.put(TE_T_SNR, 3);

        contentValues.put(DP_NO_PASSES, 1);
        contentValues.put(DP_FREQ_1K, 1);
        contentValues.put(DP_FREQ_1K5, 1);
        contentValues.put(DP_FREQ_2K, 1);
        contentValues.put(DP_FREQ_3K, 1);
        contentValues.put(DP_FREQ_4K, 1);
        contentValues.put(DP_FREQ_5K, 1);
        contentValues.put(DP_FREQ_6K, 1);
        contentValues.put(DP_FREQ_8K, 1);
        contentValues.put(DP_T_THRESH, 8);
        contentValues.put(DP_R_F1, 1);
        contentValues.put(DP_R_L1, 3);
        contentValues.put(DP_R_L2, 4);
        contentValues.put(DP_T_MAX_DUR, 15.0);
        contentValues.put(DP_T_SNR, 3);
        long result = db.insert(SETTINGS_TABLE, null, contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public int getTableRowCount(String table_name){
        SQLiteDatabase db = this.getWritableDatabase();
        String count = "SELECT count(*) FROM " + table_name;
        Cursor mCursor = db.rawQuery(count, null);
        return mCursor.getCount();
    }

    private String generateID(String family_name, String given_name, String dob){
        StringBuilder sb = new StringBuilder();

        sb.append(family_name.substring(0,3));
        sb.append(given_name.substring(0,3));

        String[] dob_arr = dob.split("/");
        sb.append(dob_arr[0]);
        sb.append(dob_arr[1]);
        sb.append(dob_arr[2]);

        return sb.toString();
    }

    public void closeDB(){
        SQLiteDatabase db = this.getReadableDatabase();
        if(db != null && db.isOpen())
            db.close();
    }

    //check tables
    public void checkTables(){
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL(CREATE_TABLE_PATIENTS);
        db.execSQL(CREATE_TABLE_TESTS);
        db.execSQL(CREATE_TABLE_DP_TESTS);
        db.execSQL(CREATE_TABLE_DP_RESULTS);
        db.execSQL(CREATE_TABLE_TE_TESTS);
        db.execSQL(CREATE_TABLE_SETTINGS);
    }

    /******************************************************************************
     ************************************CREATORS**********************************
     ******************************************************************************/
    public boolean createPatient(String family_name, String given_name, String dob,
                                 Integer weight, Integer height)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(PAT_FN, family_name);
        contentValues.put(PAT_GN, given_name);
        contentValues.put(PAT_DOB, dob);
        contentValues.put(PAT_HT, height);
        contentValues.put(PAT_WT, weight);
        long result = db.insert(PATIENTS_TABLE, null, contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean createPatient(PatientModel patient)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(PAT_FN, patient.getFamilyName());
        contentValues.put(PAT_GN, patient.getGivenName());
        contentValues.put(PAT_DOB, patient.getDob());
        contentValues.put(PAT_HT, patient.getHeight());
        contentValues.put(PAT_WT, patient.getWeight());
        long result = db.insert(PATIENTS_TABLE, null, contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean createTest(String patient_ID, TestModel.TestType test_type){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(PAT_ID, patient_ID);
        switch (test_type){
            case DPOAE:
                contentValues.put(TEST_TYPE, context.getResources().getString(R.string.d_test));
                break;
            case TEOAE:
                contentValues.put(TEST_TYPE, context.getResources().getString(R.string.t_test));
                break;
            default:
                break;
        }
        long result = db.insert(TESTS_TABLE, null, contentValues);
        if(result == -1) return false;
        else return true;
    }

    public boolean createDPOAETest(DPOAETestModel test){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(TEST_ID, test.getTest_ID());
        contentValues.put(DP_T_RATIO, test.getRatio());
        contentValues.put(DP_T_THRESH, test.getThreshold());
        contentValues.put(DP_T_MAX_DUR, test.getMaximumDuration());
        long result = db.insert(DP_TESTS_TABLE, null, contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean createDPOAEResult(DPOAETestModel test){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(TEST_ID, test.getTest_ID());
        contentValues.put(DP_R_NOISE, test.getNoise());
        contentValues.put(DP_R_LEVEL, test.getDP_level());
        contentValues.put(DP_R_F1, test.getF1());
        contentValues.put(DP_R_L1, test.getL1());
        contentValues.put(DP_R_L2, test.getL2());
        long result = db.insert(DP_RESULTS_TABLE, null, contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean createTEOAETest(TEOAETestModel test){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(TEST_ID, test.getTest_ID());
        contentValues.put(TE_FILE_NAME, test.getFileName());
        contentValues.put(TE_DUR, test.getDuration());
        contentValues.put(TE_STIMULUS_LVL, test.getStimulationLevel());
        contentValues.put(TE_MAX_DUR, test.getMaximumDuration());
        long result = db.insert(TE_TESTS_TABLE, null, contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean createSettings(SettingsModel settings){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(TE_MAX_DUR, settings.getTE_max_duration());
        contentValues.put(TE_STIMULUS, String.valueOf(settings.getTE_stimulus()));
        contentValues.put(TE_STIMULUS_LVL, settings.getTE_stim_lvl());
        contentValues.put(TE_NO_PASSES, settings.getTE_num_of_passes());
        contentValues.put(TE_T_SNR, settings.getTE_SNR());

        contentValues.put(DP_NO_PASSES, settings.getDP_num_of_passes());
        contentValues.put(DP_FREQ_1K, settings.getDP_freq(SettingsModel.DP_FREQS._1K));
        contentValues.put(DP_FREQ_1K5, settings.getDP_freq(SettingsModel.DP_FREQS._1K5));
        contentValues.put(DP_FREQ_2K, settings.getDP_freq(SettingsModel.DP_FREQS._2K));
        contentValues.put(DP_FREQ_3K, settings.getDP_freq(SettingsModel.DP_FREQS._3K));
        contentValues.put(DP_FREQ_4K, settings.getDP_freq(SettingsModel.DP_FREQS._4K));
        contentValues.put(DP_FREQ_5K, settings.getDP_freq(SettingsModel.DP_FREQS._5K));
        contentValues.put(DP_FREQ_6K, settings.getDP_freq(SettingsModel.DP_FREQS._6K));
        contentValues.put(DP_FREQ_8K, settings.getDP_freq(SettingsModel.DP_FREQS._8K));
        contentValues.put(DP_T_THRESH, settings.getDP_threshold());
        contentValues.put(DP_R_F1, settings.getDP_f1());
        contentValues.put(DP_R_L1, settings.getDP_l1());
        contentValues.put(DP_R_L2, settings.getDP_l2());
        contentValues.put(DP_T_MAX_DUR, settings.getDP_max_duration());
        contentValues.put(DP_T_SNR, settings.getDP_SNR());
        long result = db.insert(SETTINGS_TABLE, null, contentValues);
        if(result == -1)
            return false;
        else
            return true;

    }

    /******************************************************************************
     ************************************FETCHING**********************************
     ******************************************************************************/
    public Cursor getAllPatientData(){

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + PATIENTS_TABLE, null);
        return res;
    }

    public PatientModel getPatientByID(String ID){
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + PATIENTS_TABLE + " WHERE " + PAT_ID + " = " + "'"+ID+"'";

        Log.e(TAG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if(c != null)
            c.moveToFirst();

        PatientModel tmp_patient = new PatientModel(this.context);
        tmp_patient.setID(Integer.parseInt(c.getString(c.getColumnIndex(PAT_ID))));
        tmp_patient.setFamilyName((c.getString(c.getColumnIndex(PAT_FN))));
        tmp_patient.setGivenName(c.getString(c.getColumnIndex(PAT_GN)));
        tmp_patient.setDob(c.getString(c.getColumnIndex(PAT_DOB)));
        tmp_patient.setHeight(c.getInt(c.getColumnIndex(PAT_HT)));
        tmp_patient.setWeight(c.getInt(c.getColumnIndex(PAT_WT)));

        return tmp_patient;
    }

    public boolean patientExists(String ID){
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + PATIENTS_TABLE + " WHERE " + PAT_ID + " = " + "'"+ID+"'";

        Log.e(TAG, selectQuery);

        Cursor c = null;

        try{
            c = db.rawQuery(selectQuery, null);

            if(c.moveToFirst()){
                c.close();
                return true;
            }else{
                c.close();
                return false;
            }
        }catch(Exception e){
            c.close();
            return false;
        }
    }

    public PatientModel getPatientByFamilyName(String family_name){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + PATIENTS_TABLE + " WHERE " + PAT_FN + " = " + family_name;

        Log.e(TAG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if(c != null)
            c.moveToFirst();

        PatientModel tmp_patient = new PatientModel(this.context);
        tmp_patient.setID(Integer.parseInt(c.getString(c.getColumnIndex(PAT_ID))));
        tmp_patient.setFamilyName((c.getString(c.getColumnIndex(PAT_FN))));
        tmp_patient.setGivenName(c.getString(c.getColumnIndex(PAT_GN)));
        tmp_patient.setDob(c.getString(c.getColumnIndex(PAT_DOB)));
        tmp_patient.setHeight(c.getInt(c.getColumnIndex(PAT_HT)));
        tmp_patient.setWeight(c.getInt(c.getColumnIndex(PAT_WT)));

        return tmp_patient;
    }

    public PatientModel[] getAllPatients(){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + PATIENTS_TABLE;

        Log.e(TAG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);
        PatientModel[] patients = new PatientModel[c.getCount()];

        c.moveToFirst();
        for(int i = 0; i < c.getCount(); i++){
            patients[i] = new PatientModel(this.context);

            patients[i].setID(Integer.parseInt(c.getString(c.getColumnIndex(PAT_ID))));
            patients[i].setFamilyName((c.getString(c.getColumnIndex(PAT_FN))));
            patients[i].setGivenName(c.getString(c.getColumnIndex(PAT_GN)));
            patients[i].setDob(c.getString(c.getColumnIndex(PAT_DOB)));
            patients[i].setHeight(c.getInt(c.getColumnIndex(PAT_HT)));
            patients[i].setWeight(c.getInt(c.getColumnIndex(PAT_WT)));

            c.moveToNext();
        }

        c.close();
        return patients;
    }

    public SettingsModel getSettings(int preset_ID){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + SETTINGS_TABLE + " WHERE " + SET_PRESET + " = " + preset_ID;

        Log.e(TAG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        SettingsModel ret_settings = new SettingsModel(this.context);

        if(c != null) {
            c.moveToFirst();

            ret_settings.setPreset_ID(c.getInt(c.getColumnIndex(SET_PRESET)));
            ret_settings.setTE_max_duration(c.getFloat(c.getColumnIndex(TE_MAX_DUR)));
            ret_settings.setTE_stimulus(SettingsModel.TE_STIMULUS.valueOf(c.getString(c.getColumnIndex(TE_STIMULUS))));
            ret_settings.setTE_num_of_passes(c.getInt(c.getColumnIndex(TE_NO_PASSES)));
            ret_settings.setTE_stim_lvl(c.getInt(c.getColumnIndex(TE_STIMULUS_LVL)));
            ret_settings.setTE_SNR(c.getInt(c.getColumnIndex(TE_T_SNR)));
            ret_settings.setDP_num_of_passes(c.getInt(c.getColumnIndex(DP_NO_PASSES)));
            ret_settings.setDP_freq(0, c.getInt(c.getColumnIndex(DP_FREQ_1K)));
            ret_settings.setDP_freq(1, c.getInt(c.getColumnIndex(DP_FREQ_1K5)));
            ret_settings.setDP_freq(2, c.getInt(c.getColumnIndex(DP_FREQ_2K)));
            ret_settings.setDP_freq(3, c.getInt(c.getColumnIndex(DP_FREQ_3K)));
            ret_settings.setDP_freq(4, c.getInt(c.getColumnIndex(DP_FREQ_4K)));
            ret_settings.setDP_freq(5, c.getInt(c.getColumnIndex(DP_FREQ_5K)));
            ret_settings.setDP_freq(6, c.getInt(c.getColumnIndex(DP_FREQ_6K)));
            ret_settings.setDP_freq(7, c.getInt(c.getColumnIndex(DP_FREQ_8K)));
            ret_settings.setDP_threshold(c.getInt(c.getColumnIndex(DP_T_THRESH)));
            ret_settings.setDP_f1(c.getFloat(c.getColumnIndex(DP_R_F1)));
            ret_settings.setDP_l1(c.getFloat(c.getColumnIndex(DP_R_L1)));
            ret_settings.setDP_l2(c.getFloat(c.getColumnIndex(DP_R_L2)));
            ret_settings.setDP_max_duration(c.getFloat(c.getColumnIndex(DP_T_MAX_DUR)));
            ret_settings.setDP_SNR(c.getInt(c.getColumnIndex(DP_T_SNR)));
        }

        c.close();
        return ret_settings;
    }

    /******************************************************************************
     ************************************UPDATING**********************************
     ******************************************************************************/
    public void updatePatient(PatientModel patient){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PAT_ID, patient.getID());
        values.put(PAT_FN, patient.getFamilyName());
        values.put(PAT_GN, patient.getGivenName());
        values.put(PAT_DOB, patient.getDob());
        values.put(PAT_HT, patient.getHeight());
        values.put(PAT_WT, patient.getWeight());

        db.update(PATIENTS_TABLE, values, PAT_ID + " =?",
                new String[]{patient.getID().toString()});
    }

    public void updateSettings(SettingsModel settings){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(TE_MAX_DUR, settings.getTE_max_duration());
        contentValues.put(TE_STIMULUS, String.valueOf(settings.getTE_stimulus()));
        contentValues.put(TE_STIMULUS_LVL, settings.getTE_stim_lvl());
        contentValues.put(TE_NO_PASSES, settings.getTE_num_of_passes());
        contentValues.put(TE_STIMULUS_LVL, settings.getTE_stim_lvl());
        contentValues.put(TE_T_SNR, settings.getTE_SNR());
        contentValues.put(DP_NO_PASSES, settings.getDP_num_of_passes());
        contentValues.put(DP_FREQ_1K, settings.getDP_freq(SettingsModel.DP_FREQS._1K));
        contentValues.put(DP_FREQ_1K5, settings.getDP_freq(SettingsModel.DP_FREQS._1K5));
        contentValues.put(DP_FREQ_2K, settings.getDP_freq(SettingsModel.DP_FREQS._2K));
        contentValues.put(DP_FREQ_3K, settings.getDP_freq(SettingsModel.DP_FREQS._3K));
        contentValues.put(DP_FREQ_4K, settings.getDP_freq(SettingsModel.DP_FREQS._4K));
        contentValues.put(DP_FREQ_5K, settings.getDP_freq(SettingsModel.DP_FREQS._5K));
        contentValues.put(DP_FREQ_6K, settings.getDP_freq(SettingsModel.DP_FREQS._6K));
        contentValues.put(DP_FREQ_8K, settings.getDP_freq(SettingsModel.DP_FREQS._8K));
        contentValues.put(DP_T_THRESH, settings.getDP_threshold());
        contentValues.put(DP_R_F1, settings.getDP_f1());
        contentValues.put(DP_R_L1, settings.getDP_l1());
        contentValues.put(DP_R_L2, settings.getDP_l2());
        contentValues.put(DP_T_MAX_DUR, settings.getDP_max_duration());
        contentValues.put(DP_T_SNR, settings.getDP_SNR());

        db.update(SETTINGS_TABLE, contentValues, SET_PRESET + " =?",
                new String[]{Integer.toString(settings.getPreset_ID())});
    }

    /******************************************************************************
     ************************************DROPPING**********************************
     ******************************************************************************/
    public void removeAllPatients(){
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DELETE FROM " + PATIENTS_TABLE);
    }

    public void removePatientByID(String ID){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(PATIENTS_TABLE, PAT_ID + " =?", new String[]{ID});
    }

    //delete databases
    public void dropPatientable(){
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DROP TABLE IF EXISTS " + PATIENTS_TABLE);
    }

    public void dropTestTable(){
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DROP TABLE IF EXISTS " + TESTS_TABLE);
    }
}
