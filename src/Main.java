import java.io.File;

public class Main {
    public void main() {
        String ruta = IO.readln("Escribe el nombre del fichero de datos: ");

        if (verificarArchivoExiste(ruta)) {
            boolean salir = false;
            do {
                IO.println("""
                        ========== MENÚ PRINCIPAL ==========
                        1. Añadir usuario
                        2. Mostrar usuarios introducidos
                        3. Generar fichero de concordancias
                        5. Salir
                        ====================================
                        Seleccione una opción:\s""");

                int opcion = Integer.parseInt(IO.readln());
                switch (opcion) {
                    case 1 -> anadirUsuario(ruta);
                    case 2 -> mostrarUsuarios(ruta);
                    case 3 -> generarConcordancias(ruta);
                    case 5 -> salir = true;
                    default -> IO.println("Opcion no valida");
                }
            } while (!salir);
        } else {
            IO.println("ERROR: El archivo no es accesible o no existe.");
        }
    }

    boolean verificarArchivoExiste(String ruta) {
        File archivo = new File(ruta);
        return archivo.exists();
    }

    public int pedirNumeroUsuarioControlErrores() {
        int numero = 0;
        do {
            try {
                numero = Integer.parseInt(IO.readln());
            } catch (Exception enrique) {
                IO.println("ERROR: Numero no valido");
            }
        } while (numero == 0);
        return numero;
    }

    int obtenerCodigoSugerido(String ruta) {

    }

    public void validarCodigo(String ruta, int codigo) {
    }

    public void anadirUsuario(String ruta) {
        IO.println("Introduce el codigo del nuevo usuario (" + "Codigo sugerido: " + obtenerCodigoSugerido(ruta) + " )");
        int codigo = pedirNumeroUsuarioControlErrores();
    }

    public void mostrarUsuarios(String ruta) {

    }

    public void generarConcordancias(String ruta) {

    }
}
