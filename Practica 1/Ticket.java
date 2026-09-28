import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Tickets, se crean gaurdando quien lo creo, el problema y sus fechas..
 */
public class Ticket {

    // Formato de fechas
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    /**
     * Contador: Se comparte por todos los tickets y nunca se repite (crece de forma consecutiva).
     */
    private static int cantidad = 0;

    private final int id;
    private final String descripcion;
    private final String nombreCompleto;
    private final LocalDateTime fechaCreacion;
    private LocalDateTime fechaResolucion; // Pendiente = null

    public Ticket(String descripcion, String nombreCompleto) {
        this.id = generarNuevoId();
        this.descripcion = descripcion;
        this.nombreCompleto = nombreCompleto;
        this.fechaCreacion = LocalDateTime.now();
        this.fechaResolucion = null;
    }

    // Genera y asigna el ID de forma consecutiva
    private static synchronized int generarNuevoId() {
        return ++cantidad;
    }

    public int getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public LocalDateTime getFechaResolucion() {
        return fechaResolucion;
    }

    // Solo se marca como resuelto un ticket con fecha de resolucion.
    public boolean estaResuelto() {
        return fechaResolucion != null;
    }

    // Resuelve el ticket al marcarlo con la fecho y hora actual.
    public void marcarResuelto() {
        this.fechaResolucion = LocalDateTime.now();
    }

    // Ticket Template
    @Override
    public String toString() {
        String textoResolucion = estaResuelto() 
                ? fechaResolucion.format(FORMATO_FECHA) 
                : "Pendiente";

        return String.format(
            "**************Ticket #%d*****************%n" +
            "Descripcion:         %s%n" +
            "Creado por:          %s%n" +
            "Fecha de creacion:   %s%n" +
            "Fecha de resolucion: %s%n" +
            "****************************************",
            id,
            descripcion,
            nombreCompleto,
            fechaCreacion.format(FORMATO_FECHA),
            textoResolucion
        );
    }           
}