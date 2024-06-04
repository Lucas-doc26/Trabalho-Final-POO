
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Medico {
    private String nome;
    private int codigo;
    private ArrayList<Paciente> pacientes = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);

    public Medico(String nome, int codigo) {
        this.nome = nome;
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public int getCodigo() {
        return codigo;
    }

    public ArrayList<Paciente> getPaciente() {
        return pacientes;
    }

    public void addPaciente(Paciente paciente) {
        pacientes.add(paciente);
    }

    public boolean verificaPaciente(Paciente paciente) {
        return this.pacientes.contains(paciente);
    }

    public static Medico getMedicoByCode(int codigo, ArrayList<Medico> medicos) {
        Medico medicoRetornar = new Medico("", 0);

        for (Medico doctor : medicos) {
            if (doctor.codigo == codigo) {
                medicoRetornar = doctor;
            }
        }

        return medicoRetornar;
    }

    public static void pegarTodosOsMedicos(ArrayList<Medico> medicos) {
        System.out.println("\nEscolha um medico:");
        for (Medico m : medicos) {
            System.out.println("Médico: " + m.getNome());
            System.out.println("Codigo: " + m.getCodigo());
            System.out.println("------------------------------");

        }

    }

    public static void pegarConsultasPorMedico(ArrayList<Medico> medicos,
            ArrayList<Consulta> consultas) throws FileNotFoundException {
        System.out.println("Digite o codigo do medico: ");
        int codigo = scanner.nextInt();

        ArrayList<Consulta> consultasPorMedico = new ArrayList<Consulta>();
        Medico medicoCodigo = getMedicoByCode(codigo, medicos);
        Saida saida = Saida.getImpressoraDeSaida("Pacientes por médico");

        try {
            for (Consulta consulta : consultas) {
                if (consulta.getMedico() == medicoCodigo && (consulta.getData().isBefore(LocalDate.now())
                        || (consulta.getData().isEqual(LocalDate.now())
                                && consulta.getHorario().isBefore(LocalTime.now())))) {
                    consultasPorMedico.add(consulta);
                }
            }

            for (Consulta c : consultasPorMedico) {
                saida.out.println("Paciente: " + c.getPaciente().getNome() + "\n" + "Data:" + c.getData() + "\n"
                        + "Horário: " + c.getHorario() + "\n" + "---------------------------------------");
            }
        } finally {
            if (saida.deveFechar) {
                saida.out.close();
            }
        }

    }

    public static void pegarPacientesPorMedico(ArrayList<Medico> medicos) throws FileNotFoundException {
        System.out.print("Digiite o codigo do medico >>");
        int codigoMedico = scanner.nextInt();
        ArrayList<Paciente> pacientesDoMedico = new ArrayList<>();
        Saida saida = Saida.getImpressoraDeSaida("Pacientes do medico");
        for (Medico m : medicos) {
            ArrayList<Paciente> pacientesMedico = m.getPaciente();
            if (m.getCodigo() == codigoMedico) {
                for (Paciente p : pacientesMedico) {
                    pacientesDoMedico.add(p);
                }

            }
        }

        for (Paciente p : pacientesDoMedico) {
            saida.out.println("---------------------------------------");
            saida.out.println("Paciente: " + p.getNome());
            saida.out.println("---------------------------------------");
        }

        if (saida.deveFechar) {
            saida.out.close();
        }

    }

    public static void pegarConsultasPorPeriodo(ArrayList<Medico> medicos)
            throws FileNotFoundException {
        ArrayList<Consulta> consultasPorPeriodo = new ArrayList<Consulta>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        System.out.print("Digite o codigo do medico >> ");
        int codigo = scanner.nextInt();

        System.out.print("Digite a data inicial (dd/MM/yyyy): ");

        String dataInicialString = scanner.next();

        System.out.print("Digite a data final(dd/MM/yyyy): ");
        String dataFinalString = scanner.next();

        LocalDate dataFinal = LocalDate.parse(dataFinalString, formatter);
        LocalDate dataInicial = LocalDate.parse(dataInicialString, formatter);
        Saida saida = Saida.getImpressoraDeSaida("Consulta por periodo");

        for (Medico medico : medicos) {
            if (medico.getCodigo() == codigo) {
                for (Paciente paciente : medico.getPaciente()) {
                    for (Consulta consulta : paciente.getConsultas()) {
                        LocalDate dataConsulta = consulta.getData();
                        if (consulta.getMedico().getCodigo() == codigo &&
                                !dataConsulta.isBefore(dataInicial) &&
                                !dataConsulta.isAfter(dataFinal)) {
                            consultasPorPeriodo.add(consulta);
                        }
                    }
                }
                break;
            }
        }

        Collections.sort(consultasPorPeriodo, new Comparator<Consulta>() {
            public int compare(Consulta consulta1, Consulta consulta2) {
                int comparacaoData = consulta1.getData().compareTo(consulta2.getData());
                if (comparacaoData != 0) {
                    return comparacaoData;
                } else {
                    return consulta1.getHorario().compareTo(consulta2.getHorario());
                }
            }
        });

        for (Consulta c : consultasPorPeriodo) {
            saida.out.println("---------------------------------------");
            saida.out.println("Paciente: " + c.getPaciente().getNome());
            saida.out.println("Data: " + c.getData());
            saida.out.println("Horário: " + c.getHorario());
            saida.out.println("---------------------------------------");
        }

        if (saida.deveFechar) {
            saida.out.close();
        }

    }

    public static List<Consulta> pegarConsultasDoMedico(int codigo, List<Consulta> consultas) {
        List<Consulta> consultasDoMedico = new ArrayList<>();

        for (Consulta consulta : consultas) {
            if (consulta.getMedico().getCodigo() == codigo) {
                consultasDoMedico.add(consulta);
            }
        }

        return consultasDoMedico;
    }

    public static void pacientesQueNaoConsultaram(ArrayList<Medico> medicos) throws FileNotFoundException {
        System.out.println("Digite o código do médico >>");
        int codigoMed = scanner.nextInt();
        System.out.println("Digite a quantidade de meses >>");
        int meses = scanner.nextInt();
        ArrayList<Consulta> pacientesNaoConsultaram = new ArrayList<Consulta>();
        Saida saida = Saida.getImpressoraDeSaida("Pacientes que não consultam");
        LocalDate dataAgora = LocalDate.now();

        LocalDate dataLimite = dataAgora.minusMonths(meses);

        for (Medico m : medicos) {
            if (m.codigo == codigoMed) {
                for (Paciente p : m.pacientes) {
                    for (Consulta c : p.getConsultas()) {
                        LocalDate dataConsulta = c.getData();

                        if (dataConsulta.isBefore(dataLimite)) {
                            pacientesNaoConsultaram.add(c);
                        }
                    }
                }
            }
        }

        for (Consulta c : pacientesNaoConsultaram) {
            saida.out.println("Paciente: " + c.getPaciente().getNome());
            saida.out.println("Data: " + c.getData());
            saida.out.println("---------------------------------------");
        }

        if (saida.deveFechar) {
            saida.out.close();
        }

    }

}
