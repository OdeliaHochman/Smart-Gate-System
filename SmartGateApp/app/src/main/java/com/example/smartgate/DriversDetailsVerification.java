
package com.example.smartgate;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DriversDetailsVerification extends AppCompatActivity {

    private Button ok_btn;
    private EditText EditTextFirstName,EditTextLastName,EditTextEmployee,EditTextID,EditTextLP;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drivers_details_verification);

        ok_btn =(Button) findViewById(R.id.ok_btn_DDV);

        EditTextFirstName =  (EditText) findViewById(R.id.firstName_editTxt_DDV);
        EditTextLastName =  (EditText) findViewById(R.id.lastName_editTxt_DDV);
        EditTextEmployee =  (EditText) findViewById(R.id.employeeNumber_editTxt_DDV);
        EditTextID =  (EditText) findViewById(R.id.IDNumber_editTxt_DDV);
        EditTextLP =  (EditText) findViewById(R.id.LPNumber_editTxt_DDV);

        ok_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(v ==  ok_btn)
                    checkDetails();
            }
        });
    }

    private void checkDetails ()
    {
        if (isEmpty())
            return;
        String fNameStr = EditTextFirstName.getText().toString();
        String lNameStr = EditTextLastName.getText().toString();
        String IDStr = EditTextID.getText().toString();
        String LPStr = EditTextLP.getText().toString();
        String employeeStr = EditTextEmployee.getText().toString();

        /*
        Missing in function:
        read details from db --> compare details  -->
           --> if one detail is incorrect, a message will appear to the guard.
           --> if all the details are correct, a message will appear to the guard and a gate will open.
        */


    }


    // return if one of the text view is empty
    private  boolean isEmpty () {
        if ((TextUtils.isEmpty(EditTextFirstName.getText().toString())) || (TextUtils.isEmpty(EditTextLastName.getText().toString())) ||
                (TextUtils.isEmpty(EditTextEmployee.getText().toString())) || (TextUtils.isEmpty(EditTextID.getText().toString())) || (TextUtils.isEmpty(EditTextLP.getText().toString()))) {
            Toast.makeText(this, "Missing details", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }
}
