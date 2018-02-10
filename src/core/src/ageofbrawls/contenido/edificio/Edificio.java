/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ageofbrawls.contenido.edificio;

import ageofbrawls.contenido.Personajes.Grupo;
import ageofbrawls.contenido.Personajes.Personaje;
import ageofbrawls.contenido.Recursos.Comida;
import ageofbrawls.contenido.Recursos.Madera;
import ageofbrawls.contenido.Recursos.Piedra;
import ageofbrawls.contenido.Recursos.Recurso;
import ageofbrawls.plataforma.Civilizacion;
import ageofbrawls.plataforma.Juego;
import ageofbrawls.plataforma.Posicion;
import ageofbrawls.z.excepciones.AccionRestringida.ExcepcionAccionRestringidaEdificio;
import ageofbrawls.z.excepciones.AccionRestringida.ExcepcionAccionRestringidaPersonaje;
import ageofbrawls.z.excepciones.Argumentos.ExcepcionArgumentosInternos;
import ageofbrawls.z.excepciones.Argumentos.ExcepcionArgumentosValoresIncorrectos;
import ageofbrawls.z.excepciones.Recursos.EscasezRecursos.EscasezRecursosCreacion;
import ageofbrawls.z.excepciones.Recursos.EscasezRecursos.ExcepcionEspacioInsuficiente;
import ageofbrawls.z.excepciones.noExiste.ExcepcionNoExisteCivilizacion;
import ageofbrawls.z.excepciones.noExiste.ExcepcionNoExisteMapa;
import ageofbrawls.z.excepciones.noExiste.ExcepcionNoExistePersonaje;
import ageofbrawls.z.excepciones.noExiste.ExcepcionNoExistePosicion;
import java.util.ArrayList;

/**
 *
 * @author Santiago
 */
public abstract class Edificio {

    private Posicion posicion;
    private Civilizacion civilizacion;
    private int capAlojDef;
    private int salud;
    private boolean destruido;
    private String nombre;

    public Edificio(Posicion posicion, String nombre, Civilizacion civilizacion) throws ExcepcionArgumentosInternos {
        if (nombre != null && civilizacion != null && posicion != null) {
            this.civilizacion = civilizacion;
            this.posicion = new Posicion(posicion);
            this.nombre = nombre;
        } else {
            throw new ExcepcionArgumentosInternos("La cantidad de un recurso no puede ser menor que 0");
        }
    }

    public Edificio(Posicion posicion, String nombre, Civilizacion civilizacion, int salud, int capAloj) throws ExcepcionArgumentosInternos {
        this(posicion, nombre, civilizacion);
        this.salud = salud;
        this.capAlojDef = capAloj;
    }

    public Posicion getPosicion() {
        return new Posicion(posicion);
    }

    public int getSalud() {
        return salud;
    }

    public Civilizacion getCivilizacion() {
        return civilizacion;
    }

    public int getCapAloj() {
        return capAlojDef;
    }

    public int getAtaque() {
        int ataque = 0;
        ArrayList<Personaje> pers = (ArrayList<Personaje>) civilizacion.getMapa().getCelda(posicion).getPersonajes().clone();
        for (Grupo g : civilizacion.getMapa().getCelda(posicion).getGrupos()) {
            pers.addAll((ArrayList<Personaje>) g.getPersonajes().clone());
        }
        for (Personaje p : pers) {
            ataque += p.danhoAtaque();
        }
        return ataque;
    }

    public int getDefensa() {
        int defensa = 0;
        ArrayList<Personaje> pers = (ArrayList<Personaje>) civilizacion.getMapa().getCelda(posicion).getPersonajes().clone();
        for (Grupo g : civilizacion.getMapa().getCelda(posicion).getGrupos()) {
            pers.addAll((ArrayList<Personaje>) g.getPersonajes().clone());
        }
        for (Personaje p : pers) {
            defensa += p.getDefensa();
        }
        return defensa;
    }

    public int getCapAlmacen() {
        return civilizacion.getCapAlmacen();
    }

    public boolean estaDestruido() {
        return destruido;
    }

    public String getNombre() {
        return nombre;
    }

    public abstract int MaxVida();

    public void setCapAloj(int aloj, boolean relative) throws ExcepcionArgumentosInternos {
        if (relative) {
            if (capAlojDef + aloj < 0) {
                throw new ExcepcionArgumentosInternos("La capacidad de alojamiento no puede ser menor que 0");
            }
            capAlojDef += aloj;
        } else {
            if (aloj < 0) {
                throw new ExcepcionArgumentosInternos("La capacidad de alojamiento no puede ser menor que 0");
            }
            capAlojDef = aloj;
        }
    }

