package analizador;

public class Pila {

    //Clase para la pila dinámica de tokens.
    //Está construida como una lista enlazada simple en la que todo se inserta y se remueve
    //por el inicio, que en una pila se llama "cima". Así se cumple el principio LIFO:
    //el último nodo que entra es el primero que sale.

    private Nodo cima;
    private int tamanio;

    public Pila() {
        cima = null;
        tamanio = 0;
    }

    public int getTamanio() {
        return tamanio;
    }

    //Inserta un nodo nuevo en la cima de la pila (igual que insertarNodoInicio() de la lista enlazada).
    public void push(String valor, String tipo) {
        Nodo nuevoNodo = new Nodo(valor, tipo);
        nuevoNodo.setSiguiente(cima);
        cima = nuevoNodo;
        tamanio++;
    }

    //Remueve el nodo de la cima y lo retorna. Si la pila está vacía retorna null.
    public Nodo pop() {
        if (estaVacia()) {
            return null;
        }
        Nodo nodoRemovido = cima;
        cima = cima.getSiguiente(); 
        tamanio--;
        return nodoRemovido;
    }

    //Retorna el nodo de la cima sin removerlo. Si la pila está vacía retorna null.
    public Nodo peek() {
        if (estaVacia()) {
            return null;
        }
        return cima;
    }

    public boolean estaVacia() {
        return cima == null;
    }

    public void vaciar() {
        cima = null;
        tamanio = 0;
    }

    //Retorna el contenido de la pila en una línea, desde la base hasta la cima.
    public String contenido() {
        if (estaVacia()) {
            return "[ ]";
        }
        String texto = "";
        Nodo nodoActual = cima;
        while (nodoActual != null) {
            texto = nodoActual.getValor() + " " + texto;
            nodoActual = nodoActual.getSiguiente();
        }
        return "[ " + texto + "]";
    }

    //Imprime la pila de forma vertical, con la cima arriba.
    public void mostrarPila() {
        if (estaVacia()) {
            System.out.println("   (la pila está vacía)");
            return;
        }
        Nodo nodoActual = cima;
        while (nodoActual != null) {
            String marca = (nodoActual == cima) ? "  <- cima" : "";
            System.out.printf("   | %-6s |%s%n", nodoActual.getValor(), marca);
            nodoActual = nodoActual.getSiguiente();
        }
        System.out.println("   +--------+");
    }
}
