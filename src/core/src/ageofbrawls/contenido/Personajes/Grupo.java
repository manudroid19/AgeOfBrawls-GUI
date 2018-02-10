/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ageofbrawls.contenido.Personajes;

import ageofbrawls.contenido.contenedor.Contenedor;
import ageofbrawls.plataforma.Celda;
import ageofbrawls.plataforma.Civilizacion;
import ageofbrawls.plataforma.Mapa;
import ageofbrawls.plataforma.Posicion;
import ageofbrawls.contenido.Personajes.Soldados.Caballero;
import ageofbrawls.contenido.contenedor.Arbusto;
import ageofbrawls.contenido.contenedor.Bosque;
import ageofbrawls.contenido.contenedor.Cantera;
import ageofbrawls.plataforma.Juego;
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
public final class Grupo extends Personaje {

    private ArrayList<Personaje> personajes;
    private boolean haySoldado;

    public Grupo(ArrayList<Personaje> personajes, Posicion posicion, String nombre, Civilizacion civilizacion) throws ExcepcionArgumentosInternos, ExcepcionAccionRestringidaPersonaje {
        super(posicion, nombre, civilizacion);
        if (personajes != null) {
            this.personajes = new ArrayList<>(personajes);
            for (int i = 0; i < this.personajes.size(); i++) {
                personajes.get(i).setGrupo(this);
                if (this.personajes.get(i) instanceof Soldado) {
                    this.haySoldado = true;
                }
            }
        } else {
            throw new ExcepcionArgumentosInternos("Error parametros en Grupo");
        }
    }

    @Override
    public int getSalud() {
        int salud = 0;
        for (Personaje p : personajes) {
            salud += p.getSalud();
        }
        return salud;
    }

    @Override
    public int getDefensa() {
        int valor = 0;
        for (Personaje p : personajes) {
            valor += p.getDefensa();
        }
        return valor;
    }

    @Override
    public int danhoAtaque() {
        int valor = 0;
        for (Personaje p : personajes) {
            valor += p.danhoAtaque();
        }
        return valor;
    }

    @Override
    public int getCapRec() {
        int valor = 0;
        for (Personaje p : personajes) {
            valor += p.getCapRec();
        }
        return valor;
    }

    @Override
    public int capacidadMovimiento() {
        for (Personaje p : personajes) {
            if (!(p instanceof Caballero)) {
                return 1;
            }
        }
        return 2;
    }

    @Override
    public int getCantRecMadera() {
        int valor = 0;
        for (Personaje p : personajes) {
            valor += p.getCantRecMadera();
        }
        return valor;
    }

    public boolean getHaySoldado() {
        return haySoldado;
    }

    @Override
    public int getCantRecPiedra() {
        int valor = 0;
        for (Personaje p : personajes) {
            valor += p.getCantRecPiedra();
        }
        return valor;
    }

    @Override
    public int getCantRecComida() {
        int valor = 0;
        for (Personaje p : personajes) {
            valor += p.getCantRecComida();
        }
        return valor;
    }

    @Override
    public int getCantRecTotal() {
        int valor = 0;
        for (Personaje p : personajes) {
            valor += p.getCantRecTotal();
        }
        return valor;
    }

    public ArrayList<Personaje> getPersonajes() {
        return personajes;
    }

    @Override
    public void setGrupo(Grupo grupo) throws ExcepcionAccionRestringidaPersonaje {
        throw new ExcepcionAccionRestringidaPersonaje("Un grupo no puede estar en un grupo");
    }

    @Override
    public void setPosicion(Posicion posicion) throws ExcepcionArgumentosInternos {
        super.setPosicion(posicion);
        actualizarPosiciones();
    }

    @Override
    public void vaciarCantRecComida() throws ExcepcionArgumentosValoresIncorrectos {
        for (Personaje p : personajes) {
            if (p instanceof Paisano) {
                ((Paisano) p).setCantRecComida(0);
            }
        }
    }

