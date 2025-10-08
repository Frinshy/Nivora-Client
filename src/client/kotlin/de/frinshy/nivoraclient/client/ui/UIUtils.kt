package de.frinshy.nivoraclient.client.ui

import de.frinshy.nivoraclient.client.gui.MagicalTheme
import net.minecraft.client.gui.Click
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.input.CharInput
import net.minecraft.client.input.KeyInput
import net.minecraft.text.Text

object UIUtils {
    
    // Lightweight header render (title + subtitle)
    fun renderHeader(context: DrawContext, screen: Screen, animationTime: Float) {
        val title = "NIVORA"
        val subtitle = "CLIENT"

        val titleY = 36
        val subtitleY = 58

        // animated hue for subtle motion
        val hue = (animationTime * 0.02f) % 1.0f
        val color = net.minecraft.util.math.MathHelper.hsvToRgb(hue, 0.6f, 1.0f) or 0xFF000000.toInt()

        context.drawCenteredTextWithShadow(screen.textRenderer, Text.literal(title), screen.width / 2, titleY, color)
        context.drawCenteredTextWithShadow(screen.textRenderer, Text.literal(subtitle), screen.width / 2, subtitleY, 0xFF8b5cf6.toInt())
    }

    // Create a non-interactive overlay widget that draws the background and header.
    fun createOverlayDrawable(screen: Screen): net.minecraft.client.gui.Drawable {
        return OverlayDrawable(screen)
    }

    // Legacy fallback: create a ButtonWidget-style overlay suitable for insertion into `children`.
    fun createOverlay(screen: Screen): ButtonWidget {
        return object : ButtonWidget(0, 0, screen.width, screen.height, Text.literal(""), {}, DEFAULT_NARRATION_SUPPLIER) {
            private var anim = 0f
            init { this.active = false }

            override fun renderWidget(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
                anim += delta
                try {
                    MagicalTheme.renderOverlay(context, screen.textRenderer, screen.width, screen.height)
                } catch (_: Throwable) {
                    MagicalTheme.renderModernBackground(context, screen.width, screen.height, anim)
                    MagicalTheme.renderModernParticles(context, screen.width, screen.height, anim)
                    MagicalTheme.renderVersionInfo(context, screen.textRenderer, screen.width, screen.height)
                }
            }

            override fun mouseClicked(click: Click, doubled: Boolean): Boolean { return false }
            override fun mouseReleased(click: Click): Boolean { return false }
            override fun mouseScrolled(mouseX: Double, mouseY: Double, horizontalAmount: Double, verticalAmount: Double): Boolean { return false }
            override fun mouseDragged(click: Click, deltaX: Double, deltaY: Double): Boolean { return false }
            override fun keyPressed(keyInput: KeyInput): Boolean { return false }
            override fun keyReleased(keyInput: KeyInput): Boolean { return false }
            override fun charTyped(charInput: CharInput): Boolean { return false }
        }
    }

    // Restyle buttons on the screen to match theme
    fun restyleButtons(screen: Screen) {
        try {
            var c: Class<*>? = screen.javaClass
            while (c != null) {
                try {
                    val childrenField = c.getDeclaredField("children")
                    childrenField.isAccessible = true
                    @Suppress("UNCHECKED_CAST")
                    val children = childrenField.get(screen) as MutableList<Any>
                    
                    for (i in children.indices) {
                        val child = children[i]
                        if (child is ButtonWidget) {
                            val wrapper = createWrapperFor(child, screen)
                            children[i] = wrapper
                        }
                    }
                    break
                } catch (_: Throwable) {
                    c = c.superclass
                }
            }
        } catch (_: Throwable) {
            // Ignore styling errors
        }
    }

    private fun createWrapperFor(orig: ButtonWidget, screen: Screen): ButtonWidget {
        return object : ButtonWidget(orig.x, orig.y, orig.width, orig.height, orig.message, orig.onPress, orig.createNarrationMessage) {
            override fun renderWidget(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
                // Custom button styling
                val bgColor = if (isHovered) 0x806366f1.toInt() else 0x401a1a2e.toInt()
                val borderColor = if (isHovered) 0xFF6366f1.toInt() else 0xFF4338ca.toInt()
                
                context.fill(x, y, x + width, y + height, bgColor)
                context.drawBorder(x, y, width, height, borderColor)
                
                val textColor = if (active) 0xFFffffff.toInt() else 0xFF666666.toInt()
                context.drawCenteredTextWithShadow(screen.textRenderer, message, x + width / 2, y + height / 2 - 4, textColor)
            }
        }
    }
}
