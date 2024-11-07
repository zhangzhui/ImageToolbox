/*
 * ImageToolbox is an image editor for android
 * Copyright (c) 2024 T8RIN (Malik Mukhametzyanov)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * You should have received a copy of the Apache License
 * along with this program.  If not, see <http://www.apache.org/licenses/LICENSE-2.0>.
 */

package ru.tech.imageresizershrinker.feature.gradient_maker.presentation

import android.net.Uri
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Collections
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.t8rin.dynamic.theme.LocalDynamicThemeState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.tech.imageresizershrinker.core.domain.model.IntegerSize
import ru.tech.imageresizershrinker.core.resources.R
import ru.tech.imageresizershrinker.core.settings.presentation.provider.LocalSettingsState
import ru.tech.imageresizershrinker.core.settings.presentation.provider.rememberAppColorTuple
import ru.tech.imageresizershrinker.core.ui.theme.blend
import ru.tech.imageresizershrinker.core.ui.utils.confetti.LocalConfettiHostState
import ru.tech.imageresizershrinker.core.ui.utils.helper.Picker
import ru.tech.imageresizershrinker.core.ui.utils.helper.asClip
import ru.tech.imageresizershrinker.core.ui.utils.helper.isPortraitOrientationAsState
import ru.tech.imageresizershrinker.core.ui.utils.helper.localImagePickerMode
import ru.tech.imageresizershrinker.core.ui.utils.helper.parseSaveResult
import ru.tech.imageresizershrinker.core.ui.utils.helper.parseSaveResults
import ru.tech.imageresizershrinker.core.ui.utils.helper.rememberImagePicker
import ru.tech.imageresizershrinker.core.ui.utils.navigation.Screen
import ru.tech.imageresizershrinker.core.ui.utils.provider.LocalComponentActivity
import ru.tech.imageresizershrinker.core.ui.widget.AdaptiveLayoutScreen
import ru.tech.imageresizershrinker.core.ui.widget.buttons.BottomButtonsBlock
import ru.tech.imageresizershrinker.core.ui.widget.buttons.CompareButton
import ru.tech.imageresizershrinker.core.ui.widget.buttons.ShareButton
import ru.tech.imageresizershrinker.core.ui.widget.buttons.ShowOriginalButton
import ru.tech.imageresizershrinker.core.ui.widget.controls.SaveExifWidget
import ru.tech.imageresizershrinker.core.ui.widget.controls.selection.AlphaSelector
import ru.tech.imageresizershrinker.core.ui.widget.controls.selection.ImageFormatSelector
import ru.tech.imageresizershrinker.core.ui.widget.dialogs.ExitWithoutSavingDialog
import ru.tech.imageresizershrinker.core.ui.widget.dialogs.LoadingDialog
import ru.tech.imageresizershrinker.core.ui.widget.dialogs.OneTimeImagePickingDialog
import ru.tech.imageresizershrinker.core.ui.widget.dialogs.OneTimeSaveLocationSelectionDialog
import ru.tech.imageresizershrinker.core.ui.widget.image.ImageCounter
import ru.tech.imageresizershrinker.core.ui.widget.image.Picture
import ru.tech.imageresizershrinker.core.ui.widget.modifier.container
import ru.tech.imageresizershrinker.core.ui.widget.modifier.detectSwipes
import ru.tech.imageresizershrinker.core.ui.widget.modifier.withModifier
import ru.tech.imageresizershrinker.core.ui.widget.other.LocalToastHostState
import ru.tech.imageresizershrinker.core.ui.widget.other.TopAppBarEmoji
import ru.tech.imageresizershrinker.core.ui.widget.other.showError
import ru.tech.imageresizershrinker.core.ui.widget.preferences.PreferenceItem
import ru.tech.imageresizershrinker.core.ui.widget.sheets.PickImageFromUrisSheet
import ru.tech.imageresizershrinker.core.ui.widget.sheets.ProcessImagesPreferenceSheet
import ru.tech.imageresizershrinker.core.ui.widget.text.TopAppBarTitle
import ru.tech.imageresizershrinker.feature.compare.presentation.components.CompareSheet
import ru.tech.imageresizershrinker.feature.gradient_maker.presentation.components.ColorStopSelection
import ru.tech.imageresizershrinker.feature.gradient_maker.presentation.components.GradientPreview
import ru.tech.imageresizershrinker.feature.gradient_maker.presentation.components.GradientPropertiesSelector
import ru.tech.imageresizershrinker.feature.gradient_maker.presentation.components.GradientSizeSelector
import ru.tech.imageresizershrinker.feature.gradient_maker.presentation.components.GradientTypeSelector
import ru.tech.imageresizershrinker.feature.gradient_maker.presentation.components.TileModeSelector
import ru.tech.imageresizershrinker.feature.gradient_maker.presentation.components.rememberGradientState
import ru.tech.imageresizershrinker.feature.gradient_maker.presentation.screenLogic.GradientMakerComponent

