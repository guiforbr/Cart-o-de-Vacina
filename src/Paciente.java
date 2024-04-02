import java.util.ArrayList;
import java.util.Date;

public class Paciente {
    private String nome;
    private Date dataNascimento;
    private String cartaoSus;
    private ArrayList<VacinaTomada> vacinasTomadas;

    public Paciente(String nome, Date dataNascimento, String cartaoSus) {
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.cartaoSus = cartaoSus;
        this.vacinasTomadas = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public String getCartaoSus() {
        return cartaoSus;
    }

    public ArrayList<VacinaTomada> getVacinasTomadas() {
        return vacinasTomadas;
    }

    public void addVacinaTomada(Vacina vacina, Date dataTomada, int dose) {
        VacinaTomada vacinaTomada = new VacinaTomada(vacina, dataTomada, dose);
        vacinasTomadas.add(vacinaTomada);
    }

    public VacinaTomada getVacinaTomadaMaisRecente(Vacina vacina) {
        VacinaTomada maisRecente = null;
        for (VacinaTomada vacinaTomada : vacinasTomadas) {
            if (vacinaTomada.getVacina().equals(vacina)) {
                if (maisRecente == null || vacinaTomada.getDataTomada().after(maisRecente.getDataTomada())) {
                    maisRecente = vacinaTomada;
                }
            }
        }
        return maisRecente;
    }
}
