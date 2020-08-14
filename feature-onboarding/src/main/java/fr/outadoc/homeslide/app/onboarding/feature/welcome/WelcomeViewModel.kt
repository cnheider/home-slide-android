package fr.outadoc.homeslide.app.onboarding.feature.welcome

import fr.outadoc.homeslide.app.onboarding.navigation.NavigationEvent
import io.uniflow.androidx.flow.AndroidDataFlow

class WelcomeViewModel : AndroidDataFlow() {

    fun onContinueClicked() = action {
        sendEvent { NavigationEvent.Next }
    }
}