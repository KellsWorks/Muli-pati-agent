package app.mulipati_agent.network.responses

data class User(
    val fcmId: List<Int>,
    val id: Int,
    val membership: String,
    val name: String,
    val phone: String,
    val profile: List<Profile>,
    val token: String
)