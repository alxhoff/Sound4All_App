package hearscreening.rcs.ei.tum.de.sound4all;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    DatabaseHelper myDb;

    Button clear_db;
    Button btn_drop_patients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        myDb = new DatabaseHelper(this);

        clear_db = (Button) findViewById(R.id.btn_settings_clear_db);
        clear_db.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDb.removeAllPatients();

                Cursor all_data = myDb.getAllPatientData();

                if(all_data.getCount() == 0)
                    Toast.makeText(SettingsActivity.this, "Database cleared", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(SettingsActivity.this, "Database error", Toast.LENGTH_LONG).show();
            }
        });

        btn_drop_patients = (Button) findViewById(R.id.btn_settings_drop_patients);
        btn_drop_patients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDb.dropPatientable();
            }
        });
    }
}
