package ec.edu.uisek.githubclient

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import ec.edu.uisek.githubclient.models.Repository
import ec.edu.uisek.githubclient.services.AuthService
import ec.edu.uisek.githubclient.services.RetrofitClient
import ec.edu.uisek.githubclient.ui.screens.LoginForm
import ec.edu.uisek.githubclient.ui.screens.RepoForm
import ec.edu.uisek.githubclient.ui.screens.RepoList
import ec.edu.uisek.githubclient.ui.theme.GithubClientTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        val authService = AuthService(this)
        RetrofitClient.init(this)

        setContent {
            GithubClientTheme {

                var isLoggedIn by remember {
                    mutableStateOf(authService.isLoggedIn())
                }

                var currentScreen by remember {
                    mutableStateOf(if (isLoggedIn) "repoList" else "login")
                }

                var selectedRepo by remember {
                    mutableStateOf<Repository?>(null)
                }

                when (currentScreen) {

                    "login" -> LoginForm(
                        onLoginSuccess = {
                            isLoggedIn = true
                            currentScreen = "repoList"
                        }
                    )

                    "repoList" -> RepoList(
                        onNavigateToRepoForm = {
                            selectedRepo = null
                            currentScreen = "repoForm"
                        },
                        onEditRepo = { repo ->
                            selectedRepo = repo
                            currentScreen = "repoForm"
                        },
                        onLogout = {
                            authService.logout()
                            isLoggedIn = false
                            currentScreen = "login"
                        }
                    )

                    "repoForm" -> RepoForm(
                        repositoryToEdit = selectedRepo,
                        onBackClick = {
                            currentScreen = "repoList"
                        },
                        onSaveSuccess = {
                            currentScreen = "repoList"
                        }
                    )
                }
            }
        }
    }
}