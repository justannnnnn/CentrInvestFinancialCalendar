package com.example.sdk.domain.model

enum class Category(
    val value: Int,
    val title: String,
    val icon: String,
    val isIncome: Boolean,
    val color: Long
) {
    WORK(0, "Работа", "💼", true, 0xFF00A86B),
    HOUSE(1, "Дом и быт", "🏠", false, 0xFF32CD32),
    SHOPPING(2, "Покупки", "💳", false, 0xFFF95E5A),
    GROCERIES(3, "Продукты", "🛒", false, 0xFFFFA500),
    TRANSPORT(4, "Транспорт", "🚕", false, 0xFF1E90FF),
    HEALTH(5, "Здоровье", "🏥", false, 0xFF8A2BE2),
    ENTERTAINMENT(6, "Развлечения", "🎬", false, 0xFFFFB800);

    companion object {
        fun parse(value: Int) = entries.firstOrNull { it.value == value }
    }
}