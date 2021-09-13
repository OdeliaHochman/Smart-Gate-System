
package com.example.smartgate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.smartgate.dataObject.AuthorizedPerson;
import com.example.smartgate.dialogs.FailsDialog_DDV;
import com.example.smartgate.dialogs.SuccessDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DriversDetailsVerification extends AppCompatActivity  {

    private Button ok_btn;
    private EditText EditTextFirstName,EditTextLastName,EditTextEmployee,EditTextID,EditTextLP;
    private String placeName;
    private DatabaseReference rootRef,uidRef;
    private FirebaseDatabase firebaseDatabase;


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

        firebaseDatabase = FirebaseDatabase.getInstance();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        rootRef = FirebaseDatabase.getInstance().getReference();
        uidRef = rootRef.child("Users").child(uid);


        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                placeName = datasnapshot.child("name").getValue(String.class);
                Log.d("debug" , placeName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("firebase", error.getMessage());
            }
        };
        uidRef.addListenerForSingleValueEvent(valueEventListener);




        ok_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(v ==  ok_btn)
                    checkDetails(placeName);
            }
        });
    }

    private void checkDetails (String place)
    {
        if (isEmpty())
            return;
        String fNameStr = EditTextFirstName.getText().toString();
        String lNameStr = EditTextLastName.getText().toString();
        String IDStr = EditTextID.getText().toString();
        String LPStr = EditTextLP.getText().toString();
        String employeeStr = EditTextEmployee.getText().toString();


        DatabaseReference reference = firebaseDatabase.getReference("Places").child(place).child("Authorized People").child(IDStr);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                AuthorizedPerson authperson = dataSnapshot.getValue(AuthorizedPerson.class);

                if (fNameStr.equals(authperson.getFirstName()) && lNameStr.equals(authperson.getLastName()) &&
                        IDStr.equals(authperson.getIDNumber()) && LPStr.equals(authperson.getLPNumber())
                         && employeeStr.equals(authperson.getEmployeeNumber()))
                {
                    openSuccessDialog();
                    EditTextFirstName.getText().clear();
                    EditTextLastName.getText().clear();
                    EditTextEmployee.getText().clear();
                    EditTextID.getText().clear();
                    EditTextLP.getText().clear();
                    //TODO: we need to add --> open gate!
                }
                else
                {
                    openFailsDialog();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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

    public void openSuccessDialog() {
        SuccessDialog successDialogDDV = new SuccessDialog();
        successDialogDDV.show(getSupportFragmentManager(), "success dialog");
    }

    public void openFailsDialog() {
        FailsDialog_DDV failsDialogDDV = new FailsDialog_DDV();
        failsDialogDDV.show(getSupportFragmentManager(), "fails dialog");
    }
}


