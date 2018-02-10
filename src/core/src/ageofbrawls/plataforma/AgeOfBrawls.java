package ageofbrawls.plataforma;

import ageofbrawls.z.excepciones.AccionRestringida.ExcepcionAccionRestringida;
import ageofbrawls.z.excepciones.AccionRestringida.ExcepcionAccionRestringidaEdificio;
import ageofbrawls.z.excepciones.AccionRestringida.ExcepcionAccionRestringidaGrupo;
import ageofbrawls.z.excepciones.AccionRestringida.ExcepcionAccionRestringidaPersonaje;
import ageofbrawls.z.excepciones.Argumentos.ExcepcionArgumentos;
import ageofbrawls.z.excepciones.Argumentos.ExcepcionArgumentosInternos;
import ageofbrawls.z.excepciones.Argumentos.ExcepcionArgumentosValoresIncorrectos;
import ageofbrawls.z.excepciones.Argumentos.ExcepcionDireccionNoValida;
import ageofbrawls.z.excepciones.Argumentos.ExcepcionNumeroArgumentos;
import ageofbrawls.z.excepciones.Movimiento.ExcepcionFueraDeLimites;
import ageofbrawls.z.excepciones.Movimiento.ExcepcionNoTransitable;
import ageofbrawls.z.excepciones.Recursos.EscasezRecursos.EscasezRecursosConstruccion;
import ageofbrawls.z.excepciones.Recursos.EscasezRecursos.EscasezRecursosCreacion;
import ageofbrawls.z.excepciones.Recursos.EscasezRecursos.ExcepcionEscasezRecursos;
import ageofbrawls.z.excepciones.Recursos.EscasezRecursos.ExcepcionEspacioInsuficiente;
import ageofbrawls.z.excepciones.Recursos.EscasezRecursos.ExcepcionNadaQueRecolectar;
import ageofbrawls.z.excepciones.noExiste.ExcepcionNoExisteCivilizacion;
import ageofbrawls.z.excepciones.noExiste.ExcepcionNoExisteGrupo;
import ageofbrawls.z.excepciones.noExiste.ExcepcionNoExistePosicion;
import ageofbrawls.z.excepciones.noExiste.ExcepcionNoExisteSujeto;
import ageofbrawls.z.excepciones.Recursos.ExcepcionCorrespondenciaRecursos;
import ageofbrawls.z.excepciones.noExiste.ExcepcionNoExiste;
import ageofbrawls.z.excepciones.noExiste.ExcepcionNoExisteArchivo;
import ageofbrawls.z.excepciones.noExiste.ExcepcionNoExisteEdificio;
import ageofbrawls.z.excepciones.noExiste.ExcepcionNoExisteMapa;

/**
 *
 * @author prada
 */
public class AgeOfBrawls {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        CargadorJuego cargador;
        String cargar = Juego.CONSOLA.leerEnLinea("Deseas cargar el juego (s) o empezar uno nuevo(n)? ");
        if (cargar.contains("s")) {
            String ruta = Juego.CONSOLA.leerEnLinea("Introduce la ruta de los archivos (mismo directorio=.) ");
            cargador = (CargadorJuego) new CargadorArchivo(ruta);
        } else {
            cargador = (CargadorJuego) new CargadorNuevo(20);
        }

        Comando juego = null;
        try {
            juego = (Comando) cargador.cargarJuego(null);
        } catch (ExcepcionDireccionNoValida | ExcepcionNoExiste | ExcepcionArgumentosInternos | ExcepcionCorrespondenciaRecursos | ExcepcionAccionRestringidaPersonaje ex) {
            Juego.CONSOLA.imprimir(ex.getMensaje());
            Juego.CONSOLA.imprimir("No se pudo cargar el juego, empezando uno nuevo...");
            cargador = (CargadorJuego) new CargadorNuevo(20);
            try {
                juego = (Comando) cargador.cargarJuego(null);
            } catch (ExcepcionDireccionNoValida | ExcepcionNoExiste | ExcepcionArgumentosInternos | ExcepcionCorrespondenciaRecursos | ExcepcionAccionRestringidaPersonaje ex1) {
                Juego.CONSOLA.imprimir(ex1.getMensaje());
            }
        }

