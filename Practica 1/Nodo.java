/**
 * Nodo, se utiliza en la cola y en la lista enlzada.
 * Guarda el ticket y despues la referencia al nodo.
 */
public class Nodo {

    private final Ticket ticket;
    private Nodo siguiente;

    public Nodo(Ticket ticket) {
        this.ticket = ticket;
        this.siguiente = null;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public Nodo getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(Nodo siguiente) {
        this.siguiente = siguiente;
    }
}
