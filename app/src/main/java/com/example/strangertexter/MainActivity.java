package com.example.strangertexter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private CountryCodePicker ccp;
    private EditText phone;
    private Button chat;
    private String code, getPhone;
    private int condition;
    private Spinner picker;
    private ArrayList<String> apps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Text without saving number");
        ccp = findViewById(R.id.ccp);
        phone = findViewById(R.id.phone);
        chat = findViewById(R.id.chat);
        picker = findViewById(R.id.picker);
        getPhone="";

        //Making a dropdown menu for the applist using spinner
        apps = new ArrayList<>();
        apps.add(0, "Choose an app");
        apps.add("Whatsapp");
        apps.add("Telegram");

        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, apps);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        picker.setAdapter(adapter);

        //Click Listener when an app is selected
        picker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (adapterView.getItemAtPosition(i).equals("Choose an app")) {
                    phone.setHint("Choose an app");
                    condition = 0;
                } else if (adapterView.getItemAtPosition(i).equals("Whatsapp")) {
                    phone.setHint("Enter Phone Number");
                    phone.setInputType(InputType.TYPE_CLASS_NUMBER);
                    condition = 1;
                } else if (adapterView.getItemAtPosition(i).equals("Telegram")) {
                    phone.setHint("Enter Phone/Username");
                    phone.setInputType(InputType.TYPE_CLASS_TEXT);
                    condition = 2;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                condition = 0;
            }
        });

        //Logic when long-clicking the 'Chat' button
        chat.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                startActivity(new Intent(getApplicationContext(), About.class));
                return false;
            }
        });


        //Logic when clicking the 'Chat' button
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPhone = phone.getText().toString();
                //When no app is selected
                if(condition==0) {
                    Toast.makeText(MainActivity.this, "Select an app", Toast.LENGTH_SHORT).show();
                }
                else
                    //When 'Whatsapp' is selected
                    if (condition==1) {
                        if(getPhone.equals(""))
                            Toast.makeText(MainActivity.this, "Enter Phone Number", Toast.LENGTH_SHORT).show();
                        else
                            code = "https://wa.me/" + ccp.getSelectedCountryCode() + getPhone;
                    }
                else
                    //When 'Telegram' is selected
                    if(condition==2)
                    {
                        if(getPhone.equals(""))
                            Toast.makeText(MainActivity.this, "Enter Phone/Username", Toast.LENGTH_SHORT).show();
                        else
                        {
                            //if checkAlpha(String) == true, input is username
                            if(checkAlpha(getPhone))
                            {
                                if(getPhone.charAt(0)=='@')
                                    code="https://t.me/"+getPhone.substring(1);
                                else
                                    code="https://t.me/"+getPhone;
                            }
                            //if false, it means it is a phone number
                            else
                            {
                                code="https://t.me/"+ccp.getSelectedCountryCodeWithPlus()+getPhone;
                            }
                        }
                    }

                    if(!getPhone.equalsIgnoreCase("") && condition!=0) {
                        Uri uri = Uri.parse(code);
                        startActivity(new Intent(Intent.ACTION_VIEW, uri));
                    }
            }
        });
    }

    //checkAlpha(String) checks whether the input text contains
    //only digits or has any other character in it
    //returns true if it has any character, else false
    private boolean checkAlpha(String s)
    {
        int i, l;
        boolean k=false;
        l=s.length();
        for(i=0;i<l;i++)
        {
            if((int)s.charAt(i)<48 || (int)s.charAt(i)>57) {
                k = true;
                break;
            }
        }
        return k;
    }

    @Override
    public void onBackPressed() {
        finish();
        System.exit(1);
    }
}