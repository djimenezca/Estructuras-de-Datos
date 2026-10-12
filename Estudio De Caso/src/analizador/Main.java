package analizador;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    private static Scanner entrada = new Scanner(System.in);
    private static Analizador analizador = new Analizador();
    private static Pila pilaPrueba = new Pila();   //Pila para practicar push, pop y peek.
    private static String expresion = "";          

    public static void main(String[] args) {
        System.out.println("  ANALIZADOR DE EXPRESIONES ARITMÉTICAS CON PILAS");
        System.out.println("Operadores permitidos: +  -  *  /  y paréntesis ( )");

        menu();

        System.out.println("Programa finalizado.");
        entrada.close();
    }

    public static void menu() {
        int opcion;
        do {
            System.out.println();
            System.out.println("------------------- MENÚ PRINCIPAL -------------------");
            System.out.println("Expresión actual: " + (expresion.isEmpty() ? "(ninguna)" : expresion));
            System.out.println("1. Ingresar una expresión");
            System.out.println("2. Definir una variable");
            System.out.println("3. Ver variables");
            System.out.println("4. Ver los tokens de la expresión");
            System.out.println("5. Validar paréntesis (paso a paso)");
            System.out.println("6. Convertir a postfija (paso a paso)");
            System.out.println("7. Evaluar la expresión (paso a paso)");
            System.out.println("8. Practicar push, pop y peek");
            System.out.println("9. Cargar un ejemplo");
            System.out.println("0. Salir");
            opcion = leerEntero("Seleccione una opción: ");
            System.out.println();

            switch (opcion) {
                case 1:
                    ingresarExpresion();
                    break;
                case 2:
                    definirVariable();
                    break;
                case 3:
                    verVariables();
                    break;
                case 4:
                    verTokens();
                    break;
                case 5:
                    validarParentesis();
                    break;
                case 6:
                    convertirAPostfija();
                    break;
                case 7:
                    evaluarExpresion();
                    break;
                case 8:
                    menuPila();
                    break;
                case 9:
                    cargarEjemplo();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

    // Opciones del menú

    public static void ingresarExpresion() {
        System.out.print("Escriba la expresión (ejemplo: (a + 3) * b - 8 / 2): ");
        expresion = entrada.nextLine().trim();
        System.out.println("Expresión guardada.");
    }

    public static void definirVariable() {
        System.out.print("Nombre de la variable: ");
        String nombre = entrada.nextLine().trim();
        if (!analizador.esNombreValido(nombre)) {
            System.out.println("Nombre no válido: debe empezar con una letra y tener solo letras o dígitos.");
            return;
        }
        double valor = leerDecimal("Valor de " + nombre + ": ");
        analizador.definirVariable(nombre, valor);
        System.out.println("Variable guardada: " + nombre + " = " + analizador.formatear(valor));
    }

    public static void verVariables() {
        if (analizador.getVariables().isEmpty()) {
            System.out.println("No hay variables definidas.");
            return;
        }
        System.out.println("Variables definidas:");
        for (String nombre : analizador.getVariables().keySet()) {
            System.out.println("  " + nombre + " = " + analizador.formatear(analizador.getVariables().get(nombre)));
        }
    }

    public static void verTokens() {
        ArrayList<Nodo> tokens = obtenerTokens();
        if (tokens == null) {
            return;
        }
        System.out.println("Tokens de la expresión:");
        for (Nodo token : tokens) {
            System.out.println("  " + token);
        }
    }

    public static void validarParentesis() {
        ArrayList<Nodo> tokens = obtenerTokens();
        if (tokens == null) {
            return;
        }
        System.out.println("Validación de paréntesis:");
        if (analizador.validarParentesis(tokens, true)) {
            System.out.println("Los paréntesis están balanceados.");
        }
    }

    public static void convertirAPostfija() {
        ArrayList<Nodo> tokens = obtenerTokensValidados();
        if (tokens == null) {
            return;
        }
        System.out.println("Conversión de infija a postfija:");
        ArrayList<Nodo> postfija = analizador.convertirAPostfija(tokens, true);
        System.out.println("Infija:   " + expresion);
        System.out.println("Postfija: " + analizador.textoTokens(postfija));
    }

    public static void evaluarExpresion() {
        ArrayList<Nodo> tokens = obtenerTokensValidados();
        if (tokens == null) {
            return;
        }
        ArrayList<Nodo> postfija = analizador.convertirAPostfija(tokens, false);
        System.out.println("Evaluación de la postfija: " + analizador.textoTokens(postfija));
        Double resultado = analizador.evaluarPostfija(postfija, true);
        if (resultado != null) {
            System.out.println("Resultado: " + expresion + " = " + analizador.formatear(resultado));
        }
    }

    public static void cargarEjemplo() {
        analizador.definirVariable("a", 2);
        analizador.definirVariable("b", 5);
        expresion = "(a + 3) * b - 8 / 2";
        System.out.println("Ejemplo cargado: " + expresion + "   (a = 2, b = 5)");
    }

    //Submenú
    public static void menuPila() {
        int opcion;
        do {
            System.out.println();
            System.out.println("--------- PRACTICAR OPERACIONES DE LA PILA ---------");
            pilaPrueba.mostrarPila();
            System.out.println("1. push   2. pop   3. peek   4. vaciar   0. volver");
            opcion = leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1:
                    System.out.print("Valor a apilar: ");
                    String valor = entrada.nextLine().trim();
                    if (valor.isEmpty()) {
                        System.out.println("No escribió ningún valor.");
                    } else {
                        pilaPrueba.push(valor, "DATO");
                        System.out.println("push: se apiló " + valor);
                    }
                    break;
                case 2:
                    Nodo removido = pilaPrueba.pop();
                    System.out.println(removido == null ? "La pila está vacía." : "pop: se sacó " + removido.getValor());
                    break;
                case 3:
                    Nodo cima = pilaPrueba.peek();
                    System.out.println(cima == null ? "La pila está vacía." : "peek: la cima es " + cima.getValor());
                    break;
                case 4:
                    pilaPrueba.vaciar();
                    System.out.println("La pila se vació.");
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

    //Separa la expresión actual en tokens. Retorna null si no hay expresión o si tiene errores.
    public static ArrayList<Nodo> obtenerTokens() {
        if (expresion.isEmpty()) {
            System.out.println("Primero ingrese una expresión (opción 1) o cargue el ejemplo (opción 9).");
            return null;
        }
        return analizador.separarTokens(expresion);
    }

    //Igual que obtenerTokens(), pero revisa que los paréntesis estén balanceados.
    public static ArrayList<Nodo> obtenerTokensValidados() {
        ArrayList<Nodo> tokens = obtenerTokens();
        if (tokens == null || !analizador.validarParentesis(tokens, false)) {
            return null;
        }
        return tokens;
    }

    //Lee un número entero; si el usuario escribe otra cosa, lo vuelve a pedir.
    public static int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Integer.parseInt(entrada.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Debe escribir un número entero.");
            }
        }
    }

    //Lee un número decimal; si el usuario escribe otra cosa, lo vuelve a pedir.
    public static double leerDecimal(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Double.parseDouble(entrada.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Debe escribir un número (ejemplo: 4 o 2.5).");
            }
        }
    }
}
