package de.frinshy.nivoraclient.client.gui

import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext
import net.minecraft.text.Text
import net.minecraft.util.math.MathHelper
import kotlin.math.cos
import kotlin.math.sin

object MagicalTheme {

    private var globalAnimationTime = 0f
    private var globalParticleTime = 0f
    private var lastTimeNanos = System.nanoTime()
    private var lastRenderInvocationNanos = 0L

    private const val GRID_STEP = 60
    private const val PARTICLE_COUNT = 12

    // Renders the full background + particles + version info as a single-call overlay for screens
    fun renderOverlay(context: DrawContext, textRenderer: TextRenderer, width: Int, height: Int) {
        val now = System.nanoTime()
        // If we've already rendered an overlay very recently (same frame), skip to avoid double-draw and blur issues
        if (now - lastRenderInvocationNanos < 10_000_000L) { // 10ms
            return
        }
        lastRenderInvocationNanos = now

        // compute delta based on system time for screens that don't supply a delta
        val delta = ((now - lastTimeNanos) / 1_000_000_000.0).toFloat().coerceAtMost(0.1f)
        lastTimeNanos = now

        // accumulate time for smooth animated effects
        globalAnimationTime += delta
        globalParticleTime += delta

        renderModernBackground(context, width, height, globalAnimationTime)
        renderModernParticles(context, width, height, globalParticleTime)
        renderVersionInfo(context, textRenderer, width, height)
    }

    fun renderModernBackground(context: DrawContext, width: Int, height: Int, animationTime: Float) {
        val topColor = 0xFF0a0a0f.toInt()
        val middleColor = 0xFF1a1a2e.toInt()
        val bottomColor = 0xFF16213e.toInt()

        context.fillGradient(0, 0, width, height / 3, topColor, middleColor)
        context.fillGradient(0, height / 3, width, height * 2 / 3, middleColor, middleColor)
        context.fillGradient(0, height * 2 / 3, width, height, middleColor, bottomColor)

        val patternAlpha = (0.02 + 0.01 * sin(animationTime * 0.02)).toFloat()
        val patternColor = (patternAlpha * 255).toInt() shl 24 or 0x6366f1
        drawGrid(context, width, height, GRID_STEP, patternColor)
    }

    private fun drawGrid(context: DrawContext, width: Int, height: Int, step: Int, color: Int) {
        for (i in 0 until width step step) context.fill(i, 0, i + 1, height, color)
        for (i in 0 until height step step) context.fill(0, i, width, i + 1, color)
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
            drawCharShadow(context, textRenderer, char, charX, titleY, charColor)
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

    private fun drawCharShadow(
        context: DrawContext,
        textRenderer: TextRenderer,
        ch: String,
        x: Int,
        y: Int,
        color: Int
    ) {
        // Draw a small 2x2 'shadow' to give the title a crisp glow
        context.drawText(textRenderer, ch, x, y, color, false)
        context.drawText(textRenderer, ch, x + 1, y, color, false)
        context.drawText(textRenderer, ch, x, y + 1, color, false)
        context.drawText(textRenderer, ch, x + 1, y + 1, color, false)
    }

    fun renderModernParticles(context: DrawContext, width: Int, height: Int, particleTime: Float) {
        for (i in 0 until PARTICLE_COUNT) {
            val particleX = (width * 0.2 + sin(particleTime * 0.015 + i * 2) * width * 0.6).toFloat()
            val particleY = (height * 0.3 + cos(particleTime * 0.02 + i * 1.8) * height * 0.4).toFloat()
            val alpha = (0.15 + 0.1 * sin(particleTime * 0.025 + i * 3)).toFloat()
            val particleColor = (alpha * 255).toInt() shl 24 or 0x6366f1
            val size = (2 + sin(particleTime * 0.03 + i)).toInt()
            val px = particleX.toInt()
            val py = particleY.toInt()
            context.fill(px, py, px + size, py + size, particleColor)
        }
    }

    fun renderVersionInfo(context: DrawContext, textRenderer: TextRenderer, width: Int, height: Int) {
        val versionText = "Nivora Client v1.0.0"
        val buildText = "Build 2025.10.06"
        val powerText = "Powered by Fabric & Kotlin"

        val rightMargin = 20
        val bottomMargin = 20

        val cardWidth = 200
        val cardHeight = 60
        val cardX = width - cardWidth - rightMargin
        val cardY = height - cardHeight - bottomMargin

        val cardBg = 0x20FFFFFF
        val borderColor = 0x30FFFFFF
        drawInfoCard(context, cardX, cardY, cardWidth, cardHeight, cardBg, borderColor)

        context.drawTextWithShadow(textRenderer, versionText, cardX + 10, cardY + 8, 0xFFFFFFFF.toInt())
        context.drawTextWithShadow(textRenderer, buildText, cardX + 10, cardY + 20, 0xFFa1a1aa.toInt())
        context.drawTextWithShadow(textRenderer, powerText, cardX + 10, cardY + 32, 0xFF6366f1.toInt())
    }

    private fun drawInfoCard(context: DrawContext, x: Int, y: Int, w: Int, h: Int, bg: Int, border: Int) {
        context.fill(x, y, x + w, y + h, bg)
        context.fill(x, y, x + w, y + 1, border)
        context.fill(x, y + h - 1, x + w, y + h, border)
        context.fill(x, y, x + 1, y + h, border)
        context.fill(x + w - 1, y, x + w, y + h, border)
    }
}
