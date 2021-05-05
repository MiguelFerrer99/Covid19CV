package miguel.uv.es;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AdapterMunicipios extends RecyclerView.Adapter<AdapterMunicipios.ViewHolder> implements Filterable {

    private ArrayList<Municipio> municipios;
    private ArrayList<Municipio> municipiosFiltered;
    private String[] nombresMunicipios;
    Context context;
    private final static boolean FILTERED = true;
    private final static boolean NOT_FILTERED = false;
    private boolean filtering;

    public AdapterMunicipios(Context context, ArrayList<Municipio> municipios, String[] nombresMunicipios) {
        filtering = NOT_FILTERED;
        this.context = context;
        this.municipios = municipios;
        this.nombresMunicipios = nombresMunicipios;
    }

    public String[] getNombresMunicipios() {
        return nombresMunicipios;
    }

    public void ordenarMunicipiosAlfabeticamente(int ascendente) {
        Collections.sort(municipios, new Comparator<Municipio>() {
            @Override
            public int compare(Municipio municipio1, Municipio municipio2) {
                if(ascendente==1) {
                    return (municipio1.getNombre().toLowerCase().compareTo(municipio2.getNombre().toLowerCase()));
                } else if(ascendente==0) {
                    return (municipio2.getNombre().toLowerCase().compareTo(municipio1.getNombre().toLowerCase()));
                }
                return 0;
            }
        });
    }

    public void ordenarMunicipiosPorCasos(int ascendente) {
        Collections.sort(municipios, (municipio1, municipio2) -> {
            if(ascendente==1) {
                return Integer.compare(municipio1.getCasos(), municipio2.getCasos());
            } else if(ascendente==0) {
                return Integer.compare(municipio2.getCasos(), municipio1.getCasos());
            }
            return 0;
        });
    }

    public void ordenarMunicipiosPorFallecimientos(int ascendente) {
        Collections.sort(municipios, (municipio1, municipio2) -> {
            if(ascendente==1) {
                return Integer.compare(municipio1.getFallecimientos(), municipio2.getFallecimientos());
            } else if(ascendente==0) {
                return Integer.compare(municipio2.getFallecimientos(), municipio1.getFallecimientos());
            }
            return 0;
        });
    }

    public void cambiarOrdenMunicipios(int sortBy, int ascendente) {
        switch (sortBy) {
            case 0:
                ordenarMunicipiosAlfabeticamente(ascendente);
                break;
            case 1:
                ordenarMunicipiosPorCasos(ascendente);
                break;
            case 2:
                ordenarMunicipiosPorFallecimientos(ascendente);
                break;
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                municipiosFiltered = new ArrayList<>();
                if(charString.isEmpty()) {
                    filtering = NOT_FILTERED;
                    municipiosFiltered = municipios;
                } else {
                    filtering = FILTERED;
                    ArrayList<Municipio> filteredList = new ArrayList<>();
                    for(Municipio municipio : municipios) {
                        if(municipio.getNombre().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(municipio);
                        }
                    }
                    municipiosFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = municipiosFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                municipiosFiltered = (ArrayList<Municipio>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvNombre;
        private final TextView tvCodigoPostal;
        private final TextView tvNumCasos;
        private final TextView tvNumFallecimientos;
        private final LinearLayout linearLayoutMunicipio;

        public ViewHolder(View view) {
            super(view);
            tvNombre = view.findViewById(R.id.nombre);
            tvCodigoPostal = view.findViewById(R.id.codigo_postal);
            tvNumCasos = view.findViewById(R.id.num_casos);
            tvNumFallecimientos = view.findViewById(R.id.num_fallecimientos);
            linearLayoutMunicipio = view.findViewById(R.id.linearLayout_municipio);
        }

        public LinearLayout getLinearLayoutMunicipio() { return linearLayoutMunicipio; }

        public TextView getTextViewNombre() {
            return tvNombre;
        }

        public TextView getTextViewCodigoPostal() {
            return tvCodigoPostal;
        }

        public TextView getTextViewNumCasos() {
            return tvNumCasos;
        }

        public TextView getTextViewNumFallecimientos() {
            return tvNumFallecimientos;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_municipio_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Municipio municipio = new Municipio();
        if(filtering == FILTERED) {
            municipio = municipiosFiltered.get(position);
        } else if(filtering == NOT_FILTERED) {
            municipio = municipios.get(position);
        }

        if(municipio.getCasos()<50) {
            holder.getLinearLayoutMunicipio().setBackgroundColor(Color.parseColor("#96E23F"));
        } else if(municipio.getCasos()>=50 && municipio.getCasos()<=99) {
            holder.getLinearLayoutMunicipio().setBackgroundColor(Color.parseColor("#E4BD0B"));
        } else if(municipio.getCasos()>=100) {
            holder.getLinearLayoutMunicipio().setBackgroundColor(Color.parseColor("#E63908"));
        }
        holder.getTextViewNombre().setText(String.valueOf(municipio.getNombre()));
        holder.getTextViewCodigoPostal().setText(String.valueOf("Código Postal: " + municipio.getCodigoPostal()));
        holder.getTextViewNumCasos().setText(String.valueOf("Casos: " + municipio.getCasos()));
        holder.getTextViewNumFallecimientos().setText(String.valueOf("Fallecimientos: " + municipio.getFallecimientos()));

        holder.linearLayoutMunicipio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Municipio municipio = new Municipio();
                if(filtering == FILTERED) {
                    municipio = municipiosFiltered.get(position);
                } else if(filtering == NOT_FILTERED) {
                    municipio = municipios.get(position);
                }
                Intent intent = new Intent(context, MunicipioActivity.class);
                intent.putExtra("municipio",municipio);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        int itemCount = 0;
        if(filtering == FILTERED) {
            itemCount = municipiosFiltered.size();
        } else if(filtering == NOT_FILTERED) {
            itemCount = municipios.size();
        }
        return itemCount;
    }
}