fun main() {

    ChatService.createMessage("Привет", 1)
    ChatService.createMessage("Ваш заказ в пути", 2)
    ChatService.createMessage("Срочно набери", 1)
    ChatService.createMessage("Оцените службу доставки", 2)
    ChatService.createMessage("Вам звонили", 3)

    println(ChatService.getChats())
    ChatService.editMessage(1, 1, "Как дела!?")
    println(ChatService.getChats())
    println(ChatService.getChatById(1, 2))
    println(ChatService.getChats())
    println(ChatService.deleteChat(2, 2))
    println(ChatService.getChats())
    println(ChatService.getUnreadChatsCount())
    println(ChatService.getLastMessage())
    println(ChatService.getChats())
    ChatService.deleteMessage(5)
    println(ChatService.getChats())
    println(ChatService.getLastMessage())
}
