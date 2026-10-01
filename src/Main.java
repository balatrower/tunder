import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

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
                        4. Salir
                        ====================================
                        Seleccione una opción:\s""");

                int opcion = Integer.parseInt(IO.readln());
                switch (opcion) {
                    case 1 -> anadirUsuario(ruta);
                    case 2 -> mostrarUsuarios(ruta);
                    case 3 -> generarConcordancias(ruta);
                    case 4 -> salir = true;
                    default -> IO.println("Opcion no valida");
                }
            } while (!salir);
        } else {
            IO.println("ERROR: El archivo de usuarios no es accesible o no existe.");
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

    public boolean hayHuecosEnLaListaDeCodigos(ArrayList<Integer> listaCodigos) {
        boolean huecos = false;
        for (int i = 1; i < listaCodigos.size(); i++) {
            if (listaCodigos.get(i) != listaCodigos.get(i - 1) + 1) { //verificar que siguen un orden 100,101,102,103,...
                huecos = true;
                return huecos;
            }
        }
        return huecos;
    }

    ArrayList<Integer> obtenerListaCodigos(String ruta) {
        ArrayList<Integer> listaCodigos = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea = "";
            while ((linea = br.readLine()) != null) {
                int numeroCodigo = Integer.parseInt(linea.substring(1,4)); //sacar numeral del codigo
                listaCodigos.add(numeroCodigo);
            }
        } catch(Exception enrique) {
            IO.println("ERROR: se ha producido un error al intentar obtener el codigo sugerido.");
        }
        return listaCodigos;
    }

    int obtenerCodigoSugerido(String ruta) {
        ArrayList<Integer> listaCodigos = obtenerListaCodigos(ruta);
        if (!hayHuecosEnLaListaDeCodigos(listaCodigos)) {
            return listaCodigos.getLast() + 1; //ya que el ultimo ya esta pillado entocnes el + 1 que esta libre
        } else {
            int codigoHuecoDisponible = 0;
            for (int i = 1; i < listaCodigos.size() + 1; i++) {
                if (listaCodigos.get(i) != listaCodigos.get(i - 1) + 1) {
                    codigoHuecoDisponible = listaCodigos.get(i - 1);
                }
            }
            return codigoHuecoDisponible;
        }
    }

    public boolean esValidoElCodigo(String ruta, int codigoAComprobar) {
        ArrayList<Integer> listaCodigos = obtenerListaCodigos(ruta);

        for (Integer codigoDeLista : listaCodigos) {
            if (codigoDeLista == codigoAComprobar) {
                return false;
            }
        }
        return true;
    }

    public String[] eliminarAficionesRepetidas(String[] aficiones) {
        ArrayList<String> unicas = new ArrayList<>();
        for (String aficion : aficiones) {
            boolean esUnica = true;
            for (String aficionUnica : unicas) {
                if (aficion.equals(aficionUnica)) {
                    esUnica = false;
                    break; //salir antes si se sabe que no es unica
                }
            }

            if (esUnica) {
                unicas.add(aficion);
            }
        }

        return unicas.toArray(new String[0]);
    }

    public void anadirUsuario(String ruta) {
        IO.println("Introduce el codigo del nuevo usuario (" + "Codigo sugerido: " + obtenerCodigoSugerido(ruta) + ")");

        int codigo = 0;
        boolean codigoValido = false;
        do {
            codigo = pedirNumeroUsuarioControlErrores();

            if (esValidoElCodigo(ruta, codigo)) {
                codigoValido = true;
                String[] aficiones = IO.readln("Introduce ahora las aficiones del usuario: ").toUpperCase().split(" ");
                aficiones = eliminarAficionesRepetidas(aficiones);
                try (FileWriter fileWriter = new FileWriter(ruta, true)) {
                    fileWriter.write("\n"); //el caracter de final de linea va primero

                    fileWriter.write("U" + codigo);

                    for (String aficion : aficiones) {
                            fileWriter.write(" " + aficion);
                    }
                } catch (IOException enrique) {
                    IO.println("ERROR: fallo al intentar escribir en el fichero");
                }
            } else {
                IO.println("ERROR: Codigo no valido, por favor introduzca uno nuevo");
            }
        } while (!codigoValido);
    }

    public void mostrarUsuarios(String ruta) {

    }

    public void generarConcordancias(String ruta) {

    }
}
