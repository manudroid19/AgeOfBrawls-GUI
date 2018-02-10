package ageofbrawls.contenido.Personajes;

import ageofbrawls.contenido.contenedor.Contenedor;
import ageofbrawls.contenido.edificio.Edificio;
import ageofbrawls.contenido.Personajes.Soldados.Arquero;
import ageofbrawls.contenido.Personajes.Soldados.Caballero;
import ageofbrawls.contenido.Personajes.Soldados.Legionario;
import ageofbrawls.contenido.Recursos.Comida;
import ageofbrawls.contenido.Recursos.Madera;
import ageofbrawls.contenido.Recursos.Piedra;
import ageofbrawls.contenido.Recursos.Recurso;
import ageofbrawls.contenido.edificio.Casa;
import ageofbrawls.contenido.edificio.Ciudadela;
import ageofbrawls.contenido.edificio.Cuartel;
import ageofbrawls.plataforma.Celda;
import ageofbrawls.plataforma.Civilizacion;
import ageofbrawls.plataforma.Juego;
import ageofbrawls.plataforma.Mapa;
import ageofbrawls.plataforma.Posicion;
import ageofbrawls.z.excepciones.AccionRestringida.ExcepcionAccionRestringidaGrupo;
import ageofbrawls.z.excepciones.AccionRestringida.ExcepcionAccionRestringidaPersonaje;
import ageofbrawls.z.excepciones.Argumentos.ExcepcionArgumentosInternos;
import ageofbrawls.z.excepciones.Argumentos.ExcepcionArgumentosValoresIncorrectos;
import ageofbrawls.z.excepciones.Argumentos.ExcepcionDireccionNoValida;
import ageofbrawls.z.excepciones.Recursos.EscasezRecursos.EscasezRecursosConstruccion;
import ageofbrawls.z.excepciones.Recursos.EscasezRecursos.ExcepcionEscasezRecursos;
import ageofbrawls.z.excepciones.Recursos.EscasezRecursos.ExcepcionEspacioInsuficiente;
import ageofbrawls.z.excepciones.Recursos.EscasezRecursos.ExcepcionNadaQueRecolectar;
import java.util.ArrayList;
import java.util.Objects;

/**
 *
 * @author Santiago
 */
public abstract class Personaje {

    private int salud, defensa;
    private Posicion posicion;
    private boolean muerto;
    private Civilizacion civilizacion;
    private String nombre;
    private Grupo grupo;

    public Personaje(Posicion posicion, String nombre, Civilizacion civilizacion) {
        this.posicion = new Posicion(posicion);
        this.nombre = nombre;
        this.civilizacion = civilizacion;
        this.grupo = null;
        salud = 10;
        defensa = 10;
        muerto = false;

    }

    public Personaje(Posicion posicion, String nombre, Civilizacion civilizacion, int defensa, int salud) {
        this(posicion, nombre, civilizacion);//defensa salud
        this.defensa = defensa;
        this.salud = salud;
    }

    public int getSalud() {
        return salud;
    }

    public void setSalud(int cant, boolean relative) throws ExcepcionArgumentosInternos {
        if (relative) {
            if (salud + cant < 0) {
                salud = 0;
                return;
            }
            salud += cant;
        } else {
            if (salud + cant < 0) {
                throw new ExcepcionArgumentosInternos("error, seteo incorrecto");

            }
            salud = cant;
        }
    }

    public int getDefensa() {
        return defensa;
    }

    public void setDefensa(int defensa) {
        this.defensa = defensa;
    }

    public int danhoAtaque() {
        return 0;
    }

    public Civilizacion getCivilizacion() {
        return civilizacion;
    }

    public int getCapRec() {
        return 0;
    }

    public int getCantRecMadera() {
        return 0;
    }

    public int getCantRecPiedra() {
        return 0;
    }

    public int getCantRecComida() {
        return 0;
    }

    public int getCantRecTotal() {
        return 0;
    }

    public Posicion getPosicion() {
        return posicion;
    }

