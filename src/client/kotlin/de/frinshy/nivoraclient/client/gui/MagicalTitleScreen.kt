package de.frinshy.nivoraclient.client.gui

import de.frinshy.nivoraclient.client.gui.CreditScreen
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen
import net.minecraft.client.gui.screen.option.OptionsScreen
import net.minecraft.client.gui.screen.world.SelectWorldScreen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.realms.gui.screen.RealmsMainScreen
import net.minecraft.text.Text
import net.minecraft.util.math.MathHelper
import kotlin.math.*

class MagicalTitleScreen : Screen(Text.literal("Nivora Client")) {

    private var animationTime = 0f
    private var particleTime = 0f

    override fun init() {
        super.init()

        val buttonWidth = 200
        val buttonHeight = 24
        val centerX = width / 2
        val startY = height / 2 - 50
        val spacing = 30

        // Main menu buttons with magical styling
        addDrawableChild(
            createMagicalButton(Text.literal("✦ Singleplayer ✦"),
                centerX - buttonWidth / 2, startY, buttonWidth, buttonHeight) {
                client?.setScreen(SelectWorldScreen(this))
            }
        )

        addDrawableChild(
            createMagicalButton(Text.literal("⚡ Multiplayer ⚡"),
                centerX - buttonWidth / 2, startY + spacing, buttonWidth, buttonHeight) {
                client?.setScreen(MultiplayerScreen(this))
            }
        )

        addDrawableChild(
            createMagicalButton(Text.literal("🌟 Minecraft Realms 🌟"),
                centerX - buttonWidth / 2, startY + spacing * 2, buttonWidth, buttonHeight) {
                client?.setScreen(RealmsMainScreen(this))
            }
        )

        addDrawableChild(
            createMagicalButton(Text.literal("⚙ Options ⚙"),
                centerX - buttonWidth / 2, startY + spacing * 3, buttonWidth, buttonHeight) {
                client?.setScreen(OptionsScreen(this, client?.options))
            }
        )

        // Nivora Credits button with special styling
        addDrawableChild(
            createMagicalButton(Text.literal("✨ Nivora Credits ✨"),
                centerX - buttonWidth / 2, startY + spacing * 4, buttonWidth, buttonHeight) {
                client?.setScreen(CreditScreen(this))
            }
        )

        // Quit button
        addDrawableChild(
            createMagicalButton(Text.literal("❌ Quit Game"),
                centerX - buttonWidth / 2, startY + spacing * 5, buttonWidth, buttonHeight) {
                client?.scheduleStop()
            }
        )

        // Small decorative buttons in corners
        addDrawableChild(
            createSmallMagicalButton("🔮", 20, 20) {
                client?.setScreen(OptionsScreen(this, client?.options))
            }
        )

        addDrawableChild(
            createSmallMagicalButton("📜", width - 40, 20) {
                client?.setScreen(CreditScreen(this))
            }
        )
    }

    private fun createMagicalButton(text: Text, x: Int, y: Int, width: Int, height: Int, onPress: (ButtonWidget) -> Unit): ButtonWidget {
        return ButtonWidget.builder(text, onPress)
            .dimensions(x, y, width, height)
            .build()
    }

    private fun createSmallMagicalButton(symbol: String, x: Int, y: Int, onPress: (ButtonWidget) -> Unit): ButtonWidget {
        return ButtonWidget.builder(Text.literal(symbol), onPress)
            .dimensions(x, y, 20, 20)
            .build()
    }

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        animationTime += delta
        particleTime += delta

        // Render magical background
        renderMagicalBackground(context)

        // Draw animated title
        renderAnimatedTitle(context)

        // Draw floating particles
        renderMagicalParticles(context)

        // Draw version info
        renderVersionInfo(context)

