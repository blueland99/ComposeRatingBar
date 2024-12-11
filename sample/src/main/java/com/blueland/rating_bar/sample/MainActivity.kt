package com.blueland.rating_bar.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.blueland.rating_bar.RatingBar
import com.blueland.rating_bar.ShapeType

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Scaffold { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    RatingBarDemo()
                }
            }
        }
    }
}

@Composable
fun RatingBarDemo() {
    var starRating by remember { mutableDoubleStateOf(1.0) }
    var heartRating by remember { mutableDoubleStateOf(2.5) }
    var circleRating by remember { mutableDoubleStateOf(3.0) }
    var squareRating by remember { mutableDoubleStateOf(4.2) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RatingBar(
            rating = starRating,
            onRatingChanged = { newRating -> starRating = newRating },
            count = 5,
            stepSize = 0.5,
            size = 50.dp,
            shapeType = ShapeType.Star,
            selectColor = Color.Yellow,
            unselectColor = Color.LightGray
        )

        RatingBar(
            rating = starRating,
            onRatingChanged = { newRating -> starRating = newRating },
            count = 5,
            stepSize = 0.5,
            size = 50.dp,
            borderWidth = 2.dp,
            borderColor = Color.DarkGray,
            shapeType = ShapeType.Star,
            selectColor = Color.Yellow,
            unselectColor = Color.LightGray
        )

        RatingBar(
            rating = starRating,
            onRatingChanged = { newRating -> starRating = newRating },
            count = 5,
            stepSize = 0.5,
            size = 50.dp,
            borderWidth = 5.dp,
            borderColor = Color.DarkGray,
            shapeType = ShapeType.Star,
            selectColor = Color.Yellow,
            unselectColor = Color.LightGray
        )

        RatingBar(
            rating = heartRating,
            onRatingChanged = { newRating -> heartRating = newRating },
            count = 5,
            size = 50.dp,
            borderWidth = 4.dp,
            borderColor = Color.Black,
            shapeType = ShapeType.Heart,
            selectColor = Color.Red,
            unselectColor = Color.LightGray
        )

        RatingBar(
            rating = circleRating,
            onRatingChanged = { newRating -> circleRating = newRating },
            count = 5,
            size = 50.dp,
            shapeType = ShapeType.Circle,
            selectColor = Color.Green,
            unselectColor = Color.LightGray
        )

        RatingBar(
            rating = squareRating,
            onRatingChanged = { newRating -> squareRating = newRating },
            count = 5,
            size = 50.dp,
            shapeType = ShapeType.Square,
            selectColor = Color.Blue,
            unselectColor = Color.LightGray
        )
    }
}

@Preview
@Composable
fun Preview() {
    RatingBarDemo()
}