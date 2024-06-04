
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Paciente {
    private String nome;
    private String cpf;
    private ArrayList<Consulta> consultas = new ArrayList<>();
    static Scanner scanner = new Scanner(System.in);

    public Paciente(String nome, String cpf) {
        this.nome = nome;
        this.cpf = cpf;

    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void addConsulta(Consulta consulta) {
        consultas.add(consulta);
    }

    public ArrayList<Consulta> getConsultas() {
        return consultas;
    }

    public static Paciente getPacienteByCpf(String cpf, ArrayList<Paciente> pacientes) {
        Paciente patientToReturn = new Paciente("", "");

        for (Paciente paciente : pacientes) {
            if (paciente.cpf.equals(cpf)) {
                patientToReturn = paciente;
            }
        }

        return patientToReturn;
    }

    public static String getNomeByCPF(String cpf, List<Paciente> pacientes) {
        for (Paciente paciente : pacientes) {
            if (paciente.getCpf() == cpf) {
                return paciente.getNome();
            }
        }
        return null;
    }

    public static void pegarFuturasConsultas(ArrayList<Paciente> pacientes)
            throws FileNotFoundException {
        System.out.print("Digite o cpf do Paciente: ");

        String cpf = scanner.next();
        Saida saida = Saida.getImpressoraDeSaida("Consultas Futuras");

        ArrayList<Consulta> consultasFuturas = new ArrayList<Consulta>();
        ArrayList<Consulta> consultasPaciente = new ArrayList<>();

        for (Paciente paciente : pacientes) {
            if (paciente.getCpf().equals(cpf)) {
                consultasPaciente.addAll(paciente.getConsultas());
                for (Consulta c : consultasPaciente) {
                    if (c.getData().isAfter(LocalDate.now())
                            || (c.getData().isEqual(LocalDate.now())
                                    && c.getHorario().isAfter(LocalTime.now()))) {
                        consultasFuturas.add(c);
                    }
                }
            }
        }
        for (Consulta c : consultasFuturas) {
            saida.out.println("Medico: " + c.getMedico().getNome());
            saida.out.println("Data: " + c.getData());
            saida.out.println("Horário: " + c.getHorario());
            saida.out.println("---------------------------------------");
        }

        if (saida.deveFechar) {
            saida.out.close();
        }

    }

    public static void pegarMedicosPorPaciente(ArrayList<Paciente> pacientes)
            throws FileNotFoundException {

        System.out.println("Digite o cpf do paciente: ");
        String cpfPaciente = scanner.next();
        Paciente paciente = Paciente.getPacienteByCpf(cpfPaciente, pacientes);
        Saida saida = Saida.getImpressoraDeSaida("Medicos por paciente");

        System.out.println("\nMédicos de " + paciente.getNome() + " :");
        ArrayList<Medico> medicosPorPaciente = new ArrayList<Medico>();

        for (Consulta c : paciente.consultas) {
            if (paciente == c.getPaciente()) {
                if (!medicosPorPaciente.contains(c.getMedico())) {
                    medicosPorPaciente.add(c.getMedico());
                }
            }
        }

        for (Medico m : medicosPorPaciente) {
            saida.out.println("Nome: " + m.getNome() + " | Código " + m.getCodigo());
        }

        if (saida.deveFechar) {
            saida.out.close();
        }
    }

    public static void pegarMedicosEspecificiosDePacientes(ArrayList<Paciente> pacientes,
            String cpf)
            throws FileNotFoundException {

        Paciente paciente = Paciente.getPacienteByCpf(cpf, pacientes);

        System.out.println("\nMédicos de " + paciente.getNome() + " :");

        ArrayList<Integer> medicosImpressos = new ArrayList<>();

        for (Paciente p : pacientes) {
            if (p.getCpf().equals(cpf)) {
                for (Consulta consulta : p.consultas) {
                    int codigoMedico = consulta.getMedico().getCodigo();
                    if (!medicosImpressos.contains(codigoMedico) && consulta.getData().isBefore(LocalDate.now())
                            && consulta.getHorario().isBefore(LocalTime.now())) {
                        System.out.println("Nome: " + consulta.getMedico().getNome() + " | Código: " + codigoMedico);
                        medicosImpressos.add(codigoMedico);
                    }
                }
            }
        }

    }

    public static void consultasEspecificasPacienteComMedico(ArrayList<Paciente> pacientes, ArrayList<Medico> medicos)
            throws FileNotFoundException {

        System.out.print("Digite o cpf do paciente >>");
        String cpf = scanner.next();
        Paciente paciente = Paciente.getPacienteByCpf(cpf, pacientes);
        pegarMedicosEspecificiosDePacientes(pacientes, cpf);

        System.out.print("Digite o código do médico >>");
        int codMedico = scanner.nextInt();
        Medico medico = Medico.getMedicoByCode(codMedico, medicos);
        LocalDate dataAtual = LocalDate.now();

        Saida saida = Saida.getImpressoraDeSaida("Paciente por medico");
        ArrayList<Consulta> consultasRealizadas = new ArrayList<Consulta>();

        for (Paciente p : pacientes) {
            for (Consulta c : p.consultas) {
                if (p.cpf.equals(cpf)) {
                    if (c.getData().isBefore(dataAtual) && c.getMedico().equals(medico)) {
                        consultasRealizadas.add(c);
                    }
                }
            }
        }

        if (!consultasRealizadas.isEmpty()) {
            saida.out.println("\nO paciente " + paciente.getNome() + " realizou as seguintes consultas com o médico "
                    + medico.getNome() + " :");
            for (Consulta c : consultasRealizadas) {
                saida.out.println(
                        "Consulta realizada na data: " + c.getData() + " com o médico(a) " + c.getMedico().getNome());
            }
        } else {
            System.out
                    .println(("o paciente não realizou consultas com o médico " + medico.getNome() + " antes de hoje.")
                            .toLowerCase());
        }

        if (saida != null && saida.deveFechar) {
            saida.out.close();
        }
    }
}
