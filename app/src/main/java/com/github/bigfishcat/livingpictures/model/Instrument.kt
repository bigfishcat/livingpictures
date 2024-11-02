package com.github.bigfishcat.livingpictures.model

enum class Instrument {
    Pencil,
    Brush,
    Eraser,
    Triangle,
    Rectangle,
    Circle,
    Arrow;

    val canTouch: Boolean
        get() = when (this) {
            Triangle, Rectangle, Circle, Arrow -> true
            Pencil, Brush, Eraser -> false
        }

    val canDraw: Boolean
        get() = when (this) {
            Triangle, Rectangle, Circle, Arrow -> false
            Pencil, Brush, Eraser -> true
        }
}