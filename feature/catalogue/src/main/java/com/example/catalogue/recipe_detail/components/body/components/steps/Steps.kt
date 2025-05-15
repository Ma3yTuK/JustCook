package com.example.catalogue.recipe_detail.components.body.components.steps

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.catalogue.R
import com.example.catalogue.recipe_detail.components.body.components.steps.components.StepInfo
import com.example.components.JustDivider
import com.example.components.theme.JustCookColorPalette
import com.example.data.models.RecipeStep

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Steps(
    recipeSteps: List<RecipeStep>,
    setStepDescription: (RecipeStep, String) -> Unit,
    setStepImage: (RecipeStep, Uri) -> Unit,
    onDeleteStep: (RecipeStep) -> Unit,
    addStep: (String) -> Unit,
    isInEditMode: Boolean,
    modifier: Modifier = Modifier
) {
    var showAddSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    Column(modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .heightIn(min = 56.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.steps),
                style = MaterialTheme.typography.titleLarge,
                color = JustCookColorPalette.colors.textPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            if (isInEditMode) {
                IconButton(onClick = { showAddSheet = true }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.add_step),
                        tint = JustCookColorPalette.colors.textHelp
                    )
                }
            }
        }

        JustDivider()

        recipeSteps.forEachIndexed { index, recipeStep ->
            StepInfo(
                recipeStep = recipeStep,
                index = index,
                setStepDescription = setStepDescription,
                setStepImage = setStepImage,
                onDeleteStep = onDeleteStep,
                isInEditMode,
                modifier = Modifier.padding(vertical = 4.dp)
            )
            JustDivider()
        }
    }

    if (showAddSheet) {
        var newDesc by remember { mutableStateOf("") }
        ModalBottomSheet(
            onDismissRequest = { showAddSheet = false },
            sheetState = sheetState
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.add_step),
                    style = MaterialTheme.typography.titleMedium
                )
                TextField(
                    value = newDesc,
                    onValueChange = { newDesc = it },
                    placeholder = { Text(stringResource(R.string.step_description_hint)) },
                    maxLines = 8,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 100.dp)
                )

                // Кнопка «Сохранить»
                Button(
                    onClick = {
                        addStep(newDesc)
                        showAddSheet = false
                    },
                    enabled = newDesc.isNotBlank(),
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(text = stringResource(R.string.save))
                }
            }
        }
    }
}