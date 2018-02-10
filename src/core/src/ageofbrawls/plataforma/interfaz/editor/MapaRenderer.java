package ageofbrawls.plataforma.interfaz.editor;

import ageofbrawls.plataforma.*;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import java.util.HashMap;

public class MapaRenderer extends ApplicationAdapter implements InputProcessor {

    private Texture img;
    private TiledMap tiledMap;
    private OrthographicCamera camera;
    private TiledMapRenderer tiledMapRenderer;
    private SpriteBatch sb;
    private HashMap<String, Sprite> sprites;
    private boolean disenando;
    private Disenador dis;

    private Comando juego;
    private ApoyoJuego apoyo;
    private int xSel = -1, ySel = -1;

    public MapaRenderer(Comando juego, ApoyoJuego apoyo) {
        this.disenando = false;
        this.juego = juego;
        this.apoyo = apoyo;
    }

    public MapaRenderer(Comando juego, Disenador dis) {
        disenando = true;
        this.dis = dis;
        this.juego = juego;
    }

    private void inicilializarElementos() {
        sprites = new HashMap<>();
        sprites.put("pradera", new Sprite(new Texture(Gdx.files.internal("pradera.png"))));
        sprites.put("arbusto", new Sprite(new Texture(Gdx.files.internal("arbusto.png"))));
        sprites.put("paisano", new Sprite(new Texture(Gdx.files.internal("paisano.png"))));
        sprites.put("grupo", new Sprite(new Texture(Gdx.files.internal("grupo.png"))));
        sprites.put("arquero", new Sprite(new Texture(Gdx.files.internal("arquero.png"))));
        sprites.put("bosque", new Sprite(new Texture(Gdx.files.internal("bosque.png"))));
        sprites.put("caballero", new Sprite(new Texture(Gdx.files.internal("caballero.png"))));
        sprites.put("casa", new Sprite(new Texture(Gdx.files.internal("casa.png"))));
        sprites.put("ciudadela", new Sprite(new Texture(Gdx.files.internal("ciudadela.png"))));
        sprites.put("cuartel", new Sprite(new Texture(Gdx.files.internal("cuartel.png"))));
        sprites.put("piedra", new Sprite(new Texture(Gdx.files.internal("piedra.png"))));
        sprites.put("legionario", new Sprite(new Texture(Gdx.files.internal("legionario.png"))));
        sprites.put("oculto", new Sprite(new Texture(Gdx.files.internal("oculto.png"))));
    }

    private void renderMap(SpriteBatch sb) {
        Mapa mapa = juego.getMapa();
        for (int i = 0; i < mapa.getFilas(); i++) {
            for (int j = 0; j < mapa.getColumnas(); j++) {
                int xRender = i;
                int yRender = mapa.getFilas() - 1 - j;
                Sprite s = new Sprite(sprites.get(juego.pngCiv(i, j, disenando)));
                s.setPosition(xRender * 32, yRender * 32);
                s.draw(sb);
                if (i == xSel && j == ySel) {
                    Sprite sa = new Sprite(new Sprite(new Texture(Gdx.files.internal("sel.png"))));
                    sa.setPosition(i * 32, (mapa.getFilas() - 1 - j) * 32);
                    sa.draw(sb);
                }
            }
        }
    }

    private void procesarMovimiento() {
        if (disenando) {
            return;
        }
        if (!apoyo.isMoviendo()) {
            return;
        }
        if (System.currentTimeMillis() / 1000 > apoyo.getEmpezadoAMover() / 1000 + 0.5) {
            apoyo.moverStep();
            apoyo.setEmpezadoAMover(System.currentTimeMillis());
        }
    }

    @Override
    public void create() {
        float w = 32 * juego.getMapa().getFilas();
        float h = 32 * juego.getMapa().getFilas();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, w, h);
        camera.update();
        tiledMap = new TmxMapLoader().load("mapaEj.tmx");

        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        Gdx.input.setInputProcessor(this);

        sb = new SpriteBatch();
        inicilializarElementos();

    }

    @Override
    public void render() {
        procesarMovimiento();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        renderMap(sb);
        sb.end();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        
        return false;
    }

    @Override
    public boolean keyTyped(char character) {

        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 clickCoordinates = new Vector3(screenX, screenY, 0);
        Vector3 position = camera.unproject(clickCoordinates);
        int posX = (int) (position.x - position.x % 32) / 32;
        int posY = (int) (position.y - position.y % 32) / 32;
        if (button == Input.Buttons.RIGHT) {
            apoyo.showPopUp(screenX, screenY, posX, posY);
        } else {
            xSel = posX;
            ySel = (-posY + juego.getMapa().getFilas() - 1);
            if (!disenando) {
                apoyo.seleccionado(posX, posY);
            } else {
                dis.seleccionado(posX, posY);
            }
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

}
