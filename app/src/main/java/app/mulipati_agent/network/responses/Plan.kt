package app.mulipati_agent.network.responses

data class Plan(
    val created_at: String,
    val id: Int,
    val plan: String,
    val price: Int,
    val trips: Int,
    val updated_at: String
)