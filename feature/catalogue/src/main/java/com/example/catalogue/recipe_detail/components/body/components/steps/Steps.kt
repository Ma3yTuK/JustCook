package com.example.catalogue.recipe_detail.components.body.components.steps

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.catalogue.R
import com.example.catalogue.recipe_detail.components.body.components.steps.components.StepInfo
import com.example.components.JustDivider
import com.example.components.theme.JustCookColorPalette
import com.example.data.models.RecipeStep

@Composable
fun Steps(
    recipeSteps: List<RecipeStep>,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Text(
            text = stringResource(R.string.steps),
            style = MaterialTheme.typography.titleLarge,
            color = JustCookColorPalette.colors.textPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .heightIn(min = 56.dp)
                .wrapContentHeight()
        )

        JustDivider()

        recipeSteps.forEachIndexed { index, recipeStep ->
            StepInfo(recipeStep, index)
            JustDivider()
        }
    }
}