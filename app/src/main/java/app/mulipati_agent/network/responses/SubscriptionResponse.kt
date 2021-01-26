package app.mulipati_agent.network.responses

data class SubscriptionResponse(
    val plan: List<Plan>,
    val subscription: Subscription
)