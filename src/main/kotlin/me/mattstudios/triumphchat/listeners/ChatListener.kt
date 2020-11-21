package me.mattstudios.triumphchat.listeners

import me.mattstudios.core.func.Task.async
import me.mattstudios.triumphchat.TriumphChat
import me.mattstudios.triumphchat.chat.ChatMessage
import me.mattstudios.triumphchat.events.TriumphChatEvent
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import kotlin.system.measureTimeMillis

class ChatListener(private val plugin: TriumphChat) : Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun AsyncPlayerChatEvent.onPlayerChat() {
        isCancelled = true

        if (!isAsynchronous) {
            async { handleChat() }
            return
        }

        handleChat()

        format
    }

    private fun AsyncPlayerChatEvent.handleChat() {

        val time = measureTimeMillis {

            val chatMessage = ChatMessage(player, message, recipients, plugin.config)

            val triumphChatEvent = TriumphChatEvent(chatMessage)
            Bukkit.getPluginManager().callEvent(triumphChatEvent)

            if (triumphChatEvent.isCancelled) return

            chatMessage.sendMessage()

        }

        //player.sendMessage("Time - ${time}ms")

    }

}