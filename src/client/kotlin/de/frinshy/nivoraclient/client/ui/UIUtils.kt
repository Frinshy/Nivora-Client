package de.frinshy.nivoraclient.client.ui

import net.minecraft.client.gui.Drawable
import net.minecraft.client.gui.screen.Screen

object UIUtils {

    // Create a non-interactive overlay widget that draws the background and header.
    // It's added behind other widgets (we insert at index 0).
    fun createOverlayDrawable(screen: Screen): Drawable {
        return OverlayDrawable(screen)
    }

}
