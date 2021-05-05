package miguel.uv.es;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Covid19CvCursorAdapter extends CursorAdapter {

    public static final int REQUEST_CREACION_INFORME = 0;
    public static final int RESULT_CREACION_INFORME = 1;
    private Activity activity;
    public Covid19CvCursorAdapter(Activity activity, Cursor cursor) {
        super(activity, cursor, 0);
        this.activity = activity;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.row_informe_view, parent, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView tvInicioSintomas = view.findViewById(R.id.inicioSintomas);
        TextView tvNumeroSintomas = view.findViewById(R.id.numeroSintomas);
        TextView tvContactoCercano = view.findViewById(R.id.closeContact);
        LinearLayout linearLayout_informe = view.findViewById(R.id.linearLayout_informe);
        int numeroSintomas = 0;

        String fechaInicioSintomas = cursor.getString(cursor.getColumnIndexOrThrow(Covid19CvContract.Covid19CvEntry.COLUMN_NAME_FECHA_INICIO_SINTOMAS)); //AQUÍ LA FECHA NO SE ESTÁ GETTEANDO BIEN
        int contactoCercano = cursor.getInt(cursor.getColumnIndexOrThrow(Covid19CvContract.Covid19CvEntry.COLUMN_NAME_CONTACTO_CERCANO));
        int sintomaFiebre = cursor.getInt(cursor.getColumnIndexOrThrow(Covid19CvContract.Covid19CvEntry.COLUMN_NAME_SINTOMA_FIEBRE));
        int sintomaDificultadRespiratoria = cursor.getInt(cursor.getColumnIndexOrThrow(Covid19CvContract.Covid19CvEntry.COLUMN_NAME_SINTOMA_DIFICULTAD_RESPIRATORIA));
        int sintomaTos = cursor.getInt(cursor.getColumnIndexOrThrow(Covid19CvContract.Covid19CvEntry.COLUMN_NAME_SINTOMA_TOS));
        int sintomaFatiga = cursor.getInt(cursor.getColumnIndexOrThrow(Covid19CvContract.Covid19CvEntry.COLUMN_NAME_SINTOMA_FATIGA));
        int sintomaDolorMuscular = cursor.getInt(cursor.getColumnIndexOrThrow(Covid19CvContract.Covid19CvEntry.COLUMN_NAME_SINTOMA_DOLOR_MUSCULAR));
        int sintomaDolorCabeza = cursor.getInt(cursor.getColumnIndexOrThrow(Covid19CvContract.Covid19CvEntry.COLUMN_NAME_SINTOMA_DOLOR_CABEZA));
        int sintomaDiarrea = cursor.getInt(cursor.getColumnIndexOrThrow(Covid19CvContract.Covid19CvEntry.COLUMN_NAME_SINTOMA_DIARREA));
        int sintomaDolorGarganta = cursor.getInt(cursor.getColumnIndexOrThrow(Covid19CvContract.Covid19CvEntry.COLUMN_NAME_SINTOMA_DOLOR_GARGANTA));
        int sintomaCongestion = cursor.getInt(cursor.getColumnIndexOrThrow(Covid19CvContract.Covid19CvEntry.COLUMN_NAME_SINTOMA_CONGESTION));
        int sintomaNauseas = cursor.getInt(cursor.getColumnIndexOrThrow(Covid19CvContract.Covid19CvEntry.COLUMN_NAME_SINTOMA_NAUSEAS));
        int sintomaPerdidaOlfatoGusto = cursor.getInt(cursor.getColumnIndexOrThrow(Covid19CvContract.Covid19CvEntry.COLUMN_NAME_SINTOMA_PERDIDA_OLFATO_GUSTO));

        if(sintomaFiebre == 1) {
            numeroSintomas++;
        }
        if(sintomaDificultadRespiratoria == 1) {
            numeroSintomas++;
        }
        if(sintomaTos == 1) {
            numeroSintomas++;
        }
        if(sintomaFatiga == 1) {
            numeroSintomas++;
        }
        if(sintomaDolorMuscular == 1) {
            numeroSintomas++;
        }
        if(sintomaDolorCabeza == 1) {
            numeroSintomas++;
        }
        if(sintomaDiarrea == 1) {
            numeroSintomas++;
        }
        if(sintomaDolorGarganta == 1) {
            numeroSintomas++;
        }
        if(sintomaCongestion == 1) {
            numeroSintomas++;
        }
        if(sintomaNauseas == 1) {
            numeroSintomas++;
        }
        if(sintomaPerdidaOlfatoGusto == 1) {
            numeroSintomas++;
        }

        tvInicioSintomas.setText("Inicio síntomas: " + fechaInicioSintomas);
        tvNumeroSintomas.setText("Número síntomas: " + numeroSintomas);
        if(contactoCercano == 1) {
            tvContactoCercano.setText("Contacto cercano: Si");
        }else {
            tvContactoCercano.setText("Contacto cercano: No");
        }

        linearLayout_informe.setOnClickListener(v -> {
            Intent intent = new Intent(context, InformeActivity.class);
            intent.putExtra("informeId",cursor.getString(cursor.getColumnIndexOrThrow(Covid19CvContract.Covid19CvEntry._ID)));
            activity.startActivityForResult(intent,REQUEST_CREACION_INFORME);
        });
    }
}
