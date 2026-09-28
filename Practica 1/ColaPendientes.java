/**
 * Estructura de cola con prioridad para gestionar solicitudes pendientes.
 * Los tickets se posicionan automáticamente en orden ascendente por su ID,
 * garantizando que las solicitudes más antiguas (menor ID) se atiendan primero.
 */
public class ColaPendientes {

    private Nodo primero;
    private int cantidadRegistros;

    public ColaPendientes() {
        this.primero = null;
        this.cantidadRegistros = 0;
    }

    /**
     * Evalúa si existen solicitudes en espera.
     */
    public boolean estaVacia() {
        return primero == null;
    }

    /**
     * Retorna la cantidad total de solicitudes en cola.
     */
    public int getTamanio() {
        return cantidadRegistros;
    }

    /**
     * Inserta un nuevo ticket manteniendo el orden de prioridad según el ID (menor a mayor).
     * 
     * @param nuevoTicket Instancia de Ticket a encolar.
     */
    public void agregar(Ticket nuevoTicket) {
        Nodo nuevoNodo = new Nodo(nuevoTicket);

        // Caso 1: La cola está vacía o el nuevo ticket tiene menor ID que el primero actual
        if (estaVacia() || nuevoTicket.getId() < primero.getTicket().getId()) {
            nuevoNodo.setSiguiente(primero);
            primero = nuevoNodo;
        } else {
            // Caso 2: Recorrido para ubicar la posición correcta según prioridad
            Nodo rastreador = primero;
            while (rastreador.getSiguiente() != null 
                    && rastreador.getSiguiente().getTicket().getId() <= nuevoTicket.getId()) {
                rastreador = rastreador.getSiguiente();
            }
            
            nuevoNodo.setSiguiente(rastreador.getSiguiente());
            rastreador.setSiguiente(nuevoNodo);
        }
        
        cantidadRegistros++;
    }

    /**
     * Inspecciona el ticket prioritario al frente de la cola sin removerlo.
     * 
     * @return El Ticket con menor ID o null si la cola está vacía.
     */
    public Ticket verFrente() {
        return estaVacia() ? null : primero.getTicket();
    }

    /**
     * Extrae y retorna el ticket prioritario al frente de la cola.
     * 
     * @return El Ticket removido o null si no hay pendientes.
     */
    public Ticket sacarFrente() {
        if (estaVacia()) {
            return null;
        }

        Ticket ticketAtendido = primero.getTicket();
        primero = primero.getSiguiente();
        cantidadRegistros--;

        return ticketAtendido;
    }
}