    public void setPosicion(Posicion posicion) throws ExcepcionArgumentosInternos {
        if (posicion != null) {
            this.posicion = new Posicion(posicion);
        } else {
            throw new ExcepcionArgumentosInternos("Error: posicion introducida errónea");
        }
    }

    public String getNombre() {
        return nombre;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) throws ExcepcionAccionRestringidaPersonaje {
        this.grupo = grupo;
    }

    public boolean isMuerto() {
        return muerto;
    }

    public void describir() {
        Juego.CONSOLA.imprimir("Nombre del personaje: " + nombre);
        Juego.CONSOLA.imprimir("Este personaje es un " + this.toString());
        Juego.CONSOLA.imprimir("Salud :" + salud);
        Juego.CONSOLA.imprimir("Civilizacion: " + this.civilizacion.getNombre());
        Juego.CONSOLA.imprimir("Armadura :" + defensa);
    }

    public String leerGrupo() {
        if (grupo == null) {
            return "";
        } else {
            return grupo.getNombre();
        }
    }

    public void mover(String direccion) throws ExcepcionAccionRestringidaPersonaje, ExcepcionArgumentosInternos, ExcepcionDireccionNoValida {
        Posicion posicion = this.posicion.getAdy(direccion);
        Mapa mapa = civilizacion.getMapa();
        Celda celdaAntigua = mapa.getCelda(this.posicion);
        int cuantoSeMueve = this.capacidadMovimiento();
        for (int i = 1; i < cuantoSeMueve; i++) {
            if (!mapa.perteneceAMapa(posicion.getAdy(direccion)) || !mapa.getCelda(posicion.getAdy(direccion)).esCeldaLibre(false)) {
                break;
            }
            posicion = posicion.getAdy(direccion);
        }
        moverGenerico(posicion, 1);
        celdaAntigua.removePersonaje(this);
        mapa.getCelda(posicion).addPersonaje(this);

        System.out.println();
        mapa.imprimirCabecera();
        mapa.imprimir(civilizacion);

    }

    public void defender(String direccion) throws ExcepcionArgumentosInternos, ExcepcionDireccionNoValida, ExcepcionAccionRestringidaPersonaje, ExcepcionAccionRestringidaGrupo, ExcepcionEspacioInsuficiente, ExcepcionArgumentosValoresIncorrectos {
        Posicion pos = posicion.getAdy(direccion);
        if (direccion == null || pos == null || civilizacion.getMapa() == null || !civilizacion.getMapa().perteneceAMapa(pos) || civilizacion.getMapa().getCelda(pos).getEdificio() == null) {
            throw new ExcepcionArgumentosInternos("No hay edificio en la posición indicada.");

        }
        if (this.civilizacion != civilizacion.getMapa().getCelda(pos).getEdificio().getCivilizacion()) {
            throw new ExcepcionAccionRestringidaPersonaje("El personaje no puede entrar en el edificio de la otra civilización");

        }

        if (this.grupo != null) {
            throw new ExcepcionAccionRestringidaPersonaje("El personaje no puede defender por si solo el edificio ya que pertenece a un grupo");

        }

        if (civilizacion.getMapa().getCelda(pos).getEdificio().getCapAloj() == 0) {
            throw new ExcepcionEspacioInsuficiente(civilizacion.getMapa().getCelda(pos).getEdificio().getNombre() + " ya está al máximo de su capacidad. El " + this.getNombre() + "no ha podido entrar en " + civilizacion.getMapa().getCelda(pos).getEdificio().getNombre() + " .");

        }

        civilizacion.getMapa().getCelda(this.posicion).removePersonaje(this);
        civilizacion.getMapa().getCelda(pos).addPersonaje(this);
        posicion = new Posicion(pos);
        civilizacion.getMapa().imprimirCabecera();
        civilizacion.getMapa().imprimir(civilizacion);
        civilizacion.getMapa().getCelda(pos).getEdificio().setCapAloj(-1, true);
        Juego.CONSOLA.imprimir("El " + this.getNombre() + " ha entrado en " + civilizacion.getMapa().getCelda(pos).getEdificio().getNombre() + " (capacidad restante " + civilizacion.getMapa().getCelda(pos).getEdificio().getCapAloj() + ").");
        this.recuperarVida();
    }

