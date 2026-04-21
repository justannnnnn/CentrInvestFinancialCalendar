package com.example.sdk.sdk

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.example.sdk.presentation.models.CustomCalendarTheme
import com.example.sdk.presentation.models.ThemeCustomizationMode
import com.example.sdk.presentation.models.ThemeSelection
import org.json.JSONArray
import org.json.JSONObject

internal object CalendarThemeStorage {

    private const val PREFS_NAME = "calendar_theme_prefs"

    private const val KEY_SELECTED_PRESET = "selected_preset_legacy"

    private const val KEY_THEME_SELECTION_TYPE = "theme_selection_type"
    private const val KEY_THEME_SELECTION_PRESET = "theme_selection_preset"
    private const val KEY_THEME_SELECTION_CUSTOM_ID = "theme_selection_custom_id"

    private const val KEY_CUSTOM_THEMES_JSON = "custom_themes_json"

    private const val SELECTION_TYPE_PRESET = "preset"
    private const val SELECTION_TYPE_CUSTOM = "custom"

    fun saveThemeSelection(
        context: Context,
        selection: ThemeSelection
    ) {
        val prefs = prefs(context)

        when (selection) {
            is ThemeSelection.Preset -> {
                prefs.edit()
                    .putString(KEY_THEME_SELECTION_TYPE, SELECTION_TYPE_PRESET)
                    .putString(KEY_THEME_SELECTION_PRESET, selection.preset.name)
                    .remove(KEY_THEME_SELECTION_CUSTOM_ID)
                    .putString(KEY_SELECTED_PRESET, selection.preset.name)
                    .apply()
            }

            is ThemeSelection.Custom -> {
                prefs.edit()
                    .putString(KEY_THEME_SELECTION_TYPE, SELECTION_TYPE_CUSTOM)
                    .putString(KEY_THEME_SELECTION_CUSTOM_ID, selection.themeId)
                    .remove(KEY_THEME_SELECTION_PRESET)
                    .apply()
            }
        }
    }

    fun getThemeSelection(context: Context): ThemeSelection? {
        val prefs = prefs(context)
        val savedType = prefs.getString(KEY_THEME_SELECTION_TYPE, null)

        if (savedType != null) {
            return when (savedType) {
                SELECTION_TYPE_PRESET -> {
                    val presetName = prefs.getString(KEY_THEME_SELECTION_PRESET, null)
                    val preset = presetName?.let {
                        runCatching { CalendarThemePreset.valueOf(it) }.getOrNull()
                    }
                    preset?.let { ThemeSelection.Preset(it) }
                }

                SELECTION_TYPE_CUSTOM -> {
                    val themeId = prefs.getString(KEY_THEME_SELECTION_CUSTOM_ID, null)
                    themeId?.let { ThemeSelection.Custom(it) }
                }

                else -> null
            }
        }

        val legacyPreset = getSelectedPreset(context)
        return legacyPreset?.let { ThemeSelection.Preset(it) }
    }

    fun clearThemeSelection(context: Context) {
        prefs(context).edit()
            .remove(KEY_THEME_SELECTION_TYPE)
            .remove(KEY_THEME_SELECTION_PRESET)
            .remove(KEY_THEME_SELECTION_CUSTOM_ID)
            .apply()
    }

    fun saveSelectedPreset(
        context: Context,
        preset: CalendarThemePreset
    ) {
        saveThemeSelection(context, ThemeSelection.Preset(preset))
    }

    fun getSelectedPreset(context: Context): CalendarThemePreset? {
        val presetName = prefs(context).getString(KEY_SELECTED_PRESET, null)
        return presetName?.let {
            runCatching { CalendarThemePreset.valueOf(it) }.getOrNull()
        }
    }

    fun getCustomThemes(context: Context): List<CustomCalendarTheme> {
        val rawJson = prefs(context).getString(KEY_CUSTOM_THEMES_JSON, null) ?: return emptyList()

        return runCatching {
            val jsonArray = JSONArray(rawJson)
            val result = mutableListOf<CustomCalendarTheme>()

            for (index in 0 until jsonArray.length()) {
                val item = jsonArray.optJSONObject(index) ?: continue
                val parsed = jsonToCustomTheme(item)
                if (parsed != null) {
                    result.add(parsed)
                }
            }

            result
        }.getOrDefault(emptyList())
    }

