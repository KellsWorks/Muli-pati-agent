package app.mulipati_agent.network.responses

data class Subscription(
    val agent_id: Int,
    val created_at: String,
    val expiry_date: String,
    val id: Int,
    val plan: String,
    val subscribed: Int,
    val updated_at: String
)