@Composable
fun GradientMakerContent(
    onGoBack: () -> Unit,
    onNavigate: (Screen) -> Unit,
    component: GradientMakerComponent
) {
    val scope = rememberCoroutineScope()
    val themeState = LocalDynamicThemeState.current

    val settingsState = LocalSettingsState.current
    val allowChangeColor = settingsState.allowChangeColorByImage

    val appColorTuple = rememberAppColorTuple()

    val context = LocalComponentActivity.current

    val confettiHostState = LocalConfettiHostState.current
    val showConfetti: () -> Unit = {
        scope.launch { confettiHostState.showConfetti() }
    }
    val toastHostState = LocalToastHostState.current

    var allowPickingImage by rememberSaveable(component.initialUris) {
        mutableStateOf(
            if (component.initialUris.isNullOrEmpty()) null
            else true
        )
    }

    LaunchedEffect(component.brush, component.selectedUri) {
        if (allowChangeColor && allowPickingImage != null) {
            component.createGradientBitmap(
                data = component.selectedUri,
                integerSize = IntegerSize(1000, 1000)
            )?.let { bitmap ->
                themeState.updateColorByImage(bitmap)
            }
        }
    }

    LaunchedEffect(allowPickingImage) {
        if (allowPickingImage != true) {
            component.clearState()
        }
    }

    var showExitDialog by rememberSaveable { mutableStateOf(false) }

    val colorScheme = MaterialTheme.colorScheme
    LaunchedEffect(component.colorStops) {
        if (component.colorStops.isEmpty()) {
            colorScheme.apply {
                component.addColorStop(0f to primary.blend(primaryContainer, 0.5f))
                component.addColorStop(0.5f to secondary.blend(secondaryContainer, 0.5f))
                component.addColorStop(1f to tertiary.blend(tertiaryContainer, 0.5f))
            }
        }
    }


    val imagePicker = rememberImagePicker(
        mode = localImagePickerMode(Picker.Multiple)
    ) { uris ->
        uris.takeIf { it.isNotEmpty() }?.let {
            allowPickingImage = true
            component.setUris(it) {
                scope.launch {
                    toastHostState.showError(context, it)
                }
            }
            component.updateGradientAlpha(0.5f)
        }
    }

    val pickImage = imagePicker::pickImage

    val isPortrait by isPortraitOrientationAsState()

    var showPickImageFromUrisSheet by rememberSaveable { mutableStateOf(false) }

    var showCompareSheet by rememberSaveable { mutableStateOf(false) }
    var showOriginal by rememberSaveable { mutableStateOf(false) }

    AdaptiveLayoutScreen(
        shouldDisableBackHandler = !component.haveChanges,
        isPortrait = isPortrait,
        canShowScreenData = allowPickingImage != null,
        title = {
            TopAppBarTitle(
                title = if (allowPickingImage != true) {
                    stringResource(R.string.gradient_maker)
                } else stringResource(R.string.gradient_maker_type_image),
                input = Unit,
                isLoading = false,
                size = null
            )
        },
        onGoBack = {
            if (component.haveChanges) showExitDialog = true
            else onGoBack()
        },
        actions = {
            if (component.uris.isNotEmpty()) {
                ShowOriginalButton(
                    canShow = true,
                    onStateChange = {
                        showOriginal = it
                    }
                )
            }
            var editSheetData by remember {
                mutableStateOf(listOf<Uri>())
            }
            ShareButton(
                enabled = component.brush != null,
                onShare = {
                    component.shareBitmaps(showConfetti)
                },
                onCopy = { manager ->
                    component.cacheCurrentImage { uri ->
                        manager.setClip(uri.asClip(context))
                        showConfetti()
                    }
                },
                onEdit = {
                    component.cacheImages {
                        editSheetData = it
                    }
                }
            )
            ProcessImagesPreferenceSheet(
                uris = editSheetData,
                visible = editSheetData.isNotEmpty(),
                onDismiss = {
                    if (!it) {
                        editSheetData = emptyList()
                    }
                },
                onNavigate = { screen ->
                    scope.launch {
                        editSheetData = emptyList()
                        delay(200)
                        onNavigate(screen)
                    }
                }
            )
        },
        topAppBarPersistentActions = {
            if (allowPickingImage == null) {
                TopAppBarEmoji()
            }
            CompareButton(
                onClick = { showCompareSheet = true },
                visible = component.brush != null && allowPickingImage == true && component.selectedUri != Uri.EMPTY
            )
        },
        imagePreview = {
            Box(
                modifier = Modifier
                    .detectSwipes(
                        onSwipeRight = component::selectLeftUri,
                        onSwipeLeft = component::selectRightUri
                    )
                    .container()
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                GradientPreview(
                    brush = component.brush,
                    gradientAlpha = if (showOriginal) 0f else component.gradientAlpha,
                    allowPickingImage = allowPickingImage,
                    gradientSize = component.gradientSize,
                    onSizeChanged = component::setPreviewSize,
                    selectedUri = component.selectedUri,
                    imageAspectRatio = component.imageAspectRatio
                )
            }
        },
        controls = {
            ImageCounter(
                imageCount = component.uris.size.takeIf { it > 1 },
                onRepick = {
                    showPickImageFromUrisSheet = true
                }
            )
            AnimatedContent(
                allowPickingImage == false
            ) { canChangeSize ->
                if (canChangeSize) {
                    GradientSizeSelector(
                        value = component.gradientSize,
                        onWidthChange = component::updateWidth,
                        onHeightChange = component::updateHeight
                    )
                } else {
                    AlphaSelector(
                        value = component.gradientAlpha,
                        onValueChange = component::updateGradientAlpha,
                        modifier = Modifier
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            GradientTypeSelector(
                value = component.gradientType,
                onValueChange = component::setGradientType
            ) {
                GradientPropertiesSelector(
                    gradientType = component.gradientType,
                    linearAngle = component.angle,
                    onLinearAngleChange = component::updateLinearAngle,
                    centerFriction = component.centerFriction,
                    radiusFriction = component.radiusFriction,
                    onRadialDimensionsChange = component::setRadialProperties
                )
            }
            Spacer(Modifier.height(8.dp))
            ColorStopSelection(
                colorStops = component.colorStops,
                onRemoveClick = component::removeColorStop,
                onValueChange = component::updateColorStop,
                onAddColorStop = component::addColorStop
            )
            Spacer(Modifier.height(8.dp))
            TileModeSelector(
                value = component.tileMode,
                onValueChange = component::setTileMode
            )
            Spacer(Modifier.height(8.dp))
            SaveExifWidget(
                checked = component.keepExif,
                imageFormat = component.imageFormat,
                onCheckedChange = component::toggleKeepExif
            )
            Spacer(Modifier.height(8.dp))
            ImageFormatSelector(
                value = component.imageFormat,
                forceEnabled = allowPickingImage == false,
                onValueChange = component::setImageFormat,
                backgroundColor = MaterialTheme.colorScheme.surfaceContainer
            )
        },
        noDataControls = {
            val preference1 = @Composable {
                val screen = remember {
                    Screen.GradientMaker()
                }
                PreferenceItem(
                    title = stringResource(screen.title),
                    subtitle = stringResource(screen.subtitle),
                    startIcon = screen.icon,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        allowPickingImage = false
                    }
                )
            }
            val preference2 = @Composable {
                PreferenceItem(
                    title = stringResource(R.string.gradient_maker_type_image),
                    subtitle = stringResource(R.string.gradient_maker_type_image_sub),
                    startIcon = Icons.Outlined.Collections,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = pickImage
                )
            }
            if (isPortrait) {
                Column {
                    preference1()
                    Spacer(modifier = Modifier.height(8.dp))
                    preference2()
                }
            } else {
                val direction = LocalLayoutDirection.current
                Row(
                    modifier = Modifier.padding(
                        WindowInsets.displayCutout.asPaddingValues().let {
                            PaddingValues(
                                start = it.calculateStartPadding(direction),
                                end = it.calculateEndPadding(direction)
                            )
                        }
                    )
                ) {
                    preference1.withModifier(modifier = Modifier.weight(1f))
                    Spacer(modifier = Modifier.width(8.dp))
                    preference2.withModifier(modifier = Modifier.weight(1f))
                }
            }
        },
        buttons = { actions ->
            val saveBitmap: (oneTimeSaveLocationUri: String?) -> Unit = {
                if (component.brush != null) {
                    component.saveBitmaps(
                        oneTimeSaveLocationUri = it,
                        onStandaloneGradientSaveResult = { saveResult ->
                            context.parseSaveResult(
                                saveResult = saveResult,
                                onSuccess = showConfetti,
                                toastHostState = toastHostState,
                                scope = scope
                            )
                        },
                        onResult = { results ->
                            context.parseSaveResults(
                                scope = scope,
                                results = results,
                                toastHostState = toastHostState,
                                isOverwritten = settingsState.overwriteFiles,
                                showConfetti = showConfetti
                            )
                        }
                    )
                }
            }
            var showFolderSelectionDialog by rememberSaveable {
                mutableStateOf(false)
            }
            var showOneTimeImagePickingDialog by rememberSaveable {
                mutableStateOf(false)
            }
            BottomButtonsBlock(
                targetState = (allowPickingImage == null) to isPortrait,
                onSecondaryButtonClick = pickImage,
                isSecondaryButtonVisible = allowPickingImage == true,
                isPrimaryButtonVisible = component.brush != null,
                showNullDataButtonAsContainer = true,
                onPrimaryButtonClick = {
                    saveBitmap(null)
                },
                onPrimaryButtonLongClick = {
                    showFolderSelectionDialog = true
                },
                actions = {
                    if (isPortrait) actions()
                },
                onSecondaryButtonLongClick = {
                    showOneTimeImagePickingDialog = true
                }
            )
            OneTimeSaveLocationSelectionDialog(
                visible = showFolderSelectionDialog,
                onDismiss = { showFolderSelectionDialog = false },
                onSaveRequest = saveBitmap,
                formatForFilenameSelection = component.getFormatForFilenameSelection()
            )
            OneTimeImagePickingDialog(
                onDismiss = { showOneTimeImagePickingDialog = false },
                picker = Picker.Multiple,
                imagePicker = imagePicker,
                visible = showOneTimeImagePickingDialog
            )
        },
        forceImagePreviewToMax = showOriginal,
        contentPadding = animateDpAsState(
            if (allowPickingImage == null) 12.dp
            else 20.dp
        ).value
    )

    PickImageFromUrisSheet(
        transformations = remember(component.brush) {
            derivedStateOf {
                listOf(
                    component.getGradientTransformation()
                )
            }
        }.value,
        visible = showPickImageFromUrisSheet,
        onDismiss = {
            showPickImageFromUrisSheet = false
        },
        uris = component.uris,
        selectedUri = component.selectedUri,
        onUriPicked = { uri ->
            component.updateSelectedUri(uri = uri) {
                scope.launch {
                    toastHostState.showError(context, it)
                }
            }
        },
        onUriRemoved = { uri ->
            component.updateUrisSilently(removedUri = uri)
        },
        columns = if (isPortrait) 2 else 4,
    )

    CompareSheet(
        beforeContent = {
            Picture(
                model = component.selectedUri,
                modifier = Modifier.aspectRatio(
                    component.imageAspectRatio
                ),
                shape = MaterialTheme.shapes.medium
            )
        },
        afterContent = {
            val gradientState = rememberGradientState()
            LaunchedEffect(component.brush) {
                gradientState.gradientType = component.gradientType
                gradientState.linearGradientAngle = component.angle
                gradientState.centerFriction = component.centerFriction
                gradientState.radiusFriction = component.radiusFriction
                gradientState.colorStops.apply {
                    clear()
                    addAll(component.colorStops)
                }
                gradientState.tileMode = component.tileMode
            }
            GradientPreview(
                brush = gradientState.brush,
                gradientAlpha = component.gradientAlpha,
                allowPickingImage = allowPickingImage,
                gradientSize = component.gradientSize,
                onSizeChanged = {
                    gradientState.size = it
                },
                selectedUri = component.selectedUri,
                imageAspectRatio = component.imageAspectRatio
            )
        },
        visible = showCompareSheet,
        onDismiss = {
            showCompareSheet = false
        },
        shape = MaterialTheme.shapes.medium
    )

    if (component.left != -1) {
        LoadingDialog(
            visible = component.isSaving || component.isImageLoading,
            done = component.done,
            left = component.left,
            onCancelLoading = component::cancelSaving
        )
    } else {
        LoadingDialog(
            visible = component.isSaving || component.isImageLoading,
            canCancel = component.isSaving,
            onCancelLoading = component::cancelSaving
        )
    }

    ExitWithoutSavingDialog(
        onExit = {
            if (allowPickingImage != null) {
                allowPickingImage = null
                themeState.updateColorTuple(appColorTuple)
                component.clearState()
            } else onGoBack()
        },
        onDismiss = { showExitDialog = false },
        visible = showExitDialog
    )
}