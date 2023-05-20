package com.mygdx.game;

import static com.mygdx.game.MyGdxGame.SCR_HEIGHT;
import static com.mygdx.game.MyGdxGame.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;

public class ScreenAbout implements Screen {
    MyGdxGame mgg;

    Texture imgBackGround; // фоновое изображение

    TextButton btnBack;
    String textAboutGame =  "Эта игра создана в рамках курса\n" +
                            "Mbile Game Development на языке\n" +
                            "java с использованием фреймворка\n" +
                            "LibGDX.\n" +
                            "Цель игры - как можно быстрее\n" +
                            "сбить всех комаров. Победитель\n" +
                            "попадает в таблицу рекордов.";

    public ScreenAbout(MyGdxGame g) {
        mgg = g;

        imgBackGround = new Texture("swamp3.jpg");

        btnBack = new TextButton(mgg.font, "Back", 500, 100);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // касания экрана
        if(Gdx.input.justTouched()){
            mgg.touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            mgg.camera.unproject(mgg.touch);

            if(btnBack.hit(mgg.touch.x, mgg.touch.y)){
                mgg.setScreen(mgg.screenIntro);
            }
        }

        // события игры


        // вывод изображений
        mgg.camera.update();
        mgg.batch.setProjectionMatrix(mgg.camera.combined);
        mgg.batch.begin();
        mgg.batch.draw(imgBackGround, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        mgg.font.draw(mgg.batch, textAboutGame, 100, 600);
        btnBack.font.draw(mgg.batch, btnBack.text, btnBack.x, btnBack.y);
        mgg.batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        imgBackGround.dispose();
    }
}
