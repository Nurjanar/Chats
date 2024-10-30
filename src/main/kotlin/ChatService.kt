object ChatService {
    private var chats = mutableListOf<Chats>()
    private var chatCounter = 0
    private var messageCounter = 0

    private fun createChat(chat: Chats): Chats {
        chats += chat.copy(
            chatId = ++chatCounter
        )
        return chats.last()
    }

    fun deleteMessage(fromId: Int, messageId: Int): Boolean {
        val chat = chats.asSequence().find {
            it.fromId == fromId
        }
        val result = chat?.directMessages?.removeIf { it.messageId == messageId } ?: throw MessageNotFoundException()
        if (result) {
            chat.unReadMessages -= 1
        }
        return result
    }

    fun deleteChat(chatId: Int, fromId: Int): Boolean {
        return chats.remove(chats.find {
            it.chatId == chatId &&
                    it.fromId == fromId
        }
            ?: throw ChatNotFoundException())
    }

    fun getChatById(fromId: Int, count: Int): List<Message> {
        var counter = 0
        val chat = chats.asSequence().find { it.fromId == fromId }
            ?: throw ChatNotFoundException()
        val lastMessages = chat.directMessages.takeLast(count)
        lastMessages.forEach { sms ->
            sms.unRead = false
            counter += 1
        }
        chat.unReadMessages -= counter
        return lastMessages
    }

    fun getUnreadChatsCount(): Int = chats.count { it.unReadMessages > 0 }


    fun getLastMessage(): List<String> =
        chats.asSequence().map { chat ->
            chat.directMessages.lastOrNull()?.text ?: "Нет сообщений"
        }
            .map {
                "$it\n"
            }.toList()

    fun getChats(): List<Chats> =
        chats.asSequence().sortedByDescending {
            it.unReadMessages
        }.toList()

    fun createMessage(text: String, fromId: Int): Message {
        val message = Message(
            text = text,
            fromId = fromId,
            messageId = ++messageCounter
        )
        val result = chats.asSequence().find { it.fromId == fromId }

        if (result == null) {
            createChat(
                Chats(
                    fromId = fromId,
                    directMessages = mutableListOf(message),
                    unReadMessages = 1
                )
            )
        } else {
            result.directMessages += message
            result.unReadMessages++
        }

        return message
    }

    fun editMessage(fromId: Int, messageId: Int, newText: String): Boolean {

        chats.asSequence().forEach { chat ->
            chat.directMessages.find {
                it.fromId == fromId &&
                        it.messageId == messageId
            }?.let { it.text = newText }
        }
        return true
    }

    fun clear() {
        chats.clear()
        chatCounter = 0
        messageCounter = 0
    }
}