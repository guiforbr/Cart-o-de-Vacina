import java.util.Date;

public class VacinaTomada {
    private Vacina vacina;
    private Date dataTomada;
    private int dose;

    public VacinaTomada(Vacina vacina, Date dataTomada, int dose) {
        this.vacina = vacina;
        this.dataTomada = dataTomada;
        this.dose = dose;
    }

    public Vacina getVacina() {
        return vacina;
    }

    public Date getDataTomada() {
        return dataTomada;
    }

    public int getDose() {
        return dose;
    }
}
