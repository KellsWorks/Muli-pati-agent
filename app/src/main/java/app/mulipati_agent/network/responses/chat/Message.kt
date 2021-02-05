package app.mulipati_agent.network.responses.chat

data class Message(
    val created_at: String,
    val from: Int,
    val id: Int,
    val is_read: Int,
    val message: String,
    val to: Int,
    var time: String,
    val trip_id: Int,
    val updated_at: String
)