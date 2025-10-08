package de.frinshy.nivoraclient.client.gui

import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen
import net.minecraft.client.gui.screen.option.OptionsScreen
import net.minecraft.client.gui.screen.world.SelectWorldScreen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.realms.gui.screen.RealmsMainScreen
import net.minecraft.text.Text
import kotlin.math.sin

class MagicalTitleScreen : MagicalScreenBase(Text.literal("Nivora Client")) {

    private companion object {
        const val BUTTON_WIDTH = 280
        const val BUTTON_HEIGHT = 36
        const val SPACING = 48
        const val FLOAT_BUTTON_MARGIN = 60
    }

    private data class ButtonSpec(
        val text: String,
        val icon: String,
        val action: (ButtonWidget) -> Unit,
        val accent: Boolean = false
    )

    override fun init() {
        super.init()

        val centerX = width / 2
        val startY = height / 2 - 80

        val mainButtons = listOf(
            ButtonSpec("Singleplayer", "🎮", { _: ButtonWidget -> client?.setScreen(SelectWorldScreen(this)) }),
            ButtonSpec("Multiplayer", "🌐", { _: ButtonWidget -> client?.setScreen(MultiplayerScreen(this)) }),
            ButtonSpec("Minecraft Realms", "☁", { _: ButtonWidget -> client?.setScreen(RealmsMainScreen(this)) }),
            ButtonSpec("Settings", "⚙", { _: ButtonWidget -> client?.setScreen(OptionsScreen(this, client?.options)) }),
            ButtonSpec(
                "Nivora Credits",
                "✨",
                { _: ButtonWidget -> client?.setScreen(CreditScreen(this)) },
                accent = true
            ),
            ButtonSpec("Quit Game", "⏻", { _: ButtonWidget -> client?.scheduleStop() })
        )

        mainButtons.forEachIndexed { idx, spec ->
            val y = startY + SPACING * idx
            val x = centerX - BUTTON_WIDTH / 2
            val button = if (spec.accent) {
                createAccentButton(spec.text, spec.icon, x, y, spec.action)
            } else {
                createModernButton(spec.text, spec.icon, x, y, spec.action)
            }
            addDrawableChild(button)
        }

        addDrawableChild(
            createFloatingActionButton(
                "ℹ",
                width - FLOAT_BUTTON_MARGIN,
                height - FLOAT_BUTTON_MARGIN
            ) { _ -> client?.setScreen(CreditScreen(this)) })
    }

    private fun createModernButton(
        text: String,
        icon: String,
        x: Int,
        y: Int,
        onPress: (ButtonWidget) -> Unit
    ): ButtonWidget {
        return object : ButtonWidget(
            x,
            y,
            BUTTON_WIDTH,
            BUTTON_HEIGHT,
            Text.literal("$icon  $text"),
            onPress,
            DEFAULT_NARRATION_SUPPLIER
        ) {
            override fun renderWidget(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
                val alpha = if (isHovered()) 0.15f else 0.08f
                val bgColor = (alpha * 255).toInt() shl 24 or 0xFFFFFF
                val borderColor = if (isHovered()) 0x40FFFFFF else 0x20FFFFFF

                context.fill(this.x + 2, this.y + 2, this.x + this.width - 2, this.y + this.height - 2, bgColor)
                context.fill(this.x, this.y + 1, this.x + this.width, this.y + 2, borderColor)
                context.fill(
                    this.x,
                    this.y + this.height - 2,
                    this.x + this.width,
                    this.y + this.height - 1,
                    borderColor
                )
                context.fill(this.x + 1, this.y, this.x + 2, this.y + this.height, borderColor)
                context.fill(
                    this.x + this.width - 2,
                    this.y,
                    this.x + this.width - 1,
                    this.y + this.height,
                    borderColor
                )

                val textColor = if (isHovered()) 0xFFFFFFFF.toInt() else 0xFFE0E0E0.toInt()
                context.drawCenteredTextWithShadow(
                    this@MagicalTitleScreen.textRenderer,
                    this.message,
                    this.x + this.width / 2,
                    this.y + (this.height - 8) / 2,
                    textColor
                )
            }
        }
    }

    private fun createAccentButton(
        text: String,
        icon: String,
        x: Int,
        y: Int,
        onPress: (ButtonWidget) -> Unit
    ): ButtonWidget {
        return object : ButtonWidget(
            x,
            y,
            BUTTON_WIDTH,
            BUTTON_HEIGHT,
            Text.literal("$icon  $text"),
            onPress,
            DEFAULT_NARRATION_SUPPLIER
        ) {
            override fun renderWidget(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
                val baseAlpha = if (isHovered) 0.8f else 0.6f
                val glowIntensity = (0.3 + 0.2 * sin(animationTime * 0.08)).toFloat()

                val topColor = (baseAlpha * 255).toInt() shl 24 or 0x6366f1
                val bottomColor = (baseAlpha * 255).toInt() shl 24 or 0x8b5cf6

                context.fillGradient(
                    this.x + 2,
                    this.y + 2,
                    this.x + this.width - 2,
                    this.y + this.height - 2,
                    topColor,
                    bottomColor
                )

                val glowColor = (glowIntensity * 255).toInt() shl 24 or 0xa855f7
                context.fill(this.x, this.y + 1, this.x + this.width, this.y + 2, glowColor)
                context.fill(this.x, this.y + this.height - 2, this.x + this.width, this.y + this.height - 1, glowColor)
                context.fill(this.x + 1, this.y, this.x + 2, this.y + this.height, glowColor)
                context.fill(this.x + this.width - 2, this.y, this.x + this.width - 1, this.y + this.height, glowColor)

                context.drawCenteredTextWithShadow(
                    this@MagicalTitleScreen.textRenderer,
                    this.message,
                    this.x + this.width / 2,
                    this.y + (this.height - 8) / 2,
                    0xFFFFFFFF.toInt()
                )
            }
        }
    }

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        renderTheme(context, delta, "NIVORA", "CLIENT", "Next-Generation Minecraft Experience")
        super.render(context, mouseX, mouseY, delta)
    }

    override fun shouldCloseOnEsc(): Boolean = false
}
