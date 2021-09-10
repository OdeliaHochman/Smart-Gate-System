package com.example.smartgate.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.smartgate.R;
import com.example.smartgate.SearchActivity;
import com.example.smartgate.dataObject.User;
import com.example.smartgate.firebaseHelper.FirebaseUserHelper;


public class AdminDialog extends AppCompatDialogFragment {
    private EditText editTCode;
   // public ExampleDialogListener listener;
    private String code;
    private String adminCode;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        new FirebaseUserHelper().readUser(new FirebaseUserHelper.DataStatusUser() {
            @Override
            public void DataIsLoaded(User userHelper, String key) {
                adminCode = userHelper.getAdminCode();
                System.out.println("admin code:"+adminCode);
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);
        editTCode = view.findViewById(R.id.code_EditText);


        builder.setView(view)
                .setTitle("Administrator Code")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })

                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                      code = editTCode.getText().toString();
                      //listener.applyTexts(code,flag);

                      if(adminCode.equals(code))
                      {
                          Intent intent = new Intent(getActivity(), SearchActivity.class);
                          startActivity(intent);
                          getDialog().dismiss();
                      }
                      else
                          {
                              Toast.makeText(getActivity(), " Invalid code", Toast.LENGTH_SHORT).show();

                          }
                    }
                });

        return builder.create();

    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//
//        try {
//            listener = (ExampleDialogListener) context;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(context.toString() +
//                    "must implement ExampleDialogListener");
//        }
//    }
//
//    public interface ExampleDialogListener {
//        void applyTexts(String code,boolean flag);
//    }
}
