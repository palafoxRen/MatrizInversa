import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.File;

public class MatrizInversa {
    
    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        
        try {
            
            // Solicitar dimension de la matriz
            System.out.print("Ingresa el numero de filas de la matriz: ");
            int filas = Integer.parseInt(reader.readLine());
            
            System.out.print("Ingresa el numero de columnas de la matriz: ");
            int columnas = Integer.parseInt(reader.readLine());
            
            if (filas <= 0 || columnas <= 0) {
                System.out.println("Error: Las dimensiones deben ser mayores a 0.");
                return;
            }
            
            // Crear matriz
            int[][] matriz = new int[filas][columnas];
            
            System.out.println("\nIngresa los elementos de la matriz " + filas + "x" + columnas + ":");
            System.out.println("(Ingresa los valores fila por fila, separados por espacios)");
            
            // Leer valores de la matriz
            for (int i = 0; i < filas; i++) {
                boolean filaValida = false;
                while (!filaValida) {
                    System.out.print("Fila " + (i + 1) + ": ");
                    String inputFila = reader.readLine();
                    String[] valores = inputFila.trim().split("\\s+");
                    
                    if (valores.length != columnas) {
                        System.out.println("Error: Debes ingresar exactamente " + columnas + " valores.");
                        continue;
                    }
                    
                    try {
                        for (int j = 0; j < columnas; j++) {
                            matriz[i][j] = Integer.parseInt(valores[j]);
                        }
                        filaValida = true;
                    } catch (NumberFormatException e) {
                        System.out.println("Error: Ingresa solo numeros enteros.");
                    }
                }
            }
            
            // Mostrar matriz original
            mostrarMatriz(matriz);
            
            // Rotar 180 grados
            int[][] matrizRotada = rotacion180Grados(matriz);
            
            // Mostrar matriz rotada
            mostrarMatriz(matrizRotada);
            
            // Mostrar proceso detallado
            mostrarProcesoRotacion(matriz, matrizRotada);
            
            // Opcion para guardar en archivo
            System.out.print("\n¿Deseas guardar la matriz rotada en un archivo? (s/n): ");
            String guardar = reader.readLine().trim().toLowerCase();
            
            if (guardar.equals("s") || guardar.equals("si")) {
                System.out.print("Ingresa el nombre del archivo (sin extension): ");
                String nombreArchivo = reader.readLine().trim();
                
                if (nombreArchivo.isEmpty()) {
                    nombreArchivo = "matriz_rotada_180_" + filas + "x" + columnas;
                } else {
                    nombreArchivo += ".txt";
                }
                
                String rutaCompleta = "C:/Archivos/" + nombreArchivo;
                guardarMatrizEnArchivo(matrizRotada, rutaCompleta);
                System.out.println("Matriz rotada guardada en: " + rutaCompleta);
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
    
    // Rotacion 180 grados
    private static int[][] rotacion180Grados(int[][] matriz) {
        int filas = matriz.length;
        int columnas = matriz[0].length;
        int[][] rotada = new int[filas][columnas];
        
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                rotada[i][j] = matriz[filas - 1 - i][columnas - 1 - j];
            }
        }
        
        return rotada;
    }
    
    // Mostrar proceso detallado de rotacion
    private static void mostrarProcesoRotacion(int[][] original, int[][] rotada) {
        int filas = original.length;
        int columnas = original[0].length;
        
        System.out.println("Formula: rotada[i][j] = original[" + (filas-1) + " - i][" + (columnas-1) + " - j]");
        System.out.println("Proceso de mapeo de posiciones:");
        
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                int nuevaFila = filas - 1 - i;
                int nuevaColumna = columnas - 1 - j;
                System.out.printf("  original[%d][%d] = %d", i, j, original[i][j]);
                System.out.printf(" -> rotada[%d][%d] = %d%n", 
                    nuevaFila, nuevaColumna, rotada[nuevaFila][nuevaColumna]);
            }
        }
        
        // Mostrar ejemplo visual de algunas posiciones
        System.out.println("\nEjemplos visuales:");
        if (filas >= 2 && columnas >= 2) {
            System.out.printf("  Esquina superior izquierda: original[0][0] = %d -> rotada[%d][%d] = %d%n",
                original[0][0], filas-1, columnas-1, rotada[filas-1][columnas-1]);
            System.out.printf("  Esquina superior derecha: original[0][%d] = %d -> rotada[%d][0] = %d%n",
                columnas-1, original[0][columnas-1], filas-1, rotada[filas-1][0]);
            System.out.printf("  Esquina inferior izquierda: original[%d][0] = %d -> rotada[0][%d] = %d%n",
                filas-1, original[filas-1][0], columnas-1, rotada[0][columnas-1]);
            System.out.printf("  Esquina inferior derecha: original[%d][%d] = %d -> rotada[0][0] = %d%n",
                filas-1, columnas-1, original[filas-1][columnas-1], rotada[0][0]);
        }
    }
    
    // Mostrar matriz
    private static void mostrarMatriz(int[][] matriz) {
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                System.out.printf("%4d", matriz[i][j]);
            }
            System.out.println();
        }
    }
    
    // Guardar matriz en archivo
    private static void guardarMatrizEnArchivo(int[][] matriz, String rutaArchivo) throws IOException {
        // Crear directorio si no existe
        File directorio = new File("C:/Archivos/");
        if (!directorio.exists()) {
            directorio.mkdirs();
        }
        
        BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo));
        writer.write("Matriz Rotada 180 Grados - " + matriz.length + "x" + matriz[0].length);
        writer.newLine();
        writer.write("==================================");
        writer.newLine();
        
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                writer.write(String.format("%4d", matriz[i][j]));
                if (j < matriz[i].length - 1) {
                    writer.write(" ");
                }
            }
            writer.newLine();
        }
        writer.close();
    }
}