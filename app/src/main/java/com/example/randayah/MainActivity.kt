package com.example.randayah

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.randayah.ui.theme.RandAyahTheme
import kotlin.random.Random

// ডেটা ক্লাস আয়াত সংরক্ষণের জন্য
data class AyahData(
    val surah: String,
    val ayah: String,
    val text: String,
    val translation: String
)

// আপনার দেওয়া আয়াতগুলোর তালিকা
val quranAyahs = listOf(
    AyahData("সূরা বাকারাহ", "২:২", "ذَٰلِكَ الْكِتَابُ لَا رَيْبَ ۛ فِيهِ", "এই কিতাবে কোনো সন্দেহ নেই, এটি মুত্তাকীদের জন্য পথনির্দেশ।"),
    AyahData("সূরা আল-ইখলাস", "১১২:১", "قُلْ هُوَ ٱللَّهُ أَحَدٌ", "বলুন, তিনি আল্লাহ, একমাত্র।"),
    AyahData("সূরা ফাতিহা", "১:৫", "إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ", "আমরা কেবল তোমারই ইবাদত করি এবং তোমারই সাহায্য চাই।"),
    AyahData("সূরা আল-ইনশিরাহ", "৯৪:৬", "إِنَّ مَعَ ٱلْعُسْرِ يُسْرًۭا", "নিশ্চয়ই কষ্টের সাথে স্বস্তিও আছে।"),
    AyahData("সূরা আল-কাহফ", "১৮:১০৯", "وَلَوْ أَنَّمَا فِى ٱلْأَرْضِ مِن شَجَرَةٍ أَقْلَـٰمٌ", "যদি পৃথিবীর সব গাছ কলম হয়ে যেতেও আল্লাহর বাক্য শেষ হবে না।"),
    AyahData("সূরা আল-আনফাল", "৮:৪৬", "وَأَطِيعُواْ ٱللَّهَ وَرَسُولَهُ", "তোমরা আল্লাহ ও তাঁর রাসূলের আদেশ মান্য করো।"),
    AyahData("সূরা আলে ইমরান", "৩:১৩৯", "وَلَا تَهِنُوا وَلَا تَحْزَنُوا", "তোমরা দুর্বল হয়ে পড়ো না ও দুঃখ করো না।"),
    AyahData("সূরা আল-বাকারা", "২:২৮৬", "لَا يُكَلِّفُ ٱللَّهُ نَفْسًا إِلَّا وُسْعَهَا", "আল্লাহ কাউকে তার সামর্থ্যের বাইরে দায়িত্ব দেন না।"),
    AyahData("সূরা ইয়াসিন", "৩৬:৫৮", "سَلَـٰمٌ قَوْلًۭا مِّن رَّبٍّ رَّحِيمٍۢ", "রহমান রাব্বের পক্ষ থেকে তাদের প্রতি সালাম ঘোষণা করা হবে।"),
    AyahData("সূরা আশ-শারহ", "৯৪:৭-৮", "فَإِذَا فَرَغْتَ فَٱنصَبْ وَإِلَىٰ رَبِّكَ فَٱرْغَبْ", "অতএব কাজ শেষ হলে পরিশ্রমে মনোনিবেশ করো, আর তোমার রবের প্রতি মনোযোগ দাও।"), // Note: Combined text for 7-8 based on typical presentation
    AyahData("সূরা আন-নূর", "২৪:৩৫", "ٱللَّهُ نُورُ ٱلسَّمَـٰوَٰتِ وَٱلْأَرْضِ", "আল্লাহ আসমান ও জমিনের আলো।"),
    AyahData("সূরা মুহাম্মদ", "৪৭:৭", "إِن تَنصُرُواْ ٱللَّهَ يَنصُرْكُمْ", "তোমরা যদি আল্লাহর সাহায্য করো, আল্লাহ তোমাদের সাহায্য করবেন।"),
    AyahData("সূরা তাওবা", "৯:৫১", "قُل لَّن يُصِيبَنَآ إِلَّا مَا كَتَبَ ٱللَّهُ لَنَا", "বল, আমাদের কিছুই স্পর্শ করবে না, কিন্তু যা আল্লাহ আমাদের জন্য নির্ধারণ করেছেন।"),
    AyahData("সূরা হুজরাত", "৪৯:১৩", "إِنَّ أَكْرَمَكُمْ عِندَ ٱللَّهِ أَتْقَىٰكُمْ", "আল্লাহর নিকট তোমাদের মধ্যে সবচেয়ে সম্মানিত সে যে সবচেয়ে বেশি তাকওয়াবান।"),
    AyahData("সূরা লুকমান", "৩১:১৪", "وَوَصَّيْنَا ٱلْإِنسَـٰنَ بِوَٰلِدَيْهِ", "আমি মানুষকে তার পিতা-মাতার সাথে সদ্ব্যবহারের নির্দেশ দিয়েছি।"),
    AyahData("সূরা ইনসান", "৭৬:৯", "إِنَّمَا نُطْعِمُكُمْ لِوَجْهِ ٱللَّهِ", "আমরা কেবল আল্লাহর সন্তুষ্টির জন্য তোমাদের খাদ্য দেই।"),
    AyahData("সূরা হাশর", "৫৯:২১", "لَّوْ أَنزَلْنَا هَـٰذَا ٱلْقُرْءَانَ عَلَىٰ جَبَلٍ", "যদি আমি এই কুরআনকে কোনো পাহাড়ে নাজিল করতাম, তবে তুমি দেখতে যে তা ভয়ে বিধ্বস্ত হয়ে যেত।"),
    AyahData("সূরা কাহফ", "১৮:১০", "رَبَّنَآ ءَاتِنَا مِن لَّدُنكَ رَحْمَةًۭ", "হে আমাদের পালনকর্তা! আমাদেরকে তুমার পক্ষ থেকে রহমত দান করো।"),
    AyahData("সূরা আসর", "১০৩:৩", "وَتَوَاصَوْا۟ بِٱلْحَقِّ وَتَوَاصَوْا۟ بِٱلصَّبْرِ", "তারা একে অপরকে সত্যের উপদেশ দেয়, এবং ধৈর্যের উপদেশ দেয়।"),
    AyahData("সূরা আন-নাস", "১১৪:১", "قُلْ أَعُوذُ بِرَبِّ ٱلنَّاسِ", "বল, আমি আশ্রয় চাই মানুষের পালনকর্তার নিকট।")
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RandAyahTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    RandomAyahScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun RandomAyahScreen(modifier: Modifier = Modifier) {
    var currentAyah by remember { mutableStateOf(quranAyahs.first()) }

    fun getRandomAyah() {
        val randomIndex = Random.nextInt(quranAyahs.size)
        currentAyah = quranAyahs[randomIndex]
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "${currentAyah.surah} - ${currentAyah.ayah}",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = currentAyah.text,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = currentAyah.translation,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = { getRandomAyah() }) {
            Text("অন্য আয়াত দেখুন")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RandomAyahScreenPreview() {
    RandAyahTheme {
        RandomAyahScreen()
    }
}
