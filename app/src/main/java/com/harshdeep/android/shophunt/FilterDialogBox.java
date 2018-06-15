package com.harshdeep.android.shophunt;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

public class FilterDialogBox extends DialogFragment {

    static int finaly=2;
    int choice;
    private GetList list;

    @Override
    public void onAttach(Context context) {

        if(context instanceof GetList){
            list=(GetList)context;
        }
        super.onAttach(context);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Sort");

        builder.setNegativeButton("Cancel", null);


        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (choice!=finaly){
                    finaly=choice;
                    list.sortListAndNotifyAdapter();
                }
            }
        });

        String[] arr = {"Price: Low to High","Price: High to Low","None"};
        builder.setSingleChoiceItems(arr, finaly, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i){
                    case 0:
                        choice=0;
                        break;
                    case 1:
                        choice=1;
                        break;
                    case 2:
                        choice=2;
                }
            }
        });

        return builder.create();
    }

    interface GetList{
        void sortListAndNotifyAdapter();
    }

}
