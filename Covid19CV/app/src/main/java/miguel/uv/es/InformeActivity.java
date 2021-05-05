package miguel.uv.es;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class InformeActivity extends AppCompatActivity {

    private AutoCompleteTextView tvNombreMunicipio;
    private TextView tvFechaInicioSintomas;
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private RadioButton rbCloseContactSi, rbCloseContactNo;
    private CheckBox checkBoxFiebre, checkBoxDificultadRespiratoria, checkBoxTos, checkBoxFatiga, checkBoxDolorMuscular, checkBoxDolorCabeza, checkBoxDiarrea, checkBoxDolorGarganta, checkBoxCongestion, checkBoxNauseas, checkBoxPerdidaOlfatoGusto;
    private Context context;
    private Covid19CvDbHelper dbHelper;
    private SQLiteDatabase db;
    private MenuItem menuItem1, menuItem2;
    private LocationManager locationManager;
    private String cityFromLocation;
    private Geocoder geocoder;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informe);
        context = this;
        dbHelper = new Covid19CvDbHelper(context);
        db = dbHelper.getWritableDatabase();

        tvFechaInicioSintomas = findViewById(R.id.tvFechaInicioSintomas);
        tvNombreMunicipio = findViewById(R.id.autoCompleteTextView);
        rbCloseContactSi = findViewById(R.id.radioButton);
        rbCloseContactNo = findViewById(R.id.radioButton2);
        checkBoxFiebre = findViewById(R.id.checkBox);
        checkBoxDificultadRespiratoria = findViewById(R.id.checkBox2);
        checkBoxTos = findViewById(R.id.checkBox3);
        checkBoxFatiga = findViewById(R.id.checkBox4);
        checkBoxDolorMuscular = findViewById(R.id.checkBox5);
        checkBoxDolorCabeza = findViewById(R.id.checkBox6);
        checkBoxDiarrea = findViewById(R.id.checkBox7);
        checkBoxDolorGarganta = findViewById(R.id.checkBox8);
        checkBoxCongestion = findViewById(R.id.checkBox11);
        checkBoxNauseas = findViewById(R.id.checkBox10);
        checkBoxPerdidaOlfatoGusto = findViewById(R.id.checkBox9);

        Bundle extras = getIntent().getExtras();
        if (getIntent().hasExtra("nombreMunicipio")) { // VIENE DE MUNICIPIO_ACTIVITY, CLICADO EL BOTON FLOTANTE (Crear)
            String nombreMunicipio = (String) extras.get("nombreMunicipio");
            tvNombreMunicipio.setText(nombreMunicipio);
            tvNombreMunicipio.setEnabled(false);
            setTitle("Añadir informe");
        } else if (getIntent().hasExtra("nombresMunicipios")) { // VIENE DE MUNICIPIOS_ACTIVITY, CLICADO EL BOTON FLOTANTE (Crear) Y OBTIENE LOCALIDAD DEL GPS
            String[] nombresMunicipios = (String[]) extras.get("nombresMunicipios");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nombresMunicipios);
            tvNombreMunicipio.setAdapter(adapter);
            String cityFromLocation = (String) extras.get("cityFromLocation");
            tvNombreMunicipio.setText(cityFromLocation);
            if(cityFromLocation.isEmpty()) {
                tvNombreMunicipio.setEnabled(true);
            } else {
                tvNombreMunicipio.setEnabled(false);
            }
            setTitle("Añadir informe");
        } else if(getIntent().hasExtra("informeId")) { // VIENE DE MUNICIPIO_ACTIVITY, CLICADO EL LINEAR_LAYOUT_INFORME (Actualizar/Borrar)
            int informeId = Integer.parseInt((String) extras.get("informeId"));
            setTitle("Actualizar/Borrar informe");
            // Cursor
            Cursor cursorReportById = dbHelper.FindReportById(db,informeId);
            cursorReportById.moveToFirst();
            // Obtenemos la información del cursor y la setteamos a cada elemento de la vista de InformeActivity
            tvFechaInicioSintomas.setText(cursorReportById.getString(cursorReportById.getColumnIndexOrThrow(Covid19CvContract.Covid19CvEntry.COLUMN_NAME_FECHA_INICIO_SINTOMAS)));
            tvNombreMunicipio.setText(cursorReportById.getString(cursorReportById.getColumnIndexOrThrow(Covid19CvContract.Covid19CvEntry.COLUMN_NAME_MUNICIPIO)));
            int radioButtonSi = cursorReportById.getInt(cursorReportById.getColumnIndexOrThrow(Covid19CvContract.Covid19CvEntry.COLUMN_NAME_CONTACTO_CERCANO));
            if(radioButtonSi==1) {
                rbCloseContactSi.setChecked(true);
            } else {
                rbCloseContactNo.setChecked(true);
            }
            checkBoxFiebre.setChecked(cursorReportById.getInt(cursorReportById.getColumnIndexOrThrow(Covid19CvContract.Covid19CvEntry.COLUMN_NAME_SINTOMA_FIEBRE))==1);
            checkBoxDificultadRespiratoria.setChecked(cursorReportById.getInt(cursorReportById.getColumnIndexOrThrow(Covid19CvContract.Covid19CvEntry.COLUMN_NAME_SINTOMA_DIFICULTAD_RESPIRATORIA))==1);
            checkBoxTos.setChecked(cursorReportById.getInt(cursorReportById.getColumnIndexOrThrow(Covid19CvContract.Covid19CvEntry.COLUMN_NAME_SINTOMA_TOS))==1);
            checkBoxFatiga.setChecked(cursorReportById.getInt(cursorReportById.getColumnIndexOrThrow(Covid19CvContract.Covid19CvEntry.COLUMN_NAME_SINTOMA_FATIGA))==1);
            checkBoxDolorMuscular.setChecked(cursorReportById.getInt(cursorReportById.getColumnIndexOrThrow(Covid19CvContract.Covid19CvEntry.COLUMN_NAME_SINTOMA_DOLOR_MUSCULAR))==1);
            checkBoxDolorCabeza.setChecked(cursorReportById.getInt(cursorReportById.getColumnIndexOrThrow(Covid19CvContract.Covid19CvEntry.COLUMN_NAME_SINTOMA_DOLOR_CABEZA))==1);
            checkBoxDiarrea.setChecked(cursorReportById.getInt(cursorReportById.getColumnIndexOrThrow(Covid19CvContract.Covid19CvEntry.COLUMN_NAME_SINTOMA_DIARREA))==1);
            checkBoxDolorGarganta.setChecked(cursorReportById.getInt(cursorReportById.getColumnIndexOrThrow(Covid19CvContract.Covid19CvEntry.COLUMN_NAME_SINTOMA_DOLOR_GARGANTA))==1);
            checkBoxCongestion.setChecked(cursorReportById.getInt(cursorReportById.getColumnIndexOrThrow(Covid19CvContract.Covid19CvEntry.COLUMN_NAME_SINTOMA_CONGESTION))==1);
            checkBoxNauseas.setChecked(cursorReportById.getInt(cursorReportById.getColumnIndexOrThrow(Covid19CvContract.Covid19CvEntry.COLUMN_NAME_SINTOMA_NAUSEAS))==1);
            checkBoxPerdidaOlfatoGusto.setChecked(cursorReportById.getInt(cursorReportById.getColumnIndexOrThrow(Covid19CvContract.Covid19CvEntry.COLUMN_NAME_SINTOMA_PERDIDA_OLFATO_GUSTO))==1);
        }

        tvFechaInicioSintomas.setOnClickListener(v -> {
            calendar = Calendar.getInstance();
            int dia = calendar.get(Calendar.DAY_OF_MONTH);
            int mes = calendar.get(Calendar.MONTH);
            int año = calendar.get(Calendar.YEAR);

            datePickerDialog = new DatePickerDialog(InformeActivity.this, (view, year, month, dayOfMonth) -> tvFechaInicioSintomas.setText(dayOfMonth + "/" + (month+1) + "/" + year), dia, mes, año);
            datePickerDialog.show();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.actionbar3,menu);

        menuItem1 = menu.findItem(R.id.añadir_actualizar_informe);
        menuItem2 = menu.findItem(R.id.borrar_informe);
        if(getIntent().hasExtra("nombreMunicipio")) {  // VIENE DE MUNICIPIO_ACTIVITY, CLICADO EL BOTON FLOTANTE (Crear)
            menuItem1.setTitle("Añadir informe");
            menuItem2.setVisible(false);
        } else if(getIntent().hasExtra("nombresMunicipios")) { // VIENE DE MUNICIPIOS_ACTIVITY, CLICADO EL BOTON FLOTANTE (Crear)
            menuItem1.setTitle("Añadir informe");
            menuItem2.setVisible(false);
        } else if(getIntent().hasExtra("informeId")) { // VIENE DE MUNICIPIO_ACTIVITY, CLICADO EL LINEAR_LAYOUT_INFORME (Actualizar/Borrar)
            menuItem1.setTitle("Actualizar informe");
            menuItem2.setVisible(true);
            menuItem2.setTitle("Borrar infome");
        }

        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Bundle extras = getIntent().getExtras();

        switch (item.getItemId()) {
            case R.id.añadir_actualizar_informe:
                if(getIntent().hasExtra("nombreMunicipio")) {
                    if(camposRellenados()) {
                        Informe informe = new Informe();
                        informe.setFechaInicio_sintomas((String) tvFechaInicioSintomas.getText());
                        if(rbCloseContactSi.isChecked()) {
                            informe.setClose_contact(1);
                        } else if(rbCloseContactNo.isChecked()) {
                            informe.setClose_contact(0);
                        }
                        informe.setSintoma_fiebre(checkBoxFiebre.isChecked() ? 1 : 0);
                        informe.setSintoma_dificultad_respiratoria(checkBoxDificultadRespiratoria.isChecked() ? 1 : 0);
                        informe.setSintoma_tos(checkBoxTos.isChecked() ? 1 : 0);
                        informe.setSintoma_fatiga(checkBoxFatiga.isChecked() ? 1 : 0);
                        informe.setSintoma_dolor_muscular(checkBoxDolorMuscular.isChecked() ? 1 : 0);
                        informe.setSintoma_dolor_cabeza(checkBoxDolorCabeza.isChecked() ? 1 : 0);
                        informe.setSintoma_diarrea(checkBoxDiarrea.isChecked() ? 1 : 0);
                        informe.setSintoma_dolor_garganta(checkBoxDolorGarganta.isChecked() ? 1 : 0);
                        informe.setSintoma_congestion(checkBoxCongestion.isChecked() ? 1 : 0);
                        informe.setSintoma_nauseas(checkBoxNauseas.isChecked() ? 1 : 0);
                        informe.setSintoma_perdida_olfato_gusto(checkBoxPerdidaOlfatoGusto.isChecked() ? 1 : 0);
                        informe.setNombreMunicipio(tvNombreMunicipio.getText() + "");
                        dbHelper.InsertReport(db,informe);
                        Toast.makeText(context, "Informe añadido", Toast.LENGTH_LONG).show();
                        setResult(MunicipioActivity.RESULT_CREACION_INFORME);
                        finish();
                    } else {
                        Toast.makeText(context, "Rellena los campos vacíos", Toast.LENGTH_LONG).show();
                    }
                } else if(getIntent().hasExtra("nombresMunicipios")) {
                    if(camposRellenados()) {
                        Informe informe = new Informe();
                        informe.setFechaInicio_sintomas((String) tvFechaInicioSintomas.getText());
                        if(rbCloseContactSi.isChecked()) {
                            informe.setClose_contact(1);
                        } else if(rbCloseContactNo.isChecked()) {
                            informe.setClose_contact(0);
                        }
                        informe.setSintoma_fiebre(checkBoxFiebre.isChecked() ? 1 : 0);
                        informe.setSintoma_dificultad_respiratoria(checkBoxDificultadRespiratoria.isChecked() ? 1 : 0);
                        informe.setSintoma_tos(checkBoxTos.isChecked() ? 1 : 0);
                        informe.setSintoma_fatiga(checkBoxFatiga.isChecked() ? 1 : 0);
                        informe.setSintoma_dolor_muscular(checkBoxDolorMuscular.isChecked() ? 1 : 0);
                        informe.setSintoma_dolor_cabeza(checkBoxDolorCabeza.isChecked() ? 1 : 0);
                        informe.setSintoma_diarrea(checkBoxDiarrea.isChecked() ? 1 : 0);
                        informe.setSintoma_dolor_garganta(checkBoxDolorGarganta.isChecked() ? 1 : 0);
                        informe.setSintoma_congestion(checkBoxCongestion.isChecked() ? 1 : 0);
                        informe.setSintoma_nauseas(checkBoxNauseas.isChecked() ? 1 : 0);
                        informe.setSintoma_perdida_olfato_gusto(checkBoxPerdidaOlfatoGusto.isChecked() ? 1 : 0);
                        informe.setNombreMunicipio(tvNombreMunicipio.getText() + "");
                        dbHelper.InsertReport(db,informe);
                        Toast.makeText(context, "Informe añadido", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(context, "Rellena los campos vacíos", Toast.LENGTH_LONG).show();
                    }
                } else if(getIntent().hasExtra("informeId")) {
                    if(camposRellenados()) {
                        Informe informe = new Informe();
                        informe.setFechaInicio_sintomas(tvFechaInicioSintomas.getText() + "");
                        if(rbCloseContactSi.isChecked()) {
                            informe.setClose_contact(1);
                        } else if(rbCloseContactNo.isChecked()) {
                            informe.setClose_contact(0);
                        }
                        informe.setSintoma_fiebre(checkBoxFiebre.isChecked() ? 1 : 0);
                        informe.setSintoma_dificultad_respiratoria(checkBoxDificultadRespiratoria.isChecked() ? 1 : 0);
                        informe.setSintoma_tos(checkBoxTos.isChecked() ? 1 : 0);
                        informe.setSintoma_fatiga(checkBoxFatiga.isChecked() ? 1 : 0);
                        informe.setSintoma_dolor_muscular(checkBoxDolorMuscular.isChecked() ? 1 : 0);
                        informe.setSintoma_dolor_cabeza(checkBoxDolorCabeza.isChecked() ? 1 : 0);
                        informe.setSintoma_diarrea(checkBoxDiarrea.isChecked() ? 1 : 0);
                        informe.setSintoma_dolor_garganta(checkBoxDolorGarganta.isChecked() ? 1 : 0);
                        informe.setSintoma_congestion(checkBoxCongestion.isChecked() ? 1 : 0);
                        informe.setSintoma_nauseas(checkBoxNauseas.isChecked() ? 1 : 0);
                        informe.setSintoma_perdida_olfato_gusto(checkBoxPerdidaOlfatoGusto.isChecked() ? 1 : 0);
                        informe.setNombreMunicipio(tvNombreMunicipio.getText() + "");
                        dbHelper.UpdateReport(db,informe,Integer.parseInt((String) extras.get("informeId")));
                        Toast.makeText(context, "Informe actualizado", Toast.LENGTH_LONG).show();
                        setResult(MunicipioActivity.RESULT_CREACION_INFORME);
                        finish();
                    } else {
                        Toast.makeText(context, "Rellena los campos vacíos", Toast.LENGTH_LONG).show();
                    }
                }
                return true;
            case R.id.borrar_informe:
                if(getIntent().hasExtra("informeId")) {
                    int informeId = Integer.parseInt((String) extras.get("informeId"));
                    setTitle("Actualizar/Borrar informe");
                    dbHelper.DeleteReport(db,informeId);
                    Toast.makeText(context, "Informe borrado", Toast.LENGTH_LONG).show();
                    setResult(MunicipioActivity.RESULT_CREACION_INFORME);
                    finish();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean camposRellenados() {
        if(tvFechaInicioSintomas.getText()=="" || tvNombreMunicipio.getText().toString()=="" || (!rbCloseContactSi.isChecked() && !rbCloseContactNo.isChecked())) {
            return false;
        } else {
            return true;
        }
    }
}