    public void setSalud(int valor) throws ExcepcionArgumentosInternos {
        if (valor < 0) {
            throw new ExcepcionArgumentosInternos("La salud no puede ser menor que 0");
        } else {
            this.salud = valor;
        }
    }

    public void atacar(Personaje[] personajes) throws ExcepcionNoExistePersonaje, ExcepcionArgumentosInternos, ExcepcionAccionRestringidaPersonaje {
        if(personajes.length==0){
            throw new ExcepcionNoExistePersonaje("No hay personajes a los que atacar");
        }
        int ataque = this.getAtaque();
        int quitados = ataque/personajes.length;
        for (Personaje atacado : personajes) {
            atacado.setSalud(-quitados, true);
            if (atacado.getSalud() <= 0) {
                if (atacado.getGrupo() != null) {
                    atacado.getGrupo().desligar(atacado);
                }
                civilizacion.getMapa().getCelda(atacado.getPosicion()).getPersonajes().remove(atacado);
                civilizacion.getPersonajes().remove(atacado.getNombre());
                civilizacion.getMapa().imprimirCabecera();
                civilizacion.getMapa().imprimir(civilizacion);
                Juego.CONSOLA.imprimir("Has inflingido " + quitados + " de daño al personaje " + atacado.getNombre() + " de la celda " + atacado.getPosicion().toStringMapa() + " (civ " + atacado.getCivilizacion().getNombre() + ").");
                Juego.CONSOLA.imprimir("El personaje: " + atacado.getNombre() + " ha muerto");
            } else {
                Juego.CONSOLA.imprimir("Has inflingido " + quitados + " de daño al personaje " + atacado.getNombre() + " de la celda " + atacado.getPosicion().toStringMapa() + " (civ " + atacado.getCivilizacion().getNombre() + ").");
            }
        }
        
    }

    public void almacenar(Recurso recurso) throws ExcepcionArgumentosValoresIncorrectos {
        if (recurso instanceof Madera) {
            civilizacion.setMadera(recurso.getCantidad(), true);
        }else if (recurso instanceof Piedra) {
            civilizacion.setPiedra(recurso.getCantidad(), true);
        }else if (recurso instanceof Comida) {
            civilizacion.setAlimentos(recurso.getCantidad(), true);
        }
    }

    public Personaje crear(String tipo_personaje) throws ExcepcionAccionRestringidaEdificio, ExcepcionArgumentosInternos, ExcepcionNoExisteMapa, ExcepcionEspacioInsuficiente, EscasezRecursosCreacion, ExcepcionNoExistePosicion, ExcepcionArgumentosValoresIncorrectos {
        throw new ExcepcionAccionRestringidaEdificio("Este edificio no puede crear personajes");
    }

    @Override
    public String toString() {
        return "edificio";
    }

    public void describirEdificio() {
        Juego.CONSOLA.imprimir("Tipo: " + this.toString());
        Juego.CONSOLA.imprimir("Salud: " + salud);
        Juego.CONSOLA.imprimir("Capacidad de Alojamiento para defenderlo " + capAlojDef);
        Juego.CONSOLA.imprimir("Capacidad de ataque: " + getAtaque());
        Juego.CONSOLA.imprimir("Capacidad de defensa: " + getDefensa());
        Juego.CONSOLA.imprimir("Nombre: " + nombre);
        Juego.CONSOLA.imprimir("Civilizacion: " + civilizacion.getNombre());
    }
    public String getDescribir() {
        return "Edificio: " + this.toString()+"\r\n"+
        "Salud: " + salud+"\r\n"+
        "Alojamiento defensivo restante: " + capAlojDef+"\r\n"+
        "Capacidad de ataque: " + getAtaque()+"\r\n"+
        "Capacidad de defensa: " + getDefensa()+"\r\n"+
        "Nombre: " + nombre+"\r\n"+
        "Civilizacion: " + civilizacion.getNombre();
    }

    public void danar(int dano) throws ExcepcionArgumentosInternos {
        if (dano >= 0) {
            if (salud - dano > 0) {
                salud -= dano;
            } else {
                salud = 0;
                destruido = true;
                civilizacion.getMapa().getCelda(posicion).setEdificio(null);
                civilizacion.getEdificios().remove(this.getNombre());
                civilizacion.getMapa().imprimirCabecera();
                civilizacion.getMapa().imprimir(civilizacion);
                Juego.CONSOLA.imprimir("El edificio " + this.getNombre() + " ha sido destruido");

            }
        } else {
            throw new ExcepcionArgumentosInternos("El daño a un edificio no puede ser menor que 0");
        }
    }

    public void reparar() {
        salud = MaxVida();
    }

}
