/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ageofbrawls.contenido.edificio;

import ageofbrawls.contenido.Personajes.Paisano;
import ageofbrawls.contenido.Personajes.Personaje;
import ageofbrawls.plataforma.Civilizacion;
import ageofbrawls.plataforma.Juego;
import ageofbrawls.plataforma.Posicion;
import ageofbrawls.z.excepciones.Argumentos.ExcepcionArgumentosInternos;
import ageofbrawls.z.excepciones.Argumentos.ExcepcionArgumentosValoresIncorrectos;
import ageofbrawls.z.excepciones.Recursos.EscasezRecursos.EscasezRecursosCreacion;
import ageofbrawls.z.excepciones.Recursos.EscasezRecursos.ExcepcionEspacioInsuficiente;
import ageofbrawls.z.excepciones.noExiste.ExcepcionNoExisteMapa;
import ageofbrawls.z.excepciones.noExiste.ExcepcionNoExistePosicion;

/**
 *
 * @author Santiago
 */
public final class Ciudadela extends Edificio {

    public Ciudadela(Posicion posicion, String nombre, Civilizacion civilizacion) throws ExcepcionArgumentosInternos {
        super(posicion, nombre, civilizacion, 1000, 10);
    }

    @Override
    public Personaje crear(String tipo) throws ExcepcionEspacioInsuficiente, EscasezRecursosCreacion, ExcepcionNoExistePosicion, ExcepcionNoExisteMapa, ExcepcionArgumentosInternos, ExcepcionArgumentosValoresIncorrectos {
        Civilizacion civilizacion = super.getCivilizacion();
        if (civilizacion.getPersonajes().size() >= civilizacion.contarEdificios(Casa.class) * Casa.CAPALOJ) {
            throw new ExcepcionEspacioInsuficiente("No hay suficiente espacio para crear personajes", civilizacion.getPersonajes().size());
        }
        if (civilizacion.getAlimentos() < 50) {
            throw new EscasezRecursosCreacion("No hay suficientes recursos para crear un personaje.", 50 - civilizacion.getAlimentos());
        }
        Posicion pos = super.getPosicion().posicionAdyacenteLibre(civilizacion.getMapa());
        if (pos == super.getPosicion()) {
            throw new ExcepcionNoExistePosicion("No hay posiciones adyacentes libres");
        }
        int i = 1;
        String nombrePers = "paisano1";
        while (civilizacion.getPersonajes().containsKey(nombrePers)) {
            nombrePers = nombrePers.replace("paisano" + i, "paisano" + (++i));
        }
        Personaje person = new Paisano(pos, nombrePers, civilizacion);
        civilizacion.setAlimentos(-50, true);
        return person;

    }
    
    @Override
    public void describirEdificio() {
        super.describirEdificio();
        Juego.CONSOLA.imprimir("Recursos: " + getCivilizacion().getMadera() + " de madera, " + getCivilizacion().getPiedra() + " de piedra y " + getCivilizacion().getAlimentos() + " de alimentos");
    }

    public void danar(int dano) throws ExcepcionArgumentosInternos {
        super.danar(dano);
        if (dano > 0 && this.getSalud() - dano <= 0) {
            getCivilizacion().quitarCiudadela();
        }

    }

    @Override
    public int MaxVida() {
        return 1000;
    }

    @Override
    public String toString() {
        return "ciudadela";
    }

}
