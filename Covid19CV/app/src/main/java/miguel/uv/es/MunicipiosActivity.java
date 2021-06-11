package miguel.uv.es;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import javax.net.ssl.HttpsURLConnection;

public class MunicipiosActivity extends AppCompatActivity implements LocationListener {

    private AdapterMunicipios adapterMunicipios;
    private int sortBy = 0;
    private int ascendente = 1;
    private RecyclerView recyclerView;
    private ArrayList<Municipio> municipios;
    private String[] nombresMunicipios;
    private FloatingActionButton floatingActionButton;
    private String cityFromLocation = "";
    private LocationManager locationManager;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_municipios);

        setTitle("Lista municipios");
        swipeRefreshLayout = findViewById(R.id.refreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                HTTPConnector httpConnector = new HTTPConnector(getApplicationContext(), HTTPConnector.ACTUALIZAR);
                httpConnector.execute("https://dadesobertes.gva.es/api/3/action/package_show?id=38e6d3ac-fd77-413e-be72-aed7fa6f13c2");
            }
        });
        floatingActionButton = findViewById(R.id.floatingActionButton);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        HTTPConnector httpConnector = new HTTPConnector(this, HTTPConnector.INICIO);
        httpConnector.execute("https://dadesobertes.gva.es/api/3/action/package_show?id=38e6d3ac-fd77-413e-be72-aed7fa6f13c2");

        floatingActionButton.setOnClickListener(v -> {
            Intent intent = new Intent(MunicipiosActivity.this,InformeActivity.class);
            intent.putExtra("nombresMunicipios", (Serializable) adapterMunicipios.getNombresMunicipios());
            intent.putExtra("cityFromLocation", cityFromLocation); //Falta controlar en el destino...
            startActivity(intent);
        });

        floatingActionButton.setVisibility(View.GONE);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.actionbar1,menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                adapterMunicipios.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // filter recycler view when text is changed
                adapterMunicipios.getFilter().filter(newText);
                return false;
            }
        });
        searchView.setOnCloseListener(() -> {
            adapterMunicipios.getFilter().filter("");
            return false;
        });
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.ordenar_alfabeticamente_button:
                sortBy = 0;
                adapterMunicipios.ordenarMunicipiosAlfabeticamente(ascendente);
                adapterMunicipios.notifyDataSetChanged();
                return true;
            case R.id.ordenar_por_casos:
                sortBy = 1;
                adapterMunicipios.ordenarMunicipiosPorCasos(ascendente);
                adapterMunicipios.notifyDataSetChanged();
                return true;
            case R.id.ordenar_por_fallecimientos:
                sortBy = 2;
                adapterMunicipios.ordenarMunicipiosPorFallecimientos(ascendente);
                adapterMunicipios.notifyDataSetChanged();
                return true;
            case R.id.sort_up_down:
                if(ascendente == 1) {
                    ascendente = 0;
                } else if(ascendente == 0) {
                    ascendente = 1;
                }
                adapterMunicipios.cambiarOrdenMunicipios(sortBy, ascendente);
                adapterMunicipios.notifyDataSetChanged();
                return true;
            case R.id.cv_web:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://coronavirus.san.gva.es/es/estadisticas"));
                startActivity(browserIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            cityFromLocation = addressList.get(0).getLocality();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(@NonNull String provider) {}

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        //Cuando desactivamos el GPS, ponemos "cityFromLocation" vacío
        if(provider.equals(LocationManager.GPS_PROVIDER)) {
            cityFromLocation = "";
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 100) {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED) {
                try {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 10, this);
                } catch(SecurityException e) {
                    e.printStackTrace();
                }
            } else { //No se han aceptado los permisos de utilización del GPS
                cityFromLocation = "";
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class HTTPConnector extends AsyncTask<String, Void, ArrayList<Municipio>> {

        public static final int INICIO = 1;
        public static final int ACTUALIZAR = 2;

        private Context context;
        private int estado;

        public HTTPConnector(Context context, int estado) {
            this.context = context;
            this.estado = estado;
        }

        @Override
        protected ArrayList<Municipio> doInBackground(String... strings) {
            //Perform the request and get the answer
            String url = strings[0];
            Writer writer = new StringWriter();
            char[] buffer = new char[1024];
            ArrayList<Municipio> municipiosAux = new ArrayList<>();
            try {
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("GET");
                //add request header
                con.setRequestProperty("user-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
                con.setRequestProperty("accept", "application/json;");
                con.setRequestProperty("accept-language", "es");
                con.connect();
                int responseCode = con.getResponseCode();
                if(responseCode != HttpURLConnection.HTTP_OK) {
                    throw new IOException("HTTP error code: " + responseCode);
                }
                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
                int n;
                while((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
                reader.close();
                //Leemos el JSON, creamos los municipios y los añadimos al ArrayList de Municipios
                String stringJson = writer.toString();
                JSONObject jsonObject = new JSONObject(stringJson);
                JSONObject jsonObjectResult = jsonObject.getJSONObject("result");
                JSONArray jsonArrayResources = jsonObjectResult.getJSONArray("resources");
                JSONObject jsonObjectResource = jsonArrayResources.getJSONObject(jsonArrayResources.length()-1);
                String updated_url = jsonObjectResource.getString("url");

                URL resourceUrl = new URL(updated_url);
                HttpsURLConnection resourceConnection = (HttpsURLConnection) resourceUrl.openConnection();
                resourceConnection.setRequestMethod("GET");
                //add request header
                resourceConnection.setRequestProperty("user-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
                resourceConnection.setRequestProperty("accept", "application/json;");
                resourceConnection.setRequestProperty("accept-language", "es");
                resourceConnection.connect();

                BufferedReader resourceReader = new BufferedReader(new InputStreamReader(resourceConnection.getInputStream(), StandardCharsets.UTF_8));
                resourceReader.readLine(); //Leemos la cabecera y nos la saltamos
                String municipioString = resourceReader.readLine(); //Esto es lo que queremos

                while(municipioString != null) {
                    if(!municipioString.isEmpty()) {
                        Municipio municipio = new Municipio();
                        StringTokenizer stringTokenizer = new StringTokenizer(municipioString, ";");
                        municipio.setCodigoPostal(Integer.parseInt(stringTokenizer.nextToken()));
                        municipio.setNombre(stringTokenizer.nextToken());
                        municipio.setCasos(Integer.parseInt(stringTokenizer.nextToken().trim()));
                        stringTokenizer.nextToken(); //Nos saltamos este campo, no nos interesa
                        stringTokenizer.nextToken(); //Nos saltamos este campo, no nos interesa
                        stringTokenizer.nextToken(); //Nos saltamos este campo, no nos interesa
                        municipio.setFallecimientos(Integer.parseInt(stringTokenizer.nextToken().trim()));
                        municipiosAux.add(municipio);
                    }
                    municipioString = resourceReader.readLine();
                }
                resourceReader.close();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return municipiosAux;
        }

        @Override
        protected void onPostExecute(ArrayList<Municipio> municipiosAux) {
            municipios = municipiosAux;
            nombresMunicipios = new String[municipios.size()];
            for (int i = 0; i < municipios.size(); i++) {
                nombresMunicipios[i] = municipios.get(i).getNombre();
            }
            adapterMunicipios = new AdapterMunicipios(context, municipiosAux, nombresMunicipios);
            recyclerView.setAdapter(adapterMunicipios);
            adapterMunicipios.notifyDataSetChanged();
            //Hacemos visible el floatingButton cuando se han terminado de cargar los municipios
            floatingActionButton.setVisibility(View.VISIBLE);
            if(estado == ACTUALIZAR) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(context, "MUNICIPIOS ACTUALIZADOS", Toast.LENGTH_SHORT).show();
            }
        }
    }
}