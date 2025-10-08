package de.frinshy.nivoraclient.client

import de.frinshy.nivoraclient.client.gui.MagicalScreenBase
import de.frinshy.nivoraclient.client.ui.UIUtils
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.ButtonWidget

class NivoraClientClient : ClientModInitializer {

    override fun onInitializeClient() {
        ScreenEvents.AFTER_INIT.register { client, screen, _, _ ->
            if (screen is MagicalScreenBase) return@register

            try {
                val overlay = UIUtils.createOverlayDrawable(screen)
                if (!insertOverlay(screen, overlay)) {
                    if (insertIntoFieldList(screen, "children", overlay, addAtStart = true)) {
                        Log.d("NivoraClient: inserted widget overlay into ${screen.javaClass.simpleName} via children field (fallback)")
                        try {
                            ensureButtonsEnabled(screen)
                        } catch (_: Throwable) {
                        }
                        if (Log.DEBUG) debugChildrenSnapshot(screen)
                    }
                }
            } catch (_: Throwable) {
            }

            try {
                client.execute {
                    try {
                        val overlay = UIUtils.createOverlayDrawable(screen)
                        if (!insertOverlay(screen, overlay)) {
                            if (insertIntoFieldList(screen, "children", overlay, addAtStart = true)) {
                                Log.d("NivoraClient: reinserted drawable overlay into ${screen.javaClass.simpleName} via children on next tick (fallback)")
                                try {
                                    ensureButtonsEnabled(screen)
                                } catch (_: Throwable) {
                                }
                                if (Log.DEBUG) debugChildrenSnapshot(screen)
                            }
                        } else {
                            try {
                                ensureButtonsEnabled(screen)
                            } catch (_: Throwable) {
                            }
                        }
                    } catch (_: Throwable) {
                    }
                }
            } catch (_: Throwable) {
            }
        }
    }

    private fun insertIntoFieldList(
        screen: Screen,
        fieldName: String,
        element: Any,
        addAtStart: Boolean = true
    ): Boolean {
        var c: Class<*>? = screen.javaClass
        while (c != null) {
            try {
                val field = c.getDeclaredField(fieldName)
                field.isAccessible = true
                @Suppress("UNCHECKED_CAST")
                val list = field.get(screen) as MutableList<Any>

                val already = list.any { it is de.frinshy.nivoraclient.client.ui.OverlayDrawable }
                if (!already) {
                    if (addAtStart) list.add(0, element) else list.add(element)
                }
                return true
            } catch (_: Throwable) {
                c = c.superclass
            }
        }
        return false
    }

    private fun insertOverlay(screen: Screen, overlay: Any): Boolean {
        var inserted = false
        var c: Class<*>? = screen.javaClass
        while (c != null && !inserted) {
            try {
                val drawablesField = c.getDeclaredField("drawables")
                drawablesField.isAccessible = true
                @Suppress("UNCHECKED_CAST")
                val drawables = drawablesField.get(screen) as MutableList<Any>

                val already = drawables.any { it is de.frinshy.nivoraclient.client.ui.OverlayDrawable }
                if (!already) {
                    if (drawables.isEmpty()) drawables.add(overlay) else drawables.add(0, overlay)
                }

                inserted = true
                Log.d("NivoraClient: inserted drawable overlay into ${screen.javaClass.simpleName} via drawables field")
            } catch (_: Throwable) {
                c = c.superclass
            }
        }
        return inserted
    }

    private fun debugChildrenSnapshot(screen: Screen) {
        var c: Class<*>? = screen.javaClass
        while (c != null) {
            try {
                val childrenField = c.getDeclaredField("children")
                childrenField.isAccessible = true
                @Suppress("UNCHECKED_CAST")
                val children = childrenField.get(screen) as MutableList<Any>

                Log.d(
                    "NivoraClient: children snapshot for ${screen.javaClass.simpleName}: ${
                        children.take(10).joinToString(",") { it.javaClass.simpleName }
                    } (total=${children.size})"
                )
                Log.d("NivoraClient: overlay index in children = ${children.indexOfFirst { it is de.frinshy.nivoraclient.client.ui.OverlayDrawable }}")
                return
            } catch (_: Throwable) {
                c = c.superclass
            }
        }
    }

    private fun ensureButtonsEnabled(screen: Screen) {
        var c: Class<*>? = screen.javaClass
        while (c != null) {
            try {
                val childrenField = c.getDeclaredField("children")
                childrenField.isAccessible = true
                @Suppress("UNCHECKED_CAST")
                val children = childrenField.get(screen) as MutableList<Any>

                for (child in children) {
                    try {
                        if (child is ButtonWidget) {
                            try {
                                val activeField = child.javaClass.getDeclaredField("active")
                                activeField.isAccessible = true
                                activeField.setBoolean(child, true)
                            } catch (_: Throwable) {
                            }
                        }
                    } catch (_: Throwable) {
                    }
                }
                return
            } catch (_: Throwable) {
                c = c.superclass
            }
        }
    }
}
