package com.example.components

import android.app.Activity
import android.content.ClipDescription
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.AlertDialogDefaults.iconContentColor
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.foundation.background
import androidx.compose.material.icons.filled.Edit
import com.example.components.theme.JustCookColorPalette
import androidx.compose.foundation.layout.size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.zIndex
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.yalantis.ucrop.UCrop
import java.io.File
import java.io.FileOutputStream

@Composable
fun JustImage(
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    model: Any = R.drawable.placeholder,
    isEditable: Boolean = false,
    isVerified: Boolean = false,
    isPremium: Boolean = false,
    onImageChange: (Uri) -> Unit = {},
    elevation: Dp = 0.dp,
) {
    val context = LocalContext.current
    val openDialog = remember { mutableStateOf(false) }

    val cropLauncher = rememberLauncherForActivityResult(object : ActivityResultContract<Uri, Uri?>() {
        override fun createIntent(context: Context, input: Uri): Intent {
            val destinationUri = Uri.fromFile(File(context.cacheDir, "edit_image_${System.currentTimeMillis()}.png"))
            return UCrop.of(input, destinationUri)
                .withAspectRatio(1f, 1f)
                .withOptions(UCrop.Options().apply {
                    setShowCropGrid(false)
                    setShowCropFrame(false)
                    setHideBottomControls(true)
                    setToolbarTitle(context.getString(R.string.edit_image))
                    setCircleDimmedLayer(true) // circular crop
                })
                .getIntent(context)
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
            return if (resultCode == Activity.RESULT_OK) UCrop.getOutput(intent!!) else null
        }
    }) { uri ->
        uri?.let { onImageChange(it) }
    }
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        bitmap?.let {
            val uri = saveBitmapToCache(context, it)
            cropLauncher.launch(uri)
        }
    }
    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { cropLauncher.launch(it) }
    }

    Box(modifier = modifier) {
        JustSurface(
            elevation = elevation,
            shape = CircleShape,
            modifier = Modifier.fillMaxSize()
        ) {
            AsyncImage(
                model = model,
                contentDescription = contentDescription,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        if (isEditable) {
            IconButton(
                onClick = { openDialog.value = true },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .fillMaxSize(0.2f)
                    .background(
                        color = JustCookColorPalette.colors.uiBackground,
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit image",
                    tint = JustCookColorPalette.colors.textPrimary
                )
            }
        } else if (isVerified) {
            Box (
                modifier = Modifier.zIndex(3.0f)
                    .align(Alignment.BottomEnd)
                    .fillMaxSize(0.2f)
                    .background(
                        color = JustCookColorPalette.colors.uiBackground,
                        shape = CircleShape
                    )
            ) {
                Icon(
                    painter = painterResource(R.drawable.verified),
                    contentDescription = stringResource(R.string.verified_user),
                    tint = JustCookColorPalette.colors.textPrimary
                )
            }
        }
    }

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            title = { Text("Сменить фото") },
            text = {
                Column {
                    TextButton(onClick = {
                        openDialog.value = false
                        galleryLauncher.launch("image/*")
                    }) {
                        Text("Из галереи")
                    }
                    TextButton(onClick = {
                        openDialog.value = false
                        cameraLauncher.launch(null)
                    }) {
                        Text("С камеры")
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { openDialog.value = false }) {
                    Text("Отмена")
                }
            }
        )
    }
}

// Вспомогательная функция: сохраняем Bitmap во временный файл и возвращаем Uri
fun saveBitmapToCache(context: Context, bitmap: Bitmap): Uri {
    val cachePath = File(context.cacheDir, "images")
    cachePath.mkdirs()
    val file = File(cachePath, "edit_image_${System.currentTimeMillis()}.png")
    FileOutputStream(file).use { out ->
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
    }
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        file
    )
}