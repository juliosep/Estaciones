package com.example.julio.estaciones;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Julio on 24/04/2017.
 */

public class UsuarioSQLiteHelper extends SQLiteOpenHelper {

    // Creamos una variable que contenga la sentencia SQL de creacion de la BD
    String sql = "CREATE TABLE estaciones (_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, mercado TEXT NOT NULL, nombre_et TEXT NOT NULL, id_et TEXT NOT NULL, direccion_et TEXT NOT NULL, lat_et TEXT NOT NULL, long_et TEXT NOT NULL, ciudad_et TEXT NOT NULL, estado_et TEXT NOT NULL)";


    public UsuarioSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Este metodo se ejecuta automaticamente cuando no existe la BD
        // la primera vez que hace llamado a la clase, crea la BD
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Este metodo se ejecuta cuando se detecta que la
        // version de la BD cambio, se debe reemplazar la
        // estructura vieja por la nueva
        // para este ejemplo se eliminara la tabla y se creara nuevamente
        db.execSQL("DROP TABLE IF EXISTS estaciones");
        db.execSQL(sql);
    }
}
