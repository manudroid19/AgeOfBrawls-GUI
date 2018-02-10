/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ageofbrawls.contenido.Personajes.Soldados;

import ageofbrawls.contenido.Personajes.Soldado;
import ageofbrawls.plataforma.Civilizacion;
import ageofbrawls.plataforma.Posicion;

/**
 *
 * @author mprad
 */
public final class Caballero extends Soldado{

    public Caballero(Posicion posicion, String nombre, Civilizacion civilizacion) {
        super(posicion, nombre, civilizacion);
    }
    
    @Override
    public int capacidadMovimiento(){
        return 2;
    }
    @Override
    public String toString(){
        return "caballero";
    }
}
