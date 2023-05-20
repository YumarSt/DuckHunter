package com.mygdx.game;

import static com.mygdx.game.MyGdxGame.SCR_HEIGHT;
import static com.mygdx.game.MyGdxGame.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;

public class ScreenGame implements Screen {
    MyGdxGame mgg;

    Texture[] imgKomar = new Texture[11]; // ссылки на изображения
    Texture imgBackGround; // фоновое изображение
    Texture imgBtnExit;
    Texture imgBtnSndOn, imgBtnSndOff;
    //Texture imgBtnPause, imgBtnPlay;

    Sound[] sndKomar = new Sound[4];
    Music sndMusic;

    // логические переменные
    boolean soundOn = true;
    boolean musicOn = true;

    // кнопки интерфейса игры
    MosButton btnExit;

    // создаём массив ссылок на объекты комаров
    Mosquito[] komar = new Mosquito[35];
    int kills;

    // переменные для работы с таймером
    long timeStartGame, timeCurrently;

    // состояния игры
    public static final int PLAY_GAME = 1, ENTER_NAME = 2, SHOW_TABLE = 3;
    int situation;

    String name;
    long time;

    // таблица рекордов
    Player[] players = new Player[8];

    public ScreenGame(MyGdxGame g) {
        mgg = g;

        // загружаем картинки
        for(int i=0; i<imgKomar.length; i++) {
            imgKomar[i] = new Texture("mosq"+i+".png"); // создать объект-картинку и загрузить в него изображение
        }
        imgBackGround = new Texture("swamp0.jpg");
        imgBtnExit = new Texture("exit.png");
        imgBtnSndOn = new Texture("sndon.png");
        imgBtnSndOff = new Texture("sndoff.png");

        // загружаем звуки
        for(int i=0; i<sndKomar.length; i++) {
            sndKomar[i] = Gdx.audio.newSound(Gdx.files.internal("mos"+i+".mp3"));
        }
        sndMusic = Gdx.audio.newMusic(Gdx.files.internal("jinglebells.mp3"));
        sndMusic.setLooping(true);
        sndMusic.setVolume(0.5f);

        // создаём кнопки
        btnExit = new MosButton(SCR_WIDTH -60, SCR_HEIGHT -60, 50);

        for (int i = 0; i < players.length; i++) {
            players[i] = new Player("noname", 0);
        }
        loadTableOfRecords();
    }

    @Override
    public void show() {
        gameStart();
    }

