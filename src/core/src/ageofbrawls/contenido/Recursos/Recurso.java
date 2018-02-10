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
public abstract class Recurso {

    private int cantidad;

    public Recurso(int cantidad) throws ExcepcionArgumentosInternos {
        if (cantidad >= 0) {
            this.cantidad = cantidad;
        } else {
            throw new ExcepcionArgumentosInternos("La cantidad de un recurso no puede ser menor que 0");
        }

    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) throws ExcepcionArgumentosInternos {
        if (cantidad >= 0) {
            this.cantidad = cantidad;
        } else {
            throw new ExcepcionArgumentosInternos("La cantidad de un recurso no puede ser menor que 0");
        }
    }

    public Recurso clonar() throws ExcepcionArgumentosInternos {
        if(this instanceof Comida){
            return (Recurso) new Comida(cantidad);
        }else if(this instanceof Piedra){
            return (Recurso) new Piedra(cantidad);
        }else if(this instanceof Madera){
            return (Recurso) new Madera(cantidad);
        }else{
            return null;
        }
    }

    @Override
    public String toString() {
        return "recurso";
    }

}
