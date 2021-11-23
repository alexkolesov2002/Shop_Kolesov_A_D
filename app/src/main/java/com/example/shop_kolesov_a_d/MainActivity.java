package com.example.shop_kolesov_a_d;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnAdd, btnClear,  btnNewPage , btnGoLogin;
    EditText etNazvanie, etPrice;
    DBHelper dbHelper;
    ContentValues contentValues;
    SQLiteDatabase database;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = (Button) findViewById(R.id.Add);
        btnAdd.setOnClickListener(this);

        btnClear = (Button) findViewById(R.id.Clear);
        btnClear.setOnClickListener(this);

        etNazvanie = (EditText) findViewById(R.id.Nazvanie);
        etPrice = (EditText) findViewById(R.id.Price);


        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();
        btnGoLogin = (Button) findViewById(R.id.GoLogin);
        btnGoLogin.setOnClickListener(this);

        btnNewPage= (Button) findViewById(R.id.NewPage);
        btnNewPage.setOnClickListener(this);



        UpdateTable();

    }

    public void UpdateTable() {
        Cursor cursor = database.query(DBHelper.TABLE_GOODS, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int NazvanieIndex = cursor.getColumnIndex(DBHelper.KEY_NAZVANIE);
            int PriceIndex = cursor.getColumnIndex(DBHelper.KEY_PRICE);
            TableLayout tb2 = findViewById(R.id.tableLayout2);
            tb2.removeAllViews();
            do {
                TableRow tbOUT = new TableRow(this);
                tbOUT.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);

                TextView ID = new TextView(this);
                params.weight = 1.0f;
                ID.setLayoutParams(params);
                ID.setText(cursor.getString(idIndex));
                tbOUT.addView(ID);


                TextView NAZVANIE = new TextView(this);
                params.weight = 1.0f;

                NAZVANIE.setLayoutParams(params);
                NAZVANIE.setText(cursor.getString(NazvanieIndex));
                tbOUT.addView(NAZVANIE);


                TextView PRICE = new TextView(this);
                params.weight = 3.0f;
                PRICE.setLayoutParams(params);
                PRICE.setText(cursor.getString(PriceIndex));
                tbOUT.addView(PRICE);


                Button DEL = new Button(this);
                DEL.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        View outBDRow = (View) view.getParent();
                        ViewGroup outBD = (ViewGroup) outBDRow.getParent();
                        outBD.removeView(outBDRow);
                        outBD.invalidate();

                        database.delete(DBHelper.TABLE_GOODS, DBHelper.KEY_ID + " = ?", new String[]{String.valueOf((view.getId()))});
                        contentValues = new ContentValues();
                        Cursor cursorUPD = database.query(DBHelper.TABLE_GOODS, null, null, null, null, null, null);

                        if (cursorUPD.moveToFirst()) {
                            int idIndex = cursorUPD.getColumnIndex(DBHelper.KEY_ID);
                            int NazvanieIndex = cursorUPD.getColumnIndex(DBHelper.KEY_NAZVANIE);
                            int PriceIndex = cursorUPD.getColumnIndex(DBHelper.KEY_PRICE);
                            int realID = 1;
                            do {
                                if (cursorUPD.getInt(idIndex) > realID) {
                                    contentValues.put(DBHelper.KEY_ID, realID);
                                    contentValues.put(DBHelper.KEY_NAZVANIE, cursorUPD.getString(NazvanieIndex));
                                    contentValues.put(DBHelper.KEY_PRICE, cursorUPD.getString(PriceIndex));
                                    database.replace(DBHelper.TABLE_GOODS, null, contentValues);
                                }
                                realID++;
                            } while (cursorUPD.moveToNext());
                            if (cursorUPD.moveToLast() && view.getId() != realID) {

                                database.delete(DBHelper.TABLE_GOODS, DBHelper.KEY_ID + " = ?", new String[]{cursorUPD.getString(idIndex)});
                            }
                            UpdateTable();
                        }
                    }
                });

                params.weight = 1.0f;
                DEL.setLayoutParams(params);
                DEL.setText("Удалить запись");
                DEL.setId(cursor.getInt(idIndex));
                tbOUT.addView(DEL);

                tb2.addView(tbOUT);


            } while (cursor.moveToNext());

        }
        cursor.close();
    }

    @Override
    public void onClick(View v) {
        dbHelper = new DBHelper(this);
        String Nazvanie = etNazvanie.getText().toString();
        String Price = etPrice.getText().toString();
        contentValues = new ContentValues();

        switch (v.getId()) {


            case R.id.Add:


                contentValues.put(DBHelper.KEY_NAZVANIE, Nazvanie);
                contentValues.put(DBHelper.KEY_PRICE, Price);


                database.insert(DBHelper.TABLE_GOODS, null, contentValues);
                UpdateTable();
                etNazvanie.setText(null);
                etPrice.setText(null);


                break;
            case  R.id.NewPage:
                Intent intent =new Intent(MainActivity.this, Shop.class);
                startActivity(intent);
                break;

            case R.id.Clear:
                database.delete(DBHelper.TABLE_GOODS, null, null);
                TableLayout dbOutput = findViewById(R.id.tableLayout2);
                dbOutput.removeAllViews();
                etNazvanie.setText(null);
                etPrice.setText(null);

                UpdateTable();
                break;
            case R.id.GoLogin:
                Intent intent2 =new Intent(this, Authorization.class);
                startActivity(intent2);
                break;

            default:
                break;


        }
        dbHelper.close();
    }

}

