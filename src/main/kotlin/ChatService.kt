object ChatService {
    private var chats = mutableListOf<Chats>()
    private var chatCounter = 0
    private var messages = mutableListOf<Message>()
    private var messageCounter = 0

    private fun createChat(chat: Chats): Chats {
        chats += chat.copy(
            chatId = ++chatCounter
        )
        return chats.last()
    }

    fun deleteMessage(messageId: Int): Boolean {
        messages.remove(messages.find { it.messageId == messageId }
            ?: throw MessageNotFoundException())
        chats.forEach { chat ->
            val result = chat.directMessages.find { it.messageId == messageId }
            result?.text = ""
            result?.messageId = 0
            chat.unReadMessages -= 1
        }
        return true
    }

    fun deleteChat(chatId: Int, fromId: Int): Boolean {
        chats.remove(chats.find { it.chatId == chatId && it.fromId == fromId }
            ?: throw ChatNotFoundException())
        messages.removeAll(messages.filter { it.fromId == fromId })
        return true
    }

    fun getChatById(fromId: Int): Chats {
        val chat = chats.find { it.fromId == fromId }
            ?: throw ChatNotFoundException()
        chat.unReadMessages = 0
        return chat
    }

    fun getUnreadChatsCount(): Int {
        return chats.sumOf { it.unReadMessages }
    }

    fun getLastMessage(): List<String> {
        var sms: String
        val list = mutableListOf("")
        chats.forEachIndexed { _: Int, chat: Chats ->
            val result = chat.directMessages.findLast { true }?.text
            sms = if (result.isNullOrEmpty()) {
                "Нет сообщений"
            } else {
                result
            }
            list.add(sms + "\n")
        }
        return list
    }

    fun getChats(): List<Chats> {
        val result = chats.sortedWith(compareByDescending<Chats> {
            it.unReadMessages
        })
        return result
    }

    fun createMessage(text: String, fromId: Int): Message {
        val message = Message(
            text = text,
            fromId = fromId,
            messageId = ++messageCounter
        )
        messages += message
        val result = chats.find { it.fromId == fromId }
        if (result == null) {
            createChat(
                Chats(
                    fromId = fromId,
                    directMessages = messages.filter { it.fromId == fromId },
                    unReadMessages = 1
                )
            )
        } else {
            result.directMessages = messages.filter { it.fromId == fromId }
            result.unReadMessages += 1
        }

        return messages.last()
    }

    fun editMessage(messageId: Int, newText: String): Boolean {
        val message = messages.find {
            it.messageId == messageId
        } ?: throw MessageNotFoundException()
        message.text = newText
        return true
    }

    fun clear() {
        chats.clear()
        chatCounter = 0
        messages.clear()
        messageCounter = 0
    }
}