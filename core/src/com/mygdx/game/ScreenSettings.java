package com.mygdx.game;

import static com.mygdx.game.MyGdxGame.SCR_HEIGHT;
import static com.mygdx.game.MyGdxGame.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;

public class ScreenSettings implements Screen {
    MyGdxGame mgg;

    Texture imgBackGround; // фоновое изображение

    TextButton btnSound;
    TextButton btnMusic;
    TextButton btnClearTable;
    TextButton btnBack;

    public ScreenSettings(MyGdxGame g) {
        mgg = g;

        imgBackGround = new Texture("settings.jpg");
        btnSound = new TextButton(mgg.font, "Sound on", 500, 500);
        btnMusic = new TextButton(mgg.font, "Music on", 500, 400);
        btnClearTable = new TextButton(mgg.font, "Clear records", 500, 300);
        btnBack = new TextButton(mgg.font, "Back", 500, 200);
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
            if(btnSound.hit(mgg.touch.x, mgg.touch.y)){
                mgg.screenGame.soundOn = !mgg.screenGame.soundOn;
                if(mgg.screenGame.soundOn) {
                    btnSound.text = "Sound on";
                } else {
                    btnSound.text = "Sound off";
                }
            }
            if(btnMusic.hit(mgg.touch.x, mgg.touch.y)){
                mgg.screenGame.musicOn = !mgg.screenGame.musicOn;
                if(mgg.screenGame.musicOn){
                    btnMusic.text = "Music on";
                } else {
                    btnMusic.text = "Music off";
                }
            }
            if(btnClearTable.hit(mgg.touch.x, mgg.touch.y)){
                mgg.screenGame.clearTableOfRecords();
                mgg.screenGame.saveTableOfRecords();
                btnClearTable.text = "Records Cleared";
            }
            if(btnBack.hit(mgg.touch.x, mgg.touch.y)){
                mgg.setScreen(mgg.screenIntro);
                btnClearTable.text = "Clear records";
            }
        }

        // события игры


        // вывод изображений
        mgg.camera.update();
        mgg.batch.setProjectionMatrix(mgg.camera.combined);
        mgg.batch.begin();
        mgg.batch.draw(imgBackGround, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        btnSound.font.draw(mgg.batch, btnSound.text, btnSound.x, btnSound.y);
        btnMusic.font.draw(mgg.batch, btnMusic.text, btnMusic.x, btnMusic.y);
        btnClearTable.font.draw(mgg.batch, btnClearTable.text, btnClearTable.x, btnClearTable.y);
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
