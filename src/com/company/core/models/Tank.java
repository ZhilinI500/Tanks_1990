package com.company.core.models;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Модель шарика, описывает его свойства
 */
public class Tank {
    public int x;
    public int y;
    // Скорости по осям
    public double vx;
    public double vy;

	public final int width;
	public final int height;
	private final Color color;

	public Tank(int width, int height) {
		this.width = width;
		this.height = height;
		color = Color.darkGray;
	}

	public void draw(Graphics g) {
        // Отрисовка танка, вначале установим цвет кисти
		g.setColor(color);
		g.fillRect(x - width/2, y - height/2, width, height);
	}
}
