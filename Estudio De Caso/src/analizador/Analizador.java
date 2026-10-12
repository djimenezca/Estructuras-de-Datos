package analizador;

import java.util.ArrayList;
import java.util.HashMap;

public class Analizador {

    // Clase que analiza y evalúa expresiones aritméticas usando pilas.
    // El análisis se hace en cuatro pasos:
    // 1. separarTokens(): divide la expresión en tokens.
    // 2. validarParentesis(): revisa con una pila que los paréntesis estén
    // balanceados.
    // 3. convertirAPostfija(): pasa la expresión de infija a postfija con una pila
    // de operadores.
    // 4. evaluarPostfija(): calcula el resultado con una pila de operandos.

    // Atributos.
    // Guarda las variables definidas por el usuario: nombre -> valor (ejemplo: "a"
    // -> 2.0).
    private HashMap<String, Double> variables;

    public Analizador() {
        variables = new HashMap<>();
    }

    public HashMap<String, Double> getVariables() {
        return variables;
    }

    // Variables
    public void definirVariable(String nombre, double valor) {
        variables.put(nombre, valor);
    }

    // Un nombre de variable es válido si empieza con una letra y luego tiene solo
    // letras o dígitos.
    public boolean esNombreValido(String nombre) {
        if (nombre.isEmpty() || !Character.isLetter(nombre.charAt(0))) {
            return false;
        }
        for (int i = 1; i < nombre.length(); i++) {
            if (!Character.isLetterOrDigit(nombre.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    // Paso 1: separar la expresión en tokens

    // Recorre la expresión carácter por carácter y crea un nodo por cada token.
    // Retorna null si encuentra un carácter que no es válido.
    public ArrayList<Nodo> separarTokens(String expresion) {
        ArrayList<Nodo> tokens = new ArrayList<>();
        int i = 0;

        while (i < expresion.length()) {
            char caracter = expresion.charAt(i);

            if (caracter == ' ') {
                i++;

            } else if (Character.isDigit(caracter)) {
                // Se leen todos los dígitos seguidos (y un punto decimal si lo tiene).
                String numero = "";
                while (i < expresion.length()
                        && (Character.isDigit(expresion.charAt(i)) || expresion.charAt(i) == '.')) {
                    numero += expresion.charAt(i);
                    i++;
                }
                if (numero.indexOf('.') != numero.lastIndexOf('.')) {
                    System.out.println("Error: el número '" + numero + "' tiene más de un punto decimal.");
                    return null;
                }
                tokens.add(new Nodo(numero, "NUMERO"));

            } else if (Character.isLetter(caracter)) {
                // Se leen todas las letras y dígitos seguidos (ejemplo: precio, x1).
                String nombre = "";
                while (i < expresion.length() && Character.isLetterOrDigit(expresion.charAt(i))) {
                    nombre += expresion.charAt(i);
                    i++;
                }
                tokens.add(new Nodo(nombre, "VARIABLE"));

            } else if (esOperador(String.valueOf(caracter))) {
                tokens.add(new Nodo(String.valueOf(caracter), "OPERADOR"));
                i++;

            } else if (caracter == '(' || caracter == ')') {
                tokens.add(new Nodo(String.valueOf(caracter), "PARENTESIS"));
                i++;

            } else {
                System.out.println("Error: el carácter '" + caracter + "' no es válido.");
                return null;
            }
        }
        return tokens;
    }

    // Paso 2: validar los paréntesis

    // Cada "(" se apila y cada ")" desapila un "(".
    // Si llega un ")" con la pila vacía, o si al final quedan "(" en la pila, hay
    // un error.
    public boolean validarParentesis(ArrayList<Nodo> tokens, boolean mostrarPasos) {
        Pila pila = new Pila();

        for (Nodo token : tokens) {
            if (token.getValor().equals("(")) {
                pila.push("(", "PARENTESIS");
                if (mostrarPasos) {
                    System.out.println("Token: (   ->  push      Pila: " + pila.contenido());
                }
            } else if (token.getValor().equals(")")) {
                if (pila.estaVacia()) {
                    System.out.println("Error: hay un ')' que no tiene su '(' correspondiente.");
                    return false;
                }
                pila.pop();
                if (mostrarPasos) {
                    System.out.println("Token: )   ->  pop       Pila: " + pila.contenido());
                }
            }
        }

        if (!pila.estaVacia()) {
            System.out.println("Error: hay " + pila.getTamanio() + " '(' sin cerrar.");
            return false;
        }
        return true;
    }

    // Paso 3: convertir de infija a postfija

    // Algoritmo shunting yard:
    // - Los números y variables pasan directo a la salida.
    // - "(" se apila.
    // - ")" saca operadores de la pila hasta encontrar el "(", que se descarta.
    // - Un operador saca de la pila los operadores con precedencia mayor o igual, y
    // luego se apila.
    // - Al final se sacan todos los operadores que queden en la pila.
    // Antes de llamar este método se deben validar los paréntesis con
    // validarParentesis().
    public ArrayList<Nodo> convertirAPostfija(ArrayList<Nodo> tokens, boolean mostrarPasos) {
        ArrayList<Nodo> salida = new ArrayList<>();
        Pila pilaOperadores = new Pila();

        for (Nodo token : tokens) {
            String valor = token.getValor();
            String tipo = token.getTipo();

            if (tipo.equals("NUMERO") || tipo.equals("VARIABLE")) {
                salida.add(token);

            } else if (valor.equals("(")) {
                pilaOperadores.push(valor, tipo);

            } else if (valor.equals(")")) {
                while (!pilaOperadores.peek().getValor().equals("(")) {
                    salida.add(pilaOperadores.pop());
                }
                pilaOperadores.pop();

            } else {

                while (!pilaOperadores.estaVacia()
                        && esOperador(pilaOperadores.peek().getValor())
                        && precedencia(pilaOperadores.peek().getValor()) >= precedencia(valor)) {
                    salida.add(pilaOperadores.pop());
                }
                pilaOperadores.push(valor, tipo);
            }

            if (mostrarPasos) {
                System.out.printf("Token: %-4s Pila: %-14s Salida: %s%n",
                        valor, pilaOperadores.contenido(), textoTokens(salida));
            }
        }

        while (!pilaOperadores.estaVacia()) {
            salida.add(pilaOperadores.pop());
            if (mostrarPasos) {
                System.out.printf("Fin        Pila: %-14s Salida: %s%n",
                        pilaOperadores.contenido(), textoTokens(salida));
            }
        }
        return salida;
    }

    // Paso 4: evaluar la expresión postfija

    // Los números se apilan. Cada operador saca dos números, calcula y apila el
    // resultado.
    // Al final debe quedar el resultado en la fila.
    // Retorna null si hay un error (variable no definida, división entre cero,
    // etc.).
    public Double evaluarPostfija(ArrayList<Nodo> postfija, boolean mostrarPasos) {
        Pila pilaOperandos = new Pila();

        for (Nodo token : postfija) {
            String valor = token.getValor();
            String tipo = token.getTipo();

            if (tipo.equals("NUMERO")) {
                pilaOperandos.push(valor, "NUMERO");

            } else if (tipo.equals("VARIABLE")) {
                if (!variables.containsKey(valor)) {
                    System.out.println("Error: la variable '" + valor + "' no está definida.");
                    return null;
                }
                pilaOperandos.push(formatear(variables.get(valor)), "NUMERO");

            } else {
                Nodo derecho = pilaOperandos.pop();
                Nodo izquierdo = pilaOperandos.pop();
                if (derecho == null || izquierdo == null) {
                    System.out.println("Error: faltan números para el operador '" + valor + "'.");
                    return null;
                }
                double b = Double.parseDouble(derecho.getValor());
                double a = Double.parseDouble(izquierdo.getValor());

                if (valor.equals("/") && b == 0) {
                    System.out.println("Error: división entre cero.");
                    return null;
                }
                double resultado = calcular(a, valor, b);
                pilaOperandos.push(formatear(resultado), "NUMERO");
            }

            if (mostrarPasos) {
                System.out.printf("Token: %-4s Pila: %s%n", valor, pilaOperandos.contenido());
            }
        }

        if (pilaOperandos.getTamanio() != 1) {
            System.out.println("Error: la expresión está mal escrita (sobran o faltan números).");
            return null;
        }
        return Double.parseDouble(pilaOperandos.pop().getValor());
    }

    // Métodos auxiliares

    public boolean esOperador(String valor) {
        return valor.equals("+") || valor.equals("-") || valor.equals("*") || valor.equals("/");
    }

    public int precedencia(String operador) {
        if (operador.equals("*") || operador.equals("/")) {
            return 2;
        }
        if (operador.equals("+") || operador.equals("-")) {
            return 1;
        }
        return 0;
    }

    public double calcular(double a, String operador, double b) {
        switch (operador) {
            case "+":
                return a + b;
            case "-":
                return a - b;
            case "*":
                return a * b;
            default:
                return a / b;
        }
    }

    public String textoTokens(ArrayList<Nodo> tokens) {
        String texto = "";
        for (Nodo token : tokens) {
            texto += token.getValor() + " ";
        }
        return texto.trim();
    }

    public String formatear(double numero) {
        if (numero == (long) numero) {
            return String.valueOf((long) numero);
        }
        return String.valueOf(numero);
    }
}
