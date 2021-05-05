package miguel.uv.es;

import java.io.Serializable;

public class Informe implements Serializable {

    private String fechaInicio_sintomas;
    private int close_contact; //0-->false, 1-->true
    private String nombreMunicipio;
    private int sintoma_fiebre;
    private int sintoma_dificultad_respiratoria;
    private int sintoma_tos;
    private int sintoma_fatiga;
    private int sintoma_dolor_muscular;
    private int sintoma_dolor_cabeza;
    private int sintoma_diarrea;
    private int sintoma_dolor_garganta;
    private int sintoma_congestion;
    private int sintoma_nauseas;
    private int sintoma_perdida_olfato_gusto;

    public Informe() {
    }

    public String getFechaInicio_sintomas() {
        return fechaInicio_sintomas;
    }

    public void setFechaInicio_sintomas(String fechaInicio_sintoma) { this.fechaInicio_sintomas = fechaInicio_sintoma; }

    public int getSintoma_fiebre() { return sintoma_fiebre; }

    public void setSintoma_fiebre(int sintoma_fiebre) { this.sintoma_fiebre = sintoma_fiebre; }

    public int getSintoma_dificultad_respiratoria() { return sintoma_dificultad_respiratoria; }

    public void setSintoma_dificultad_respiratoria(int sintoma_dificultad_respiratoria) { this.sintoma_dificultad_respiratoria = sintoma_dificultad_respiratoria; }

    public int getSintoma_tos() { return sintoma_tos; }

    public void setSintoma_tos(int sintoma_tos) { this.sintoma_tos = sintoma_tos; }

    public int getSintoma_fatiga() { return sintoma_fatiga; }

    public void setSintoma_fatiga(int sintoma_fatiga) { this.sintoma_fatiga = sintoma_fatiga; }

    public int getSintoma_dolor_muscular() { return sintoma_dolor_muscular; }

    public void setSintoma_dolor_muscular(int sintoma_dolor_muscular) { this.sintoma_dolor_muscular = sintoma_dolor_muscular; }

    public int getSintoma_dolor_cabeza() { return sintoma_dolor_cabeza; }

    public void setSintoma_dolor_cabeza(int sintoma_dolor_cabeza) { this.sintoma_dolor_cabeza = sintoma_dolor_cabeza; }

    public int getSintoma_diarrea() { return sintoma_diarrea; }

    public void setSintoma_diarrea(int sintoma_diarrea) { this.sintoma_diarrea = sintoma_diarrea; }

    public int getSintoma_dolor_garganta() { return sintoma_dolor_garganta; }

    public void setSintoma_dolor_garganta(int sintoma_dolor_garganta) { this.sintoma_dolor_garganta = sintoma_dolor_garganta; }

    public int getSintoma_congestion() { return sintoma_congestion; }

    public void setSintoma_congestion(int sintoma_congestion) { this.sintoma_congestion = sintoma_congestion; }

    public int getSintoma_nauseas() { return sintoma_nauseas; }

    public void setSintoma_nauseas(int sintoma_nauseas) { this.sintoma_nauseas = sintoma_nauseas; }

    public int getSintoma_perdida_olfato_gusto() { return sintoma_perdida_olfato_gusto; }

    public void setSintoma_perdida_olfato_gusto(int sintoma_perdida_olfato_gusto) { this.sintoma_perdida_olfato_gusto = sintoma_perdida_olfato_gusto; }

    public int getClose_contact() {
        return close_contact;
    }

    public void setClose_contact(int close_contact) {
        this.close_contact = close_contact;
    }

    public String getNombreMunicipio() { return nombreMunicipio; }

    public void setNombreMunicipio(String nombreMunicipio) { this.nombreMunicipio = nombreMunicipio; }
}
