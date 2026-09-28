/**
 * Estructura de datos enlazada para almacenar y consultar el historial 
 * de tickets procesados y resueltos.
 */
public class ListaResueltos {

    private Nodo cabeza;
    private int contadorElementos;

    public ListaResueltos() {
        this.cabeza = null;
        this.contadorElementos = 0;
    }

    /**
     * Verifica si el historial contiene elementos registrados.
     */
    public boolean estaVacia() {
        return cabeza == null;
    }

    /**
     * Retorna la cantidad de tickets archivados como resueltos.
     */
    public int getTamanio() {
        return contadorElementos;
    }

    /**
     * Inserta un ticket resuelto al principio de la cadena enlazada (LIFO).
     */
    public void agregarAlInicio(Ticket ticket) {
        Nodo nuevoNodo = new Nodo(ticket);
        nuevoNodo.setSiguiente(cabeza);
        cabeza = nuevoNodo;
        contadorElementos++;
    }

    /**
     * Recorre la lista linealmente buscando la primera coincidencia del ID.
     * 
     * @param id Identificador único del ticket a localizar.
     * @return El objeto Ticket si existe en el historial; de lo contrario, null.
     */
    public Ticket buscarPorId(int id) {
        for (Nodo aux = cabeza; aux != null; aux = aux.getSiguiente()) {
            if (aux.getTicket().getId() == id) {
                return aux.getTicket();
            }
        }
        return null;
    }

    /**
     * Comprueba si un ticket con el ID especificado existe en la lista.
     */
    public boolean contieneTicket(int id) {
        return buscarPorId(id) != null;
    }
}