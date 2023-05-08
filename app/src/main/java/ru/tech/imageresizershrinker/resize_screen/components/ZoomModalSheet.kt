package ru.tech.imageresizershrinker.resize_screen.components

import android.graphics.Bitmap
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.smarttoolfactory.image.zoom.animatedZoom
import com.smarttoolfactory.image.zoom.rememberAnimatedZoomState
import eu.wewox.modalsheet.ExperimentalSheetApi
import eu.wewox.modalsheet.ModalSheet
import ru.tech.imageresizershrinker.R
import ru.tech.imageresizershrinker.main_screen.components.LocalBorderWidth
import ru.tech.imageresizershrinker.main_screen.components.TitleItem
import ru.tech.imageresizershrinker.theme.outlineVariant

@OptIn(ExperimentalSheetApi::class)
@Composable
fun ZoomModalSheet(
    bitmap: Bitmap?,
    visible: MutableState<Boolean>
) {
    var showSheet by visible


    if (bitmap != null) {
        ModalSheet(
            visible = showSheet,
            onVisibleChange = {
                showSheet = it
            },
        ) {
            Box(
                Modifier.height(
                    LocalConfiguration.current.screenHeightDp.dp - WindowInsets.systemBars.asPaddingValues()
                        .calculateTopPadding()
                )
            ) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .navigationBarsPadding()
                        .padding(
                            bottom = 80.dp,
                            start = 16.dp,
                            end = 16.dp,
                        )
                        .clip(RoundedCornerShape(4.dp))
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.outlineVariant(),
                            RoundedCornerShape(4.dp)
                        )
                        .background(
                            MaterialTheme.colorScheme
                                .outlineVariant()
                                .copy(alpha = 0.1f),
                            RoundedCornerShape(4.dp)
                        )
                        .animatedZoom(animatedZoomState = rememberAnimatedZoomState())
                )
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                        .navigationBarsPadding()
                ) {
                    TitleItem(text = stringResource(R.string.zoom))
                    Spacer(Modifier.weight(1f))
                    OutlinedButton(
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        ),
                        border = BorderStroke(
                            LocalBorderWidth.current,
                            MaterialTheme.colorScheme.outlineVariant(onTopOf = MaterialTheme.colorScheme.secondaryContainer)
                        ),
                        onClick = {
                            showSheet = false
                        },
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        Text(stringResource(R.string.close))
                    }
                }
            }
        }
    }

}