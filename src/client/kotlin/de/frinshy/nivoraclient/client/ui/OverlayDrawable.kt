package de.frinshy.nivoraclient.client.ui

import de.frinshy.nivoraclient.client.gui.MagicalTheme
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.Drawable
import net.minecraft.client.gui.screen.Screen

// Lightweight drawable that only renders the Nivora background/particles/version info.
// It does not implement input-handling interfaces, so it will not intercept input.
class OverlayDrawable(private val screen: Screen) : Drawable {
    private var anim = 0f

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        anim += delta

        MagicalTheme.renderOverlay(context, screen.textRenderer, screen.width, screen.height)
    }
}

