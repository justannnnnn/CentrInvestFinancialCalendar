package com.example.sdk.presentation.models

import java.util.Calendar

data class Transaction(
    val id: Long,
    val date: Calendar,
    val amount: Double,             // > 0 = доход, < 0 = расход
    val title: String,
    val category: String,
    val categoryEmoji: String,
    val isCardPayment: Boolean = false,
    val isRecurring: Boolean = false
)

object MockTransactions {

    private fun dateOf(year: Int, month: Int, day: Int): Calendar =
        Calendar.getInstance().apply {
            set(year, month - 1, day, 12, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }

    val list = listOf(
        Transaction(1, dateOf(2026, 2, 20), -450.0,  "Кофе с коллегами",      "Кафе",           "☕",  true,  false),
        Transaction(2, dateOf(2026, 2, 21), -1280.0, "Пятёрочка",             "Продукты",       "🛒",  true,  false),
        Transaction(3, dateOf(2026, 2, 22),  75000.0, "Зарплата февраль",      "Зарплата",       "💰",  false, false),
        Transaction(4, dateOf(2026, 2, 23), -6700.0, "Аренда",                "Жильё",          "🏠",  false, true),
        Transaction(5, dateOf(2026, 2, 24), -920.0,  "Яндекс Go",             "Транспорт",      "🚖",  true,  false),
        Transaction(6, dateOf(2026, 2, 24), -380.0,  "Обед в столовой",       "Кафе",           "🍲",  true,  false),

        Transaction(7,  dateOf(2026, 1, 5), -1450.0, "Netflix",               "Подписки",       "🎬",  false, true),
        Transaction(8,  dateOf(2026, 1, 15), 12000.0, "Фриланс",              "Доход",          "💻",  false, false),
        Transaction(9,  dateOf(2026, 2, 1), -2340.0, "ВкусВилл",              "Продукты",       "🥬",  true,  false),
        Transaction(10, dateOf(2026, 2, 10), -890.0, "Кино",                  "Развлечения",    "🎥",  false, false),

        // можно добавить ещё 10–20 строк
    )
}