    public int capacidadMovimiento() {
        return 1;
    }

    public void atacar(String direccion) throws ExcepcionArgumentosInternos, ExcepcionAccionRestringidaGrupo, ExcepcionAccionRestringidaPersonaje, ExcepcionDireccionNoValida {
        throw new ExcepcionAccionRestringidaPersonaje("Este personaje no puede atacar");
    }

    protected void vaciarCantRecComida() throws ExcepcionArgumentosValoresIncorrectos, ExcepcionAccionRestringidaPersonaje {
        throw new ExcepcionAccionRestringidaPersonaje("Este personaje no puede vaciar su cantidad recolectada de comida");
    }

    protected void vaciarCantRecPiedra() throws ExcepcionArgumentosValoresIncorrectos, ExcepcionAccionRestringidaPersonaje {
        throw new ExcepcionAccionRestringidaPersonaje("Este personaje no puede vaciar su cantidad recolectada de piedra");
    }

    protected void vaciarCantRecMadera() throws ExcepcionArgumentosValoresIncorrectos, ExcepcionAccionRestringidaPersonaje {
        throw new ExcepcionAccionRestringidaPersonaje("Este personaje no puede vaciar su cantidad recolectada de madera");
    }

    public void recuperarVida() throws ExcepcionAccionRestringidaPersonaje, ExcepcionArgumentosInternos, ExcepcionArgumentosValoresIncorrectos {
        throw new ExcepcionAccionRestringidaPersonaje("Este personaje no puede recuperar vida");
    }

    public void recuperar() throws ExcepcionArgumentosInternos, ExcepcionAccionRestringidaPersonaje {
        throw new ExcepcionAccionRestringidaPersonaje("Este personaje no puede recuperar toda la vida");
    }

    public void recolectar(String direccion) throws ExcepcionArgumentosInternos, ExcepcionAccionRestringidaPersonaje, ExcepcionAccionRestringidaGrupo, ExcepcionArgumentosValoresIncorrectos, ExcepcionDireccionNoValida, ExcepcionNadaQueRecolectar {
        throw new ExcepcionAccionRestringidaPersonaje("Este personaje no puede recolectar");
    }

    public void almacenar(String direccion) throws ExcepcionDireccionNoValida, ExcepcionArgumentosValoresIncorrectos, ExcepcionAccionRestringidaGrupo, ExcepcionArgumentosInternos, ExcepcionAccionRestringidaPersonaje, ExcepcionEscasezRecursos {
        throw new ExcepcionAccionRestringidaPersonaje("Este personaje no puede almacenar recursos");
    }

    public void construir(String tipoC, String dir) throws ExcepcionArgumentosValoresIncorrectos, ExcepcionArgumentosInternos, ExcepcionAccionRestringidaGrupo, EscasezRecursosConstruccion, ExcepcionAccionRestringidaPersonaje, ExcepcionDireccionNoValida {
        throw new ExcepcionAccionRestringidaPersonaje("Este personaje no puede construir");

    }

    public void reparar(Posicion pos) throws ExcepcionArgumentosInternos, ExcepcionAccionRestringidaGrupo, ExcepcionArgumentosValoresIncorrectos, ExcepcionAccionRestringidaPersonaje, ExcepcionEscasezRecursos {
        throw new ExcepcionAccionRestringidaPersonaje("Este personaje no puede reparar");
    }

