package hearscreening.rcs.ei.tum.de.sound4all;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class PatientHistory extends AppCompatActivity {

    private static final String TAG = "PatientHistoryActivity";

    String patient_ID;

    DatabaseHelper myDb;

    PatientModel patient;

    FloatingActionButton fab_delete_patient;
    FloatingActionButton fab_add_test;

    Dialog NFCDialog;

    //patient display views
    TextView tv_name;
    TextView tv_dob;
    TextView tv_height;
    TextView tv_weight;

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_history);

        patient_ID = getIntent().getStringExtra("PATIENT_ID");

        //get patient information
        myDb = new DatabaseHelper(PatientHistory.this);
        patient = myDb.getPatientByID(patient_ID);

        getSupportActionBar().setTitle("Patient History");

        fab_add_test = (FloatingActionButton) findViewById(R.id.fab_patient_history_add_test);

        fab_add_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to add test view
                AlertDialog.Builder b = new AlertDialog.Builder(PatientHistory.this);

                b.setTitle("New Test");

                String[] test_types = {getResources().getString(R.string.d_test),
                getResources().getString(R.string.t_test)};

                b.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                b.setItems(test_types, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(PatientHistory.this, TestActivity.class);
                        intent.putExtra("PATIENT", patient);
                        switch(which){
                            case 0:
                                //DPOE
                                intent.putExtra("TEST_TYPE", 1);
                                break;
                            case 1:
                                //TPOE
                                intent.putExtra("TEST_TYPE", 2);
                                break;
                            default:
                                break;
                        }
                        PatientHistory.this.startActivity(intent);
                    }
                });
                final AlertDialog alert = b.create();
                alert.show();
            }
        });

        tv_name = (TextView) findViewById(R.id.tv_patient_history_name);
        tv_name.setText(capitalizeWord(patient.getFamilyName()) + ", " +
                capitalizeWord(patient.getGivenName()));
        tv_dob = (TextView) findViewById(R.id.tv_patient_history_DOB);
        tv_dob.setText(patient.getDob());
        tv_height = (TextView) findViewById(R.id.tv_patient_history_height);
        tv_height.setText(Integer.toString(patient.getHeight()));
        tv_weight = (TextView) findViewById(R.id.tv_patient_history_weight);
        tv_weight.setText(Integer.toString(patient.getWeight()));
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.patientmenu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.menu_patient_edit:
                editPatientAlertDialog();
                return true;
            case R.id.menu_patient_delete:
                myDb.removePatientByID(patient_ID);
                Intent intent = new Intent(PatientHistory.this, PatientListActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void editPatientAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.edit_patient_dialog, null);
        builder.setView(dialogLayout);

        builder.setTitle("Edit Patient");

        //get views
        final EditText et_family_name = (EditText) dialogLayout.findViewById(R.id.edit_patient_family_name);
        et_family_name.setText(patient.getFamilyName());

        final EditText et_given_name = (EditText) dialogLayout.findViewById(R.id.edit_patient_given_name);
        et_given_name.setText(patient.getGivenName());

        final EditText et_height = (EditText) dialogLayout.findViewById(R.id.edit_patient_height);
        et_height.setText(Integer.toString(patient.getHeight()));

        final EditText et_weight = (EditText) dialogLayout.findViewById(R.id.edit_patient_weight);
        et_weight.setText(Integer.toString(patient.getWeight()));

        final TextView tv_dob = (TextView) dialogLayout.findViewById(R.id.edit_patient_dob);
        tv_dob.setText(patient.getDob());

        //TODO spinner
        Button btn_dob = (Button) dialogLayout.findViewById(R.id.edit_patient_dob_button);

        btn_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(PatientHistory.this,
                        android.R.style.Theme_Holo_Light_DarkActionBar,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCanceledOnTouchOutside(true);
                dialog.setTitle(getString(R.string.date_picker_title));
                dialog.getWindow().setGravity(Gravity.CENTER);
                dialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                Log.d(TAG, "onDateSet: date: " + year + "/" + month + "/" + dayOfMonth);

                String date = dayOfMonth + "/" + month + "/" + year;
//                tv_dob = findViewById(R.id.edit_patient_dob);
                tv_dob.setText(date);

            }
        };

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                patient.setFamilyName(et_family_name.getText().toString());
                patient.setGivenName(et_given_name.getText().toString());
                patient.setDob(tv_dob.getText().toString());
                patient.setHeight(Integer.parseInt(et_height.getText().toString()));
                patient.setWeight(Integer.parseInt(et_weight.getText().toString()));

                myDb.updatePatient(patient);

                Toast.makeText(PatientHistory.this,
                        getResources().getString(R.string.patient_updated_toast) , Toast.LENGTH_LONG).show();

                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

        AlertDialog d = builder.create();
        d.show();
    }

    private String capitalizeWord(String word){
        StringBuilder sb = new StringBuilder(word);
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        return sb.toString();
    }

}
