package ru.tech.imageresizershrinker.resize_screen.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun BoxScope.Loading() {
    Box(
        Modifier
            .size(84.dp)
            .clip(RoundedCornerShape(24.dp))
            .shadow(
                8.dp,
                RoundedCornerShape(24.dp)
            )
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .align(Alignment.Center)
    ) {
        CircularProgressIndicator(
            Modifier.align(
                Alignment.Center
            ),
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}

@Composable
fun Loading(modifier: Modifier = Modifier) {
    Box(
        modifier
            .size(84.dp)
            .clip(RoundedCornerShape(24.dp))
            .shadow(
                8.dp,
                RoundedCornerShape(24.dp)
            )
            .background(MaterialTheme.colorScheme.secondaryContainer)
    ) {
        CircularProgressIndicator(
            Modifier.align(
                Alignment.Center
            ),
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}

@Composable
fun BoxScope.Loading(done: Int, left: Int) {
    Column(
        Modifier
            .size(108.dp)
            .clip(RoundedCornerShape(24.dp))
            .shadow(
                8.dp,
                RoundedCornerShape(24.dp)
            )
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .align(Alignment.Center),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(8.dp))
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            progress = animateFloatAsState(targetValue = done / left.toFloat()).value
        )
        Spacer(Modifier.height(8.dp))
        Text("$done / $left")
    }
}

@Composable
fun LoadingDialog() {
    Dialog(onDismissRequest = { }) {
        Box(Modifier.fillMaxSize()) { Loading() }
    }
}

@Composable
fun LoadingDialog(done: Int, left: Int) {
    Dialog(onDismissRequest = { }) {
        Box(Modifier.fillMaxSize()) {
            Loading(done, left)
        }
    }
}