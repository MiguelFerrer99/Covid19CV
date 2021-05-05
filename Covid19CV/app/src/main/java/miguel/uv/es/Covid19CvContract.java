package miguel.uv.es;

import android.provider.BaseColumns;

public class Covid19CvContract {

    private Covid19CvContract() {}

    public static class Covid19CvEntry implements BaseColumns {
        public static final String TABLE_NAME = "covid19cv";
        public static final String COLUMN_NAME_FECHA_INICIO_SINTOMAS = "fecha_inicio_sintomas";
        public static final String COLUMN_NAME_CONTACTO_CERCANO = "contacto_cercano";
        public static final String COLUMN_NAME_MUNICIPIO = "municipio";
        public static final String COLUMN_NAME_SINTOMA_FIEBRE = "sintoma_fiebre";
        public static final String COLUMN_NAME_SINTOMA_DIFICULTAD_RESPIRATORIA = "sintoma_dificultad_respiratoria";
        public static final String COLUMN_NAME_SINTOMA_TOS = "sintoma_tos";
        public static final String COLUMN_NAME_SINTOMA_FATIGA = "sintoma_fatiga";
        public static final String COLUMN_NAME_SINTOMA_DOLOR_MUSCULAR = "sintoma_dolor_muscular";
        public static final String COLUMN_NAME_SINTOMA_DOLOR_CABEZA = "sintoma_dolor_cabeza";
        public static final String COLUMN_NAME_SINTOMA_DIARREA = "sintoma_diarrea";
        public static final String COLUMN_NAME_SINTOMA_DOLOR_GARGANTA = "sintoma_dolor_garganta";
        public static final String COLUMN_NAME_SINTOMA_CONGESTION = "sintoma_congestion";
        public static final String COLUMN_NAME_SINTOMA_NAUSEAS = "sintoma_nauseas";
        public static final String COLUMN_NAME_SINTOMA_PERDIDA_OLFATO_GUSTO = "sintoma_perdida_olfato_gusto";
    }

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + Covid19CvEntry.TABLE_NAME + " (" +
                    Covid19CvEntry._ID + " INTEGER PRIMARY KEY," +
                    Covid19CvEntry.COLUMN_NAME_FECHA_INICIO_SINTOMAS + " TEXT," +
                    Covid19CvEntry.COLUMN_NAME_CONTACTO_CERCANO + " INTEGER," +
                    Covid19CvEntry.COLUMN_NAME_MUNICIPIO + " TEXT," +
                    Covid19CvEntry.COLUMN_NAME_SINTOMA_FIEBRE + " INTEGER," +
                    Covid19CvEntry.COLUMN_NAME_SINTOMA_DIFICULTAD_RESPIRATORIA + " INTEGER," +
                    Covid19CvEntry.COLUMN_NAME_SINTOMA_TOS + " INTEGER," +
                    Covid19CvEntry.COLUMN_NAME_SINTOMA_FATIGA + " INTEGER," +
                    Covid19CvEntry.COLUMN_NAME_SINTOMA_DOLOR_MUSCULAR + " INTEGER," +
                    Covid19CvEntry.COLUMN_NAME_SINTOMA_DOLOR_CABEZA + " INTEGER," +
                    Covid19CvEntry.COLUMN_NAME_SINTOMA_DIARREA + " INTEGER," +
                    Covid19CvEntry.COLUMN_NAME_SINTOMA_DOLOR_GARGANTA + " INTEGER," +
                    Covid19CvEntry.COLUMN_NAME_SINTOMA_CONGESTION + " INTEGER," +
                    Covid19CvEntry.COLUMN_NAME_SINTOMA_NAUSEAS + " INTEGER," +
                    Covid19CvEntry.COLUMN_NAME_SINTOMA_PERDIDA_OLFATO_GUSTO + " INTEGER)";

    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + Covid19CvEntry.TABLE_NAME;

    public static final String SQL_FIND_REPORT_BY_ID = "SELECT * FROM " + Covid19CvEntry.TABLE_NAME + " WHERE " + Covid19CvEntry._ID + " = ?";

    public static final String SQL_FIND_REPORT_BY_MUNICIPALITY = "SELECT * FROM " + Covid19CvEntry.TABLE_NAME + " WHERE " + Covid19CvEntry.COLUMN_NAME_MUNICIPIO + " = ?";

}
