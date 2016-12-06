package com.company.core.models;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Модель шарика, описывает его свойства
 */
public class Tank {
    public int x;
    public int y;
	public boolean turningLocked = false;
    // Скорости по осям
    public double vx;
    public double vy;

	public enum Direction{
		UP,
		DOWN,
		RIGHT,
		LEFT
	}

	public final int width;
	public final int height;
	public Direction curDirection = Direction.UP;

	private final Color color;

	public Tank(int width, int height) {
		this.width = width;
		this.height = height;
		color = Color.darkGray;
	}

	public void draw(Graphics g) {
        // Отрисовка танка
		switch(curDirection){
			case UP:
				g.setColor(color);
				g.fillRect(x - width / 2, y - height / 2, width, height);
				g.setColor(Color.black);
				g.fillRect(x - width/8, y - height, width/5, height/2);
				break;
			case DOWN:
				g.setColor(color);
				g.fillRect(x - width / 2, y - height / 2, width, height);
				g.setColor(Color.black);
				g.fillRect(x - width/8, y + height / 2, width/5, height/2);
				break;
			case LEFT:
				g.setColor(color);
				g.fillRect(x - height / 2, y - (width / 2), height, width);
				g.setColor(Color.black);
				g.fillRect(x - height, y - width/8, height/2, width/5);
				break;
			case RIGHT:
				g.setColor(color);
				g.fillRect(x - height / 2, y - width / 2, height, width);
				g.setColor(Color.black);
				g.fillRect(x + height / 2, y - width/8, height/2, width/5);
				break;
		}


	}
}