    @Override
    public void vaciarCantRecMadera() throws ExcepcionArgumentosValoresIncorrectos {
        for (Personaje p : personajes) {
            if (p instanceof Paisano) {
                ((Paisano) p).setCantRecMadera(0);
            }
        }
    }

    @Override
    public void vaciarCantRecPiedra() throws ExcepcionArgumentosValoresIncorrectos {
        for (Personaje p : personajes) {
            if (p instanceof Paisano) {
                ((Paisano) p).setCantRecPiedra(0);
            }
        }
    }

    public boolean estaFormadoPor(Class clase) {
        for (Personaje p : personajes) {
            if (!clase.isInstance(p)) {
                return false;
            }
        }
        return true;
    }

    public void desligar(Personaje personaje) throws ExcepcionArgumentosInternos, ExcepcionAccionRestringidaPersonaje {
        if (personaje == null) {
            throw new ExcepcionArgumentosInternos("Error con argumento al desligar");
        }
        if (!this.getPersonajes().contains(personaje)) {
            throw new ExcepcionAccionRestringidaPersonaje("El personaje no está en el grupo");
        }
        Civilizacion civilizacion = getCivilizacion();
        Posicion posicion = getPosicion();
        this.getPersonajes().remove(personaje);
        personaje.setGrupo(null);
        civilizacion.getMapa().getCelda(posicion).getPersonajes().add(personaje);
        revisarVacio();
        civilizacion.getMapa().imprimirCabecera();
        civilizacion.getMapa().imprimir(civilizacion);
        Juego.CONSOLA.imprimir(personaje.getNombre() + " desligado de " + getNombre());

    }

    public void desagrupar() throws ExcepcionAccionRestringidaPersonaje {
        Civilizacion civilizacion = getCivilizacion();
        Posicion posicion = getPosicion();
        for (Personaje p : personajes) {
            civilizacion.getMapa().getCelda(posicion).getPersonajes().add(p);
            p.setGrupo(null);
        }
        civilizacion.getMapa().getCelda(posicion).removeGrupo(this);
        getPersonajes().clear();
        civilizacion.getGrupos().remove(getNombre());
        civilizacion.getMapa().imprimirCabecera();
        civilizacion.getMapa().imprimir(civilizacion);
        Juego.CONSOLA.imprimir(getNombre() + " desagrupado.");
    }

    @Override
    public void describir() {
        Juego.CONSOLA.imprimir("Nombre del grupo: " + getNombre());
        Juego.CONSOLA.imprimir("Civilizacion: " + getCivilizacion().getNombre());
        Juego.CONSOLA.imprimir("Armadura :" + getDefensa());
        if (this.haySoldado) {
            Juego.CONSOLA.imprimir("Ataque :" + danhoAtaque());
        }
        if (!this.haySoldado) {
            Juego.CONSOLA.imprimir("Capacidad de recoleccion del grupo:" + getCapRec());
            Juego.CONSOLA.imprimir("Cantidad de madera que transporta: " + getCantRecMadera());
            Juego.CONSOLA.imprimir("Cantidad de comida que transporta: " + getCantRecComida());
            Juego.CONSOLA.imprimir("Cantidad de piedra que transporta: " + getCantRecPiedra());
            Juego.CONSOLA.imprimir("Cantidad de recursos que lleva: " + (getCantRecTotal()));
        }
        Juego.CONSOLA.imprimir("En este grupo están los siguientes personajes: ");
        for (Personaje p : personajes) {
            p.describir();
        }
    }
    @Override
    public String getDescribir() {
        String a = "Nombre del grupo: " + getNombre()+"\r\n"+
        "Civilizacion: " + getCivilizacion().getNombre()+"\r\n"+
        "Armadura :" + getDefensa()+"\r\n";
        if (this.haySoldado) {
            a=a+"Ataque :" + danhoAtaque()+"\r\n";
        }
        if (!this.haySoldado) {
            a=a+"Capacidad de recoleccion del grupo:" + getCapRec()+"\r\n"+
            "Cantidad de madera que transporta: " + getCantRecMadera()+"\r\n"+
            "Cantidad de comida que transporta: " + getCantRecComida()+"\r\n"+
            "Cantidad de piedra que transporta: " + getCantRecPiedra()+"\r\n"+
            "Cantidad de recursos que lleva: " + (getCantRecTotal())+"\r\n";
        }
        a=a+"En este grupo están los siguientes personajes: "+"\r\n";
        for (Personaje p : personajes) {
            a=a+p.getDescribir()+"\r\n";
        }
        return a;
    }

