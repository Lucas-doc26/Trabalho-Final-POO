import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class aplicativo {
    public static void Interface() {
        System.out.println("\n[1]Quais são todos os pacientes de um determinado médico\n" +
                "[2]Quais são todas as consultas agendadas para um determinado médico em determinado período\n" +
                "[3]Quais são todos os médicos que um determinado paciente já consultou ou tem consulta agendada?\n" +
                "[4]Quais são todas as consultas que um determinado paciente realizou com determinado médico?\n" +
                "[5]Quais são todas as consultas agendadas que um determinado paciente possui?\n" +
                "[6]Quais são os pacientes de um determinado médico que não o consulta há mais que um determinado tempo (em meses)?\n");

    }

    public static void main(String[] args) throws IOException {

        ArrayList<Consulta> consultas = new ArrayList<>();
        ArrayList<Medico> medicos = new ArrayList<>();
        ArrayList<Paciente> pacientes = new ArrayList<>();

        Leitor leitor = new Leitor("csv/medicos.csv", "csv/pacientes.csv", "csv/consultas.csv");
        leitor.ler(medicos, pacientes, consultas);

        Scanner scanner = new Scanner(System.in);

        int escolha;
        do {
            Interface();
            try {
                System.out.print("Digite sua escolha >> ");
                escolha = scanner.nextInt();

                switch (escolha) {
                    case 1:
                        Medico.pegarTodosOsMedicos(medicos);
                        Medico.pegarPacientesPorMedico(medicos);
                        break;

                    case 2:
                        Medico.pegarTodosOsMedicos(medicos);
                        Medico.pegarConsultasPorPeriodo(medicos);
                        break;
                    case 3:
                        Paciente.pegarMedicosPorPaciente(pacientes);
                        break;
                    case 4:
                        Paciente.consultasEspecificasPacienteComMedico(pacientes, medicos);
                        break;

                    case 5:
                        Paciente.pegarFuturasConsultas(pacientes);
                        break;
                    case 6:
                        Medico.pegarTodosOsMedicos(medicos);
                        Medico.pacientesQueNaoConsultaram(medicos);
                    default:
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida.\n");
                scanner.nextLine();
                escolha = -1;
            }
        } while (escolha != 7);
    }
}