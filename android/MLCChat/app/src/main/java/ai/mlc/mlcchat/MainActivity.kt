package ai.mlc.mlcchat

import ai.mlc.mlcchat.ui.theme.MLCChatTheme
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import io.objectbox.BoxStore
import io.objectbox.BoxStoreBuilder
import io.objectbox.annotation.Entity
import io.objectbox.annotation.HnswIndex
import io.objectbox.annotation.Id

@Entity
class Schedule {
    @Id var id: Long = 0
    var content: String? = null
}

class MainActivity : ComponentActivity() {

    private var _store: BoxStore? = null

    @RequiresApi(Build.VERSION_CODES.R)
    @ExperimentalMaterial3Api
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestStoragePermission()

        val storeBuilder = BoxStoreBuilder.createDebugWithoutModel()
        _store = storeBuilder.build()

        setContent {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                MLCChatTheme {
                    NavView()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun requestStoragePermission() {
        val intent: Intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
        intent.setData(Uri.parse("package:$packageName"))
        startActivity(intent)
    }
}