        super.render(context, mouseX, mouseY, delta)
    }

    private fun renderMagicalBackground(context: DrawContext) {
        // Create a magical gradient background
        val topColor = 0xFF1a0d2e.toInt() // Deep purple
        val middleColor = 0xFF16213e.toInt() // Dark blue
        val bottomColor = 0xFF0f3460.toInt() // Navy blue

        // Draw gradient background
        context.fillGradient(0, 0, width, height / 2, topColor, middleColor)
        context.fillGradient(0, height / 2, width, height, middleColor, bottomColor)

        // Add magical sparkle overlay
        val sparkleAlpha = (0.1 + 0.05 * sin(animationTime * 0.03)).toFloat()
        val sparkleColor = (sparkleAlpha * 255).toInt() shl 24 or 0x9d4edd

        for (i in 0 until 50) {
            val x = (sin(animationTime * 0.01 + i) * 50 + width * (i % 7) / 7).toInt()
            val y = (cos(animationTime * 0.015 + i * 2) * 30 + height * (i % 5) / 5).toInt()
            context.fill(x, y, x + 2, y + 2, sparkleColor)
        }
    }

    private fun renderAnimatedTitle(context: DrawContext) {
        val title = "NIVORA CLIENT"
        val subtitle = "✨ Magical Minecraft Experience ✨"

        // Animated title color
        val hue = (animationTime * 0.02f) % 1.0f
        val titleColor = MathHelper.hsvToRgb(hue, 0.8f, 1.0f) or 0xFF000000.toInt()

        // Title with glow effect
        val titleWidth = textRenderer.getWidth(title)
        val titleX = width / 2 - titleWidth / 2
        val titleY = 50

        // Draw title shadow/glow
        context.drawText(textRenderer, title, titleX + 2, titleY + 2, 0x88000000.toInt(), false)
        context.drawText(textRenderer, title, titleX + 1, titleY + 1, 0xAA000000.toInt(), false)

        // Draw main title
        context.drawText(textRenderer, title, titleX, titleY, titleColor, false)

        // Draw subtitle
        val subtitleColor = 0xFFd4a4ff.toInt()
        context.drawCenteredTextWithShadow(textRenderer, Text.literal(subtitle), width / 2, titleY + 20, subtitleColor)
    }

    private fun renderMagicalParticles(context: DrawContext) {
        // Floating magical particles
        for (i in 0 until 20) {
            val particleX = (width * 0.1 + sin(particleTime * 0.02 + i) * width * 0.8).toFloat()
            val particleY = (height * 0.2 + cos(particleTime * 0.025 + i * 1.5) * height * 0.6).toFloat()

            val alpha = (0.3 + 0.2 * sin(particleTime * 0.03 + i * 2)).toFloat()
            val particleColor = (alpha * 255).toInt() shl 24 or 0xffd700 // Golden particles

            // Draw particle
            context.fill(particleX.toInt(), particleY.toInt(), particleX.toInt() + 3, particleY.toInt() + 3, particleColor)

            // Draw particle glow
            if (alpha > 0.4) {
                context.fill(particleX.toInt() - 1, particleY.toInt() - 1, particleX.toInt() + 4, particleY.toInt() + 4,
                    (alpha * 0.3 * 255).toInt() shl 24 or 0xffd700)
            }
        }
    }

    private fun renderVersionInfo(context: DrawContext) {
        val versionText = "Nivora Client v1.0.0 | Minecraft 1.21.9"
        val magicText = "✨ Powered by Magic & Code ✨"

        // Version info in bottom right
        context.drawTextWithShadow(textRenderer, versionText,
            width - textRenderer.getWidth(versionText) - 10, height - 30, 0xFFb19cd9.toInt())

        // Magic text in bottom center
        context.drawCenteredTextWithShadow(textRenderer, Text.literal(magicText),
            width / 2, height - 15, 0xFFd4a4ff.toInt())
    }

    override fun shouldCloseOnEsc(): Boolean = false // Main menu shouldn't close on ESC
}
