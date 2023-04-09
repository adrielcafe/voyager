package cafe.adriel.voyager.sample.multiplatform.routing

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.routing.core.application.call
import cafe.adriel.voyager.routing.core.routing.VoyagerRouter
import cafe.adriel.voyager.routing.core.routing.route
import cafe.adriel.voyager.routing.core.routing.screen
import cafe.adriel.voyager.routing.core.routing.voyagerRouting
import cafe.adriel.voyager.sample.multiplatform.routing.screens.HomeScreen
import cafe.adriel.voyager.sample.multiplatform.routing.screens.PathScreen
import cafe.adriel.voyager.sample.multiplatform.routing.screens.ReplaceAllScreen
import cafe.adriel.voyager.sample.multiplatform.routing.screens.ReplaceScreen

val router = voyagerRouting {
    screen(path = "/") {
        HomeScreen()
    }

    route(path = "/path", name = "pathWithoutParameters") {
        screen {
            PathScreen()
        }

        screen(path = "/{id}", name = "pathWithParameters") {
            PathScreen(id = call.parameters["id"], q = call.parameters["q"])
        }
    }

    route(path = "/replace", name = "replaceWithoutParameters") {
        screen {
            ReplaceScreen()
        }

        screen(path = "/{id}", name = "replaceWithParameters") {
            ReplaceScreen(id = call.parameters["id"], q = call.parameters["q"])
        }
    }

    route(path = "/replaceAll", name = "replaceAllWithoutParameters") {
        screen {
            ReplaceAllScreen()
        }

        screen(path = "/{id}", name = "replaceAllWithParameters") {
            ReplaceAllScreen(id = call.parameters["id"], q = call.parameters["q"])
        }
    }
}

@Composable
public fun SampleApplication() {
    VoyagerRouter(router)
}
