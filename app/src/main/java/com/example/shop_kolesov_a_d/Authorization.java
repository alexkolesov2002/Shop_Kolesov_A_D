package com.example.shop_kolesov_a_d;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class Authorization extends AppCompatActivity implements View.OnClickListener {
    Button btnLog, btnReg;
    EditText etLogin, etPass;
    DBHelper dbHelper;

    SQLiteDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);

        btnLog = (Button) findViewById(R.id.Autor);
        btnLog.setOnClickListener(this);

        btnReg = (Button) findViewById(R.id.Reg);
        btnReg.setOnClickListener(this);

        etLogin = (EditText) findViewById(R.id.Login);
        etPass = (EditText) findViewById(R.id.Pass);

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.KEY_LOGIN, "admin");
        contentValues.put(DBHelper.KEY_PASSWORD, "admin");
        database.insert(DBHelper.TABLE_USERS,null, contentValues);
    }

    @Override
    public void onClick(View view) {
        Boolean f = false;
        switch(view.getId())
        {
            case R.id.Autor:
                Cursor cursor = database.query(DBHelper.TABLE_USERS, null,null,null,null,null,null);
                Boolean check = false;
                if(cursor.moveToFirst()){
                    int loginIndex = cursor.getColumnIndex(DBHelper.KEY_LOGIN);
                    int passIndex = cursor.getColumnIndex(DBHelper.KEY_PASSWORD);
                    do{ if (etLogin.getText().toString().equals("admin") && etPass.getText().toString().equals("admin")) {
                        if (etLogin.getText().toString().equals(cursor.getString(loginIndex)) && etPass.getText().toString().equals(cursor.getString(passIndex))) {
                            UserVisible.pokaz1 =true;
                            startActivity(new Intent(this, MainActivity.class));
                            check = true;
                            break;
                        }
                        } else if (etLogin.getText().toString().equals(cursor.getString(loginIndex)) && etPass.getText().toString().equals(cursor.getString(passIndex))) {
                        UserVisible.pokaz1= false;
                        Intent intent =new Intent(this, Shop.class);
                        startActivity(intent);
                            check = true;
                            break;
                        }


                    }while (cursor.moveToNext());
                }
                cursor.close();

                if (check == false) Toast.makeText(this,"Такой пользователь не зарегестрирован", Toast.LENGTH_LONG).show();
                break;
            case R.id.Reg:
                Cursor cursorRegProvLog = database.query(DBHelper.TABLE_USERS, null,null,null,null,null,null);

                Boolean checkRegProvLog   = false;

                if(cursorRegProvLog.moveToFirst()){
                    int loginIndex = cursorRegProvLog.getColumnIndex(DBHelper.KEY_LOGIN);

                    do{
                        if (etLogin.getText().toString().equals(cursorRegProvLog.getString(loginIndex))) {
                            Toast.makeText(this,"Введенный логин уже занят", Toast.LENGTH_LONG).show();
                            checkRegProvLog = true;
                            break;
                        }

                    }while (cursorRegProvLog.moveToNext());
                }
                if (checkRegProvLog == false)
                {ContentValues contentValues = new ContentValues();
                    contentValues.put(DBHelper.KEY_LOGIN, etLogin.getText().toString());
                    contentValues.put(DBHelper.KEY_PASSWORD, etPass.getText().toString());
                    database.insert(DBHelper.TABLE_USERS,null, contentValues);
                    Toast.makeText(this,"Регистрация прошла успешно", Toast.LENGTH_SHORT).show();
                }
                cursorRegProvLog.close();

                break;
        }

    }
}