    @Override
    public void mover(String direccion) throws ExcepcionAccionRestringidaPersonaje, ExcepcionArgumentosInternos, ExcepcionDireccionNoValida {
        Posicion posicion = getPosicion().getAdy(direccion);
        Mapa mapa = getCivilizacion().getMapa();
        Celda celdaAntigua = mapa.getCelda(getPosicion());
        int cuantoSeMueve = this.capacidadMovimiento();
        for (int i = 1; i < cuantoSeMueve; i++) {
            if (!mapa.perteneceAMapa(posicion.getAdy(direccion)) || !mapa.getCelda(posicion.getAdy(direccion)).esCeldaLibre(false)) {
                break;
            }
            posicion = posicion.getAdy(direccion);
        }

        moverGenerico(posicion, personajes.size());
        celdaAntigua.removeGrupo(this);
        actualizarPosiciones();
        mapa.getCelda(posicion).addGrupo(this);

        Juego.CONSOLA.imprimir();
        mapa.imprimirCabecera();
        mapa.imprimir(getCivilizacion());

    }

    public void recolectar(String direccion) throws ExcepcionArgumentosInternos, ExcepcionAccionRestringidaGrupo, ExcepcionArgumentosValoresIncorrectos, ExcepcionDireccionNoValida, ExcepcionNadaQueRecolectar {
        Mapa mapa = getCivilizacion().getMapa();
        if (direccion == null) {
            throw new ExcepcionDireccionNoValida("Error en recolectar.");

        }
        if (this.haySoldado) {
            throw new ExcepcionAccionRestringidaGrupo("Como hay un soldado en el grupo, este grupo no puede recolectar");

        }
        Posicion posicion = getPosicion();
        Civilizacion civilizacion = getCivilizacion();
        Posicion pos = posicion.getAdy(direccion);
        Contenedor contenedor = mapa.getCelda(pos).getContenedorRec();
        if (pos.equals(posicion)) { //error con la posicion
            return;
        }
        if (civilizacion.getMapa().getCelda(posicion).getEdificio() != null) {
            throw new ExcepcionAccionRestringidaGrupo("No se puede recolectar desde un edificio");

        }
        if (this.getCantRecTotal() == getCapRec()) {
            throw new ExcepcionAccionRestringidaGrupo(this.getNombre() + " no puede recolectar más");

        }
        if (contenedor.getRecurso() == null) {
            throw new ExcepcionNadaQueRecolectar("Error: La celda destino no es un contenedor de recursos.");
        }
        int recolectando = Math.min(getCapRec() - this.getCantRecTotal(), contenedor.procesar().getCantidad());
        if (contenedor.getRecurso().getCantidad() == 0 || contenedor.getRecurso().getCantidad() - recolectando == 0) {
            mapa.getCelda(pos).hacerPradera();
        }
        contenedor.getRecurso().setCantidad(contenedor.getRecurso().getCantidad() - recolectando);
        if (mapa.getCelda(pos).getContenedorRec().getRecurso() == null) { //si se ha vuelto pradera, imprimo
            mapa.imprimir(civilizacion);
        }
        if (contenedor instanceof Bosque) {
            Juego.CONSOLA.imprimir("Has recolectado " + recolectando + " unidades de madera");
            for (int i = 0; i < this.getPersonajes().size(); i++) {
                if (this.getPersonajes().get(i) instanceof Paisano) {
                    int loQuePuedeRec = (this.getPersonajes().get(i).getCapRec() - this.getPersonajes().get(i).getCantRecTotal());
                    if (recolectando > loQuePuedeRec) {
                        ((Paisano) this.getPersonajes().get(i)).setCantRecMadera(this.getPersonajes().get(i).getCantRecMadera() + loQuePuedeRec);
                        recolectando = recolectando - loQuePuedeRec;
                    } else {
                        ((Paisano) this.getPersonajes().get(i)).setCantRecMadera(this.getPersonajes().get(i).getCantRecMadera() + recolectando);
                        recolectando = 0;
                    }

                }
            }
        }
        if (contenedor instanceof Arbusto) {
            Juego.CONSOLA.imprimir("Has recolectado " + recolectando + " unidades de comida");
            for (int i = 0; i < this.getPersonajes().size(); i++) {
                if (this.getPersonajes().get(i) instanceof Paisano) {
                    int recolect = (this.getPersonajes().get(i).getCapRec() - this.getPersonajes().get(i).getCantRecTotal());
                    if (recolectando > recolect) {
                        ((Paisano) this.getPersonajes().get(i)).setCantRecComida(this.getPersonajes().get(i).getCantRecComida() + recolect);
                        recolectando = recolectando - recolect;
                    } else {
                        ((Paisano) this.getPersonajes().get(i)).setCantRecComida(this.getPersonajes().get(i).getCantRecComida() + recolectando);
                        recolectando = 0;
                    }

                }
            }
        } else if (contenedor instanceof Cantera) {
            Juego.CONSOLA.imprimir("Has recolectado " + recolectando + " unidades de piedra");
            for (int i = 0; i < this.getPersonajes().size(); i++) {
                if (recolectando == 0) {
                    return;
                }

                if (this.getPersonajes().get(i) instanceof Paisano) {
                    int recolect = (this.getPersonajes().get(i).getCapRec() - this.getPersonajes().get(i).getCantRecTotal());
                    if (recolectando > recolect) {
                        ((Paisano) this.getPersonajes().get(i)).setCantRecPiedra(this.getPersonajes().get(i).getCantRecPiedra() + recolect);
                        recolectando = recolectando - recolect;
                    } else {
                        ((Paisano) this.getPersonajes().get(i)).setCantRecPiedra(this.getPersonajes().get(i).getCantRecPiedra() + recolectando);
                        recolectando = 0;
                    }

                }
            }

        }
    }

