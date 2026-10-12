package analizador;

public class Nodo {

    private String valor;
    private String tipo;
    private Nodo siguiente;


    public Nodo(String valorNodo, String tipoNodo) {
        valor = valorNodo;
        tipo = tipoNodo;
        siguiente = null;
    }

    public String getValor() {
        return valor;
    }

    public String getTipo() {
        return tipo;
    }

    public Nodo getSiguiente() {
        return siguiente;
    }

    //Setters.
    public void setValor(String nuevoValor) {
        valor = nuevoValor;
    }

    public void setTipo(String nuevoTipo) {
        tipo = nuevoTipo;
    }

    public void setSiguiente(Nodo nuevoSiguiente) {
        siguiente = nuevoSiguiente;
    }

    @Override
    public String toString() {
        return valor + " (" + tipo + ")";
    }
}
