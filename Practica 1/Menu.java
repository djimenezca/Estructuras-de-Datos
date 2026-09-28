import java.util.Scanner;

/**
 * Gestor principal de la interfaz de consola para la atención y seguimiento de tickets.
 */
public class Menu {

    private final Scanner scanner;
    private final ColaPendientes pendientes;
    private final ListaResueltos resueltos;

    public Menu(Scanner scanner) {
        this.scanner = scanner;
        this.pendientes = new ColaPendientes();
        this.resueltos = new ListaResueltos();
    }

    public void iniciar() {
        desplegarEncabezado();
        
        boolean continuar = true;
        while (continuar) {
            mostrarMenuPrincipal();
            int seleccion = solicitarEnteroEnRango("Seleccione un rol o función: ", 1, 3);

            switch (seleccion) {
                case 1:
                    ejecutarFlujoUsuario();
                    break;
                case 2:
                    ejecutarFlujoAdministrador();
                    break;
                case 3:
                    System.out.println("\nFinalizando sesión en el sistema. ¡Hasta luego!");
                    continuar = false;
                    break;
            }
        }
    }

    private void desplegarEncabezado() {
        System.out.println("=================================================");
        System.out.println("   MÓDULO DE ATENCIÓN Y SEGUIMIENTO DE TICKETS   ");
        System.out.println("=================================================");
    }

    private void mostrarMenuPrincipal() {
        System.out.println("\n--- MENÚ DE ACCESO ---");
        System.out.println("1. Portal de Usuarios");
        System.out.println("2. Panel de Administración");
        System.out.println("3. Salir del sistema");
    }

    // --- SUBMENÚ: USUARIOS ---

    private void ejecutarFlujoUsuario() {
        boolean enSubmenu = true;
        while (enSubmenu) {
            System.out.println("\n[ PORTAL DE USUARIOS ]");
            System.out.println("1. Registrar nueva solicitud");
            System.out.println("2. Consultar estado por número de ticket");
            System.out.println("3. Regresar al menú anterior");

            int opcion = solicitarEnteroEnRango("Opción: ", 1, 3);

            switch (opcion) {
                case 1:
                    registrarSolicitud();
                    break;
                case 2:
                    consultarEstadoTicket();
                    break;
                case 3:
                    enSubmenu = false;
                    break;
            }
        }
    }

    private void registrarSolicitud() {
        System.out.println("\n--- Registro de Ticket ---");
        String usuario = solicitarTextoRequerido("Nombre del solicitante: ");
        String detalle = solicitarTextoRequerido("Descripción detallada del incidente: ");

        Ticket nuevoTicket = new Ticket(detalle, usuario);
        pendientes.agregar(nuevoTicket);

        System.out.printf("%nTicket #%d registrado con éxito.%n", nuevoTicket.getId());
        System.out.println("Conserve este número identificador para futuras consultas.");
    }

    private void consultarEstadoTicket() {
        System.out.println("\n--- Consulta de Estado ---");
        int idBuscado = solicitarEntero("Ingrese el ID del ticket a buscar: ");

        Ticket ticketResuelto = resueltos.buscarPorId(idBuscado);

        if (ticketResuelto != null) {
            System.out.println("\nEl ticket se encuentra RESUELTO:");
            System.out.println(ticketResuelto);
        } else {
            System.out.printf("%nEl ticket #%d no aparece en el historial de resueltos (permanece pendiente o el ID es incorrecto).%n", idBuscado);
        }
    }

    // --- SUBMENÚ: ADMINISTRADORES ---

    private void ejecutarFlujoAdministrador() {
        boolean enSubmenu = true;
        while (enSubmenu) {
            System.out.println("\n[ PANEL DE ADMINISTRACIÓN ]");
            System.out.println("1. Inspeccionar ticket prioritario en cola");
            System.out.println("2. Procesar y resolver ticket prioritario");
            System.out.println("3. Regresar al menú anterior");

            int opcion = solicitarEnteroEnRango("Opción: ", 1, 3);

            switch (opcion) {
                case 1:
                    inspeccionarProximoTicket();
                    break;
                case 2:
                    procesarProximoTicket();
                    break;
                case 3:
                    enSubmenu = false;
                    break;
            }
        }
    }

    private void inspeccionarProximoTicket() {
        System.out.println("\n--- Ticket al frente de la cola ---");
        if (pendientes.estaVacia()) {
            System.out.println("Sin tickets pendientes en cola de espera.");
        } else {
            System.out.println(pendientes.verFrente());
        }
    }

    private void procesarProximoTicket() {
        System.out.println("\n--- Procesar Ticket ---");
        if (pendientes.estaVacia()) {
            System.out.println("No hay solicitudes pendientes por resolver.");
            return;
        }

        Ticket ticketAtendido = pendientes.sacarFrente();
        ticketAtendido.marcarResuelto();
        resueltos.agregarAlInicio(ticketAtendido);

        System.out.printf("Ticket #%d marcado como resuelto correctamente.%n", ticketAtendido.getId());
        System.out.println(ticketAtendido);
    }

    // --- AUXILIARES DE LECTURA Y VALIDACIÓN ---

    private int solicitarEntero(String mensajePrompt) {
        while (true) {
            System.out.print(mensajePrompt);
            String entrada = scanner.nextLine().trim();
            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Entrada no válida. Por favor, ingrese un número entero.");
            }
        }
    }

    private int solicitarEnteroEnRango(String mensajePrompt, int min, int max) {
        while (true) {
            int valor = solicitarEntero(mensajePrompt);
            if (valor >= min && valor <= max) {
                return valor;
            }
            System.out.printf("Por favor ingrese un valor dentro del rango permitido (%d a %d).%n", min, max);
        }
    }

    private String solicitarTextoRequerido(String mensajePrompt) {
        while (true) {
            System.out.print(mensajePrompt);
            String entrada = scanner.nextLine().trim();
            if (!entrada.isEmpty()) {
                return entrada;
            }
            System.out.println("El campo no puede quedar en blanco.");
        }
    }
}