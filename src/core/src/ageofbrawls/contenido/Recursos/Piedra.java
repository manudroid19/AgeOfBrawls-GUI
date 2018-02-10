/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ageofbrawls.contenido.Recursos;

import ageofbrawls.z.excepciones.Argumentos.ExcepcionArgumentosInternos;

/**
 *
 * @author Santiago
 */
public final class Piedra extends Recurso {

    private final int cantidadInicial;

    public Piedra(int cantidad) throws ExcepcionArgumentosInternos {
        super(cantidad);
        cantidadInicial = cantidad;
    }
    public int getCantidadInicial(){
        return cantidadInicial;
    }

    @Override
    public String toString() {
        return "piedra";
    }

}
