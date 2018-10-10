package hearscreening.rcs.ei.tum.de.sound4all;

import android.graphics.Color;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuInflater;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class PatientListActivity extends AppCompatActivity {

    DatabaseHelper myDb;

    private TableLayout parentLayout;
    PatientModel[] patientList;

    FloatingActionButton add_patient_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_list);

        myDb = new DatabaseHelper(this);
        myDb.checkTables();

        patientList = myDb.getAllPatients();

        parentLayout = (TableLayout) findViewById(R.id.patient_list_layout);

        parentLayout.setStretchAllColumns(true);

        loadData();

        //settings default added
        int setting_table_count = myDb.getTableRowCount(DatabaseHelper.SETTINGS_TABLE);
        if(myDb.getTableRowCount(DatabaseHelper.SETTINGS_TABLE) == 1)
            myDb.settingsDefaults();

        //add patient button
        add_patient_button = (FloatingActionButton) findViewById(R.id.fab_add_patient);
        add_patient_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //action
                Intent createNewUser = new Intent(PatientListActivity.this,
                        MainActivity.class);
                startActivity(createNewUser);
            }
        });
    }

    private String capitalizeWord(String word){
        StringBuilder sb = new StringBuilder(word);
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        return sb.toString();
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settingsmenu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.menu_about:
                Toast.makeText(PatientListActivity.this, "About menu",
                        Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_settings:
                Intent intent = new Intent(getBaseContext(), SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void loadData(){
        int leftRowMargin=0;
        int topRowMargin=0;
        int rightRowMargin=0;
        int bottomRowMargin = 0;
        int textSize = 0, smallTextSize =0, mediumTextSize = 0;

        textSize = (int) getResources().getDimension(R.dimen.font_size_verysmall);
        smallTextSize = (int) getResources().getDimension(R.dimen.font_size_small);
        mediumTextSize = (int) getResources().getDimension(R.dimen.font_size_medium);

        int rows = patientList.length;
        getSupportActionBar().setTitle("Patients (" + String.valueOf(rows) + ")");
        TextView textSpacer = null;

        parentLayout.removeAllViews();

        //-1 is heading row
        for(int i = -1; i < rows; i++){
            PatientModel row = null;
            if(i > -1)
                row = patientList[i];
            else{
                textSpacer = new TextView(this);
                textSpacer.setText("");
            }

            //columns
            //col1
            final TextView tv = new TextView(this);
            tv.setLayoutParams(new TableRow.LayoutParams(WRAP_CONTENT,
                    WRAP_CONTENT));

            tv.setGravity(Gravity.LEFT);

            tv.setPadding(5,15,0,15);
            if(i == -1){
                tv.setText("ID#");
                tv.setBackgroundColor(Color.parseColor("#f0f0f0"));
                tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
            }else{
                tv.setBackgroundColor(Color.parseColor("#f8f8f8"));
                tv.setText(String.valueOf(row.getID()));
                tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            }

            //col2
            final TextView tv2 = new TextView(this);
            if(i == -1){
                tv2.setLayoutParams(new TableRow.LayoutParams(MATCH_PARENT,
                        WRAP_CONTENT));
                tv2.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            }else{
                tv2.setLayoutParams(new TableRow.LayoutParams(WRAP_CONTENT,
                        MATCH_PARENT));
                tv2.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            }

            tv2.setGravity(Gravity.LEFT);


            tv2.setPadding(5, 15, 0, 15);
            if (i == -1) {
                tv2.setText("DOB");
                tv2.setBackgroundColor(Color.parseColor("#f7f7f7"));
            }else {
                tv2.setBackgroundColor(Color.parseColor("#ffffff"));
                tv2.setTextColor(Color.parseColor("#000000"));
                tv2.setText(row.getDob());
            }

            //col3
            final LinearLayout layPatient = new LinearLayout(this);
            layPatient.setOrientation(LinearLayout.VERTICAL);
            layPatient.setPadding(0, 10, 0, 10);
            layPatient.setBackgroundColor(Color.parseColor("#f8f8f8"));

            final TextView tv3 = new TextView(this);
            if (i == -1) {
                tv3.setLayoutParams(new TableRow.LayoutParams(MATCH_PARENT,
                        MATCH_PARENT));
                tv3.setPadding(5, 5, 0, 5);
                tv3.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
            } else {
                tv3.setLayoutParams(new TableRow.LayoutParams(MATCH_PARENT,
                        MATCH_PARENT));
                tv3.setPadding(5, 0, 0, 5);
                tv3.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            }

            tv3.setGravity(Gravity.TOP);

            if (i == -1) {
                tv3.setText("Name");
                tv3.setBackgroundColor(Color.parseColor("#f0f0f0"));
            } else {
                tv3.setBackgroundColor(Color.parseColor("#f8f8f8"));
                tv3.setTextColor(Color.parseColor("#000000"));
                tv3.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                tv3.setText(capitalizeWord(row.getFamilyName()));
            }
            layPatient.addView(tv3);

            //substring
            if (i > -1) {
                final TextView tv3b = new TextView(this);
                tv3b.setLayoutParams(new TableRow.LayoutParams(WRAP_CONTENT,
                        WRAP_CONTENT));

                tv3b.setGravity(Gravity.RIGHT);
                tv3b.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                tv3b.setPadding(5, 1, 0, 5);
                tv3b.setTextColor(Color.parseColor("#aaaaaa"));
                tv3b.setBackgroundColor(Color.parseColor("#f8f8f8"));
                tv3b.setText(capitalizeWord(row.getGivenName()));
                layPatient.addView(tv3b);
            }

            final LinearLayout layAmounts = new LinearLayout(this);
            layAmounts.setOrientation(LinearLayout.VERTICAL);
            layAmounts.setGravity(Gravity.RIGHT);
            layAmounts.setPadding(0, 10, 0, 10);
            layAmounts.setLayoutParams(new TableRow.LayoutParams(MATCH_PARENT,
                    MATCH_PARENT));

            //add table row
            final TableRow tr = new TableRow(this);
            tr.setId(i + 1);
            TableLayout.LayoutParams trParams = new TableLayout.LayoutParams(MATCH_PARENT,
                    MATCH_PARENT);
            trParams.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin);
            tr.setPadding(0,0,0,0);
            tr.setLayoutParams(trParams);

            tr.addView(tv);
            tr.addView(tv2);
            tr.addView(layPatient);

            if (i > -1) {

                tr.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        TableRow tr = (TableRow) v;
                        //do whatever action is needed
                        Intent intent = new Intent(getBaseContext(), PatientHistory.class);
                        intent.putExtra("PATIENT_ID", ((TextView)tr.getChildAt(0)).getText().toString());
                        startActivity(intent);
                    }
                });
            }

            parentLayout.addView(tr, trParams);

            if (i > -1) {

                // add separator row
                final TableRow trSep = new TableRow(this);
                TableLayout.LayoutParams trParamsSep = new TableLayout.LayoutParams(MATCH_PARENT,
                        MATCH_PARENT);
                trParamsSep.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin);

                trSep.setLayoutParams(trParamsSep);
                TextView tvSep = new TextView(this);
                TableRow.LayoutParams tvSepLay = new TableRow.LayoutParams(MATCH_PARENT,
                        WRAP_CONTENT);
                tvSepLay.span = 4;
                tvSep.setLayoutParams(tvSepLay);
                tvSep.setBackgroundColor(Color.parseColor("#d9d9d9"));
                tvSep.setHeight(1);

                trSep.addView(tvSep);
                parentLayout.addView(trSep, trParamsSep);
            }
        }
    }
}
