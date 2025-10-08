package de.frinshy.nivoraclient.client.gui

import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text

class CreditScreen(private val parent: Screen) : MagicalScreenBase(Text.literal("Nivora Client Credits")) {

    private var scrollOffset = 0f

    private companion object {
        const val LINE_HEIGHT = 12
        const val START_Y = 60
        const val FADE_ZONE = 60
        const val SCROLL_STEP = 10f
    }

    override fun init() {
        super.init()
        addDrawableChild(createFloatingActionButton("←", width - 60, height - 60) { _ -> client?.setScreen(parent) })
    }

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        renderTheme(context, delta, "NIVORA", "CREDITS", "Thank you for using Nivora")

        val credits = listOf(
            "",
            "§6§l✦ NIVORA CLIENT ✦",
            "",
            "§e━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━",
            "",
            "§6Created with passion by:",
            "§b§lFrinshy",
            "§7Lead Developer & Designer",
            "",
            "§6Special Features:",
            "§a• Custom GUI Enhancements",
            "§a• Performance Optimizations",
            "§a• Quality of Life Improvements",
            "§a• Beautiful Visual Effects",
            "",
            "§6Built with:",
            "§7• Fabric Mod Loader",
            "§7• Minecraft Forge API",
            "§7• Kotlin Programming Language",
            "§7• Modern Minecraft Modding Practices",
            "",
            "§6Special Thanks to:",
            "§d• Fabric Community",
            "§d• Minecraft Modding Community",
            "§d• All Beta Testers",
            "§d• Feature Requesters",
            "",
            "§6External Libraries:",
            "§7• Fabric API",
            "§7• Mixin Framework",
            "§7• Kotlin Standard Library",
            "",
            "§e━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━",
            "",
            "§c§l❤ §6Made with love for the Minecraft community §c§l❤",
            "",
            "§8§oThank you for using Nivora Client!",
            "§8§oReport bugs and suggest features on our GitHub",
            "",
            "§5§l~ End of Credits ~"
        )

        val contentHeight = credits.size * LINE_HEIGHT
        val minOffset = -height.toFloat()
        val maxOffset = contentHeight.toFloat()

        var yOffset = START_Y - scrollOffset
        for (line in credits) {
            if (yOffset > -LINE_HEIGHT && yOffset < height + LINE_HEIGHT) {
                val text = Text.literal(line)

                val alpha = when {
                    yOffset < FADE_ZONE -> (yOffset + FADE_ZONE).coerceIn(0f, FADE_ZONE.toFloat()) / FADE_ZONE
                    yOffset > height - FADE_ZONE -> (height - yOffset + FADE_ZONE).coerceIn(
                        0f,
                        FADE_ZONE.toFloat()
                    ) / FADE_ZONE

                    else -> 1f
                }

                val color = ((alpha * 255).toInt() shl 24) or 0x00FFFFFF
                if (alpha > 0.05f) context.drawCenteredTextWithShadow(
                    textRenderer,
                    text,
                    width / 2,
                    yOffset.toInt(),
                    color
                )
            }
            yOffset += LINE_HEIGHT
        }

        if (scrollOffset < minOffset) scrollOffset = minOffset
        if (scrollOffset > maxOffset) scrollOffset = maxOffset

        super.render(context, mouseX, mouseY, delta)
    }

    override fun mouseScrolled(
        mouseX: Double,
        mouseY: Double,
        horizontalAmount: Double,
        verticalAmount: Double
    ): Boolean {
        scrollOffset -= (verticalAmount * SCROLL_STEP).toFloat()
        return true
    }

    override fun shouldCloseOnEsc(): Boolean = true

    override fun close() {
        client?.setScreen(parent)
    }
}