    public void almacenar(String direccion) throws ExcepcionDireccionNoValida, ExcepcionArgumentosValoresIncorrectos, ExcepcionArgumentosInternos, ExcepcionAccionRestringidaPersonaje, ExcepcionEscasezRecursos, ExcepcionAccionRestringidaGrupo {
        if (this.haySoldado) {
            throw new ExcepcionAccionRestringidaGrupo("Como hay un soldado en el grupo, este grupo no puede almacenar");

        }
        almacenarGenerico(direccion);
    }

    @Override
    public void construir(String tipoC, String dir) throws ExcepcionArgumentosInternos, EscasezRecursosConstruccion, ExcepcionAccionRestringidaPersonaje, ExcepcionDireccionNoValida, ExcepcionAccionRestringidaGrupo, ExcepcionArgumentosValoresIncorrectos {
        if (tipoC == null || dir == null) {
            throw new ExcepcionDireccionNoValida("Error en consEdif.");

        }
        if (this.haySoldado) {
            throw new ExcepcionAccionRestringidaGrupo("Como hay un soldado en el grupo, este grupo no puede construir");

        }
        construirGenerico(tipoC, dir);
    }

    @Override
    public void reparar(Posicion pos) throws ExcepcionArgumentosInternos, ExcepcionAccionRestringidaPersonaje, ExcepcionEscasezRecursos, ExcepcionAccionRestringidaGrupo, ExcepcionArgumentosValoresIncorrectos {
        if (this.haySoldado) {
            throw new ExcepcionAccionRestringidaGrupo("Como hay un soldado en el grupo, este grupo no puede reparar");

        }
        repararGenerico(pos);
    }

