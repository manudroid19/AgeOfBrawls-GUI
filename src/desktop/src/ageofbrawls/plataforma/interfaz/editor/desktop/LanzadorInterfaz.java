/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ageofbrawls.plataforma.interfaz.editor.desktop;

import ageofbrawls.plataforma.CargadorNuevo;
import ageofbrawls.plataforma.Comando;
import ageofbrawls.plataforma.Juego;
import ageofbrawls.plataforma.interfaz.editor.ApoyoJuego;
import ageofbrawls.plataforma.interfaz.editor.Disenador;
import ageofbrawls.plataforma.interfaz.editor.MapaRenderer;
import ageofbrawls.z.excepciones.AccionRestringida.ExcepcionAccionRestringidaPersonaje;
import ageofbrawls.z.excepciones.Argumentos.ExcepcionArgumentosInternos;
import ageofbrawls.z.excepciones.Argumentos.ExcepcionDireccionNoValida;
import ageofbrawls.z.excepciones.Recursos.ExcepcionCorrespondenciaRecursos;
import ageofbrawls.z.excepciones.noExiste.ExcepcionNoExisteArchivo;
import ageofbrawls.z.excepciones.noExiste.ExcepcionNoExisteCivilizacion;
import ageofbrawls.z.excepciones.noExiste.ExcepcionNoExisteMapa;
import ageofbrawls.z.excepciones.noExiste.ExcepcionNoExistePosicion;
import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;
import java.awt.Image;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author mprad
 */
public class LanzadorInterfaz {

    private Image icon;

    public LanzadorInterfaz(Image icon) {
        this.icon = icon;
    }
    
    public void lanzarJuego(boolean cargar, JFrame parent) {
        try {
            JSpinner spinner = new JSpinner(new SpinnerNumberModel(20, 10, 40, 1));
            JOptionPane.showOptionDialog(null, spinner, "Introduzca la dimensión", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
            Comando juego= (Comando) new CargadorNuevo((Integer) spinner.getValue()).cargarJuego(null);
            if (cargar) {
                JFileChooser mapa = new JFileChooser();
                mapa.setDialogTitle("Selecciona el mapa");
                mapa.addChoosableFileFilter(new FileNameExtensionFilter("*.csv", "csv"));
                mapa.setAcceptAllFileFilterUsed(false);
                if (mapa.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
                    JFileChooser personajes = new JFileChooser();
                    personajes.setDialogTitle("Selecciona el archivo de personajes");
                    personajes.addChoosableFileFilter(new FileNameExtensionFilter("*.csv", "csv"));
                    personajes.setAcceptAllFileFilterUsed(false);
                    personajes.setCurrentDirectory(mapa.getCurrentDirectory());
                    if (personajes.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
                        JFileChooser edificios = new JFileChooser();
                        edificios.setDialogTitle("Selecciona el archivo de edificios");
                        edificios.addChoosableFileFilter(new FileNameExtensionFilter("*.csv", "csv"));
                        edificios.setAcceptAllFileFilterUsed(false);
                        edificios.setCurrentDirectory(personajes.getCurrentDirectory());
                        if (edificios.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
                            juego.cargarSeparado(mapa.getSelectedFile(), edificios.getSelectedFile(), personajes.getSelectedFile());
                        }
                    }
                }
            } 
            ApoyoJuego apoyo = new ApoyoJuego(juego);
            apoyo.setVisible(true);
            apoyo.setIconImage(icon);

            LwjglAWTCanvas canvas = new LwjglAWTCanvas(new MapaRenderer(juego, apoyo));
            apoyo.setCanvas(canvas.getCanvas());

        } catch (ExcepcionNoExisteArchivo | ExcepcionArgumentosInternos | ExcepcionCorrespondenciaRecursos | ExcepcionAccionRestringidaPersonaje | ExcepcionDireccionNoValida | ExcepcionNoExistePosicion | ExcepcionNoExisteMapa | ExcepcionNoExisteCivilizacion ex) {
            Juego.CONSOLA.imprimir(ex.getMensaje());
        }
    }

    public void dialogoInicial() {
        DialogoInicial intro = new DialogoInicial(this);
        intro.setVisible(true);
        intro.setIconImage(icon);
    }

    void lanzarDisenador() {
        try {
            JSpinner spinner = new JSpinner(new SpinnerNumberModel(20, 10, 40, 1));
            JOptionPane.showOptionDialog(null, spinner, "Introduzca la dimensión", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

            Comando juego = (Comando) new CargadorNuevo((Integer) spinner.getValue()).cargarJuego(null);
            Disenador dis = new Disenador(juego, icon);
            dis.setVisible(true);

            LwjglAWTCanvas canvas = new LwjglAWTCanvas(new MapaRenderer(juego, dis));
            dis.setCanvas(canvas.getCanvas());
        } catch (ExcepcionNoExisteArchivo | ExcepcionArgumentosInternos | ExcepcionCorrespondenciaRecursos | ExcepcionAccionRestringidaPersonaje | ExcepcionDireccionNoValida | ExcepcionNoExistePosicion | ExcepcionNoExisteMapa | ExcepcionNoExisteCivilizacion ex) {
            Juego.CONSOLA.imprimir(ex.getMensaje());
        }
    }
}
