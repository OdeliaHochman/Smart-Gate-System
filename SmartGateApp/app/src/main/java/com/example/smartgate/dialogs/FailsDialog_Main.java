package com.example.smartgate.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.smartgate.DriversDetailsVerification;
import com.example.smartgate.SearchActivity;

public class FailsDialog_Main extends AppCompatDialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Identification failed")
                .setMessage("Perform manual verification with the driver.")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            Thread.sleep(20);
                            Intent intent = new Intent(getActivity(), DriversDetailsVerification.class);
                            startActivity(intent);
                            getDialog().dismiss();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });

        return builder.create();
    }
}
