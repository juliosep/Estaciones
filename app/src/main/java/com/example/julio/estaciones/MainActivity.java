package com.example.julio.estaciones;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class MainActivity extends AppCompatActivity {

    // Definir las variables para obtener los controles creados

    EditText edtmercado;
    EditText edtid_et;
    EditText edtnombre_et;
    EditText edtdireccion_et;
	EditText edtlat_et;
	EditText edtlong_et;
	EditText edtciudad_et;
	EditText edtestado_et;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Mapeamos las variables con los controles para setear los valores u obtenerlos
        edtmercado = (EditText) findViewById(R.id.edtmercado);
        edtid_et = (EditText) findViewById(R.id.edtid_et);
        edtnombre_et = (EditText) findViewById(R.id.edtnombre_et);
        edtdireccion_et = (EditText) findViewById(R.id.edtdireccion_et);
		edtlat_et = (EditText) findViewById(R.id.edtlat_et);
		edtlong_et = (EditText) findViewById(R.id.edtlong_et);
		edtciudad_et = (EditText) findViewById(R.id.edtciudad_et);
		edtestado_et = (EditText) findViewById(R.id.edtestado_et);

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

            case R.id.busq_nombre:
                // Abrimos la BD de registro_et
                UsuarioSQLiteHelper usuario2 = new UsuarioSQLiteHelper(this, "registro_et", null, 1);
                SQLiteDatabase db2 = usuario2.getWritableDatabase();
                nombres = edtnombre_et.getText().toString();
                if(nombres.length()>0){
                    Cursor fila = db2.rawQuery(

                            "select * from estaciones where id_et=" + nombres, null);

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
                    db2.close();

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

}