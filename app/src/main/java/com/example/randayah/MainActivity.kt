package com.example.randayah

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.randayah.ui.theme.RandAyahTheme
import kotlin.random.Random

// ডেটা ক্লাস আয়াত সংরক্ষণের জন্য
data class AyahData(
    val surah: String,
    val ayah: String,
    val text: String,
    val translation: String
)

// আপনার দেওয়া আয়াতগুলোর তালিকা (সংক্ষিপ্ত)
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
    AyahData("সূরা আশ-শারহ", "৯৪:৭-৮", "فَإِذَا فَرَغْتَ فَٱنصَبْ وَإِلَىٰ رَبِّكَ فَٱرْغَبْ", "অতএব কাজ শেষ হলে পরিশ্রমে মনোনিবেশ করো, আর তোমার রবের প্রতি মনোযোগ দাও।"),
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

// New Color Palette
val AyahCardBackground = Color(0xFF2E7D32) // Darker, richer green
val AyahCardTextColor = Color.White
val NavigationButtonBackground = Color(0xFF4CAF50) // Material Green
val NavigationButtonTextColor = Color.White
val UtilityButtonTextColor = Color(0xFF1B5E20) // Dark green for text/outlined buttons
val AllAyahsListCardBackground = Color(0xFFE8F5E9) // Light green for list items
val AllAyahsListCardTextColor = Color.Black


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RandAyahTheme {
                var showAllAyahsScreen by remember { mutableStateOf(false) }
                var currentAyahIndex by remember { mutableIntStateOf(Random.nextInt(quranAyahs.size)) } // Start with a random Ayah

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    if (showAllAyahsScreen) {
                        AllAyahsListScreen(
                            modifier = Modifier.padding(innerPadding),
                            onNavigateBack = { showAllAyahsScreen = false }
                        )
                    } else {
                        RandomAyahScreen(
                            modifier = Modifier.padding(innerPadding),
                            currentAyahIndex = currentAyahIndex,
                            onShowNextRandomAyah = {
                                currentAyahIndex = Random.nextInt(quranAyahs.size)
                            },
                            onShowPreviousAyah = {
                                currentAyahIndex = if (currentAyahIndex > 0) {
                                    currentAyahIndex - 1
                                } else {
                                    quranAyahs.size - 1 // Wrap around
                                }
                            },
                            onShowAllAyahs = { showAllAyahsScreen = true }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RandomAyahScreen(
    modifier: Modifier = Modifier,
    currentAyahIndex: Int,
    onShowNextRandomAyah: () -> Unit,
    onShowPreviousAyah: () -> Unit,
    onShowAllAyahs: () -> Unit
) {
    val currentAyah = quranAyahs[currentAyahIndex]

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            TextButton(
                onClick = onShowAllAyahs,
                colors = ButtonDefaults.textButtonColors(contentColor = UtilityButtonTextColor)
            ) {
                Text("সব আয়াত দেখুন", fontWeight = FontWeight.SemiBold)
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f), // Allows card to take available space
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            colors = CardDefaults.cardColors(containerColor = AyahCardBackground),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize() // Fill card
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "${currentAyah.surah} - ${currentAyah.ayah}",
                    style = MaterialTheme.typography.titleLarge.copy(color = AyahCardTextColor, fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                Text(
                    text = currentAyah.text,
                    style = MaterialTheme.typography.displaySmall.copy(color = AyahCardTextColor, fontSize = 30.sp), // Custom size for Arabic
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Text(
                    text = currentAyah.translation,
                    style = MaterialTheme.typography.bodyLarge.copy(color = AyahCardTextColor.copy(alpha = 0.85f)), // Slightly transparent for subtlety
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = onShowPreviousAyah,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NavigationButtonBackground,
                    contentColor = NavigationButtonTextColor
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text("পূর্ববর্তী আয়াত", fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = onShowNextRandomAyah,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NavigationButtonBackground,
                    contentColor = NavigationButtonTextColor
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text("অন্য আয়াত দেখুন", fontSize = 16.sp)
            }
        }
        Spacer(modifier = Modifier.height(16.dp)) // Space at the bottom
    }
}

@Composable
fun AllAyahsListScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit
) {
    Column(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "সব আয়াত",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            OutlinedButton(
                onClick = onNavigateBack,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = UtilityButtonTextColor),
                border = ButtonDefaults.outlinedButtonBorder // Corrected this line
            ) {
                Text("ফিরে যান")
            }
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            itemsIndexed(quranAyahs) { index, ayah ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    colors = CardDefaults.cardColors(containerColor = AllAyahsListCardBackground),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "${ayah.surah} - ${ayah.ayah}",
                            style = MaterialTheme.typography.titleMedium.copy(color = AllAyahsListCardTextColor, fontWeight = FontWeight.SemiBold),
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Text(
                            text = ayah.text,
                            style = MaterialTheme.typography.headlineSmall.copy(color = AllAyahsListCardTextColor, fontSize = 22.sp),
                            textAlign = TextAlign.Start,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        Text(
                            text = ayah.translation,
                            style = MaterialTheme.typography.bodyMedium.copy(color = AllAyahsListCardTextColor.copy(alpha = 0.8f))
                        )
                    }
                }
                if (index < quranAyahs.size - 1) {
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
    }
}


@Preview(showBackground = true, name = "Random Ayah Screen")
@Composable
fun RandomAyahScreenPreview() {
    RandAyahTheme {
        RandomAyahScreen(
            currentAyahIndex = 0,
            onShowNextRandomAyah = {},
            onShowPreviousAyah = {},
            onShowAllAyahs = {}
        )
    }
}

@Preview(showBackground = true, name = "All Ayahs List Screen")
@Composable
fun AllAyahsListScreenPreview() {
    RandAyahTheme {
        AllAyahsListScreen(onNavigateBack = {})
    }
}
