data class Chats(
    val chatId: Int = 0,
    val fromId: Int = 0,
    var unReadMessages: Int = 0,
    var directMessages: MutableList<Message>
)

data class Message(
    var text: String = " ",
    var messageId: Int = 0,
    val fromId: Int = 0,
    var unRead: Boolean = true
)