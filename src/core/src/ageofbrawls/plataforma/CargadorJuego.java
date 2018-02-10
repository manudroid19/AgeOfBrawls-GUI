/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ageofbrawls.plataforma;

import ageofbrawls.z.excepciones.AccionRestringida.ExcepcionAccionRestringidaPersonaje;
import ageofbrawls.z.excepciones.Argumentos.ExcepcionArgumentosInternos;
import ageofbrawls.z.excepciones.Argumentos.ExcepcionDireccionNoValida;
import ageofbrawls.z.excepciones.Recursos.ExcepcionCorrespondenciaRecursos;
import ageofbrawls.z.excepciones.noExiste.ExcepcionNoExisteArchivo;
import ageofbrawls.z.excepciones.noExiste.ExcepcionNoExisteCivilizacion;
import ageofbrawls.z.excepciones.noExiste.ExcepcionNoExisteMapa;
import ageofbrawls.z.excepciones.noExiste.ExcepcionNoExistePosicion;

/**
 *
 * @author mprad
 */
public interface CargadorJuego {
    public Juego cargarJuego(Juego juegoIn) throws ExcepcionNoExisteArchivo, ExcepcionDireccionNoValida,ExcepcionNoExistePosicion,ExcepcionNoExisteMapa,ExcepcionNoExisteCivilizacion,ExcepcionArgumentosInternos, ExcepcionCorrespondenciaRecursos, ExcepcionAccionRestringidaPersonaje;
}
