import java.util.Scanner;

/**
 * Inicio del programa, pagina principal de ejecucion. Se inicia el menu para correr el sistema
 */
public class Main {

    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in); // Lee lo que ingresa el usuario al sistema

        Menu menu = new Menu(teclado);
        menu.iniciar(); // Mantiene el programa funcionando hasta que se escoja la opcion 3 "Salir"

        teclado.close();
    }
}
