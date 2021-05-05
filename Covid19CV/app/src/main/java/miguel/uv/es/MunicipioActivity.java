package miguel.uv.es;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MunicipioActivity extends AppCompatActivity {

    private Municipio municipio;
    private String nombreMunicipio;
    public static final int REQUEST_CREACION_INFORME = 0;
    public static final int RESULT_CREACION_INFORME = 1;
    private Covid19CvCursorAdapter covid19CvCursorAdapter;
    private ListView listView;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_municipio);

        listView = findViewById(R.id.lista_informes);
        TextView tvCodigoPostal = findViewById(R.id.tvCodigoPostal);
        TextView tvNumeroCasos = findViewById(R.id.tvNumeroCasos);
        TextView tvNumeroFallecimientos = findViewById(R.id.tvNumeroFallecimientos);
        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);

        Bundle extras = getIntent().getExtras();
        if(getIntent().hasExtra("municipio")) {
            municipio = (Municipio) extras.get("municipio");
            nombreMunicipio = municipio.getNombre();
            setTitle(municipio.getNombre());
            tvCodigoPostal.setText(municipio.getCodigoPostal() + "");
            tvNumeroCasos.setText(municipio.getCasos() + "");
            tvNumeroFallecimientos.setText(municipio.getFallecimientos() + "");
        }

        //INICIALIZAR CURSOR y DB
        Covid19CvDbHelper dbHelper = new Covid19CvDbHelper(this);
        // Gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // Cursor
        Cursor cursorReportsByMunicipality = dbHelper.FindReportByMunicipality(db, nombreMunicipio + "");
        // Setup cursor adapter using cursor from last step
        covid19CvCursorAdapter = new Covid19CvCursorAdapter(this, cursorReportsByMunicipality);
        // Attach cursor adapter to the ListView
        listView.setAdapter(covid19CvCursorAdapter);

        floatingActionButton.setOnClickListener(v -> {
            Intent intent = new Intent(MunicipioActivity.this,InformeActivity.class);
            intent.putExtra("nombreMunicipio",municipio.getNombre());
            startActivityForResult(intent,REQUEST_CREACION_INFORME);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CREACION_INFORME && resultCode==RESULT_CREACION_INFORME) {
            //INICIALIZAR CURSOR y DB
            Covid19CvDbHelper dbHelper = new Covid19CvDbHelper(this);
            // Gets the data repository in write mode
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            // Cursor
            Cursor cursorReportsByMunicipality = dbHelper.FindReportByMunicipality(db, nombreMunicipio + "");
            // Setup cursor adapter using cursor from last step
            covid19CvCursorAdapter = new Covid19CvCursorAdapter(this, cursorReportsByMunicipality);
            // Attach cursor adapter to the ListView
            listView.setAdapter(covid19CvCursorAdapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.actionbar2,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()) {
            case R.id.locate_municipality:
                String encodedName = Uri.encode(municipio.getNombre());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW);
                mapIntent.setData(Uri.parse("geo:0,0?q=" + encodedName));
                startActivity(mapIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}