    protected void moverGenerico(Posicion posicion, int n) throws ExcepcionAccionRestringidaPersonaje, ExcepcionArgumentosInternos {
        if (grupo != null && grupo.getPersonajes().contains(this)) {
            throw new ExcepcionAccionRestringidaPersonaje("El personaje no puede moverse por si solo ya que pertenece a un grupo");
        }
        Mapa mapa = civilizacion.getMapa();
        Celda celdaAntigua = mapa.getCelda(this.posicion);
        if (!mapa.perteneceAMapa(posicion) || !mapa.getCelda(posicion).esCeldaLibre(false)) {
            throw new ExcepcionAccionRestringidaPersonaje("Error: No te puedes mover a esa celda.");
        }
        if (celdaAntigua.getEdificio() != null) {
            Edificio edif = celdaAntigua.getEdificio();
            edif.setCapAloj(n, true);
        }
        this.posicion = posicion;
        civilizacion.makeAdyVisible(posicion);
    }

    protected void construirGenerico(String tipo, String dir) throws ExcepcionArgumentosInternos, EscasezRecursosConstruccion, ExcepcionAccionRestringidaPersonaje, ExcepcionDireccionNoValida, ExcepcionArgumentosValoresIncorrectos {
        if (civilizacion.getMapa().getCelda(posicion).getEdificio() != null) {
            throw new ExcepcionAccionRestringidaPersonaje("No se puede construir desde un edificio");

        }
        Posicion posConstruir = posicion.getAdy(dir);
        if (posConstruir.equals(posicion) || !civilizacion.getMapa().perteneceAMapa(posConstruir) || !civilizacion.getMapa().getCelda(posConstruir).esCeldaLibre(true)) { //direccion no valida
            throw new ExcepcionDireccionNoValida("Error: No se puede construir en la celda de destino.");

        }
        civilizacion.makeAdyVisible(posConstruir);
        switch (tipo) {
            case "casa":
                if (civilizacion.getMadera() < 100 || civilizacion.getPiedra() < 100) {
                    throw new EscasezRecursosConstruccion("No se puede construir! Se necesitan 100 de madera y piedra y tienes " + civilizacion.getMadera() + " y " + civilizacion.getPiedra());

                }
                civilizacion.setPiedra(-100, true);
                civilizacion.setMadera(-100, true);
                Casa edif = new Casa(posConstruir, "casa" + (civilizacion.contarEdificios(Casa.class) + 1), civilizacion);
                civilizacion.getMapa().getCelda(posConstruir).setEdificio(edif);

                System.out.println();
                civilizacion.getEdificios().put(edif.getNombre(), edif);
                civilizacion.getMapa().imprimirCabecera();
                civilizacion.getMapa().imprimir(civilizacion);

                Juego.CONSOLA.imprimir("Casa construida en " + posConstruir);
                Juego.CONSOLA.imprimir("Coste: 100 de madera, 100 de piedra.");
                break;
            case "cuartel":
                if (civilizacion.getMadera() < 200 || civilizacion.getPiedra() < 200) {
                    throw new EscasezRecursosConstruccion("No se puede construir! Se necesitan 200 de madera y piedra y tienes " + civilizacion.getMadera() + " y " + civilizacion.getPiedra());

                }
                civilizacion.setPiedra(-200, true);
                civilizacion.setMadera(-200, true);
                Cuartel cuart = new Cuartel(posConstruir, "cuartel" + (civilizacion.contarEdificios(Cuartel.class) + 1), civilizacion);
                civilizacion.getMapa().getCelda(posConstruir).setEdificio(cuart);
                civilizacion.getEdificios().put(cuart.getNombre(), cuart);
                System.out.println();
                civilizacion.getMapa().imprimirCabecera();
                civilizacion.getMapa().imprimir(civilizacion);
                Juego.CONSOLA.imprimir("Cuartel construido en " + posConstruir);
                Juego.CONSOLA.imprimir("Coste: 200 de madera, 200 de piedra.");
                break;
            case "ciudadela":
                if (civilizacion.getMadera() < 500 || civilizacion.getPiedra() < 500) {
                    throw new EscasezRecursosConstruccion("No se puede construir! Se necesitan 500 de madera y piedra y tienes " + civilizacion.getMadera() + " y " + civilizacion.getPiedra());

                }
                civilizacion.setPiedra(-500, true);
                civilizacion.setMadera(-500, true);
                Ciudadela ciud = new Ciudadela(posConstruir, "ciudadela" + (civilizacion.contarEdificios(Ciudadela.class) + 1), civilizacion);
                civilizacion.anadirCiudadela();
                civilizacion.getMapa().getCelda(posConstruir).setEdificio(ciud);
                civilizacion.getEdificios().put(ciud.getNombre(), ciud);
                System.out.println();
                civilizacion.getMapa().imprimirCabecera();
                civilizacion.getMapa().imprimir(civilizacion);
                Juego.CONSOLA.imprimir("Ciudadela construida en " + posConstruir);
                Juego.CONSOLA.imprimir("Coste: 500 de madera, 500 de piedra.");
                break;
            default:
                throw new ExcepcionArgumentosInternos("Error: tipo de construccion incorrecta.");
        }
    }

