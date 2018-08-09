package hearscreening.rcs.ei.tum.de.sound4all;

import android.app.Dialog;
import android.content.DialogInterface;
import android.media.Image;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class TestActivity extends AppCompatActivity {

    Dialog MyDialog;
    Button hello;
    Button close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        Integer test_type = getIntent().getIntExtra("TEST_TYPE", 0);

        switch(test_type){
            case 1:
                getSupportActionBar().setTitle(getResources().getString(R.string.d_test) + " Test");
                break;
            case 2:
                getSupportActionBar().setTitle(getResources().getString(R.string.t_test) + " Test");
                break;
        }

        MyCustomAlertDialog();

    }


    public void MyCustomAlertDialog(){

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
