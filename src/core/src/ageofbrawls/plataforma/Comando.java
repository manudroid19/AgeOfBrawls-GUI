/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ageofbrawls.plataforma;

import ageofbrawls.z.excepciones.AccionRestringida.ExcepcionAccionRestringidaEdificio;
import ageofbrawls.z.excepciones.AccionRestringida.ExcepcionAccionRestringidaGrupo;
import ageofbrawls.z.excepciones.AccionRestringida.ExcepcionAccionRestringidaPersonaje;
import ageofbrawls.z.excepciones.Argumentos.*;
import ageofbrawls.z.excepciones.Recursos.EscasezRecursos.ExcepcionEscasezRecursos;
import ageofbrawls.z.excepciones.Recursos.EscasezRecursos.ExcepcionNadaQueRecolectar;
import ageofbrawls.z.excepciones.Movimiento.*;
import ageofbrawls.z.excepciones.Recursos.EscasezRecursos.EscasezRecursosConstruccion;
import ageofbrawls.z.excepciones.Recursos.EscasezRecursos.EscasezRecursosCreacion;
import ageofbrawls.z.excepciones.Recursos.EscasezRecursos.ExcepcionEspacioInsuficiente;
import ageofbrawls.z.excepciones.Recursos.ExcepcionCorrespondenciaRecursos;
import ageofbrawls.z.excepciones.noExiste.*;
import java.io.File;

/**
 *
 * @author Santiago
 */
public interface Comando {

    public void agrupar(String donde) throws ExcepcionNoExistePosicion, ExcepcionArgumentosInternos, ExcepcionAccionRestringidaPersonaje;

    public void almacenar(String personaje, String direccion) throws ExcepcionNoExisteSujeto, ExcepcionAccionRestringidaGrupo, ExcepcionArgumentosValoresIncorrectos, ExcepcionDireccionNoValida, ExcepcionArgumentosInternos, ExcepcionAccionRestringidaPersonaje, ExcepcionEscasezRecursos;

    public void atacar(String personaje, String direccion) throws ExcepcionNoExisteSujeto, ExcepcionArgumentosInternos, ExcepcionAccionRestringidaPersonaje, ExcepcionDireccionNoValida, ExcepcionAccionRestringidaGrupo;

    public void cambiar(String destino) throws ExcepcionNoExisteCivilizacion;

    public void cargar(String ruta) throws ExcepcionNoExisteArchivo, ExcepcionArgumentosInternos, ExcepcionCorrespondenciaRecursos, ExcepcionAccionRestringidaPersonaje;

    public void civilizacion();

    public void construir(String constructor, String tipo, String dir) throws ExcepcionArgumentosInternos, ExcepcionArgumentosValoresIncorrectos, ExcepcionAccionRestringidaGrupo, ExcepcionNoExisteSujeto, ExcepcionAccionRestringidaPersonaje, ExcepcionDireccionNoValida, EscasezRecursosConstruccion;

    public void crear(String edificio, String tipo) throws ExcepcionNoExisteMapa, ExcepcionArgumentosValoresIncorrectos, ExcepcionNoExisteEdificio, ExcepcionAccionRestringidaEdificio, ExcepcionEspacioInsuficiente, EscasezRecursosCreacion, ExcepcionNoExistePosicion, ExcepcionArgumentosInternos;

    public void defender(String defensor, String direccion) throws ExcepcionNoExisteSujeto, ExcepcionAccionRestringidaGrupo, ExcepcionArgumentosValoresIncorrectos, ExcepcionDireccionNoValida, ExcepcionArgumentosInternos, ExcepcionAccionRestringidaPersonaje, ExcepcionEspacioInsuficiente;

    public void desagrupar(String grupo) throws ExcepcionNoExisteGrupo, ExcepcionAccionRestringidaPersonaje;

    public void describir(String sujeto) throws ExcepcionNoExisteSujeto;

    public void desligar(String desligado, String grupo) throws ExcepcionNoExisteSujeto, ExcepcionNoExisteGrupo, ExcepcionArgumentosInternos, ExcepcionAccionRestringidaPersonaje;

    public void guardar(String ruta) throws ExcepcionNoExisteArchivo;

    public void imprimirCabecera();

    public void imprimirMapa();

    public void cargarSeparado(File mapa, File edificios, File personajes);

    public void listarCivilizaciones();

    public void listarEdificios();

    public void listarGrupos();

    public void listarPersonajes();

    public void manejar(String quien) throws ExcepcionNoExisteSujeto, ExcepcionAccionRestringidaPersonaje, ExcepcionArgumentosInternos;

    public void mirar(String donde) throws ExcepcionAccionRestringidaPersonaje, ExcepcionArgumentosInternos;

    public void mover(String persona, String direccion) throws ExcepcionNoExisteSujeto, ExcepcionAccionRestringidaPersonaje, ExcepcionArgumentosInternos, ExcepcionDireccionNoValida, ExcepcionNoTransitable, ExcepcionFueraDeLimites;

    public void recolectar(String recolector, String direccion) throws ExcepcionNoExisteSujeto, ExcepcionAccionRestringidaPersonaje, ExcepcionArgumentosValoresIncorrectos, ExcepcionNadaQueRecolectar, ExcepcionAccionRestringidaGrupo, ExcepcionDireccionNoValida, ExcepcionArgumentosInternos;

    public void reparar(String reparador, String direccion) throws ExcepcionNoExisteSujeto, ExcepcionArgumentosValoresIncorrectos, ExcepcionDireccionNoValida, ExcepcionAccionRestringidaGrupo, ExcepcionEscasezRecursos, ExcepcionArgumentosInternos, ExcepcionAccionRestringidaPersonaje;

    public Mapa getMapa();

    public String getActiva();

    public String pngCiv(int i, int j, boolean disenando);

}
