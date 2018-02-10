package ageofbrawls.plataforma.interfaz.editor.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglFiles;
import javax.swing.ImageIcon;

public class DesktopLauncher {

    public static void main(String[] arg) {
        LanzadorInterfaz interfaz = new LanzadorInterfaz(new ImageIcon(new LwjglFiles().internal("icon.png").file().getAbsolutePath()).getImage());
        interfaz.dialogoInicial();
    }
    
}
