package me.mattstudios.triumphchat.listeners

import me.mattstudios.core.func.Task.async
import me.mattstudios.triumphchat.TriumphChat
import me.mattstudios.triumphchat.api.events.TriumphChatEvent
import me.mattstudios.triumphchat.chat.ChatMessage
import me.mattstudios.triumphchat.chat.ConsoleMessage
import me.mattstudios.triumphchat.config.bean.FormatsHolder
import me.mattstudios.triumphchat.config.bean.objects.MessageDisplay
import me.mattstudios.triumphchat.config.settings.Settings
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

class ChatListener(private val plugin: TriumphChat) : Listener {

    private val config = plugin.config

    /**
     * Listens to the AsyncPlayerChatEvent
     */
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun AsyncPlayerChatEvent.onPlayerChat() {
        isCancelled = true

        if (!isAsynchronous) {
            async { handleChat() }
            return
        }

        handleChat()
    }

    /**
     * Handles chat event truly async
     */
    private fun AsyncPlayerChatEvent.handleChat() {
        val chatPlayer = plugin.playerManager.getPlayer(player)
        val chatMessage = ChatMessage(chatPlayer, message, recipients, plugin,  listOf(MessageDisplay(config[Settings.CONSOLE_FORMAT])))
        val consoleMessage = ConsoleMessage(
            chatPlayer,
            message,
            recipients,
            plugin,
            listOf(MessageDisplay(config[Settings.CONSOLE_FORMAT]))
        )

        val triumphChatEvent = TriumphChatEvent(chatMessage)
        Bukkit.getPluginManager().callEvent(triumphChatEvent)

        if (triumphChatEvent.isCancelled) return

        chatMessage.sendMessage()
        consoleMessage.sendMessage()
    }

    /**
     * Selects the format to use for the message
     */
    private fun selectFormat(player: Player): FormatsHolder {
        /*val formats = plugin.formatsConfig.getFormats().
        return formats.filter { player.hasPermission("${Permission.FORMAT.permission}.${it.key}") }
                .maxByOrNull { it.value.priority }?.value ?: formats["default"] ?: DEFAULT_FORMAT*/
        return FormatsHolder()
    }

}