    protected void repararGenerico(Posicion pos) throws ExcepcionArgumentosInternos, ExcepcionAccionRestringidaPersonaje, ExcepcionEscasezRecursos, ExcepcionArgumentosValoresIncorrectos {
        if (pos == null || !civilizacion.getMapa().perteneceAMapa(pos) || civilizacion.getMapa().getCelda(pos).getEdificio() == null || civilizacion.getMapa().getCelda(pos).getEdificio().getSalud() == civilizacion.getMapa().getCelda(pos).getEdificio().MaxVida()) {
            throw new ExcepcionArgumentosInternos("Nada que reparar.");

        }
        if (civilizacion.getMapa().getCelda(posicion).getEdificio() != null) {
            throw new ExcepcionAccionRestringidaPersonaje("No se puede reparar desde un edificio");

        }
        int puntosAReparar = civilizacion.getMapa().getCelda(pos).getEdificio().MaxVida() - civilizacion.getMapa().getCelda(pos).getEdificio().getSalud();
        int costeMadera = (int) (puntosAReparar * 0.4);
        int costePiedra = (int) (puntosAReparar * 0.5);
        if (civilizacion.getMadera() < costeMadera || civilizacion.getPiedra() < costePiedra) {
            throw new ExcepcionEscasezRecursos("No tienes suficientes recursos disponibles!");

        }
        civilizacion.setMadera(-costeMadera, true);
        civilizacion.setPiedra(-costePiedra, true);
        civilizacion.getMapa().getCelda(pos).getEdificio().reparar();
        Juego.CONSOLA.imprimir("Reparación completada.");
        Juego.CONSOLA.imprimir("Coste de la reparación: " + costeMadera + " unidades de madera y " + costePiedra + " unidades de piedra de la ciudadela.");
    }

