package com.example.catalogue.recipe_detail.components.body.components.steps.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

@Composable
fun StepInfo(
    recipeStep: RecipeStep,
    index: Int,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(vertical = 8.dp)) {
         //Номер шага
        Text(
            text = stringResource(R.string.step_number_format, index), // например "Step 1"
            style = MaterialTheme.typography.bodyMedium,
            color = JustCookColorPalette.colors.textHelp,
            modifier = HzPadding
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Описание + картинка
        TextFlow(
            text = recipeStep.description,
            style = MaterialTheme.typography.titleSmall,
            color = JustCookColorPalette.colors.textSecondary,
            modifier = HzPadding,
            obstacleAlignment = TextFlowObstacleAlignment.TopEnd,
            obstacleContent = {
                if (true) {
                    JustImage(
                        contentDescription = null,
                        modifier = Modifier
                            .size(100.dp)
                    )
                }
            }
        )
    }
}
//@Composable
//fun StepInfo(
//    recipeStep: RecipeStep,
//    index: Int,
//    modifier: Modifier = Modifier
//) {
//    Text(
//        text = "Step $index",
//        style = MaterialTheme.typography.labelSmall,
//        color = JustCookColorPalette.colors.textHelp,
//        modifier = HzPadding
//    )
//
//    TextFlow(
//        text = recipeStep.description,
//        style = MaterialTheme.typography.titleMedium,
//        modifier = modifier,
//        obstacleAlignment = TextFlowObstacleAlignment.TopEnd,
//        obstacleContent = {
//            JustImage(
//                contentDescription = null,
//                modifier = Modifier.padding(4.dp)
//            )
//        }
//    )
//}