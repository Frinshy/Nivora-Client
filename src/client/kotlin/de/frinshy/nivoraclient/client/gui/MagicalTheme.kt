package de.frinshy.nivoraclient.client.gui

import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext
import net.minecraft.text.Text
import net.minecraft.util.math.MathHelper

object MagicalTheme {

    fun renderModernBackground(context: DrawContext, width: Int, height: Int, animationTime: Float) {
        val topColor = 0xFF0a0a0f.toInt()
        val middleColor = 0xFF1a1a2e.toInt()
        val bottomColor = 0xFF16213e.toInt()

        context.fillGradient(0, 0, width, height / 3, topColor, middleColor)
        context.fillGradient(0, height / 3, width, height * 2 / 3, middleColor, middleColor)
        context.fillGradient(0, height * 2 / 3, width, height, middleColor, bottomColor)

        val patternAlpha = (0.02 + 0.01 * kotlin.math.sin(animationTime * 0.02)).toFloat()
        val patternColor = (patternAlpha * 255).toInt() shl 24 or 0x6366f1

        for (i in 0 until width step 60) {
            context.fill(i, 0, i + 1, height, patternColor)
        }
        for (i in 0 until height step 60) {
            context.fill(0, i, width, i + 1, patternColor)
        }
    }

    fun renderModernTitle(
        context: DrawContext,
        textRenderer: TextRenderer,
        width: Int,
        title: String,
        subtitle: String,
        tagline: String,
        animationTime: Float
    ) {
        val titleY = 60
        val subtitleY = 95
        val taglineY = 130

        val titleWidth = textRenderer.getWidth(title) * 2
        val titleX = width / 2 - titleWidth / 2

        for (i in title.indices) {
            val charX = titleX + i * (titleWidth / title.length)
            val char = title[i].toString()
            val hue = (animationTime * 0.01f + i * 0.1f) % 1.0f
            val charColor = MathHelper.hsvToRgb(hue, 0.6f, 1.0f) or 0xFF000000.toInt()
            context.drawText(textRenderer, char, charX, titleY, charColor, false)
            context.drawText(textRenderer, char, charX + 1, titleY, charColor, false)
            context.drawText(textRenderer, char, charX, titleY + 1, charColor, false)
            context.drawText(textRenderer, char, charX + 1, titleY + 1, charColor, false)
        }

        val subtitleColor = 0xFF8b5cf6.toInt()
        context.drawCenteredTextWithShadow(textRenderer, Text.literal(subtitle), width / 2, subtitleY, subtitleColor)

        val taglineColor = 0xFF64748b.toInt()
        context.drawCenteredTextWithShadow(textRenderer, Text.literal(tagline), width / 2, taglineY, taglineColor)

        val accentY = taglineY + 15
        val accentWidth = textRenderer.getWidth(tagline)
        val accentX = width / 2 - accentWidth / 2
        val accentColor = 0xFF6366f1.toInt()
        context.fill(accentX, accentY, accentX + accentWidth, accentY + 2, accentColor)
    }

    fun renderModernParticles(context: DrawContext, width: Int, height: Int, particleTime: Float) {
        for (i in 0 until 12) {
            val particleX = (width * 0.2 + kotlin.math.sin(particleTime * 0.015 + i * 2) * width * 0.6).toFloat()
            val particleY = (height * 0.3 + kotlin.math.cos(particleTime * 0.02 + i * 1.8) * height * 0.4).toFloat()
            val alpha = (0.15 + 0.1 * kotlin.math.sin(particleTime * 0.025 + i * 3)).toFloat()
            val particleColor = (alpha * 255).toInt() shl 24 or 0x6366f1
            val size = (2 + kotlin.math.sin(particleTime * 0.03 + i) * 1).toInt()
            context.fill(particleX.toInt(), particleY.toInt(), particleX.toInt() + size, particleY.toInt() + size, particleColor)
        }
    }

    fun renderVersionInfo(context: DrawContext, textRenderer: TextRenderer, width: Int, height: Int) {
        val versionText = "Nivora Client v1.0.0"
        val buildText = "Build 2025.10.06"
        val powerText = "Powered by Fabric & Kotlin"


