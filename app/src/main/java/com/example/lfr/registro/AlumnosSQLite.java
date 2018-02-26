package com.example.lfr.registro;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by LFR on 20/02/18.
 */

public class AlumnosSQLite extends SQLiteOpenHelper {
    String sql = "CREATE TABLE alumnos (matricula TEXT PRIMARY KEY, nombre TEXT, edad INTEGER)";
    String sql3="CREATE TABLE materias(IDmateria INTEGER AUTO_INCREMENT PRYMARY KEY,materias TEXT)";
    String sql2="CREATE TABLE evaluaciones (IDEva INTEGER auto_increment PRIMARY KEY, materia TEXT, evaluacion TEXT, matricula TEXT, FOREIGN KEY(matricula) REFERENCES alumnos(matricula))";
Context c;

    public AlumnosSQLite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);


        c = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        InputStream is = null;
        try{
            is=c.getAssets().open("datos.sql");
            if(is != null){
                db.beginTransaction();
                db.execSQL(sql);
                db.execSQL(sql3);
                db.execSQL(sql2);
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String sql="";
                while ((sql=br.readLine()) != null){
                    db.execSQL(sql);
                }
                db.setTransactionSuccessful();
                db.endTransaction();
                is.close();
            }

        }catch (Exception er){
            Log.d("Error al precargar : ",er.toString());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