    fun saveCustomThemes(
        context: Context,
        themes: List<CustomCalendarTheme>
    ) {
        val jsonArray = JSONArray()

        for (theme in themes) {
            jsonArray.put(customThemeToJson(theme))
        }

        prefs(context).edit()
            .putString(KEY_CUSTOM_THEMES_JSON, jsonArray.toString())
            .apply()
    }

    fun addCustomTheme(
        context: Context,
        theme: CustomCalendarTheme
    ) {
        val current = getCustomThemes(context).toMutableList()
        current.add(theme)
        saveCustomThemes(context, current.sortedByDescending { it.updatedAt })
    }

    fun updateCustomTheme(
        context: Context,
        theme: CustomCalendarTheme
    ) {
        val current = getCustomThemes(context).toMutableList()
        val index = current.indexOfFirst { it.id == theme.id }

        if (index >= 0) {
            current[index] = theme
        } else {
            current.add(theme)
        }

        saveCustomThemes(context, current.sortedByDescending { it.updatedAt })
    }

    fun upsertCustomTheme(
        context: Context,
        theme: CustomCalendarTheme
    ) {
        updateCustomTheme(context, theme)
    }

    fun deleteCustomTheme(
        context: Context,
        themeId: String
    ) {
        val filtered = getCustomThemes(context).filterNot { it.id == themeId }
        saveCustomThemes(context, filtered)

        val currentSelection = getThemeSelection(context)
        if (currentSelection is ThemeSelection.Custom && currentSelection.themeId == themeId) {
            saveThemeSelection(
                context = context,
                selection = ThemeSelection.Preset(CalendarThemePreset.Default)
            )
        }
    }

    fun findCustomThemeById(
        context: Context,
        themeId: String
    ): CustomCalendarTheme? {
        return getCustomThemes(context).firstOrNull { it.id == themeId }
    }

    private fun customThemeToJson(theme: CustomCalendarTheme): JSONObject {
        return JSONObject().apply {
            put("id", theme.id)
            put("name", theme.name)
            put("mode", theme.mode.name)
            put("basePreset", theme.basePreset.name)
            put("themeConfig", themeConfigToJson(theme.themeConfig))
            put("createdAt", theme.createdAt)
            put("updatedAt", theme.updatedAt)
        }
    }

