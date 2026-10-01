import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public void main() {
        boolean salir = false;
        do {
            String ruta = IO.readln("Escribe el nombre del fichero de datos: ");

            if (verificarArchivoEsValido(ruta)) {
                do {
                    IO.println("""
                        ========== MENÚ PRINCIPAL ==========
                        1. Añadir usuario
                        2. Mostrar usuarios introducidos
                        3. Generar fichero de concordancias
                        4. Salir
                        ====================================
                        Seleccione una opción:\s""");

                    int opcion = pedirNumeroUsuarioControlErrores();
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
        } while (!salir);
    }

    boolean verificarArchivoEsValido(String ruta) {
        File archivo = new File(ruta);
        if (archivo.exists()) {
            return archivo.length() <= 10000; //tamaño en bytes (length devuelve tamaño en bytes)
        } else {
            return false;
        }
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

                //verificar si hay alguna aficion si quiera
                boolean aficionesValidas = false;
                String[] aficiones = new String[0];
                do {
                    String lineaAficiones = IO.readln("Introduce ahora las aficiones del usuario: ").toUpperCase().trim();

                    if (!lineaAficiones.isEmpty()) {
                        aficiones = lineaAficiones.split(" "); //solo split si hay aficiones
                        aficionesValidas = true;
                    } else {
                        IO.println("ERROR: Se debe introducir al menos una aficion.");
                    }
                } while (!aficionesValidas);

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
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(ruta))) {
            while (bufferedReader.ready()) {
                String linea = bufferedReader.readLine();
                String codigoUsuario = linea.substring(0,4); //sacar primero el codigo de usuario
                linea = linea.substring(5); //sacar solo las aficiones
                System.out.printf("Usuario: %s, Aficiones: %s\n", codigoUsuario, linea);
            }
        } catch (IOException enrique) {
            IO.println("ERROR: ha ocurrido un error al leer el archivo");
        }
        IO.println(); //para dejar un espacio y que quede mejor
    }

    public String[] obtenerListaAficiones(String ruta) {
        ArrayList<String> listaAficiones = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea = "";
            while ((linea = br.readLine()) != null) {
                String aficiones = (linea.substring(5)); //sacar las aficiones
                listaAficiones.add(aficiones);
            }
        } catch(Exception enrique) {
            IO.println("ERROR: se ha producido un error al intentar obtener las aficiones de los usuarios.");
        }

        return listaAficiones.toArray(new String[0]);
    }

    public int pedirMinimoConcordancias() {
        int minConcordancias = 0;
        boolean valido = false;
        do {
            try {
                minConcordancias = Integer.parseInt(IO.readln("Introduce el numero minimo de aficiones comunes (>= 1): "));
                if (minConcordancias >= 1) {
                    valido = true;
                } else {
                    IO.println("ERROR: El numero debe ser mayor o igual a 1.");
                }
            } catch (Exception enrique) {
                IO.println("ERROR: Numero no valido.");
            }
        } while (!valido);
        return minConcordancias;
    }

    public ArrayList<String> obtenerAficionesComunes(String[] aficiones1, String[] aficiones2) {
        ArrayList<String> comunes = new ArrayList<>();
        for (String aficion1 : aficiones1) {
            for (String aficion2 : aficiones2) {
                if (!aficion1.isEmpty() && aficion1.equals(aficion2)) {
                    comunes.add(aficion1);
                }
            }
        }
        return comunes;
    }

    public String[][] buscarParejasConcordantes(ArrayList<Integer> codigos, String[] aficionesArray, int minConcordancias) {
        int numUsuarios = codigos.size();
        int maxParejas = (numUsuarios * (numUsuarios - 1)) / 2;
        String[][] parejas = new String[maxParejas][4]; //[0] = usuario1, [1] = usuario2, [2] = aficiones comunes, [3] = cantidad
        int parejasEncontradas = 0;

        for (int i = 0; i < numUsuarios; i++) {
            for (int j = i + 1; j < numUsuarios; j++) {
                String[] aficiones1 = aficionesArray[i].split(" ");
                String[] aficiones2 = aficionesArray[j].split(" ");

                ArrayList<String> comunes = obtenerAficionesComunes(aficiones1, aficiones2);

                if (comunes.size() >= minConcordancias) {
                    comunes.sort(String::compareTo); //ordenar alfabeticamente
                    String aficionesComunesStr = String.join(" ", comunes);

                    parejas[parejasEncontradas][0] = "U" + codigos.get(i);
                    parejas[parejasEncontradas][1] = "U" + codigos.get(j);
                    parejas[parejasEncontradas][2] = aficionesComunesStr;
                    parejas[parejasEncontradas][3] = String.valueOf(comunes.size());
                    parejasEncontradas++;
                }
            }
        }

        String[][] parejasValidas = new String[parejasEncontradas][4];
        for (int i = 0; i < parejasEncontradas; i++) {
            parejasValidas[i] = parejas[i];
        }
        return parejasValidas;
    }

    public void ordenarParejasPorCantidadAficiones(String[][] parejasValidas) {
        Arrays.sort(parejasValidas, (p1, p2) -> {
            int cantidad1 = Integer.parseInt(p1[3]);
            int cantidad2 = Integer.parseInt(p2[3]);
            return Integer.compare(cantidad2, cantidad1); //ordenar descendientemente
        });
    }

    public void guardarParejasEnFichero(String[][] parejasValidas) {
        try (FileWriter fileWriter = new FileWriter("concordancias.txt", false)) { //false para que el archivo se reinicie si se llama varias veces
            for (int i = 0; i < parejasValidas.length; i++) {
                fileWriter.write(parejasValidas[i][0] + " " + parejasValidas[i][1] + " " + parejasValidas[i][2] + "\n");
            }
            IO.println("Fichero de concordancias generado exitosamente.");
            IO.println("Numero de parejas encontradas: " + parejasValidas.length);
        } catch (IOException enrique) {
            IO.println("ERROR: No se ha podido generar el fichero de concordancias.");
        }
    }

    public void generarConcordancias(String ruta) {
        int minConcordancias = pedirMinimoConcordancias();

        ArrayList<Integer> codigos = obtenerListaCodigos(ruta);
        String[] aficionesArray = obtenerListaAficiones(ruta);

        if (codigos.isEmpty() || aficionesArray.length == 0 || codigos.size() != aficionesArray.length) {
            IO.println("ERROR: No se han podido cargar los datos de los usuarios correctamente.");
            return;
        }

        String[][] parejasValidas = buscarParejasConcordantes(codigos, aficionesArray, minConcordancias);

        if (parejasValidas.length == 0) {
            IO.println("No hay parejas de usuarios que tengan al menos " + minConcordancias + " aficiones comunes.");
            return;
        }

        ordenarParejasPorCantidadAficiones(parejasValidas);
        guardarParejasEnFichero(parejasValidas);
    }
}
