import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class MatrizInversaDos {
    
    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        
        try {
            
            // Solicitar dimension de la matriz
            System.out.print("Ingresa la dimension de la matriz cuadrada: ");
            String inputDimension = reader.readLine();
            int n = Integer.parseInt(inputDimension);
            
            if (n <= 0) {
                System.out.println("Error: La dimension debe ser mayor a 0.");
                return;
            }
            
            // Crear matriz
            double[][] matriz = new double[n][n];
            
            System.out.println("\nIngresa los elementos de la matriz " + n + "x" + n + ":");
            System.out.println("(Ingresa los valores fila por fila, separados por espacios)");
            
            // Leer valores de la matriz
            for (int i = 0; i < n; i++) {
                boolean filaValida = false;
                while (!filaValida) {
                    System.out.print("Fila " + (i + 1) + ": ");
                    String inputFila = reader.readLine();
                    String[] valores = inputFila.trim().split("\\s+");
                    
                    if (valores.length != n) {
                        System.out.println("Error: Debes ingresar exactamente " + n + " valores.");
                        continue;
                    }
                    
                    try {
                        for (int j = 0; j < n; j++) {
                            matriz[i][j] = Double.parseDouble(valores[j]);
                        }
                        filaValida = true;
                    } catch (NumberFormatException e) {
                        System.out.println("Error: Ingresa solo numeros validos.");
                    }
                }
            }
            
            // Mostrar matriz ingresada
            System.out.println("\n La matriz que ingresaste es:");
            mostrarMatriz(matriz);
            
            // Calcular matriz inversa
            double[][] matrizInversa = calcularMatrizInversa(matriz);
            
            if (matrizInversa != null) {
                // Mostrar matriz inversa
                System.out.println("\nLa matriz inversa es:");
                mostrarMatriz(matrizInversa);
                
                // Verificar resultado
                System.out.println("\nVerificación");
                verificarInversa(matriz, matrizInversa);
            }
            
        } catch (NumberFormatException e) {
            System.err.println("Error: Debes ingresar un numero valido.");
        } catch (IOException e) {
            System.err.println("Error de entrada/salida: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                System.err.println("Error al cerrar BufferedReader: " + e.getMessage());
            }
        }
    }
    
    // Calcular matriz inversa usando el metodo de Gauss-Jordan
    private static double[][] calcularMatrizInversa(double[][] matriz) {
        int n = matriz.length;
        
        // Verificar si es cuadrada
        if (n != matriz[0].length) {
            System.out.println("Error: La matriz debe ser cuadrada para tener inversa");
            return null;
        }
        
        // Calcular determinante
        System.out.println("\nCalculando determinante");
        double determinante = calcularDeterminante(matriz);
        System.out.println("Determinante: " + determinante);
        
        if (Math.abs(determinante) < 1e-10) {
            System.out.println("Error: La matriz es singular (determinante ≈ 0), no tiene inversa");
            return null;
        }
        
        // Crear matriz aumentada [A|I]
        double[][] aumentada = new double[n][2 * n];
        
        // Llenar parte izquierda con la matriz original
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                aumentada[i][j] = matriz[i][j];
            }
        }
        
        // Llenar parte derecha con matriz identidad
        for (int i = 0; i < n; i++) {
            aumentada[i][n + i] = 1.0;
        }
        
        System.out.println("\nAplicando el método Gauss-Jordan");
        
        // Aplicar eliminacion de Gauss-Jordan
        for (int i = 0; i < n; i++) {
            System.out.println("\n--- Iteracion " + (i + 1) + " ---");
            
            // Buscar pivote maximo para estabilidad numerica
            int maxFila = i;
            for (int k = i + 1; k < n; k++) {
                if (Math.abs(aumentada[k][i]) > Math.abs(aumentada[maxFila][i])) {
                    maxFila = k;
                }
            }
            
            // Intercambiar filas si es necesario
            if (maxFila != i) {
                System.out.println("Intercambiando fila " + (i+1) + " con fila " + (maxFila+1));
                double[] temp = aumentada[i];
                aumentada[i] = aumentada[maxFila];
                aumentada[maxFila] = temp;
            }
            
            // Pivote
            double pivote = aumentada[i][i];
            System.out.println("Pivote: elemento [" + (i+1) + "," + (i+1) + "] = " + pivote);
            
            // Verificar si el pivote es cero
            if (Math.abs(pivote) < 1e-10) {
                System.out.println("Error: Pivote cero encontrado, la matriz es singular");
                return null;
            }
            
            // Hacer el pivote igual a 1
            for (int j = 0; j < 2 * n; j++) {
                aumentada[i][j] /= pivote;
            }
            
            System.out.println("Despues de hacer pivote = 1:");
            mostrarMatrizAumentada(aumentada, n);
            
            // Hacer ceros en otras filas
            for (int k = 0; k < n; k++) {
                if (k != i) {
                    double factor = aumentada[k][i];
                    System.out.println("Eliminando elemento en fila " + (k+1) + " usando factor: " + factor);
                    
                    for (int j = 0; j < 2 * n; j++) {
                        aumentada[k][j] -= factor * aumentada[i][j];
                    }
                    
                    System.out.println("Despues de eliminar fila " + (k+1) + ":");
                    mostrarMatrizAumentada(aumentada, n);
                }
            }
        }
        
        // Extraer la matriz inversa (parte derecha)
        double[][] inversa = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                inversa[i][j] = aumentada[i][n + j];
            }
        }
        
        System.out.println("¡Matriz inversa calculada exitosamente!");
        return inversa;
    }
    
    // Calcular determinante de forma recursiva
    private static double calcularDeterminante(double[][] matriz) {
        int n = matriz.length;
        
        // Caso base para matriz 1x1
        if (n == 1) {
            return matriz[0][0];
        }
        
        // Caso base para matriz 2x2
        if (n == 2) {
            return matriz[0][0] * matriz[1][1] - matriz[0][1] * matriz[1][0];
        }
        
        double det = 0;
        
        for (int j = 0; j < n; j++) {
            // Crear submatriz (n-1)x(n-1)
            double[][] submatriz = new double[n - 1][n - 1];
            
            for (int i = 1; i < n; i++) {
                int colIndex = 0;
                for (int k = 0; k < n; k++) {
                    if (k != j) {
                        submatriz[i - 1][colIndex++] = matriz[i][k];
                    }
                }
            }
            
            double signo = (j % 2 == 0) ? 1 : -1;
            double termino = signo * matriz[0][j] * calcularDeterminante(submatriz);
            det += termino;
        }
        
        return det;
    }
    
    // Mostrar matriz aumentada durante el proceso
    private static void mostrarMatrizAumentada(double[][] matriz, int n) {
        for (int i = 0; i < n; i++) {
            System.out.print("| ");
            for (int j = 0; j < n; j++) {
                System.out.printf("%8.4f ", matriz[i][j]);
            }
            System.out.print("| ");
            for (int j = n; j < 2 * n; j++) {
                System.out.printf("%8.4f ", matriz[i][j]);
            }
            System.out.println("|");
        }
        System.out.println();
    }
    
    // Mostrar matriz
    private static void mostrarMatriz(double[][] matriz) {
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                System.out.printf("%12.6f", matriz[i][j]);
            }
            System.out.println();
        }
    }
    
    // Verificar que A * A⁻¹ = I
    private static void verificarInversa(double[][] original, double[][] inversa) {
        int n = original.length;
        double[][] producto = new double[n][n];
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                producto[i][j] = 0;
                for (int k = 0; k < n; k++) {
                    producto[i][j] += original[i][k] * inversa[k][j];
                }
            }
        }
        
        // Mostrar resultado
        System.out.println("Resultado:");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                double valor = producto[i][j];
                // Redondear valores cercanos a 0 o 1
                if (Math.abs(valor) < 1e-10) valor = 0;
                if (Math.abs(valor - 1) < 1e-10) valor = 1;
                System.out.printf("%10.6f ", valor);
            }
            System.out.println();
        }
    }
}