package com.company.core.models;

import com.company.utils.ColorExtends;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Модель шарика, описывает его свойства
 */
public class Ball {
    public int x;
    public int y;
    // Скорости по осям
    public double vx;
    public double vy;

	public final int r;
	private final int d;
	private final Color color;

	public Ball(int r) {
		this.r = r;
		d = 2 * r;
        // Случайный цвет
		color = Color.darkGray;
	}

	public void draw(Graphics g) {
        // Отрисовка шарика, вначале установим цвет кисти
		g.setColor(color);
        // Потом рисуем овал передавая координаты левого верхнего угла, ширину и высоту
		g.fillRect(x - r, y - r, d, d);
		//g.fillOval(x - r, y - 2*r , d, d);
	}
}
