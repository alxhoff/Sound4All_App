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
    public final static String DP_R_F2 = "DP_RES_F2";
    public final static String DP_R_L1 = "DP_RES_L1";
    public final static String DP_R_L2 = "DP_RES_L2";


    //***************TE TESTS TABLE***************//s
    //test ID
    public final static String TE_FILE_NAME = "TE_FILE_NAME";
    public final static String TE_DUR = "TE_DUR";
    public final static String TE_STIM_LVL = "TE_STIM_LVL";
    public final static String TE_MAX_DUR = "TE_MAX_DUR";


    //*****************COMMON COLS****************//
    public final static String TEST_ID = "TEST_ID";


    //***************PATIENTS TABLE****************//
    private static final String CREATE_TABLE_PATIENTS = "CREATE TABLE IF NOT EXISTS "
            + PATIENTS_TABLE + " ("
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
            + DP_R_F2 + " INTEGER, "
            + DP_R_L1 + " INTEGER, "
            + DP_R_L2 + " INTEGER)";



    //***************TE TESTS TABLE***************//
    private static final String CREATE_TABLE_TE_TESTS = "CREATE TABLE IF NOT EXISTS "
            + TE_TESTS_TABLE + "("
            + TEST_ID + " INTEGER PRIMARY KEY, "
            + TE_FILE_NAME + " TEXT, "
            + TE_DUR + " REAL, "
            + TE_MAX_DUR + " REAL, "
            + TE_STIM_LVL + " REAL)";


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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PATIENTS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TESTS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DP_RESULTS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DP_TESTS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TE_TESTS_TABLE);
        onCreate(db);
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
    }

    //create entries
    public boolean createPatient(String family_name, String given_name, String dob,
                                 Integer weight, Integer height)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
//        String ROWID = generateID(family_name, given_name, dob);
//        contentValues.put(PAT_ID, ROWID);
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

    //fetching
    //all
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

        PatientModel tmp_patient = new PatientModel();
        tmp_patient.setID(Integer.parseInt(c.getString(c.getColumnIndex(PAT_ID))));
        tmp_patient.setFamily_name((c.getString(c.getColumnIndex(PAT_FN))));
        tmp_patient.setGiven_name(c.getString(c.getColumnIndex(PAT_GN)));
        tmp_patient.setDob(c.getString(c.getColumnIndex(PAT_DOB)));
        tmp_patient.set_height(c.getInt(c.getColumnIndex(PAT_HT)));
        tmp_patient.setWeight(c.getInt(c.getColumnIndex(PAT_WT)));

        return tmp_patient;
    }

    public PatientModel getPatientByFamilyName(String family_name){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + PATIENTS_TABLE + " WHERE " + PAT_FN + " = " + family_name;

        Log.e(TAG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if(c != null)
            c.moveToFirst();

        PatientModel tmp_patient = new PatientModel();
        tmp_patient.setID(Integer.parseInt(c.getString(c.getColumnIndex(PAT_ID))));
        tmp_patient.setFamily_name((c.getString(c.getColumnIndex(PAT_FN))));
        tmp_patient.setGiven_name(c.getString(c.getColumnIndex(PAT_GN)));
        tmp_patient.setDob(c.getString(c.getColumnIndex(PAT_DOB)));
        tmp_patient.set_height(c.getInt(c.getColumnIndex(PAT_HT)));
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
            patients[i] = new PatientModel();

//            String test = c.getString(5);
            Integer test_int = c.getCount();

            patients[i].setID(Integer.parseInt(c.getString(c.getColumnIndex(PAT_ID))));
            patients[i].setFamily_name((c.getString(c.getColumnIndex(PAT_FN))));
            patients[i].setGiven_name(c.getString(c.getColumnIndex(PAT_GN)));
            patients[i].setDob(c.getString(c.getColumnIndex(PAT_DOB)));
            patients[i].set_height(c.getInt(c.getColumnIndex(PAT_HT)));
            patients[i].setWeight(c.getInt(c.getColumnIndex(PAT_WT)));

            c.moveToNext();
        }
        return patients;
    }

    public void updatePatient(PatientModel patient){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PAT_ID, patient.getID());
        values.put(PAT_FN, patient.getFamily_name());
        values.put(PAT_GN, patient.getGiven_name());
        values.put(PAT_DOB, patient.getDob());
        values.put(PAT_HT, patient.get_height());
        values.put(PAT_WT, patient.getWeight());

        db.update(PATIENTS_TABLE, values, PAT_ID + " =?",
                new String[]{patient.getID().toString()});
    }

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
