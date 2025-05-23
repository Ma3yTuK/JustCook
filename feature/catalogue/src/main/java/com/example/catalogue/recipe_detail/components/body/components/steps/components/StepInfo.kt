package com.example.catalogue.recipe_detail.components.body.components.steps.components

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.catalogue.R
import com.example.catalogue.recipe_detail.HzPadding
import com.example.components.JustImage
import com.example.components.theme.JustCookColorPalette
import com.example.data.models.RecipeStep
import eu.wewox.textflow.material3.TextFlow
import eu.wewox.textflow.material3.TextFlowObstacleAlignment
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StepInfo(
    recipeStep: RecipeStep,
    index: Int,
    updateStep: (String?, Uri?) -> Unit,
    onDeleteStep: (RecipeStep) -> Unit,
    isInEditMode: Boolean,
    modifier: Modifier = Modifier
) {
    // состояние для открытия BottomSheet
    var showEditSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    // временный текст для редактирования
    var editedDescription by remember { mutableStateOf(recipeStep.description) }

    Column(modifier = modifier.padding(vertical = 8.dp)) {
        // Заголовок шага + кнопки
        Row(
            modifier = HzPadding
                .fillMaxWidth()
                .height(30.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.step_number_format, index + 1),
                style = MaterialTheme.typography.bodyMedium,
                color = JustCookColorPalette.colors.textHelp
            )

            Spacer(modifier = Modifier.weight(1f))

            if (isInEditMode) {
                IconButton(
                    modifier = Modifier
                        .width(30.dp),
                    onClick = {
                        editedDescription = recipeStep.description
                        showEditSheet = true
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = stringResource(R.string.edit_step),
                        tint = JustCookColorPalette.colors.textHelp
                    )
                }

                // Кнопка удалить
                IconButton(
                    modifier = Modifier
                        .width(30.dp),
                    onClick = { onDeleteStep(recipeStep) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.delete_step),
                        tint = JustCookColorPalette.colors.error
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Описание + картинка (нередактируемая)
        TextFlow(
            text = recipeStep.description,
            style = MaterialTheme.typography.titleSmall,
            color = JustCookColorPalette.colors.textSecondary,
            modifier = HzPadding,
            obstacleAlignment = TextFlowObstacleAlignment.TopEnd,
            obstacleContent = {
                JustImage(
                    image = recipeStep.image,
                    contentDescription = null,
                    isEditable = isInEditMode,
                    onImageChange = { uri -> updateStep(null, uri) },
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }
        )
    }

    // BottomSheet для редактирования описания
    if (showEditSheet) {
        ModalBottomSheet(
            onDismissRequest = { showEditSheet = false },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.edit_step_description),
                    style = MaterialTheme.typography.titleMedium
                )

                TextField(
                    value = editedDescription,
                    onValueChange = { editedDescription = it },
                    textStyle = MaterialTheme.typography.bodyLarge,
                    placeholder = { Text(stringResource(R.string.step_description_hint)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 100.dp)
                )

                Button(
                    onClick = {
                        updateStep(editedDescription, null)
                        showEditSheet = false
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(text = stringResource(R.string.save))
                }
            }
        }
    }
}