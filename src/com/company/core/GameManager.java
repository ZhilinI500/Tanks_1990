
package com.company.core;

import com.company.core.models.Ball;
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
    private ArrayList<Ball> balls;
    // Ширина и высота вьюшки
    private int width;
    private int height;
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
        balls = new ArrayList<>();
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
    public void addBall(int x, int y) {
        if (!isRunning) return;
        Ball ball = new Ball(10);
        ball.x = x;
        ball.y = y;
        while (ball.vx == 0) {
            ball.vx = RAND.nextInt(10) - 5;
        }
        while (ball.vy == 0) {
            ball.vy = RAND.nextInt(10) - 5;
        }
        balls.add(ball);
    }

    // =============================================================================================
    // UPDATING
    // =============================================================================================

    /**
     * Пересчет мира, положения столкновения и прочая физика
     */
    private void update() { // Переписать тело ифов
        // Для каждого шарика
        for (int i = 0; i < balls.size(); i++) {
            Ball b1 = balls.get(i);
            // 1. Передвинем
            //b1.x += b1.vx;
            //b1.y += b1.vy;

            // 2. Обсчитаем столкновение со стенами
            // слева
            /*if (b1.x - b1.r <= 0) {
                b1.vx *= -1;
                b1.x = b1.r;
            }
            // справа
            if (b1.x + b1.r >= width) {
                b1.vx *= -1;
                b1.x = width - b1.r;
            }
            // сверху
            if (b1.y - b1.r <= 0) {
                b1.vy *= -1;
                b1.y = b1.r;
            }
            // снизу
            if (b1.y + b1.r >= height) {
                b1.vy *= -1;
                b1.y = height - b1.r;
            }*/

            // 3. Обсчитаем столкновение друг с другом
            // TODO implement
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
        // Отрисовываем каждый шарик
        for (Ball ball : balls) {
            ball.draw(g);
        }
        // Вывод служебной информации
        g.setColor(Color.BLACK);
        g.drawString(String.format("ups=%d, fps=%d, count=%d", ups, fps, balls.size()), 5, 16);
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

        Ball b1 = balls.get(0);

        int code = e.getKeyCode();
        switch(code){
            case KeyEvent.VK_LEFT:
                b1.x -= 10;
                break;
            case KeyEvent.VK_UP:
                b1.y -= 10;
                break;
            case KeyEvent.VK_DOWN:
                b1.y += 10;
                break;
            case KeyEvent.VK_RIGHT:
                b1.x += 10;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
