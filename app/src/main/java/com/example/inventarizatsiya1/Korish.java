package com.example.inventarizatsiya1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Korish extends AppCompatActivity implements View.OnClickListener {

    Button toExcel, sqclear,bDelete;
    TextView tv,tv_inputExcel;
    EditText excelfilename;
    ListView lvMalumotlar;
    String deleteItemID="0";
    ArrayAdapter<Integer> arrayAdapter;
    int Itemposition=0;
    ArrayList arlMalumot;


    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_korish);
        lvMalumotlar=findViewById(R.id.lvMalumotlar);
        bDelete=findViewById(R.id.bDelete);
        toExcel = findViewById(R.id.bToExcel);
        sqclear = findViewById(R.id.bSQClear);
        excelfilename = findViewById(R.id.etExcelFileName);
        tv_inputExcel=findViewById(R.id.tv_inputExcel);

        toExcel.setOnClickListener(this);
        sqclear.setOnClickListener(this);
        bDelete.setOnClickListener(this);
        tv_inputExcel.setOnClickListener(this);

        Date date = new Date();
        DateFormat data_format = new SimpleDateFormat("MM_dd_");
        DateFormat time_format = new SimpleDateFormat("HH_mm");

        String scan_data = data_format.format(date);
        String scan_time = time_format.format(date);

        excelfilename.setText("Data"+scan_data+scan_time+".xls");


        HotOrNot malumot = new HotOrNot(this);
        malumot.open();
        arlMalumot = malumot.getDataForListView();
        malumot.close();
        arrayAdapter=new ArrayAdapter<Integer>(this, android.R.layout.simple_list_item_1,arlMalumot);
        lvMalumotlar.setAdapter(arrayAdapter);

        lvMalumotlar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                deleteItemID =  lvMalumotlar.getItemAtPosition(i).toString();
                deleteItemID=deleteItemID.substring(0,deleteItemID.indexOf(":"));
                bDelete.setText(deleteItemID+" : ID Delete");
                bDelete.setTextColor(Color.MAGENTA);
                bDelete.setEnabled(true);
                Itemposition=i;
            }
        });



    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {


            case R.id.tv_inputExcel:
                sqclear.setEnabled(true);
                break;
            case R.id.bToExcel:
                HotOrNot SQLExcel = new HotOrNot(Korish.this);
                SQLExcel.open();
                SQLExcel.getForExcel(excelfilename.getText().toString());
                SQLExcel.close();
                break;
            case R.id.bSQClear:
                ///////////////////
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("DIALOG");
                alertDialogBuilder.setMessage("CLEAR TABLE INFORMATION");
                alertDialogBuilder.setPositiveButton("ok",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                try {
                                    HotOrNot sqdelete = new HotOrNot(Korish.this);
                                    sqdelete.open();
                                    sqdelete.deleteEntry();
                                    sqdelete.close();
                                    arlMalumot.clear();
                                    arrayAdapter.clear();
                                    lvMalumotlar.invalidateViews();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                alertDialogBuilder.setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Intent negativeActivity = new Intent(getApplicationContext(),com.example.alertdialog.NegativeActivity.class);
                                //startActivity(negativeActivity);
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                //////////////////
                break;
            case R.id.bDelete:

          //      if (!deleteItemID.equals("0")) {

                    AlertDialog.Builder alertDialogItem = new AlertDialog.Builder(this);
                    alertDialogItem.setTitle("DIALOG");
                    alertDialogItem.setMessage("Delete Item ID:" + deleteItemID);
                    alertDialogItem.setPositiveButton("ok",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {

                                    HotOrNot deleteItem = new HotOrNot(Korish.this);
                                    deleteItem.open();
                                    deleteItem.deleteItemID(deleteItemID);
                                    deleteItem.open();
                                    arlMalumot.clear();
                                    arrayAdapter.clear();
                                    arlMalumot=deleteItem.getDataForListView();
                                    arrayAdapter.addAll(arlMalumot);
                                    lvMalumotlar.setAdapter(arrayAdapter);
                                    lvMalumotlar.invalidateViews();
                                    deleteItem.close();
                                    bDelete.setEnabled(false);
                                    deleteItemID ="0";
                                    Itemposition=0;
                                    bDelete.setTextColor(Color.BLACK);
                                    bDelete.setText("Delete");


                                }
                            });
                    alertDialogItem.setNegativeButton("cancel",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //Intent negativeActivity = new Intent(getApplicationContext(),com.example.alertdialog.NegativeActivity.class);
                                    //startActivity(negativeActivity);
                                }
                            });

                    AlertDialog alertDialogItemID = alertDialogItem.create();
                    alertDialogItemID.show();
         //       }


                break;
        }
    }
}