    private fun jsonToCustomTheme(json: JSONObject): CustomCalendarTheme? {
        val id = json.optString("id", "")
        val name = json.optString("name", "")
        val modeName = json.optString("mode", ThemeCustomizationMode.PARTIAL.name)
        val basePresetName = json.optString("basePreset", CalendarThemePreset.Default.name)
        val createdAt = json.optLong("createdAt", 0L)
        val updatedAt = json.optLong("updatedAt", createdAt)
        val themeConfigJson = json.optJSONObject("themeConfig") ?: JSONObject()

        if (id.isBlank() || name.isBlank()) return null

        val mode = runCatching {
            ThemeCustomizationMode.valueOf(modeName)
        }.getOrDefault(ThemeCustomizationMode.PARTIAL)

        val basePreset = runCatching {
            CalendarThemePreset.valueOf(basePresetName)
        }.getOrDefault(CalendarThemePreset.Default)

        val themeConfig = jsonToThemeConfig(themeConfigJson)

        return CustomCalendarTheme(
            id = id,
            name = name,
            mode = mode,
            basePreset = basePreset,
            themeConfig = themeConfig,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }

    private fun themeConfigToJson(themeConfig: CalendarThemeConfig): JSONObject {
        return JSONObject().apply {
            put("preset", themeConfig.preset.name)
            put("colors", colorOverridesToJson(themeConfig.colors))
            put("typography", typographyOverridesToJson(themeConfig.typography))
            put("icons", iconOverridesToJson(themeConfig.icons))
        }
    }

    private fun jsonToThemeConfig(json: JSONObject): CalendarThemeConfig {
        val preset = runCatching {
            CalendarThemePreset.valueOf(
                json.optString("preset", CalendarThemePreset.Default.name)
            )
        }.getOrDefault(CalendarThemePreset.Default)

        val colors = jsonToColorOverrides(json.optJSONObject("colors") ?: JSONObject())
        val typography = jsonToTypographyOverrides(json.optJSONObject("typography") ?: JSONObject())
        val icons = jsonToIconOverrides(json.optJSONObject("icons") ?: JSONObject())

        return CalendarThemeConfig(
            preset = preset,
            colors = colors,
            typography = typography,
            icons = icons
        )
    }

    private fun colorOverridesToJson(overrides: CalendarColorOverrides): JSONObject {
        return JSONObject().apply {
            putColorOrNull("primary", overrides.primary)
            putColorOrNull("primaryVariant", overrides.primaryVariant)
            putColorOrNull("background", overrides.background)
            putColorOrNull("surface", overrides.surface)
            putColorOrNull("textPrimary", overrides.textPrimary)
            putColorOrNull("textSecondary", overrides.textSecondary)
            putColorOrNull("textTertiary", overrides.textTertiary)
            putColorOrNull("borderLight", overrides.borderLight)
            putColorOrNull("borderMedium", overrides.borderMedium)
            putColorOrNull("selectedBackground", overrides.selectedBackground)
            putColorOrNull("selectedBorder", overrides.selectedBorder)
            putColorOrNull("income", overrides.income)
            putColorOrNull("incomeVariant", overrides.incomeVariant)
            putColorOrNull("expense", overrides.expense)
            putColorOrNull("expenseVariant", overrides.expenseVariant)
        }
    }

    private fun jsonToColorOverrides(json: JSONObject): CalendarColorOverrides {
        return CalendarColorOverrides(
            primary = json.optColor("primary"),
            primaryVariant = json.optColor("primaryVariant"),
            background = json.optColor("background"),
            surface = json.optColor("surface"),
            textPrimary = json.optColor("textPrimary"),
            textSecondary = json.optColor("textSecondary"),
            textTertiary = json.optColor("textTertiary"),
            borderLight = json.optColor("borderLight"),
            borderMedium = json.optColor("borderMedium"),
            selectedBackground = json.optColor("selectedBackground"),
            selectedBorder = json.optColor("selectedBorder"),
            income = json.optColor("income"),
            incomeVariant = json.optColor("incomeVariant"),
            expense = json.optColor("expense"),
            expenseVariant = json.optColor("expenseVariant")
        )
    }

    private fun typographyOverridesToJson(overrides: CalendarTypographyOverrides): JSONObject {
        return JSONObject().apply {
            putTextStyleOrNull("titleLarge", overrides.titleLarge)
            putTextStyleOrNull("titleMedium", overrides.titleMedium)
            putTextStyleOrNull("bodyLarge", overrides.bodyLarge)
            putTextStyleOrNull("bodyMedium", overrides.bodyMedium)
            putTextStyleOrNull("bodySmall", overrides.bodySmall)
            putTextStyleOrNull("labelMedium", overrides.labelMedium)
            putTextStyleOrNull("labelSmall", overrides.labelSmall)
        }
    }

    private fun jsonToTypographyOverrides(json: JSONObject): CalendarTypographyOverrides {
        return CalendarTypographyOverrides(
            titleLarge = json.optTextStyle("titleLarge"),
            titleMedium = json.optTextStyle("titleMedium"),
            bodyLarge = json.optTextStyle("bodyLarge"),
            bodyMedium = json.optTextStyle("bodyMedium"),
            bodySmall = json.optTextStyle("bodySmall"),
            labelMedium = json.optTextStyle("labelMedium"),
            labelSmall = json.optTextStyle("labelSmall")
        )
    }

    private fun iconOverridesToJson(overrides: CalendarIconOverrides): JSONObject {
        return JSONObject().apply {
            putIntOrNull("addIconRes", overrides.addIconRes)
            putIntOrNull("monthTabIconRes", overrides.monthTabIconRes)
            putIntOrNull("weekTabIconRes", overrides.weekTabIconRes)
            putIntOrNull("dayTabIconRes", overrides.dayTabIconRes)
            putIntOrNull("calendarBottomNavIconRes", overrides.calendarBottomNavIconRes)
            putIntOrNull("arrowLeftIconRes", overrides.arrowLeftIconRes)
            putIntOrNull("arrowRightIconRes", overrides.arrowRightIconRes)
        }
    }

    private fun jsonToIconOverrides(json: JSONObject): CalendarIconOverrides {
        return CalendarIconOverrides(
            addIconRes = json.optNullableInt("addIconRes"),
            monthTabIconRes = json.optNullableInt("monthTabIconRes"),
            weekTabIconRes = json.optNullableInt("weekTabIconRes"),
            dayTabIconRes = json.optNullableInt("dayTabIconRes"),
            calendarBottomNavIconRes = json.optNullableInt("calendarBottomNavIconRes"),
            arrowLeftIconRes = json.optNullableInt("arrowLeftIconRes"),
            arrowRightIconRes = json.optNullableInt("arrowRightIconRes")
        )
    }

    private fun JSONObject.putColorOrNull(key: String, color: Color?) {
        if (color == null) {
            put(key, JSONObject.NULL)
        } else {
            put(key, color.toArgb().toLong())
        }
    }

    private fun JSONObject.optColor(key: String): Color? {
        if (!has(key) || isNull(key)) return null
        return runCatching { Color(optLong(key).toInt()) }.getOrNull()
    }

    private fun JSONObject.putIntOrNull(key: String, value: Int?) {
        if (value == null) {
            put(key, JSONObject.NULL)
        } else {
            put(key, value)
        }
    }

    private fun JSONObject.optNullableInt(key: String): Int? {
        if (!has(key) || isNull(key)) return null
        return optInt(key)
    }

    private fun JSONObject.putTextStyleOrNull(key: String, textStyle: TextStyle?) {
        if (textStyle == null) {
            put(key, JSONObject.NULL)
        } else {
            put(key, textStyleToJson(textStyle))
        }
    }

    private fun JSONObject.optTextStyle(key: String): TextStyle? {
        if (!has(key) || isNull(key)) return null
        val styleJson = optJSONObject(key) ?: return null
        return jsonToTextStyle(styleJson)
    }

    private fun textStyleToJson(style: TextStyle): JSONObject {
        return JSONObject().apply {
            if (style.fontSize != TextUnit.Unspecified) {
                put("fontSizeSp", style.fontSize.value.toDouble())
            } else {
                put("fontSizeSp", JSONObject.NULL)
            }

            if (style.lineHeight != TextUnit.Unspecified) {
                put("lineHeightSp", style.lineHeight.value.toDouble())
            } else {
                put("lineHeightSp", JSONObject.NULL)
            }

            if (style.fontWeight != null) {
                put("fontWeight", style.fontWeight!!.weight)
            } else {
                put("fontWeight", JSONObject.NULL)
            }
        }
    }

    private fun jsonToTextStyle(json: JSONObject): TextStyle {
        val fontSize = if (json.has("fontSizeSp") && !json.isNull("fontSizeSp")) {
            json.optDouble("fontSizeSp").toFloat().sp
        } else {
            TextUnit.Unspecified
        }

        val lineHeight = if (json.has("lineHeightSp") && !json.isNull("lineHeightSp")) {
            json.optDouble("lineHeightSp").toFloat().sp
        } else {
            TextUnit.Unspecified
        }

        val fontWeight = if (json.has("fontWeight") && !json.isNull("fontWeight")) {
            FontWeight(json.optInt("fontWeight"))
        } else {
            null
        }

        return TextStyle(
            fontSize = fontSize,
            lineHeight = lineHeight,
            fontWeight = fontWeight
        )
    }

    private fun prefs(context: Context) =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
}