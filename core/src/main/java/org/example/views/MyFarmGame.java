//package org.example.views;
//
//import com.badlogic.gdx.ApplicationAdapter;
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.graphics.GL20;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import org.example.controllers.GameManager;
//import org.example.models.MyGame;
//import org.example.views.GameMapRenderer;
//
//public class MyFarmGame extends ApplicationAdapter {
//    private SpriteBatch batch;
//    private GameMapRenderer mapRenderer;
//
//    @Override
//    public void create() {
//        batch = new SpriteBatch();
////        GameManager.startGame(); // ستاپ اولیه‌ی بازی، بازیکنان و نقشه
//        mapRenderer = new GameMapRenderer();
//    }
//
//    @Override
//    public void render() {
//        Gdx.gl.glClearColor(0, 0.6f, 0, 1);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//
//        MyGame.getCurrentPlayer().update();
//
//        batch.begin();
//        mapRenderer.render(batch);
//        batch.end();
//    }
//
//    @Override
//    public void dispose() {
//        batch.dispose();
//        mapRenderer.dispose();
//    }
//}
