package com.company;

import com.company.core.GameManager;
import com.company.core.views.BaseView;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

/**
 * Главное окно игры
 */
public class MainWidow {

	private MainWidow() {
        // Инициализируем свойства окна
        JFrame frame = new JFrame();
        frame.setVisible(true);
        frame.setResizable(false);
		frame.setBounds(100, 100, 600, 800);
        // Приложение должно завершиться после закрытия окна
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Prisoned Tank");

        // Создаем вьюшку и добавляем ее в окно
        BaseView view = new BaseView();
		frame.getContentPane().add(view);

        // Создаем мэнеджер управляющий логикой игры
        final GameManager gameManager = new GameManager(view);

        // Подписываем менеджер на события клавиатуры
        frame.addKeyListener(gameManager);

        // Добавляем верхнее меню для старта, паузы игры
        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);
        final JMenuItem startMenuItem = new JMenuItem("Start");
        menuBar.add(startMenuItem);
        final JMenuItem pauseMenuItem = new JMenuItem("Pause");
        pauseMenuItem.setEnabled(false);
        menuBar.add(pauseMenuItem);

        // Подписываемся на клики по меню
        startMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameManager.start();
                startMenuItem.setEnabled(false);
                pauseMenuItem.setEnabled(true);
                gameManager.addBullet(250, 100);
                gameManager.addTank(250, 100);
                gameManager.addWall(180, 90);
                gameManager.addWall(320, 90);
            }
        });
        pauseMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameManager.pause();
                startMenuItem.setEnabled(true);
                pauseMenuItem.setEnabled(false);
            }
        });

        // Подписываемся на событие клика по вьюшке
        /*frame.getContentPane().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
                // При клике ЛЕВОЙ кнопкой добавляем 1 новый шарик с координатами клика
                int count = 1;
                if (e.getButton() == MouseEvent.BUTTON3) {
                    // При клике ПРАВОЙ кнопкой добавляем 10 новых шариков
                    count = 10;
                }
                if (e.getButton() == MouseEvent.BUTTON2) {
                    // При клике КОЛЕСКОМ кнопкой добавляем 1000 новых шариков
                    count = 1000;
                }

                for (int i = 0; i < count; i++) {
                    gameManager.addTank(e.getX(), e.getY());
                    gameManager.addBullet(e.getX(), e.getY());
                }
			}
		});*/
	}

	public static void main(String[] args) {

        // Точка входа, создаем экземпляр главного окна
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainWidow();
            }
        });
	}
}