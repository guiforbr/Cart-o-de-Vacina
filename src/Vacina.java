import java.util.Date;

public class Vacina {
    private String nome;
    private int dosesNecessarias;
    private int intervaloDias;

    public Vacina(String nome, int dosesNecessarias, int intervaloDias) {
        this.nome = nome;
        this.dosesNecessarias = dosesNecessarias;
        this.intervaloDias = intervaloDias;
    }

    public Vacina(Vacina vacina, Date dataTomada) {
    }

    public String getNome() {
        return nome;
    }

    public int getDosesNecessarias() {
        return dosesNecessarias;
    }

    public int getIntervaloDias() {
        return intervaloDias;
    }
}
