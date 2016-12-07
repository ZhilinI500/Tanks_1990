package com.company.core.models;


import java.awt.*;

public class Wall {
    public int x;
    public int y;
    // �������� �� ����
    public double vx;
    public double vy;
    public boolean isFlying = false;

    public final int width;
    public final int height;
    private final Color color;

    public Wall(int width, int height) {
        this.width = width;
        this.height = height;
        color = Color.cyan;
    }

    public void draw(Graphics g) {
        // ��������� ������, ������� ��������� ���� �����
        g.setColor(color);
        // ����� ������ ���� ��������� ���������� ������ �������� ����, ������ � ������
        g.fillRect(x - width / 2, y - height / 2, width, height);
        //g.fillOval(x - r, y - 2*r , d, d);
    }
}