    @Override
    public void render(float delta) {
        // касания экрана
        if(Gdx.input.justTouched()){
            mgg.touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            mgg.camera.unproject(mgg.touch);
            if(situation == PLAY_GAME) {
                for (int i = komar.length - 1; i >= 0; i--) {
                    if (komar[i].isAlive && komar[i].hit(mgg.touch.x, mgg.touch.y)) {
                        kills++;
                        if (soundOn) {
                            sndKomar[MathUtils.random(0, 3)].play();
                        }
                        if (kills == komar.length) {
                            situation = ENTER_NAME;
                        }
                        break;
                    }
                }
            }
            if(situation == SHOW_TABLE){
                gameStart();
            }
            if(situation == ENTER_NAME){
                mgg.keyboard.hit(mgg.touch.x, mgg.touch.y);
                if(mgg.keyboard.endOfEdit()){
                    situation = SHOW_TABLE;
                    players[players.length-1].name = mgg.keyboard.getText();
                    players[players.length-1].time = timeCurrently;
                    sortTableOfRecords();
                    saveTableOfRecords();
                }
            }

            // нажатия на экранные кнопки
            if(btnExit.hit(mgg.touch.x, mgg.touch.y)){
                mgg.setScreen(mgg.screenIntro); // выход из игры
            }
        }

        // события игры
        for(int i=0; i<komar.length; i++) {
            komar[i].fly();
        }
        if(situation == PLAY_GAME) {
            timeCurrently = TimeUtils.millis() - timeStartGame;
        }

        // вывод изображений
        mgg.camera.update();
        mgg.batch.setProjectionMatrix(mgg.camera.combined);
        mgg.batch.begin();
        mgg.batch.draw(imgBackGround, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        for(int i=0; i<komar.length; i++) {
            mgg.batch.draw(imgKomar[komar[i].faza], komar[i].x, komar[i].y, komar[i].width, komar[i].height, 0, 0, 500, 500, komar[i].isFlip(), false);
        }
        mgg.batch.draw(imgBtnExit, btnExit.x, btnExit.y, btnExit.width, btnExit.height);

        mgg.font.draw(mgg.batch, "KILLS: "+kills, 10, SCR_HEIGHT -10);
        mgg.font.draw(mgg.batch, "TIME: "+timeToString(timeCurrently), SCR_WIDTH -450, SCR_HEIGHT -10);
        if(situation == ENTER_NAME){
            mgg.keyboard.draw(mgg.batch);
        }
        if(situation == SHOW_TABLE){
            for (int i = 0; i < players.length-1; i++) {
                mgg.font.draw(mgg.batch, players[i].name+"...."+timeToString(players[i].time), SCR_WIDTH/3, SCR_HEIGHT*3/4-i*50);
            }
        }
        mgg.batch.end();
    }

    String timeToString(long t){
        String sec = "" + t/1000%60/10 + t/1000%60%10;
        String min = "" + t/1000/60/10 + t/1000/60%10;
        return min+":"+sec;
    }

    void gameStart() {
        situation = PLAY_GAME;
        kills = 0;
        // создаём объекты комаров
        for(int i=0; i<komar.length; i++){
            komar[i] = new Mosquito();
        }
        // включаем музыку
        if(musicOn) {
            sndMusic.play();
        }

        // узнаём время старта игры
        timeStartGame = TimeUtils.millis();
    }

    void gameOver(){

    }

    void sortTableOfRecords(){
        for (int i = 0; i < players.length; i++) {
            if(players[i].time == 0) players[i].time = 1000000000;
        }
        for (int j = 0; j < players.length; j++) {
            for (int i = 0; i < players.length - 1; i++) {
                if (players[i].time > players[i + 1].time) {
                    long z = players[i].time;
                    players[i].time = players[i + 1].time;
                    players[i + 1].time = z;
                    String s = players[i].name;
                    players[i].name = players[i + 1].name;
                    players[i + 1].name = s;
                }
            }
        }
        for (int i = 0; i < players.length; i++) {
            if(players[i].time == 1000000000) players[i].time = 0;
        }
    }

    void saveTableOfRecords() {
        Preferences preferences = Gdx.app.getPreferences("TaleOfRecords");
        for (int i = 0; i < players.length; i++) {
            preferences.putString("name"+i, players[i].name);
            preferences.putLong("time"+i, players[i].time);
        }
        preferences.flush();
    }

    void loadTableOfRecords() {
        Preferences preferences = Gdx.app.getPreferences("TaleOfRecords");
        for (int i = 0; i < players.length; i++) {
            if(preferences.contains("name"+i)) players[i].name = preferences.getString("name"+i, "none");
            if(preferences.contains("time"+i)) players[i].time = preferences.getLong("time"+i, 0);
        }
    }

    void clearTableOfRecords() {
        for (int i = 0; i < players.length; i++) {
            players[i].name = "noname";
            players[i].time = 0;
        }
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
        sndMusic.stop();
    }

    @Override
    public void dispose() {
        for (int i = 0; i < imgKomar.length; i++) {
            imgKomar[i].dispose();
        }
        for (int i = 0; i < sndKomar.length; i++) {
            sndKomar[i].dispose();
        }
        imgBackGround.dispose();
        imgBtnExit.dispose();
        sndMusic.dispose();
    }
}
