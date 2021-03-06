package com.example.inventarizatsiya1;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;


public class activity_scan_barcode<editText> extends AppCompatActivity {

    EditText etScanner1, etDetalNomeri, etDetalSoni, etDetalSoniJami, etAdres, etEO,etIzox;
    Button btnSaqlash, btnKorish, btnTozalash;
    TextView txtBarcode;

    Consrants constants=new Consrants();
    String sahifa="SAPinventarizatsiya.jsp";
    String URL2=constants.URL+sahifa;

    StringRequest stringRequest;
    RequestQueue requestQueue;
    boolean didItwork = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_barcode);

        initview();


        btnKorish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(activity_scan_barcode.this, Korish.class));
            }
        });

        btnTozalash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etScanner1.setText("");
                etDetalNomeri.setText("");
                etDetalSoni.setText("0");
                etAdres.setText("");
                etEO.setText("");
                etAdres.requestFocus();
                btnSaqlash.setBackgroundColor(Color.GRAY);
            }
        });


        btnSaqlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //etDetalNomeri.getText().toString().isEmpty() || etDetalNomeri.getText().toString().equals("null") == true ||
                if (etAdres.getText().toString().isEmpty()) {

                    btnSaqlash.setBackgroundColor(Color.RED);
//                    MediaPlayer[] ourSong = new MediaPlayer[0];
//                    ourSong[0] = MediaPlayer.create(this,R.raw.scanredy);
//                    ourSong[0].start();


                } else {

                    try {

                        DateFormat data_format = new SimpleDateFormat("yyyy/MM/dd");
                        DateFormat time_format = new SimpleDateFormat("HH:mm:ss");

                        Date date = new Date();
                        final String EtScanner1 = etScanner1.getText().toString();
                        final String EtDetalNomeri = etDetalNomeri.getText().toString();
                        final String EtDetalSoni = etDetalSoni.getText().toString();
                        final String EtAdres = etAdres.getText().toString();
                        final String EtEO = etEO.getText().toString();
                        final String EtIzox = etIzox.getText().toString();

                        final String scan_data = data_format.format(date);
                        final String scan_time = time_format.format(date);

//                        HotOrNot entry = new HotOrNot(activity_scan_barcode.this);
//                        entry.open();
//                        entry.createEntry(EtScanner1, EtDetalNomeri, EtDetalSoni,EtAdres, EtEO,EtIzox,scan_data, scan_time);
//                        entry.close();

                        stringRequest = new StringRequest(Request.Method.POST, URL2, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String respons) {
                                System.out.println(respons);

                                if(respons.contains("Saqlandi")==true){
                                    txtBarcode.setText("Saqlandi");
                                    txtBarcode.setTextColor(Color.BLUE);
                                    btnSaqlash.setBackgroundColor(Color.BLUE);

                                        etScanner1.setText("");
                                        etDetalNomeri.setText("");
                                        etDetalSoni.setText("0");
                                        etEO.setText("");
                                        etScanner1.requestFocus();


                                } else if(respons.contains("Saqlanmadi")==true) {
                                    txtBarcode.setText("Saqlandmadi");
                                    txtBarcode.setTextColor(Color.RED);
                                    btnSaqlash.setBackgroundColor(Color.RED);

                                } else if(respons.contains("Connection error")==true ){
                                    txtBarcode.setText("Host aloq yoq");
                                    txtBarcode.setTextColor(Color.RED);
                                    btnSaqlash.setBackgroundColor(Color.RED);

                                } else if(respons.contains("Bu barcode saqlangan")==true ){
                                    txtBarcode.setText("Bu barcode saqlangan");
                                    txtBarcode.setTextColor(Color.RED);
                                    btnSaqlash.setBackgroundColor(Color.RED);
                                }

                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                System.out.println("Aloqani tekshiring "+volleyError);
                                txtBarcode.setText("Aloqani tekshiring");
                                txtBarcode.setTextColor(Color.RED);
                                btnSaqlash.setBackgroundColor(Color.RED);

                            }
                        }) {
                            protected Map<String, String> getParams() throws
                                    AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("holat", "WRITETOAS400");
                                params.put("etScanner1", EtScanner1);
                                params.put("etDetalNomeri", EtDetalNomeri);
                                params.put("etDetalSoni", EtDetalSoni);
                                params.put("etAdres", EtAdres);
                                params.put("etEO", EtEO);
                                params.put("etIN01IDX","0");
                                params.put("etIzox", EtIzox);
                                params.put("scan_data", scan_data);
                                params.put("scan_time", scan_time);
                                return params;
                            }
                        };

                        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                                10000,
                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                        requestQueue=Volley.newRequestQueue(activity_scan_barcode.this);
                        requestQueue.add(stringRequest);

                    } catch (Exception e) {
                        didItwork = false;
                        String error = e.toString();
                        Dialog d = new Dialog(activity_scan_barcode.this);
                        d.setTitle("Dang!");
                        TextView tv = new TextView(activity_scan_barcode.this);
                        tv.setText(error);
                        d.setContentView(tv);
                        d.show();
                    }
                }
            }
        });

        etAdres.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // hideKeyboard(etAdres);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
               etScanner1.requestFocus();

            }
        });

        etScanner1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                hideKeyboard(etScanner1);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (etScanner1.getText().toString().length() > 70 && etScanner1.getText().toString().contains("[") && etScanner1.getText().toString().contains("Q")) {
                    etDetalNomeri.setText(etScanner1.getText().toString().trim().substring(8, 18));
                    int start1 = etScanner1.getText().toString().trim().indexOf('Q') + 1;
                    String oraliq = etScanner1.getText().toString().trim().substring(start1, start1 + 5);
                    String natija = "";

                    char[] ch = oraliq.toCharArray();
                    int l = oraliq.length();
                    for (int i = 0; i < l; i++) {
                        if (ch[i] == '0' || ch[i] == '1' || ch[i] == '2' || ch[i] == '3' || ch[i] == '4' || ch[i] == '5' || ch[i] == '6' || ch[i] == '7' || ch[i] == '8' || ch[i] == '9') {
                            natija = natija + ch[i];
                        } else
                            break;
                    }
                    //                   System.out.println(natija);
                    etDetalSoni.setText(natija);
                    int start2=etScanner1.getText().toString().trim().indexOf("UN");
                    etEO.setText(etScanner1.getText().toString().trim().substring(start2,start2+20));
 //                   etScanner1.setText(etDetalNomeri.getText());

 //                   etAdres.requestFocus();
//                    btnSaqlash.requestFocusFromTouch();
                }
            }
        });
    }



    public void showSoftKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    private void initview() {
        etScanner1 = findViewById(R.id.etScanner1);
        etScanner1.setBackgroundColor(Color.GREEN);
        etDetalNomeri = findViewById(R.id.etDetalNomeri);
        etDetalNomeri.setBackgroundColor(Color.GREEN);
        etDetalSoni = findViewById(R.id.etDetalSoni);
        etDetalSoni.setBackgroundColor(Color.GREEN);
        etAdres = findViewById(R.id.etAdres);
        etAdres.setBackgroundColor(Color.GREEN);
        etEO = findViewById(R.id.etEO);
        etEO.setBackgroundColor(Color.GREEN);
        etIzox=findViewById(R.id.etIzox);
        etIzox.setBackgroundColor(Color.CYAN);
        btnSaqlash = findViewById(R.id.btnSaqlash);
        btnKorish = findViewById(R.id.btnKorish);
        btnTozalash = findViewById(R.id.btnTozalash);
        txtBarcode=findViewById(R.id.txtBarcode);

        etScanner1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    etScanner1.setBackgroundColor(Color.YELLOW);
                } else {
                    etScanner1.setBackgroundColor(Color.GREEN);
                }
            }
        });

        etDetalNomeri.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    etDetalNomeri.setBackgroundColor(Color.YELLOW);
                } else
                    etDetalNomeri.setBackgroundColor(Color.GREEN);
            }
        });

        etDetalSoni.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    etDetalSoni.setBackgroundColor(Color.YELLOW);
                    if(etDetalSoni.getText().toString().equals("0"))
                    etDetalSoni.setText("");
                } else {
                    etDetalSoni.setBackgroundColor(Color.GREEN);
                if(etDetalSoni.getText().toString().equals(""))
                    etDetalSoni.setText("0");
                }
            }
        });

        etAdres.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    etAdres.setBackgroundColor(Color.YELLOW);
              //      etAdres.setText("");
                    getWindow().setSoftInputMode(
                            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                } else
                    etAdres.setBackgroundColor(Color.GREEN);
            }
        });

        etEO.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    etEO.setBackgroundColor(Color.YELLOW);
                    etEO.setText("");
                } else
                    etEO.setBackgroundColor(Color.GREEN);

            }
        });


        btnSaqlash.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    btnSaqlash.setBackgroundColor(Color.YELLOW);
                } else
                    btnSaqlash.setBackgroundColor(Color.GRAY);
            }
        });



    }
}