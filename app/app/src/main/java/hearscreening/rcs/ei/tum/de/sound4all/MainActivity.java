package hearscreening.rcs.ei.tum.de.sound4all;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    DatabaseHelper myDb;

    private Button mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private static final int field_count = 5;

    EditText et_family_name;
    EditText et_given_name;
    EditText et_weight;
    EditText et_height;
    TextView tv_date;
    Button btn_continue;

    private boolean validate(String[] fields){
        for (int i = 0; i < field_count; i++)
            if(fields[i].matches("") || fields[i].matches("-"))
                return false;
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Add Patient");

        mDisplayDate = (Button) findViewById(R.id.btn_patient_form_dob);

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(MainActivity.this,
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
                tv_date = findViewById(R.id.tv_patient_form_dob);
                tv_date.setText(date);
            }
        };

        myDb = new DatabaseHelper(MainActivity.this);

        //    screen objects
        et_family_name = (EditText) findViewById(R.id.et_patient_form_familyname);
        et_given_name = (EditText) findViewById(R.id.et_patient_form_givename);
        et_weight = (EditText) findViewById(R.id.et_patient_form_weight);
        et_height = (EditText) findViewById(R.id.et_patient_form_height);

        final TextView tv_date = (TextView) findViewById(R.id.tv_patient_form_dob);

        btn_continue = (Button) findViewById(R.id.btn_patient_form_continue);

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean fields_populated = validate(new String[]{et_family_name.getText().toString(),
                        et_given_name.getText().toString(), tv_date.getText().toString(),
                        et_height.getText().toString(), et_weight.getText().toString()});
                if(fields_populated) {
                    boolean isInserted = myDb.createPatient(et_family_name.getText().toString(),
                            et_given_name.getText().toString(),
                            tv_date.getText().toString(),
                            Integer.parseInt(et_height.getText().toString()),
                            Integer.parseInt(et_weight.getText().toString()));
                    if (isInserted == true) {
                        Toast.makeText(MainActivity.this, getResources().getString(R.string.patient_stored_toast), Toast.LENGTH_LONG).show();
                        Intent createNewUser = new Intent(MainActivity.this, PatientListActivity.class);
                        startActivity(createNewUser);
                    }else
                        Toast.makeText(MainActivity.this, "Patient not stored, DB error", Toast.LENGTH_LONG).show();
                }else
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.patient_store_failed_toast), Toast.LENGTH_LONG).show();
            }
        });
    }
}


