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
public final class Comida extends Recurso {

    public Comida(int cantidad) throws ExcepcionArgumentosInternos {
        super(cantidad);
    }

    @Override
    public String toString() {
        return "comida";
    }
    
}
