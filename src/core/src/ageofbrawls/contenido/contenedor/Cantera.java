/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ageofbrawls.contenido.contenedor;

import ageofbrawls.contenido.Recursos.Piedra;
import ageofbrawls.contenido.Recursos.Recurso;
import ageofbrawls.z.excepciones.Argumentos.ExcepcionArgumentosInternos;
import ageofbrawls.z.excepciones.Recursos.ExcepcionCorrespondenciaRecursos;

/**
 *
 * @author Santiago
 */
public final class Cantera extends Contenedor {

    public Cantera(Piedra recurso) throws ExcepcionCorrespondenciaRecursos {
        super(recurso);
    }

    public Cantera(Recurso recurso, String nombre) throws ExcepcionCorrespondenciaRecursos {
        super(recurso);
        super.setNombre(nombre);
    }

    @Override
    public void describirContenedorRecurso() {
        super.describirContenedorRecurso();
        System.out.println("Cantidad de piedra: " + this.getRecurso().getCantidad());
    }

    @Override
    public String getDescribir() {
        return super.getDescribir() + "cantera\r\n"
                + "Cantidad de piedra: " + this.getRecurso().getCantidad();
    }

    @Override
    public Recurso procesar() throws ExcepcionArgumentosInternos {
        Piedra rec = (Piedra) super.procesar();
        double cantidad = (double) getRecurso().getCantidad();
        double porcentajeRestante = (rec.getCantidadInicial() - cantidad) / rec.getCantidadInicial();
        double porcentajeProcesable = 0.5 * porcentajeRestante + 0.3;
        int cantidadProcesable = Math.max(1, (int) Math.floor(cantidad * porcentajeProcesable));
        rec.setCantidad(cantidadProcesable);
        return rec;
    }

    @Override
    protected void checkTipoRecurso() throws ExcepcionCorrespondenciaRecursos {
        if (!(super.getRecurso() instanceof Piedra)) {
            throw new ExcepcionCorrespondenciaRecursos("No corresponde el contenedor " + toString() + " con el recurso insertado");
        }
    }

    @Override
    public boolean esTransitable() {
        return false;
    }

    @Override
    public String toString() {
        return "cantera";
    }

}
