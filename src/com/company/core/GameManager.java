
package com.company.core;

import com.company.core.models.Bullet;
import com.company.core.models.Tank;
import com.company.core.models.Wall;
import com.company.core.views.BaseView;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

/**
 * Управляет логикой игры, добавлением шариков, обсчетом их движения, перерисовкой вьюшки
 */
public class GameManager implements BaseView.EventsListener, KeyListener {
    // =============================================================================================
    // CONSTANTS
    // =============================================================================================
    private static final Random RAND = new Random();

    // =============================================================================================
    // FIELDS
    // =============================================================================================
    private final BaseView view;
    // Список шариков
    private ArrayList<Tank> tanks;
    private ArrayList<Bullet> bullets;
    private ArrayList<Wall> walls;
    // Ширина и высота вьюшки
    private int width;
    private int height;
    private int laskPressedKey;
    // Запущена ли игра
    private boolean isRunning;
    // Frames per second - обновлений вьюшки в секунду
    private int fps;
    // Updates per second - обновлений мира в секунду
    private int ups;

    // =============================================================================================
    // CONSTRUCTOR
    // =============================================================================================
    public GameManager(BaseView _view) {
        view = _view;
        view.setEventsListener(this);
        tanks = new ArrayList<>();
        bullets = new ArrayList<>();
        walls = new ArrayList<>();
    }

    // =============================================================================================
    // METHODS
    // =============================================================================================

    /**
     * Запустить игру
     */
    public void start() {
        // Создаем новый поток, назначаем ему функцию и запускаем его
        new Thread(threadRunnable).start();
    }

    /**
     * Приостановить игру
     */
    public void pause() {
        // Поток умрет сам, после того как isRunning станет false
        isRunning = false;
    }

    /**
     * Функция которую исполняет поток. Она определяет когда следует вызвать пересчет мира, а когда его отрисовку.
     * Вообще говоря, внутри метода run() творится форменная магия, смысл которой вам понимать
     * не обязательно, для тех кому интересно гуглим "game loop", вот ссылки которыми руководствовался я
     * http://gameprogrammingpatterns.com/game-loop.html
     * http://entropyinteractive.com/2011/02/game-engine-design-the-game-loop/
     * http://www.java-gaming.org/index.php?topic=24220.0
     */
    private final Runnable threadRunnable = new Runnable() {
        @Override
        public void run() {
            isRunning = true;
            long lastTime = System.nanoTime();
            double delta = 0.0;
            double ns = 1000000000.0 / 60.0;
            long timer = System.currentTimeMillis();
            int updates = 0;
            int frames = 0;
            while (isRunning) {
                long now = System.nanoTime();
                delta += (now - lastTime) / ns;
                lastTime = now;
                if (delta >= 1.0) {
                    update();
                    updates++;
                    delta--;
                }
                render();
                frames++;
                if (System.currentTimeMillis() - timer > 1000) {
                    timer += 1000;
                    ups = updates;
                    fps = frames;
                    updates = 0;
                    frames = 0;
                }
            }
        }
    };

    /**
     * Добавить шарик с центром в точке с переданными координатами и случайным вектором скорости
     */
    public void addTank(int x, int y) {
        if (!isRunning) return;
        Tank tank = new Tank(60, 60);
        tank.x = x;
        tank.y = y;
        tanks.add(tank);
    }

    public void addBullet(int x, int y){
        if (!isRunning) return;
        Bullet bullet= new Bullet(10, 10);
        bullet.x=x;
        bullet.y=y;
        bullets.add(bullet);
    }
    public void addWall(int x, int y){
        if (!isRunning) return;
            Wall wall = new Wall(60, 60);
            wall.x = x;
            wall.y = y;
            walls.add(wall);

    }

    // =============================================================================================
    // UPDATING
    // =============================================================================================

    /**
     * Пересчет мира, положения столкновения и прочая физика
     */
    private void update() {
        for (int i = 0; i < tanks.size(); i++) {
            Tank t1 = tanks.get(i);
            Bullet b1 = bullets.get(i);
            // 1. Передвинем
            t1.x += t1.vx;
            t1.y += t1.vy;
            b1.x += b1.vx;
            b1.y += b1.vy;

            // 2. Обсчитаем столкновение со стенами
            // слева
            if (b1.x <= 0) {
                b1.x = t1.x;
                b1.y = t1.y;
                b1.vx = t1.vx;
                b1.vy = t1.vy;
                b1.isFlying = false;
            }
            // справа
            if (b1.x >= width) {
                b1.x = t1.x;
                b1.y = t1.y;
                b1.vx = t1.vx;
                b1.vy = t1.vy;
                b1.isFlying = false;
            }
            // сверху
            if (b1.y <= 0) {
                b1.x = t1.x;
                b1.y = t1.y;
                b1.vx = t1.vx;
                b1.vy = t1.vy;
                b1.isFlying = false;
            }
            // снизу
            if (b1.y >= height) {
                b1.x = t1.x;
                b1.y = t1.y;
                b1.vx = t1.vx;
                b1.vy = t1.vy;
                b1.isFlying = false;
            }

            if (t1.x + t1.width / 2 >= width) {
                t1.x = width - t1.width / 2;
                if (!b1.isFlying) {
                    b1.vx = 0;
                }
            }

            if (t1.x - t1.width / 2 <= 0) {
                t1.x = t1.width / 2;
                if (!b1.isFlying) {
                    b1.vx = 0;
                }
            }

            if (t1.y + t1.height / 2 >= height) {
                t1.y = height - t1.height / 2;
                if (!b1.isFlying) {
                    b1.vy = 0;
                }
            }
            if (t1.y - t1.height / 2 <= 0) {
                t1.y = t1.height / 2;
                if (!b1.isFlying) {
                    b1.vy = 0;
                }
            }

            for (int j = 0; j < walls.size(); j++) {//тут идет проверка пересечения стен с танками
                Wall w1 = walls.get(j);

                if((w1.x + w1.width / 2 >= t1.x - t1.width / 2) &&
                  (w1.x - w1.width / 2 <= t1.x + t1.width / 2) &&
                  (w1.y + w1.height / 2 >= t1.y - t1.height / 2) &&
                  (w1.y - w1.height / 2 <= t1.y + t1.height / 2)){ //Танк пересекся со стеной, но пока неизвестно с какой стороны
                    if(w1.x + w1.width / 2 >= t1.x - t1.width / 2){//Танк подъехал к стене справа
                        t1.x = w1.x + (w1.width / 2) + t1.width/2;
                    }else if(w1.x - w1.width / 2 >= t1.x + t1.width / 2){//Танк подъехал слева
                        t1.x = w1.x - (w1.width / 2) - t1.width / 2;
                    }
                }
            }
        }
    }