        Juego.CONSOLA.imprimir("Bienvenido a Age Of Brawls!!");
        Juego.CONSOLA.imprimir("Por ahora es un vasto territorio inexplorado que solo habita tu fiel paisano \"paisano1\" desde su bastión \"ciudadela1\".");
        Juego.CONSOLA.imprimir("Dispones inicialmente de 500 de piedra, madera y alimentos para levantar tu imperio.");
        Juego.CONSOLA.imprimir("Construir una casa cuesta 100 de piedra y madera, un cuartel 200 de piedra y madera, crear un paisano 50 de alimentos y crear un soldado 100 de alimentos.");
        Juego.CONSOLA.imprimir();
        juego.imprimirCabecera();
        juego.imprimirMapa();

        String orden = "";
        while (!"salir".equals(orden)) {
            orden = Juego.CONSOLA.leerEnLinea("Introduce orden: ");
            try {
                if (!orden.contains(" ") && !(orden.equals("salir") || orden.equals("civilizacion"))) {
                    throw new ExcepcionNumeroArgumentos("Error de sintaxis.");
                }
                String[] comandos = orden.split(" ");
                switch (comandos[0].toLowerCase()) {
                    case "mover":
                        if (comandos.length != 3) {
                            throw new ExcepcionNumeroArgumentos("Error de sintaxis.");
                        }
                        try {
                            juego.mover(comandos[1], comandos[2]);//quien, donde
                        } catch (ExcepcionNoExisteSujeto | ExcepcionAccionRestringidaPersonaje | ExcepcionArgumentosInternos | ExcepcionDireccionNoValida | ExcepcionNoTransitable | ExcepcionFueraDeLimites ex) {
                            Juego.CONSOLA.imprimir(ex.getMensaje());
                        }
                        break;

                    case "describir":
                        if (comandos.length != 2) {
                            throw new ExcepcionNumeroArgumentos("Error de sintaxis.");
                        }
                        try {
                            juego.describir(comandos[1]);
                        } catch (ExcepcionNoExisteSujeto ex) {
                            Juego.CONSOLA.imprimir(ex.getMensaje());
                        }
                        break;
                    case "mirar":
                        if (comandos.length != 2) {
                            throw new ExcepcionNumeroArgumentos("Error de sintaxis.");
                        }
                        try {
                            juego.mirar(comandos[1]);
                        } catch (ExcepcionAccionRestringidaPersonaje | ExcepcionArgumentosInternos ex) {
                            Juego.CONSOLA.imprimir(ex.getMensaje());
                        }
                        break;
                    case "construir":
                        if (comandos.length != 4) {
                            throw new ExcepcionNumeroArgumentos("Error de sintaxis.");
                        }
                        try {
                            juego.construir(comandos[1], comandos[2], comandos[3]);//constructor, tipo de edificio, direccion
                        } catch (ExcepcionArgumentosInternos | ExcepcionNoExisteSujeto | ExcepcionAccionRestringida | ExcepcionDireccionNoValida | EscasezRecursosConstruccion | ExcepcionArgumentosValoresIncorrectos ex) {
                            Juego.CONSOLA.imprimir(ex.getMensaje());
                        }
                        break;
                    case "reparar":
                        if (comandos.length != 3) {
                            throw new ExcepcionNumeroArgumentos("Error de sintaxis.");
                        }
                        try {
                            juego.reparar(comandos[1], comandos[2]);//reparador, direccion
                        } catch (ExcepcionNoExisteSujeto | ExcepcionEscasezRecursos | ExcepcionArgumentosInternos | ExcepcionAccionRestringidaPersonaje | ExcepcionAccionRestringidaGrupo | ExcepcionArgumentosValoresIncorrectos | ExcepcionDireccionNoValida ex) {
                            Juego.CONSOLA.imprimir(ex.getMensaje());
                        }
                        break;

                    case "crear":
                        if (comandos.length != 3) {
                            throw new ExcepcionNumeroArgumentos("Error de sintaxis.");
                        }
                        try {
                            juego.crear(comandos[1], comandos[2]); //edificio creador, tipo de edificio
                        } catch (ExcepcionAccionRestringidaEdificio | ExcepcionEspacioInsuficiente | EscasezRecursosCreacion | ExcepcionNoExistePosicion | ExcepcionArgumentosInternos | ExcepcionNoExisteMapa | ExcepcionArgumentosValoresIncorrectos | ExcepcionNoExisteEdificio ex) {
                            Juego.CONSOLA.imprimir(ex.getMensaje());
                        }
                        break;

                    case "recolectar":
                        if (comandos.length != 3) {
                            throw new ExcepcionNumeroArgumentos("Error de sintaxis.");
                        }
                        try {
                            juego.recolectar(comandos[1], comandos[2]);//persona, direccion
                        } catch (ExcepcionNoExisteSujeto | ExcepcionDireccionNoValida | ExcepcionArgumentosInternos | ExcepcionAccionRestringidaPersonaje | ExcepcionArgumentosValoresIncorrectos | ExcepcionNadaQueRecolectar | ExcepcionAccionRestringidaGrupo ex) {
                            Juego.CONSOLA.imprimir(ex.getMensaje());
                        }
                        break;

                    case "almacenar":
                        if (comandos.length != 3) {
                            throw new ExcepcionNumeroArgumentos("Error de sintaxis.");
                        }
                        try {
                            juego.almacenar(comandos[1], comandos[2]); //quien almacena, direccion
                        } catch (ExcepcionAccionRestringidaGrupo | ExcepcionArgumentosValoresIncorrectos | ExcepcionNoExisteSujeto | ExcepcionDireccionNoValida | ExcepcionArgumentosInternos | ExcepcionAccionRestringidaPersonaje | ExcepcionEscasezRecursos ex) {
                            Juego.CONSOLA.imprimir(ex.getMensaje());
                        }
                        break;
                    case "manejar":
                        if (comandos.length != 2) {
                            throw new ExcepcionNumeroArgumentos("Error de sintaxis.");
                        }
                        try {
                            juego.manejar(comandos[1]);
                        } catch (ExcepcionNoExisteSujeto | ExcepcionAccionRestringidaPersonaje | ExcepcionArgumentosInternos ex) {
                            Juego.CONSOLA.imprimir(ex.getMensaje());
                        }
                        break;
                    case "cambiar":
                        if (comandos.length != 2) {
                            throw new ExcepcionNumeroArgumentos("Error de sintaxis.");
                        }
                        try {
                            juego.cambiar(comandos[1]);
                        } catch (ExcepcionNoExisteCivilizacion ex) {
                            Juego.CONSOLA.imprimir(ex.getMensaje());
                        }
                        break;
                    case "civilizacion":
                        juego.civilizacion();
                        break;
                    case "imprimir":
                        if (comandos.length != 2 || !comandos[1].equals("mapa")) {
                            throw new ExcepcionNumeroArgumentos("Error de sintaxis.");
                        }
                        juego.imprimirCabecera();
                        juego.imprimirMapa();
                        break;
                    case "agrupar":
                        if (comandos.length != 2) {
                            throw new ExcepcionNumeroArgumentos("Error de sintaxis.");
                        }
                        try {
                            juego.agrupar(comandos[1]);
                        } catch (ExcepcionNoExistePosicion | ExcepcionArgumentosInternos | ExcepcionAccionRestringidaPersonaje ex) {
                            Juego.CONSOLA.imprimir(ex.getMensaje());
                        }
                        break;

                    case "desligar":
                        if (comandos.length != 3) {
                            throw new ExcepcionNumeroArgumentos("Error de sintaxis.");
                        }
                        try {
                            juego.desligar(comandos[1], comandos[2]); //personaje desligado, grupo
                        } catch (ExcepcionNoExisteSujeto | ExcepcionArgumentosInternos | ExcepcionAccionRestringidaPersonaje ex) {
                            Juego.CONSOLA.imprimir(ex.getMensaje());
                        }
                        break;

                    case "desagrupar":
                        if (comandos.length != 2) {
                            throw new ExcepcionNumeroArgumentos("Error de sintaxis.");
                        }
                        try {
                            juego.desagrupar(comandos[1]);
                        } catch (ExcepcionNoExisteGrupo | ExcepcionAccionRestringidaPersonaje ex) {
                            Juego.CONSOLA.imprimir(ex.getMensaje());
                        }
                        break;

                    case "defender":
                        if (comandos.length != 3) {
                            throw new ExcepcionNumeroArgumentos("Error de sintaxis.");
                        }
                        try {
                            juego.defender(comandos[1], comandos[2]);
                        } catch (ExcepcionNoExisteSujeto | ExcepcionDireccionNoValida | ExcepcionArgumentosInternos | ExcepcionAccionRestringidaPersonaje | ExcepcionEspacioInsuficiente | ExcepcionAccionRestringidaGrupo | ExcepcionArgumentosValoresIncorrectos ex) {
                            Juego.CONSOLA.imprimir(ex.getMensaje());
                        }
                        break;

                    case "atacar":
                        if (comandos.length != 3) {
                            throw new ExcepcionNumeroArgumentos("Error de sintaxis.");
                        }
                        try {
                            juego.atacar(comandos[1], comandos[2]);
                        } catch (ExcepcionNoExisteSujeto | ExcepcionDireccionNoValida | ExcepcionArgumentosInternos | ExcepcionAccionRestringidaPersonaje | ExcepcionAccionRestringidaGrupo ex) {
                            Juego.CONSOLA.imprimir(ex.getMensaje());
                        }
                        break;

                    case "cargar":
                        if (comandos.length != 2) {
                            throw new ExcepcionNumeroArgumentos("Error de sintaxis.");
                        }
                        try {
                            juego.cargar(comandos[1]);
                        } catch (ExcepcionNoExisteArchivo | ExcepcionArgumentosInternos | ExcepcionCorrespondenciaRecursos | ExcepcionAccionRestringidaPersonaje ex) {
                            Juego.CONSOLA.imprimir(ex.getMensaje());
                        }
                        break;
                    case "guardar":
                        if (comandos.length != 2) {
                            throw new ExcepcionNumeroArgumentos("Error de sintaxis.");
                        }
                        try {
                            juego.guardar(comandos[1]);
                        } catch (ExcepcionNoExisteArchivo ex) {
                            Juego.CONSOLA.imprimir(ex.getMensaje());
                        }
                        break;
                    case "listar":
                        if (comandos.length != 2) {
                            throw new ExcepcionNumeroArgumentos("Error de sintaxis.");
                        }
                        switch (comandos[1].toLowerCase()) {
                            case "personajes":
                                juego.listarPersonajes();
                                break;
                            case "grupos":
                                juego.listarGrupos();
                                break;
                            case "edificios":
                                juego.listarEdificios();
                                break;
                            case "civilizaciones":
                                juego.listarCivilizaciones();
                                break;
                            default:
                                throw new ExcepcionNumeroArgumentos("Error de sintaxis.");
                        }
                        break;
                    default:
                        if (!orden.equals("salir") && !orden.equals("\n")) {
                            throw new ExcepcionNumeroArgumentos("Error de sintaxis.");
                        }
                }

            } catch (ExcepcionArgumentos ex) {
                Juego.CONSOLA.imprimir(ex.getMensaje());
            }
        }
    }

}
