import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    private static final String SENHA_AUTORIZACAO = "senha123"; // Senha de autorização para cadastro de vacinas
    private static ArrayList<Paciente> pacientes = new ArrayList<>();
    private static ArrayList<Vacina> vacinas = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int opcao;
        do {
            System.out.println("\n====== Menu ======");
            System.out.println("1. Cadastrar Paciente (requer autorização)");
            System.out.println("2. Cadastrar Vacina (requer autorização)");
            System.out.println("3. Registrar Vacinação (requer autorização)");
            System.out.println("4. Visualizar Vacinas de um Paciente");
            System.out.println("5. Visualizar Vacinas Faltantes de um Paciente");
            System.out.println("6. Listar Dados do Paciente (requer autorização)");
            System.out.println("7. Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine(); // Consumir a quebra de linha

            switch (opcao) {
                case 1:
                case 2:
                case 3:
                case 6:
                    solicitarSenha(opcao);
                    break;
                case 4:
                    visualizarVacinasTomadas();
                    break;
                case 5:
                    visualizarVacinasFaltantes();
                    break;
                case 7:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida. Por favor, escolha novamente.");
            }
        } while (opcao != 7);

        scanner.close();
    }

    private static void solicitarSenha(int opcao) {
        System.out.print("Digite a senha de autorização: ");
        String senha = scanner.nextLine();

        if (!senha.equals(SENHA_AUTORIZACAO)) {
            System.out.println("Senha incorreta. Acesso não autorizado.");
            return;
        }

        switch (opcao) {
            case 1:
                cadastrarPaciente();
                break;
            case 2:
                cadastrarVacina();
                break;
            case 3:
                registrarVacina();
                break;
            case 6:
                listarDadosPaciente();
                break;
        }
    }

    private static void cadastrarPaciente() {
        System.out.println("\n===== Cadastro de Paciente =====");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Data de Nascimento (DD/MM/AAAA): ");
        Date dataNascimento = null;
        try {
            dataNascimento = DATE_FORMAT.parse(scanner.nextLine());
        } catch (ParseException e) {
            System.out.println("Formato de data inválido. Use o formato dd/MM/yyyy.");
            return;
        }
        System.out.print("Cartão SUS: ");
        String cartaoSus = scanner.nextLine();

        // Verificar se o cartão SUS já está cadastrado
        for (Paciente paciente : pacientes) {
            if (paciente.getCartaoSus().equals(cartaoSus)) {
                System.out.println("Este cartão SUS já está cadastrado.");
                return;
            }
        }

        // Se não encontrado, cadastrar o paciente
        Paciente paciente = new Paciente(nome, dataNascimento, cartaoSus);
        pacientes.add(paciente);
        System.out.println("Paciente cadastrado com sucesso.");
    }

    private static void cadastrarVacina() {
        System.out.println("\n===== Cadastro de Vacina (Requer Autorização) =====");
        System.out.print("Nome da Vacina: ");
        String nomeVacina = scanner.nextLine();
        System.out.print("Doses Necessárias: ");
        int dosesNecessarias = scanner.nextInt();
        System.out.print("Intervalo entre doses (em dias): ");
        int intervaloDias = scanner.nextInt();
        scanner.nextLine(); // Consumir a quebra de linha

        Vacina vacina = new Vacina(nomeVacina, dosesNecessarias, intervaloDias);
        vacinas.add(vacina);
        System.out.println("Vacina cadastrada com sucesso.");
    }

    private static void registrarVacina() {
        System.out.println("\n===== Registro de Vacinação (Requer Autorização) =====");
        System.out.print("Informe o número do Cartão SUS do paciente: ");
        String cartaoSus = scanner.nextLine();

        Paciente paciente = buscarPacientePorCartaoSus(cartaoSus);
        if (paciente == null) {
            System.out.println("Paciente não encontrado.");
            return;
        }

        System.out.print("Informe o nome da vacina: ");
        String nomeVacina = scanner.nextLine();

        Vacina vacina = buscarVacinaPorNome(nomeVacina);
        if (vacina == null) {
            System.out.println("Vacina não encontrada.");
            return;
        }

        // Registra a vacinação
        Date dataAtual = new Date();
        int dose = calcularDoseDaVacina(paciente, vacina);
        paciente.addVacinaTomada(vacina, dataAtual, dose);
        System.out.println("Vacina registrada com sucesso para o paciente " + paciente.getNome() + ".");

        // Calcular data da próxima dose
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dataAtual);
        calendar.add(Calendar.DAY_OF_MONTH, vacina.getIntervaloDias());
        Date proximaDose = calendar.getTime();
        System.out.println("Próxima dose (" + vacina.getNome() + ") em: " + DATE_FORMAT.format(proximaDose));
    }

    private static int calcularDoseDaVacina(Paciente paciente, Vacina vacina) {
        int dose = 1;
        for (VacinaTomada vacinaTomada : paciente.getVacinasTomadas()) {
            if (vacinaTomada.getVacina().equals(vacina)) {
                dose++;
            }
        }
        return dose;
    }

    private static void visualizarVacinasTomadas() {
        System.out.println("\n===== Visualização de Vacinas Tomadas =====");

        System.out.print("Informe o número do Cartão SUS do paciente: ");
        String cartaoSus = scanner.nextLine();

        Paciente paciente = buscarPacientePorCartaoSus(cartaoSus);
        if (paciente == null) {
            System.out.println("Paciente não encontrado.");
            return;
        }

        if (paciente.getVacinasTomadas().isEmpty()) {
            System.out.println("Não há vacinas tomadas registradas para este paciente.");
            return;
        }

        System.out.println("Vacinas tomadas por " + paciente.getNome() + ":");
        for (VacinaTomada vacinaTomada : paciente.getVacinasTomadas()) {
            System.out.println("- " + vacinaTomada.getVacina().getNome() + " em " + DATE_FORMAT.format(vacinaTomada.getDataTomada()) + " - Dose " + vacinaTomada.getDose());
        }
    }

    private static void visualizarVacinasFaltantes() {
        System.out.println("\n===== Visualização de Vacinas Faltantes =====");

        System.out.print("Informe o número do Cartão SUS do paciente: ");
        String cartaoSus = scanner.nextLine();

        Paciente paciente = buscarPacientePorCartaoSus(cartaoSus);
        if (paciente == null) {
            System.out.println("Paciente não encontrado.");
            return;
        }

        System.out.println("Vacinas faltantes para " + paciente.getNome() + ":");

        for (Vacina vacina : vacinas) {
            int dosesTomadas = paciente.getVacinasTomadas().stream().filter(v -> v.getVacina().equals(vacina)).mapToInt(VacinaTomada::getDose).max().orElse(0);
            int dosesFaltantes = vacina.getDosesNecessarias() - dosesTomadas;

            if (dosesFaltantes > 0) {
                System.out.println("- " + vacina.getNome() + ": " + dosesFaltantes + " doses faltantes");

                // Calcular datas das doses faltantes
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(paciente.getVacinaTomadaMaisRecente(vacina).getDataTomada());
                calendar.add(Calendar.DAY_OF_MONTH, vacina.getIntervaloDias() * (dosesTomadas + 1)); // Próxima dose
                for (int i = 0; i < dosesFaltantes; i++) {
                    Date dataDose = calendar.getTime();
                    System.out.println("  - Dose " + (dosesTomadas + i + 1) + " em " + DATE_FORMAT.format(dataDose));
                    calendar.add(Calendar.DAY_OF_MONTH, vacina.getIntervaloDias()); // Próxima dose
                }
            }
        }
    }

    private static void listarDadosPaciente() {
        System.out.println("\n===== Listagem de Dados do Paciente =====");

        System.out.print("Informe o número do Cartão SUS do paciente: ");
        String cartaoSus = scanner.nextLine();

        Paciente paciente = buscarPacientePorCartaoSus(cartaoSus);
        if (paciente == null) {
            System.out.println("Paciente não encontrado.");
            return;
        }

        System.out.println("Data de Nascimento: " + DATE_FORMAT.format(paciente.getDataNascimento()));

        System.out.println("Dados do paciente " + paciente.getNome() + ":");
        if (paciente.getVacinasTomadas().isEmpty()) {
            System.out.println("Não há vacinas tomadas registradas para este paciente.");
        } else {
            System.out.println("- Vacinas Tomadas:");
            for (VacinaTomada vacinaTomada : paciente.getVacinasTomadas()) {
                System.out.println("  - " + vacinaTomada.getVacina().getNome() + " em " + DATE_FORMAT.format(vacinaTomada.getDataTomada()) + " - Dose " + vacinaTomada.getDose());
            }
            System.out.println("- Vacinas Faltantes:");
            for (Vacina vacina : vacinas) {
                int dosesTomadas = paciente.getVacinasTomadas().stream().filter(v -> v.getVacina().equals(vacina)).mapToInt(VacinaTomada::getDose).max().orElse(0);
                int dosesFaltantes = vacina.getDosesNecessarias() - dosesTomadas;

                if (dosesFaltantes > 0) {
                    System.out.println("  - " + vacina.getNome() + ": " + dosesFaltantes + " doses faltantes");

                    // Calcular datas das doses faltantes
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(paciente.getVacinaTomadaMaisRecente(vacina).getDataTomada());
                    calendar.add(Calendar.DAY_OF_MONTH, vacina.getIntervaloDias() * (dosesTomadas + 1)); // Próxima dose
                    for (int i = 0; i < dosesFaltantes; i++) {
                        Date dataDose = calendar.getTime();
                        System.out.println("    - Dose " + (dosesTomadas + i + 1) + " em " + DATE_FORMAT.format(dataDose));
                        calendar.add(Calendar.DAY_OF_MONTH, vacina.getIntervaloDias()); // Próxima dose
                    }
                }
            }
        }
    }

    private static Paciente buscarPacientePorCartaoSus(String cartaoSus) {
        for (Paciente paciente : pacientes) {
            if (paciente.getCartaoSus().equals(cartaoSus)) {
                return paciente;
            }
        }
        return null;
    }

    private static Vacina buscarVacinaPorNome(String nomeVacina) {
        for (Vacina vacina : vacinas) {
            if (vacina.getNome().equalsIgnoreCase(nomeVacina)) {
                return vacina;
            }
        }
        return null;
    }
}