    /**
     * Отрисовка мира
     */
    private void render() {
        // Сообщаем вьюшке, что она должна перерисоваться. В ответ на это будет вызван метод onDraw(),
        // в котором мы и должны рисовать свои шарики, квадратики, whatever
        view.repaint();
    }

    @Override
    public void onDraw(Graphics g) {
        // Отрисовываем пулю
        for (Bullet bullet : bullets){
            bullet.draw(g);
        }
        // Отрисовываем каждый шарик
        for (Tank tank : tanks) {
            tank.draw(g);
        }
        // Отрисовка стены
        for (Wall wall  : walls){
            wall.draw(g);
        }
        // Вывод служебной информации
        g.setColor(Color.BLACK);
        g.drawString(String.format("ups=%d, fps=%d, count=%d", ups, fps, tanks.size()), 5, 16);
    }

    @Override
    public void onResize(int _width, int _height) {
        // При изменении размера вьюшки просто запоминаем новые значения, они нужны для обсчета колизий
        width = _width;
        height = _height;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        Tank t1 = tanks.get(0);
        Bullet b1=bullets.get(0);

        int code = e.getKeyCode();
        if (!t1.turningLocked) {
            switch (code) {
                case KeyEvent.VK_LEFT:
                    t1.vx = -2;
                    if(!b1.isFlying)
                        b1.vx = t1.vx;
                    t1.curDirection = Tank.Direction.LEFT;
                    t1.turningLocked = true;
                    laskPressedKey = KeyEvent.VK_LEFT;
                    break;
                case KeyEvent.VK_UP:
                    t1.vy = -2;
                    if(!b1.isFlying)
                        b1.vy = t1.vy;
                    t1.curDirection = Tank.Direction.UP;
                    t1.turningLocked = true;
                    laskPressedKey = KeyEvent.VK_UP;
                    break;
                case KeyEvent.VK_DOWN:
                    t1.vy = 2;
                    if(!b1.isFlying)
                        b1.vy = t1.vy;
                    t1.curDirection = Tank.Direction.DOWN;
                    t1.turningLocked = true;
                    laskPressedKey = KeyEvent.VK_DOWN;
                    break;
                case KeyEvent.VK_RIGHT:
                    t1.vx = 2;
                    if(!b1.isFlying)
                        b1.vx = t1.vx;
                    t1.curDirection = Tank.Direction.RIGHT;
                    t1.turningLocked = true;
                    laskPressedKey = KeyEvent.VK_RIGHT;
                    break;
                case KeyEvent.VK_SPACE:
                    if(!b1.isFlying) {
                        b1.isFlying = true;
                        switch (t1.curDirection) {
                            case LEFT:
                                b1.vx = -20;
                                break;
                            case RIGHT:
                                b1.vx = 20;
                                break;
                            case UP:
                                b1.vy = -20;
                                break;
                            case DOWN:
                                b1.vy = 20;
                                break;
                        }
                        break;
                    }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        Tank t1 = tanks.get(0);
        Bullet b1=bullets.get(0);

        int code = e.getKeyCode();
        switch(code){
            case KeyEvent.VK_LEFT:
                t1.vx = 0;
                if(!b1.isFlying)
                    b1.vx = t1.vx;
                if (t1.curDirection == Tank.Direction.LEFT){
                    t1.turningLocked = false;
                }
                break;
            case KeyEvent.VK_UP:
                t1.vy = 0;
                if(!b1.isFlying)
                    b1.vy = t1.vy;
                if (t1.curDirection == Tank.Direction.UP){
                    t1.turningLocked = false;
                }
                break;
            case KeyEvent.VK_DOWN:
                t1.vy = 0;
                if(!b1.isFlying)
                    b1.vy = t1.vy;
                if (t1.curDirection == Tank.Direction.DOWN){
                    t1.turningLocked = false;
                }
                break;
            case KeyEvent.VK_RIGHT:
                t1.vx = 0;
                if(!b1.isFlying)
                    b1.vx = t1.vx;
                if (t1.curDirection == Tank.Direction.RIGHT){
                    t1.turningLocked = false;
                }
                break;
        }
    }
}