    protected void atacarGenerico(String direccion) throws ExcepcionArgumentosInternos, ExcepcionAccionRestringidaPersonaje, ExcepcionDireccionNoValida {
        Posicion pos = posicion.getAdy(direccion);
        if (pos == null || civilizacion.getMapa() == null || !civilizacion.getMapa().perteneceAMapa(pos)) {
            throw new ExcepcionDireccionNoValida("No se puede atacar a esa posición.");

        }
        if (civilizacion.getMapa().getCelda(pos).getEdificio() == null && !civilizacion.getMapa().getCelda(pos).isHayGrupo() && civilizacion.getMapa().getCelda(pos).getPersonajes().isEmpty()) {
            throw new ExcepcionAccionRestringidaPersonaje("En esa celda no hay nada a lo que se le pueda atacar");

        }
        if (civilizacion.getMapa().getCelda(pos).getEdificio() != null && civilizacion == civilizacion.getMapa().getCelda(pos).getEdificio().getCivilizacion()) {
            throw new ExcepcionAccionRestringidaPersonaje("No se puede atacar a un edificio de la misma civilización");

        }
        int PuntosAQuitar = this.danhoAtaque();
        boolean atacanCaballeros = (!(this instanceof Grupo) && this instanceof Caballero) || (this instanceof Grupo && ((Grupo) this).estaFormadoPor(Caballero.class));
        boolean atacanArqueros = (!(this instanceof Grupo) && this instanceof Arquero) || (this instanceof Grupo && ((Grupo) this).estaFormadoPor(Caballero.class));
        ArrayList<Personaje> atacados = new ArrayList<>();
        if (!civilizacion.getMapa().getCelda(pos).getGrupos().isEmpty()) {
            for (int i = 0; i < civilizacion.getMapa().getCelda(pos).getGrupos().size(); i++) {
                for (int j = 0; j < civilizacion.getMapa().getCelda(pos).getGrupos().get(i).getPersonajes().size(); j++) {
                    if (civilizacion.getMapa().getCelda(pos).getGrupos().get(i).getPersonajes().get(j).getCivilizacion() != civilizacion) {
                        atacados.add(civilizacion.getMapa().getCelda(pos).getGrupos().get(i).getPersonajes().get(j));
                    }
                }
            }

        }
        for (int i = 0; i < civilizacion.getMapa().getCelda(pos).getPersonajes().size(); i++) {
            if (civilizacion.getMapa().getCelda(pos).getPersonajes().get(i).getCivilizacion() != civilizacion) {
                atacados.add(civilizacion.getMapa().getCelda(pos).getPersonajes().get(i));
            }
        }
        int PuntosAQuitarACadaUno;
        if (atacados.isEmpty() && civilizacion.getMapa().getCelda(pos).getEdificio() == null) {
            PuntosAQuitarACadaUno = 0;
            throw new ExcepcionAccionRestringidaPersonaje("No se puede atacar a personajes de la misma civilizacion");
        } else {
            PuntosAQuitarACadaUno = (int) (PuntosAQuitar / atacados.size());
        }
        for (int i = 0; i < atacados.size(); i++) {
            Personaje atacado = atacados.get(i);
            int quitados;
            if (atacanCaballeros && (atacados.get(i) instanceof Legionario || atacados.get(i) instanceof Arquero)) {
                quitados = (int) ((double) PuntosAQuitarACadaUno * 2);
            } else {//soldados en general
                quitados = PuntosAQuitarACadaUno;
            }
            atacado.setSalud(-quitados, true);
            if (atacado.getSalud() <= 0) {
                if (atacado.getGrupo() != null) {
                    atacado.getGrupo().desligar(atacado);
                }
                civilizacion.getMapa().getCelda(pos).getPersonajes().remove(atacado);
                civilizacion.getPersonajes().remove(atacado.getNombre());
                civilizacion.getMapa().imprimirCabecera();
                civilizacion.getMapa().imprimir(civilizacion);
                Juego.CONSOLA.imprimir("Has inflingido " + quitados + " de daño al personaje " + atacado.getNombre() + " de la celda " + pos.toStringMapa() + " (civ " + atacados.get(0).getCivilizacion().getNombre() + ").");
                Juego.CONSOLA.imprimir("El personaje: " + atacado.getNombre() + " ha muerto");
            } else {
                Juego.CONSOLA.imprimir("Has inflingido " + quitados + " de daño al personaje " + atacado.getNombre() + " de la celda " + pos.toStringMapa() + " (civ " + atacados.get(0).getCivilizacion().getNombre() + ").");
            }
        }

        if (PuntosAQuitarACadaUno == 0 && civilizacion.getMapa().getCelda(pos).getEdificio() != null && civilizacion != civilizacion.getMapa().getCelda(pos).getEdificio().getCivilizacion()) {
            if (atacanArqueros) {
                Juego.CONSOLA.imprimir("Has inflingido " + (int) ((double) PuntosAQuitar * 0.5) + " al edificio " + civilizacion.getMapa().getCelda(pos).getEdificio().getNombre() + " de la civilizacion (" + civilizacion.getMapa().getCelda(pos).getEdificio().getCivilizacion().getNombre() + ").");
                civilizacion.getMapa().getCelda(pos).getEdificio().danar((int) ((double) PuntosAQuitar * 0.5));
            } else {
                Juego.CONSOLA.imprimir("Has inflingido " + PuntosAQuitar + " al edificio " + civilizacion.getMapa().getCelda(pos).getEdificio().getNombre() + " de la civilizacion (" + civilizacion.getMapa().getCelda(pos).getEdificio().getCivilizacion().getNombre() + ").");
                civilizacion.getMapa().getCelda(pos).getEdificio().danar(PuntosAQuitar);
            }
        }
    }

