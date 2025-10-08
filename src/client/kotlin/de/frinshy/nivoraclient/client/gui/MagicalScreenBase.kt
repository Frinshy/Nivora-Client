package de.frinshy.nivoraclient.client.gui

import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.text.Text

abstract class MagicalScreenBase(title: Text) : Screen(title) {

    protected var animationTime = 0f
    protected var particleTime = 0f

    // Draws the shared theme visuals; call at the start of your screen's render().
    protected fun renderTheme(
        context: DrawContext,
        delta: Float,
        titleText: String,
        subtitle: String,
        tagline: String
    ) {
        animationTime += delta
        particleTime += delta

        MagicalTheme.renderModernBackground(context, width, height, animationTime)
        MagicalTheme.renderModernTitle(context, textRenderer, width, titleText, subtitle, tagline, animationTime)
        MagicalTheme.renderModernParticles(context, width, height, particleTime)
        MagicalTheme.renderVersionInfo(context, textRenderer, width, height)
    }

    protected fun createFloatingActionButton(
        symbol: String,
        x: Int,
        y: Int,
        onPress: (ButtonWidget) -> Unit
    ): ButtonWidget {
        return object : ButtonWidget(
            x,
            y,
            FLOAT_BUTTON_SIZE,
            FLOAT_BUTTON_SIZE,
            Text.literal(symbol),
            onPress,
            DEFAULT_NARRATION_SUPPLIER
        ) {
            override fun renderWidget(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
                val alpha = if (isHovered()) 0.25f else 0.15f
                val bgColor = (alpha * 255).toInt() shl 24 or 0xFFFFFF
                val borderColor = if (isHovered()) 0x60FFFFFF else 0x30FFFFFF

                val center = FLOAT_BUTTON_SIZE / 2
                val dxCenter = center
                val dyCenter = center
                val innerR2 = INNER_RADIUS * INNER_RADIUS
                val outerR2 = OUTER_RADIUS * OUTER_RADIUS

                for (i in 0 until FLOAT_BUTTON_SIZE) {
                    val dx = i - dxCenter
                    val dx2 = dx * dx
                    for (j in 0 until FLOAT_BUTTON_SIZE) {
                        val dy = j - dyCenter
                        val dist2 = dx2 + dy * dy
                        when {
                            dist2 <= innerR2 -> context.fill(
                                this.x + i,
                                this.y + j,
                                this.x + i + 1,
                                this.y + j + 1,
                                bgColor
                            )

                            dist2 <= outerR2 -> context.fill(
                                this.x + i,
                                this.y + j,
                                this.x + i + 1,
                                this.y + j + 1,
                                borderColor
                            )
                        }
                    }
                }

                val textColor = if (isHovered()) 0xFFFFFFFF.toInt() else 0xFFE0E0E0.toInt()
                context.drawCenteredTextWithShadow(
                    this@MagicalScreenBase.textRenderer,
                    this.message,
                    this.x + FLOAT_BUTTON_SIZE / 2,
                    this.y + FLOAT_BUTTON_SIZE / 2 - 4,
                    textColor
                )
            }
        }
    }

    private companion object {
        const val FLOAT_BUTTON_SIZE = 40
        const val INNER_RADIUS = 18
        const val OUTER_RADIUS = 19
    }
}