    @Override
    public void defender(String direccion) throws ExcepcionArgumentosInternos, ExcepcionAccionRestringidaPersonaje, ExcepcionAccionRestringidaGrupo, ExcepcionEspacioInsuficiente, ExcepcionDireccionNoValida, ExcepcionArgumentosValoresIncorrectos {
        Posicion posicion = getPosicion();
        Civilizacion civilizacion = getCivilizacion();
        Posicion pos = posicion.getAdy(direccion);
        if (direccion == null || pos == null || civilizacion.getMapa() == null || !civilizacion.getMapa().perteneceAMapa(pos) || civilizacion.getMapa().getCelda(pos).getEdificio() == null) {
            throw new ExcepcionDireccionNoValida("No hay edificio en la posición indicada.");

        }
        if (civilizacion != civilizacion.getMapa().getCelda(pos).getEdificio().getCivilizacion()) {
            throw new ExcepcionAccionRestringidaGrupo("El grupo no puede entrar en el edificio de la otra civilización");

        }
        if (civilizacion.getMapa().getCelda(pos).getEdificio().getCapAloj() < this.getPersonajes().size()) {
            throw new ExcepcionEspacioInsuficiente("No se puede mover el grupo. El número " + this.getPersonajes().size() + "de componentes del grupo (" + this.getNombre() + ") supera la capacidad de alojamiento actual (" + civilizacion.getMapa().getCelda(pos).getEdificio().getCapAloj() + ") de " + civilizacion.getMapa().getCelda(pos).getEdificio().getNombre() + ".");

        }

        civilizacion.getMapa().getCelda(posicion).removeGrupo(this);
        super.setPosicion(pos);
        actualizarPosiciones();
        civilizacion.makeAdyVisible(pos);
        civilizacion.getMapa().getCelda(pos).addGrupo(this);
        civilizacion.getMapa().getCelda(pos).getEdificio().setCapAloj(-(this.getPersonajes().size()), true);
        civilizacion.getMapa().imprimirCabecera();
        civilizacion.getMapa().imprimir(civilizacion);
        Juego.CONSOLA.imprimir("El " + this.getNombre() + " ha entrado en " + civilizacion.getMapa().getCelda(pos).getEdificio().getNombre() + " (capacidad restante " + civilizacion.getMapa().getCelda(pos).getEdificio().getCapAloj() + ").");
        for (int i = 0; i < this.getPersonajes().size(); i++) {
            this.getPersonajes().get(i).recuperarVida();
        }
    }

    @Override
    public void atacar(String direccion) throws ExcepcionArgumentosInternos, ExcepcionAccionRestringidaPersonaje, ExcepcionDireccionNoValida, ExcepcionAccionRestringidaGrupo {
        if (!this.haySoldado) {
            throw new ExcepcionAccionRestringidaGrupo("El grupo no tiene soldados y no puede atacar");

        }
        atacarGenerico(direccion);
    }

    private void actualizarPosiciones() throws ExcepcionArgumentosInternos {
        for (Personaje p : personajes) {
            p.setPosicion(super.getPosicion());
        }
    }

    private void revisarVacio() throws ExcepcionArgumentosInternos, ExcepcionAccionRestringidaPersonaje {
        if (getPersonajes().size() == 1) {
            desligar(personajes.get(0));
        } else if (this.getPersonajes().isEmpty()) {
            Civilizacion civilizacion = getCivilizacion();
            Posicion posicion = getPosicion();
            civilizacion.getMapa().getCelda(posicion).getGrupos().remove(this);
            civilizacion.getGrupos().remove(getNombre());
            civilizacion.getMapa().getCelda(posicion).setHaygrupo(!civilizacion.getMapa().getCelda(posicion).getGrupos().isEmpty());
        }
    }

    @Override
    public String toString() {
        return "grupo";
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.personajes);
        hash = 97 * hash + (this.haySoldado ? 1 : 0);
        return hash;
    }

    @Override
    public void recuperarVida() {

    }

    @Override
    public void recuperar() {

    }

}
