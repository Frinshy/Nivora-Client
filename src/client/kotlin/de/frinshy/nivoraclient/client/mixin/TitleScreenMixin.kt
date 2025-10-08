package de.frinshy.nivoraclient.client.mixin

import de.frinshy.nivoraclient.client.gui.MagicalTitleScreen
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.TitleScreen
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(TitleScreen::class)
class TitleScreenMixin {

    @Inject(method = ["init"], at = [At("TAIL")])
    private fun replaceWithMagicalTitleScreen(_ci: CallbackInfo) {
        MinecraftClient.getInstance().setScreen(MagicalTitleScreen())
    }
}
