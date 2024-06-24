package com.example.imagereveal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.example.imagereveal.ui.theme.ImageRevealTheme
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var circlePos by remember {
                mutableStateOf(Offset.Zero)
            }
            var oldCirclePos by remember {
                mutableStateOf(Offset.Zero)
            }
            val imageBit = ImageBitmap.imageResource(id = R.drawable.screenshot)
            val radius = 200f
            Canvas(
                modifier = Modifier.fillMaxSize()
                    .pointerInput(true) {
                        detectDragGestures(
                            onDragStart = {
                                oldCirclePos = circlePos
                            }
                        ){change, dragAmount ->
                            circlePos = oldCirclePos+change.position

                        }
                    }
            ) {
                val bmpHeight = ((imageBit.height.toFloat() / imageBit.width.toFloat()) * size.width).roundToInt()
                val circlePath = Path().apply {
                    addArc(
                        oval = Rect(circlePos, radius),
                        startAngleDegrees = 0f,
                        sweepAngleDegrees = 360f
                    )
                }
                drawImage(
                    image = imageBit,
                    dstSize = IntSize(
                        size.width.roundToInt(),
                        bmpHeight
                    ),
                    dstOffset = IntOffset(
                        0,
                        center.y.roundToInt() - bmpHeight / 2
                    ),
                    colorFilter = ColorFilter.tint(Color.Black, BlendMode.Color)

                )
                clipPath(circlePath, clipOp = ClipOp.Intersect){
                    drawImage(
                        image = imageBit,
                        dstSize = IntSize(
                            size.width.roundToInt(),
                            bmpHeight
                        ),
                        dstOffset = IntOffset(
                            0,
                            center.y.roundToInt() - bmpHeight / 2
                        )
                    )
                }
            }
        }
    }
}

