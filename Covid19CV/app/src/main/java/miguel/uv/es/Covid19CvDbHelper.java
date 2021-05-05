package miguel.uv.es;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Covid19CvDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Covid19Cv.db";

    public Covid19CvDbHelper(Context context) { super(context, DATABASE_NAME, null, DATABASE_VERSION); }

    public void onCreate(SQLiteDatabase db) { db.execSQL(Covid19CvContract.SQL_CREATE_ENTRIES); }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(Covid19CvContract.SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDelete(SQLiteDatabase db) {
        db.execSQL(Covid19CvContract.SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }

    public void InsertReport(SQLiteDatabase db, Informe informe) {
        ContentValues values = new ContentValues();
        values.put(Covid19CvContract.Covid19CvEntry.COLUMN_NAME_FECHA_INICIO_SINTOMAS, informe.getFechaInicio_sintomas());
        values.put(Covid19CvContract.Covid19CvEntry.COLUMN_NAME_CONTACTO_CERCANO, informe.getClose_contact());
        values.put(Covid19CvContract.Covid19CvEntry.COLUMN_NAME_MUNICIPIO, informe.getNombreMunicipio());
        values.put(Covid19CvContract.Covid19CvEntry.COLUMN_NAME_SINTOMA_FIEBRE, informe.getSintoma_fiebre());
        values.put(Covid19CvContract.Covid19CvEntry.COLUMN_NAME_SINTOMA_DIFICULTAD_RESPIRATORIA, informe.getSintoma_dificultad_respiratoria());
        values.put(Covid19CvContract.Covid19CvEntry.COLUMN_NAME_SINTOMA_TOS, informe.getSintoma_tos());
        values.put(Covid19CvContract.Covid19CvEntry.COLUMN_NAME_SINTOMA_FATIGA, informe.getSintoma_fatiga());
        values.put(Covid19CvContract.Covid19CvEntry.COLUMN_NAME_SINTOMA_DOLOR_MUSCULAR, informe.getSintoma_dolor_muscular());
        values.put(Covid19CvContract.Covid19CvEntry.COLUMN_NAME_SINTOMA_DOLOR_CABEZA, informe.getSintoma_dolor_cabeza());
        values.put(Covid19CvContract.Covid19CvEntry.COLUMN_NAME_SINTOMA_DIARREA, informe.getSintoma_diarrea());
        values.put(Covid19CvContract.Covid19CvEntry.COLUMN_NAME_SINTOMA_DOLOR_GARGANTA, informe.getSintoma_dolor_garganta());
        values.put(Covid19CvContract.Covid19CvEntry.COLUMN_NAME_SINTOMA_CONGESTION, informe.getSintoma_congestion());
        values.put(Covid19CvContract.Covid19CvEntry.COLUMN_NAME_SINTOMA_NAUSEAS, informe.getSintoma_nauseas());
        values.put(Covid19CvContract.Covid19CvEntry.COLUMN_NAME_SINTOMA_PERDIDA_OLFATO_GUSTO, informe.getSintoma_perdida_olfato_gusto());
        db.insert(Covid19CvContract.Covid19CvEntry.TABLE_NAME,null, values);
    }

    public void UpdateReport(SQLiteDatabase db, Informe informe, int informeId) {
        ContentValues values = new ContentValues();
        values.put(Covid19CvContract.Covid19CvEntry.COLUMN_NAME_FECHA_INICIO_SINTOMAS, informe.getFechaInicio_sintomas());
        values.put(Covid19CvContract.Covid19CvEntry.COLUMN_NAME_CONTACTO_CERCANO, informe.getClose_contact());
        values.put(Covid19CvContract.Covid19CvEntry.COLUMN_NAME_MUNICIPIO, informe.getNombreMunicipio());
        values.put(Covid19CvContract.Covid19CvEntry.COLUMN_NAME_SINTOMA_FIEBRE, informe.getSintoma_fiebre());
        values.put(Covid19CvContract.Covid19CvEntry.COLUMN_NAME_SINTOMA_DIFICULTAD_RESPIRATORIA, informe.getSintoma_dificultad_respiratoria());
        values.put(Covid19CvContract.Covid19CvEntry.COLUMN_NAME_SINTOMA_TOS, informe.getSintoma_tos());
        values.put(Covid19CvContract.Covid19CvEntry.COLUMN_NAME_SINTOMA_FATIGA, informe.getSintoma_fatiga());
        values.put(Covid19CvContract.Covid19CvEntry.COLUMN_NAME_SINTOMA_DOLOR_MUSCULAR, informe.getSintoma_dolor_muscular());
        values.put(Covid19CvContract.Covid19CvEntry.COLUMN_NAME_SINTOMA_DOLOR_CABEZA, informe.getSintoma_dolor_cabeza());
        values.put(Covid19CvContract.Covid19CvEntry.COLUMN_NAME_SINTOMA_DIARREA, informe.getSintoma_diarrea());
        values.put(Covid19CvContract.Covid19CvEntry.COLUMN_NAME_SINTOMA_DOLOR_GARGANTA, informe.getSintoma_dolor_garganta());
        values.put(Covid19CvContract.Covid19CvEntry.COLUMN_NAME_SINTOMA_CONGESTION, informe.getSintoma_congestion());
        values.put(Covid19CvContract.Covid19CvEntry.COLUMN_NAME_SINTOMA_NAUSEAS, informe.getSintoma_nauseas());
        values.put(Covid19CvContract.Covid19CvEntry.COLUMN_NAME_SINTOMA_PERDIDA_OLFATO_GUSTO, informe.getSintoma_perdida_olfato_gusto());

        String whereString = Covid19CvContract.Covid19CvEntry._ID + " = ?";
        String[] whereArgumentos = new String[] {String.valueOf(informeId)};
        db.update(Covid19CvContract.Covid19CvEntry.TABLE_NAME, values, whereString, whereArgumentos);
    }

    public void DeleteReport(SQLiteDatabase db, int informeId) {
        String whereString = Covid19CvContract.Covid19CvEntry._ID + " = ?";
        String[] whereArgumentos = new String[] {String.valueOf(informeId)};
        db.delete(Covid19CvContract.Covid19CvEntry.TABLE_NAME, whereString, whereArgumentos);
    }

    public Cursor FindReportById(SQLiteDatabase db, int informeId) {
        String string = Covid19CvContract.SQL_FIND_REPORT_BY_ID;
        Cursor cursor = db.rawQuery(string, new String[]{String.valueOf(informeId)});
        return cursor;
    }

    public Cursor FindReportByMunicipality(SQLiteDatabase db, String nombreMunicipio) {
        String string = Covid19CvContract.SQL_FIND_REPORT_BY_MUNICIPALITY;
        Cursor cursor = db.rawQuery(string, new String[]{nombreMunicipio});
        return cursor;
    }
}
