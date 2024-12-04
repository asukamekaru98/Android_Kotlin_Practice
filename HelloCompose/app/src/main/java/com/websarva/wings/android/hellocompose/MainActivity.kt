package com.websarva.wings.android.hellocompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.websarva.wings.android.hellocompose.ui.theme.HelloComposeTheme

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContent {
			HelloComposeTheme {
				Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
					Greeting(
						name = "Compose",
						modifier = Modifier.padding(innerPadding)
					)

					//TextSample()
					ModifierSample()
				}
			}
		}
	}
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
	Text(
		text = "Hello $name!",
		modifier = modifier
	)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
	HelloComposeTheme {
		//	Greeting("Android")
		ModifierSample()
	}
}

@Composable
fun TextSample() {
	// 文字列を直接指定
	Text("I like Compose")

	// 文字列リソースを指定
	Text(stringResource(R.string.i_like_compose))
}

@Composable
fun TextSample2() {
	Column {
		Text(text = "I like Compose", fontSize = 10.sp)
		Text(text = "I like Compose", fontSize = 20.sp)
		Text(text = "I like Compose", fontSize = 30.sp)
	}
}

@Composable
fun TextSample3() {
	Column {
		val story = "昔々あるところにおじいさんとおばあさんがいました。" +
				"おじいさんは山へ芝刈りに、おばあさんは川へ洗濯に行きました。"

		Text(text = story)
		Text(text = story, maxLines = 1, fontWeight = FontWeight.Bold)

	}
}

@Composable
fun ModifierSample() {
	Text(
		text = "I like Compose",
		//modifier = Modifier.border(1.dp, Color.Black).background(Color.Red).padding(10.dp)
		modifier = Modifier
			.size(
				width = 200.dp,
				height = 100.dp
			)
			.background(
				brush = Brush.linearGradient(listOf(Color.White, Color.Gray)),
				shape = RoundedCornerShape(20.dp)
			)
			.padding(
				10.dp
			)
			.border(
				width = 1.dp,
				color = Color.Black,
				shape = RoundedCornerShape(20.dp)
			)
			.clickable {
				println("Click!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
			}

	)
}