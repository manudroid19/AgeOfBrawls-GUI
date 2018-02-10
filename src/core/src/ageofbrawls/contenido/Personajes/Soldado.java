/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ageofbrawls.contenido.Personajes;

import ageofbrawls.plataforma.Civilizacion;
import ageofbrawls.plataforma.Juego;
import ageofbrawls.plataforma.Posicion;
import ageofbrawls.z.excepciones.AccionRestringida.ExcepcionAccionRestringidaPersonaje;
import ageofbrawls.z.excepciones.Argumentos.ExcepcionArgumentosInternos;
import ageofbrawls.z.excepciones.Argumentos.ExcepcionArgumentosValoresIncorrectos;
import ageofbrawls.z.excepciones.Argumentos.ExcepcionDireccionNoValida;
import java.util.ArrayList;

/**
 *
 * @author mprad
 */
public abstract class Soldado extends Personaje {

    private int ataque;

    public Soldado(Posicion posicion, String nombre, Civilizacion civilizacion) {
        super(posicion, nombre, civilizacion, 200, 100);//defensa salud
        ataque = 70;
    }

    @Override
    public int danhoAtaque() {
        return ataque;
    }

    public void setAtaque(int ataque) {
        this.ataque = ataque;
    }

    public void describir() {
        super.describir();
        Juego.CONSOLA.imprimir("Ataque :" + ataque);
    }
    public String getDescribir() {
        return super.getDescribir()+"\r\n"+
        "Ataque :" + ataque;
    }

    @Override
    public void atacar(String direccion) throws ExcepcionArgumentosInternos, ExcepcionAccionRestringidaPersonaje, ExcepcionDireccionNoValida {
        if (super.getGrupo() != null) {
            throw new ExcepcionAccionRestringidaPersonaje("El personaje no puede atacar ya que pertenece a un grupo");
            
        }
        atacarGenerico(direccion);
    }

    @Override
    public String toString() {
        return "soldado";
    }

    @Override
    public void recuperarVida() throws ExcepcionArgumentosInternos, ExcepcionAccionRestringidaPersonaje, ExcepcionArgumentosValoresIncorrectos {
        Civilizacion civilizacion = super.getCivilizacion();
        int puntosARecuperar = 100 - this.getSalud();
        if (puntosARecuperar == 0) {
            throw new ExcepcionAccionRestringidaPersonaje("El personaje tiene toda la vida");
            
        }
        int costeAlimento = (int) (puntosARecuperar * 0.8);
        if (civilizacion.getAlimentos() < costeAlimento) {
            int puntosRecuperados = (int) (civilizacion.getAlimentos() / 0.8);
            super.setSalud(puntosRecuperados, true);
            civilizacion.setAlimentos(0, false);
            return;
        }
        civilizacion.setAlimentos(-costeAlimento, true);
        this.recuperar();
        Juego.CONSOLA.imprimir("Coste de la recuperación de la vida: " + costeAlimento + " unidades de alimento de la ciudadela.");
    }

    @Override
    public void recuperar() throws ExcepcionArgumentosInternos {
        super.setSalud(50, false);
    }
}
