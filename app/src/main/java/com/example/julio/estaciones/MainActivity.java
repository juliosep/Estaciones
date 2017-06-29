package com.example.julio.estaciones;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    // Definir las variables para obtener los controles creados

    EditText edtmercado;
    EditText edtid_et;
    EditText edtnombre_et;
    Spinner spinombre_et;
    Spinner spimercado;
    EditText edtdireccion_et;
	EditText edtlat_et;
	EditText edtlong_et;
	EditText edtciudad_et;
	EditText edtestado_et;

    List<String> listaET=new ArrayList<>();

    /*
    Estos atributos representan la posición y selección actual del Spinner
     */
    protected int position;
    protected String selection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Mapeamos las variables con los controles para setear los valores u obtenerlos
        edtmercado = (EditText) findViewById(R.id.edtmercado);
        edtid_et = (EditText) findViewById(R.id.edtid_et);
        edtnombre_et = (EditText) findViewById(R.id.edtnombre_et);
        spinombre_et = (Spinner) findViewById(R.id.spinombre_et);
        spimercado = (Spinner) findViewById(R.id.spimercado);
        edtdireccion_et = (EditText) findViewById(R.id.edtdireccion_et);
		edtlat_et = (EditText) findViewById(R.id.edtlat_et);
		edtlong_et = (EditText) findViewById(R.id.edtlong_et);
		edtciudad_et = (EditText) findViewById(R.id.edtciudad_et);
		edtestado_et = (EditText) findViewById(R.id.edtestado_et);

        spinombre_et.setOnItemSelectedListener(this);
        spimercado.setOnItemSelectedListener(this);

        //Carga la BD ya grabada
        try {
            deployDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	private void deployDatabase() throws IOException {
		//Open your local db as the input stream
		String packageName = getApplicationContext().getPackageName();
		String DB_PATH = "/data/data/" + packageName + "/databases/";
		//Create the directory if it does not exist
		File directory = new File(DB_PATH);
		if (!directory.exists()) {
		directory.mkdirs();
		}

		String DB_NAME = "registro_et"; //The name of the source sqlite file

		InputStream myInput = getAssets().open("registro_et");

		// Path to the just created empty db
		String outFileName = DB_PATH + DB_NAME;

		//Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);

		//transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
		myOutput.write(buffer, 0, length);
		}

		//Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();

	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Se asigna el menu al activity
        getMenuInflater().inflate(R.menu.menu_clientes, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Se realizara una accion de acuerdo al icono seleccionado
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.action_add:
                // Trabajar con todos los controlesque definen la estacion para ingresarla
                String mercado = edtmercado.getText().toString();
                String ident = edtid_et.getText().toString();
                String nombres = edtnombre_et.getText().toString();
                String direccion_et = edtdireccion_et.getText().toString();
				String lat_et = edtlat_et.getText().toString();
				String long_et = edtlong_et.getText().toString();
				String ciudad_et = edtciudad_et.getText().toString();
				String estado_et = edtestado_et.getText().toString();
		
                // Validamos que se ingresen todos los campos
                if(ident.length()>0 && nombres.length()>0 && direccion_et.length()>0) {
                    // Abrimos la BD de estaciones
                    UsuarioSQLiteHelper usuario = new UsuarioSQLiteHelper(this, "registro_et", null, 1);
                    SQLiteDatabase db = usuario.getWritableDatabase();

                    ContentValues registro = new ContentValues();

                    registro.put("mercado", mercado);
					registro.put("id_et", ident);
                    registro.put("nombre_et", nombres);
                    registro.put("direccion_et", direccion_et);
					registro.put("lat_et", lat_et);
					registro.put("long_et", long_et);
					registro.put("ciudad_et", ciudad_et);
					registro.put("estado_et", estado_et);

                    // los inserto en la base de datos
                    db.insert("estaciones", null, registro);

                    // db.execSQL("INSERT INTO estaciones (mercado, id_et, nombre_et, direccion_et, lat_et, long_et, ciudad_et, estado_et) VALUES(" + "'" + selection + "','" + ident + ",'" + "'" + nombres + "','" + direccion_et + "','" + lat_et + "','" + long_et + "','" + ciudad_et + "','" + estado_et + "')");
                    db.close();
                    Toast.makeText(this, "Estacion agregada correctamente", Toast.LENGTH_SHORT).show();

                    edtmercado.setText("");
                    edtid_et.setText("");
                    edtnombre_et.setText("");
                    edtdireccion_et.setText("");
					edtlat_et.setText("");
					edtlong_et.setText("");
					edtciudad_et.setText("");
					edtestado_et.setText("");
                }
                else{
                    Toast.makeText(this, "Debe ingresar todos los datos", Toast.LENGTH_SHORT).show();
                }
                return true;

            // Hacemos búsqueda de usuario por ident
            case R.id.busq_ident:
                // Abrimos la BD de registro_et
                UsuarioSQLiteHelper usuario = new UsuarioSQLiteHelper(this, "registro_et", null, 1);
                SQLiteDatabase db = usuario.getWritableDatabase();
                ident = edtid_et.getText().toString();
                if(ident.length()>0){
                    Cursor fila = db.rawQuery(

                        "select * from estaciones where id_et=" + ident, null);

                    if (fila.moveToFirst()) {

                        edtmercado.setText(fila.getString(1));
                        edtnombre_et.setText(fila.getString(2));
                        edtid_et.setText(fila.getString(3));
                        edtdireccion_et.setText(fila.getString(4));
                        edtlat_et.setText(fila.getString(5));
                        edtlong_et.setText(fila.getString(6));
                        edtciudad_et.setText(fila.getString(7));
                        edtestado_et.setText(fila.getString(8));

                    } else{

                        Toast.makeText(this, "No existe alguna estacion con ese ID",

                                Toast.LENGTH_SHORT).show();
                    }
                    db.close();

                }
                else{
                    Toast.makeText(this, "Debe ingresar el ID", Toast.LENGTH_SHORT).show();
                }

            return true;

            case R.id.busq_nombre2:
                // Abrimos la BD de registro_et para cargar el Spinner
                usuario = new UsuarioSQLiteHelper(this, "registro_et", null, 1);
                db = usuario.getWritableDatabase();
                listaET.clear();
                Cursor c = db.rawQuery(

                        "select nombre_et from estaciones", null);
                //crea la lista de nombres
                for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                    listaET.add(c.getString(c.getColumnIndex("nombre_et")));
                }
                //Creamos el adaptador
                ArrayAdapter<String> adapter2=new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listaET);
                spinombre_et.setAdapter(adapter2);

            return true;

            case R.id.busq_nombre:
                // Abrimos la BD de registro_et
                usuario = new UsuarioSQLiteHelper(this, "registro_et", null, 1);
                db = usuario.getWritableDatabase();
                nombres = edtnombre_et.getText().toString();
                if(nombres.length()>0){
                    Cursor fila = db.rawQuery(

                            "select * from estaciones where nombre_et like '" + nombres + "%'", null);

                    if (fila.moveToFirst()) {

                        edtmercado.setText(fila.getString(1));
                        edtnombre_et.setText(fila.getString(2));
                        edtid_et.setText(fila.getString(3));
                        edtdireccion_et.setText(fila.getString(4));
                        edtlat_et.setText(fila.getString(5));
                        edtlong_et.setText(fila.getString(6));
                        edtciudad_et.setText(fila.getString(7));
                        edtestado_et.setText(fila.getString(8));
                    } else{

                        Toast.makeText(this, "No existe alguna estacion con ese NOMBRE",

                                Toast.LENGTH_SHORT).show();
                    }
                    db.close();

                }
                else{
                    Toast.makeText(this, "Debe ingresar el NOMBRE", Toast.LENGTH_SHORT).show();
                }
                return true;

            // Método para modificar la información de la estacion
            case R.id.modif_ident:
                // Trabajar con todos los controlesque definen la estacion para ingresarla
                mercado = edtmercado.getText().toString();
                ident = edtid_et.getText().toString();
                nombres = edtnombre_et.getText().toString();
                direccion_et = edtdireccion_et.getText().toString();
                lat_et = edtlat_et.getText().toString();
                long_et = edtlong_et.getText().toString();
                ciudad_et = edtciudad_et.getText().toString();
                estado_et = edtestado_et.getText().toString();
				
                // Validamos que se ingresen todos los campos
                if(ident.length()>0 && nombres.length()>0 && direccion_et.length()>0) {
                    // Abrimos la BD de estaciones
                    usuario = new UsuarioSQLiteHelper(this, "registro_et", null, 1);
                    db = usuario.getWritableDatabase();

                    ContentValues registro = new ContentValues();

                    registro.put("mercado", mercado);
					registro.put("id_et", ident);
                    registro.put("nombre_et", nombres);
                    registro.put("direccion_et", direccion_et);
					registro.put("lat_et", lat_et);
					registro.put("long_et", long_et);
					registro.put("ciudad_et", ciudad_et);
					registro.put("estado_et", estado_et);
					
                    int cant = db.update("estaciones", registro, "id_et=" + ident, null);

                    db.close();
                    edtmercado.setText("");
                    edtid_et.setText("");
                    edtnombre_et.setText("");
                    edtdireccion_et.setText("");
					edtlat_et.setText("");
					edtlong_et.setText("");
					edtciudad_et.setText("");
					edtestado_et.setText("");
					
                    if (cant == 1)

                        Toast.makeText(this, "Datos modificados con éxito", Toast.LENGTH_SHORT)

                                .show();

                    else

                        Toast.makeText(this, "No existe estacion",

                                Toast.LENGTH_SHORT).show();

                }

                else{
                    Toast.makeText(this, "Debe ingresar todos los datos", Toast.LENGTH_SHORT).show();
                }
                return true;

            default:
                return onOptionsItemSelected(item);

        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Salvar la posición y valor del item actual
        this.position = position;
        selection = parent.getItemAtPosition(position).toString();

        //Obteniendo el id del Spinner que recibió el evento
        int idSpinner = parent.getId();


        switch(idSpinner) {

            case R.id.spinombre_et:

                //Se coloca la selección Spinner spinombre_et en edtnombre_et
                edtnombre_et.setText(selection);
                //Toast.makeText(this, "Selección actual: " + selection, Toast.LENGTH_SHORT).show();
                //Se limpian todas las casillas
                edtid_et.setText("");
                edtdireccion_et.setText("");
                edtlat_et.setText("");
                edtlong_et.setText("");
                edtciudad_et.setText("");
                edtestado_et.setText("");
            break;

            case R.id.spimercado:
                //Se coloca la selección del Spinner spimercado en edtmercado
                edtmercado.setText(selection);
                //Toast.makeText(this, "Selección actual: " + selection, Toast.LENGTH_SHORT).show();
                armarnombres();
            break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        /*
        Nada por hacer
         */
    }
    private void armarnombres(){
        // Abrimos la BD de registro_et
        UsuarioSQLiteHelper usuario = new UsuarioSQLiteHelper(this, "registro_et", null, 1);
        SQLiteDatabase db = usuario.getWritableDatabase();
        String mercado = edtmercado.getText().toString();
        listaET.clear();
        if(mercado.length()>0){
            Cursor c = db.rawQuery(

                    "select nombre_et from estaciones where mercado like '" + mercado + "%'", null);

            //crea la lista de nombres
            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                listaET.add(c.getString(c.getColumnIndex("nombre_et")));
            }
            //Creamos el adaptador
            ArrayAdapter<String> adapter2=new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listaET);
            spinombre_et.setAdapter(adapter2);
            db.close();

        }
        else{
            Toast.makeText(this, "Debe ingresar el NOMBRE", Toast.LENGTH_SHORT).show();
        }
    }
}