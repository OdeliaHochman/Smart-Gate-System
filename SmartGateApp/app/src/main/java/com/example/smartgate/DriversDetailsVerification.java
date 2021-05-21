
package com.example.smartgate;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
                //if(v ==  ok_btn)
                    //check DB
            }
        });
    }
}
