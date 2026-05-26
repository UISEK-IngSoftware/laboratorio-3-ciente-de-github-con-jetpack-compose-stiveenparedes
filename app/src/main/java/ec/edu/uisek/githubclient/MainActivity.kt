package ec.edu.uisek.githubclient

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import ec.edu.uisek.githubclient.models.Repository
import ec.edu.uisek.githubclient.ui.screens.RepoForm
import ec.edu.uisek.githubclient.ui.screens.RepoList
import ec.edu.uisek.githubclient.ui.theme.GithubClientTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            GithubClientTheme {

                var currentScreen by remember { mutableStateOf("repoList") }
                var selectedRepo by remember { mutableStateOf<Repository?>(null) }

                when (currentScreen) {

                    "repoList" -> RepoList(
                        onNavigateToRepoForm = {
                            selectedRepo = null
                            currentScreen = "repoForm"
                        },
                        onEditRepo = { repo ->
                            selectedRepo = repo
                            currentScreen = "repoForm"
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