    protected void almacenarGenerico(String direccion) throws ExcepcionDireccionNoValida, ExcepcionArgumentosInternos, ExcepcionAccionRestringidaPersonaje, ExcepcionEscasezRecursos, ExcepcionArgumentosValoresIncorrectos {
        Mapa mapa = civilizacion.getMapa();
        if (mapa == null || direccion == null) {
            throw new ExcepcionDireccionNoValida("Error en almacenar.");

        }
        Posicion posicion = getPosicion();
        Civilizacion civilizacion = getCivilizacion();
        Posicion pos = posicion.getAdy(direccion);
        if (pos.equals(posicion)) { //error con la posicion
            throw new ExcepcionArgumentosInternos("La posicion no es correcta");
        }
        if (civilizacion.getMapa().getCelda(posicion).getEdificio() != null) {
            throw new ExcepcionAccionRestringidaPersonaje("No se puede almacenar desde un edificio");

        }
        if (mapa.getCelda(pos).getEdificio() == null || !(mapa.getCelda(pos).getEdificio() instanceof Ciudadela) || mapa.getCelda(pos).getEdificio().getCivilizacion() != this.civilizacion) {
            throw new ExcepcionAccionRestringidaPersonaje("No se puede almacenar recursos en esa celda");

        }
        if (this.getCantRecTotal() <= 0) {
            throw new ExcepcionEscasezRecursos("No se transportan recursos");

        }
        if (!civilizacion.puedeAlmacenar(getCantRecComida() + getCantRecMadera() + getCantRecPiedra())) {
            throw new ExcepcionEspacioInsuficiente("No hay espacio suficiente en la ciudadela.");

        }

        if (this.getCantRecMadera() > 0) {
            mapa.getCelda(pos).getEdificio().almacenar(new Madera(this.getCantRecMadera()));
            Juego.CONSOLA.imprimir("Almacenadas " + this.getCantRecMadera() + " unidades de madera en la ciudadela");
            this.vaciarCantRecMadera();
        }
        if (this.getCantRecPiedra() > 0) {
            mapa.getCelda(pos).getEdificio().almacenar(new Piedra(this.getCantRecPiedra()));
            Juego.CONSOLA.imprimir("Almacenadas " + this.getCantRecPiedra() + " unidades de piedra en la ciudadela");
            this.vaciarCantRecPiedra();
        }
        if (this.getCantRecComida() > 0) {
            mapa.getCelda(pos).getEdificio().almacenar(new Comida(this.getCantRecPiedra()));
            Juego.CONSOLA.imprimir("Almacenadas " + this.getCantRecComida() + " unidades de alimento en la ciudadela");
            this.vaciarCantRecComida();
        }
    }

    @Override
    public String toString() {
        return "personaje";
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + this.salud;
        hash = 37 * hash + this.defensa;
        hash = 37 * hash + Objects.hashCode(this.posicion);
        hash = 37 * hash + (this.muerto ? 1 : 0);
        hash = 37 * hash + Objects.hashCode(this.nombre);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Personaje other = (Personaje) obj;
        if (this.salud != other.salud) {
            return false;
        }
        if (this.defensa != other.defensa) {
            return false;
        }
        if (this.muerto != other.muerto) {
            return false;
        }
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        if (!Objects.equals(this.posicion, other.posicion)) {
            return false;
        }
        if (!Objects.equals(this.civilizacion, other.civilizacion)) {
            return false;
        }
        if (!Objects.equals(this.grupo, other.grupo)) {
            return false;
        }
        return true;
    }

    public String getDescribir() {
        return "Este personaje es un " + this.toString()+"\r\n"+
        "Salud :" + salud+"\r\n"+
        "Civilizacion: " + this.civilizacion.getNombre()+"\r\n"+
        "Armadura :" + defensa;
    }

}
