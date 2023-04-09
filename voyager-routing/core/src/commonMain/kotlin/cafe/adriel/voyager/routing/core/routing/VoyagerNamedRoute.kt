package cafe.adriel.voyager.routing.core.routing

public data class VoyagerNamedRoute(
    val routingPath: RoutingPath,
    val partAndSelector: Map<String, RouteSelector>,
)
