package com.example.lfr.registro;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

public class Agregar extends AppCompatActivity {
    SQLiteDatabase bd = null;
    Button Agregar, Busca, Todo, Eliminar;
    RadioButton r1, r2;
    EditText Matricula, Nombre, Edad, Que, Ver, Materia, Evaluacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar);
        r1=(RadioButton)findViewById(R.id.r1);
        r2=(RadioButton)findViewById(R.id.r2);
        Agregar = (Button) findViewById(R.id.Guar);
        Busca = (Button) findViewById(R.id.Busca);
        Todo = (Button) findViewById(R.id.Verto);
        Eliminar = (Button) findViewById(R.id.Elim);
        Ver = (EditText) findViewById(R.id.Muestra);
        Matricula = (EditText) findViewById(R.id.Mat);
        Nombre = (EditText) findViewById(R.id.Nom);
        Edad = (EditText) findViewById(R.id.Eda);
        Que = (EditText) findViewById(R.id.Ver);
        Materia = (EditText) findViewById(R.id.mate);
        Evaluacion = (EditText) findViewById(R.id.Eva);
       r1.setChecked(true);
       abilitar();
        Click();
    }

    public void Click() {
        Agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ejecutar(Nombre.getText().toString(), Matricula.getText().toString(), Edad.getText().toString(), Materia.getText().toString(), Evaluacion.getText().toString());
                Nombre.setText(null);
                Matricula.setText(null);
                Edad.setText(null);
                Materia.setText(null);
                Evaluacion.setText(null);
            }
        });
        Busca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ver.setText(buscar(Que.getText().toString()));
            }
        });
        Todo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ver.setText(ConsultarTodo());
            }
        });
        Eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Eliminar(Que.getText().toString());
            }
        });
        r1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (r1.isChecked() == true) {
                    Matricula.setEnabled(true);
                    Nombre.setEnabled(true);
                    Edad.setEnabled(true);
                    Materia.setEnabled(false);
                    Evaluacion.setEnabled(false);
                }
            }

        });
        r2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Matricula.setEnabled(true);
                Nombre.setEnabled(false);
                Edad.setEnabled(false);
                Materia.setEnabled(true);
                Evaluacion.setEnabled(true);
            }
        });

    }

    public void abilitar() {
        if (r1.isChecked() == true) {
            Matricula.setEnabled(true);
            Nombre.setEnabled(true);
            Edad.setEnabled(true);
            Materia.setEnabled(false);
            Evaluacion.setEnabled(false);

        } else if (r2.isChecked() == true) {
            Matricula.setEnabled(true);
            Nombre.setEnabled(false);
            Edad.setEnabled(false);
            Materia.setEnabled(true);
            Evaluacion.setEnabled(true);

        }
    }

    public boolean ConstruirBD() {
        AlumnosSQLite alumno = new AlumnosSQLite(this, "UTM2", null, 1);
        bd = alumno.getWritableDatabase();
        if (bd != null)
            return true;

        return false;
    }

    public void ejecutar(String nombre, String matricula, String edad, String materia, String evaluacion) {
        try {
            if (r1.isChecked() == true) {
                if (ConstruirBD()) {
                    bd.execSQL("INSERT INTO alumnos (matricula, nombre, edad)" + "VALUES('" + matricula + "','" + nombre + "'," + edad + ")");
                }
                }
            else if (r2.isChecked() == true) {
                if (ConstruirBD()) {
                    bd.execSQL("INSERT INTO evaluaciones (materia, evaluacion, matricula)" + "VALUES('" + materia + "','" + evaluacion + "','" + matricula + "')");
                }
            }
            bd.close();
        } catch (Exception e) {
            Ver.setText("Error de Guardado: "+e.toString());
            Log.d("Error de Guardado: ", e.toString());
        }

    }

    public String buscar(String matricula) {
        try {
            String alumno = "";
            if (ConstruirBD()) {
                Cursor c = bd.rawQuery("SELECT * FROM alumnos WHERE matricula='" + matricula + "'", null);
                if (c.moveToFirst()) {
                    alumno = "Matricula: " + c.getString(0) + "\n Nombre: " + c.getString(1) + "\nEdad: " + c.getString(2);
                    bd.close();
                    return alumno;
                }
                return "Alumno no encontrado";
            }

        } catch (Exception e) {
            Log.d("Error Consultar: ", e.toString());
        }
        return "No es posible buscar";
    }

    public String ConsultarTodo() {
        try {
            String alumno = "";
            if (ConstruirBD()) {
                Cursor c = bd.rawQuery("SELECT * FROM alumnos", null);
                if (c.moveToFirst()) {
                    do {
                        alumno += "Matricula" + c.getString(0) + "\nNombre: " +
                                c.getString(1) + "\nEdad: " + c.getString(2) + "\n\n";
                    } while (c.moveToNext());
                    bd.close();
                    return alumno;
                }
            }
            return "No Hay Alumnos Registrados";
        } catch (Exception e) {
            Log.d("Error de Consulta: ", e.toString());
        }
        return "No es posible Consultar";
    }

    public void Eliminar(String Matricula) {
        //
        try {
            if (ConstruirBD()) {
            //bd.execSQL("DELETE * FROM evaluacion WHERE matricula='" + Matricula + "'");
            bd.execSQL("DELETE FROM alumnos WHERE matricula='" + Matricula + "'");
            }
            bd.close();
        } catch (Exception e) {
            Log.d("Error Al eleminar: ", e.toString());
        }

    }
}
