package com.company.utils;

import java.awt.Color;
import java.util.Random;

/**
 * Вспомогательный класс для работы с цветом
 */
public final class ColorExtends {
    private static final Random RAND = new Random();

    public static Color getRandomColor() {
        return new Color(RAND.nextFloat(), RAND.nextFloat(), RAND.nextFloat());
    }

    private ColorExtends() {
        // Нельзя создать экземпляр так как конструктор приватен
    }
}