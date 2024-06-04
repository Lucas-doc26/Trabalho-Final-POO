import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Consulta {

    private LocalDate data;
    private LocalTime horario;
    private Medico medico;
    private Paciente paciente;
    public Object date;

    public Consulta(LocalDate data, LocalTime horario, Medico medico, Paciente paciente) {
        this.data = data;
        this.horario = horario;
        this.medico = medico;
        this.paciente = paciente;

        paciente.addConsulta(this);
        if (!medico.verificaPaciente(paciente)) {
            medico.addPaciente(this.paciente);
        }
    }

    public LocalDate getData() {
        return data;
    }

    public LocalTime getHorario() {
        return horario;
    }

    public Medico getMedico() {
        return medico;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public static void Printar(ArrayList<Consulta> consultas) {
        for (Consulta c : consultas) {
            System.out.println(c.data);
            System.out.println(c.horario);
            System.out.println(c.medico);
            System.out.println(c.paciente.getNome());
        }
    }

}