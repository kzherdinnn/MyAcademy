package lat.pam.myacademy

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.gridlayout.widget.GridLayout
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.button.MaterialButton
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var toggleButton: MaterialButton
    private lateinit var gridLayout: GridLayout
    private lateinit var imageSlider: ViewPager2
    private lateinit var sliderHandler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Hubungkan tombol ke WebView
        setupWebViewButtons()

        // Inisialisasi ViewPager2 untuk Image Slider
        imageSlider = findViewById(R.id.imageSlider)

        // Gambar-gambar yang ingin ditampilkan di slider
        val images = listOf(
            R.drawable.image1,
            R.drawable.image2,
        )

        // Set adapter untuk ViewPager2
        imageSlider.adapter = ImageSliderAdapter(images)

        // Auto-slide setup
        sliderHandler = Handler(Looper.getMainLooper())
        setupAutoSlider(images.size)

        // Pengguna dapat menggeser manual, reset auto-slide saat swipe manual
        imageSlider.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                // Reset timer setiap kali pengguna menggeser secara manual
                sliderHandler.removeCallbacks(sliderRunnable)
                sliderHandler.postDelayed(sliderRunnable, 3000)
            }
        })

        // Setup BottomNavigationView
        setupBottomNavigation()
    }

    private fun setupAutoSlider(totalImages: Int) {
        sliderHandler.postDelayed(sliderRunnable, 3000)
    }

    // Runnable untuk auto-slide
    private val sliderRunnable = object : Runnable {
        override fun run() {
            val currentItem = imageSlider.currentItem
            if (currentItem == imageSlider.adapter!!.itemCount - 1) {
                // Jika di slide terakhir, pindah ke slide pertama tanpa animasi
                imageSlider.setCurrentItem(0, false)
            } else {
                // Pindah ke slide berikutnya dengan animasi
                imageSlider.setCurrentItem(currentItem + 1, true)
            }
            // Ulangi setiap 3 detik
            sliderHandler.postDelayed(this, 3000)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Hentikan auto-slide ketika Activity dihancurkan
        sliderHandler.removeCallbacks(sliderRunnable)
    }

    // Fungsi untuk inisialisasi tombol dan menghubungkannya ke URL yang sesuai
    private fun setupWebViewButtons() {
        findViewById<ImageButton>(R.id.button1).setOnClickListener {
            openWebView("https://salam.uinsgd.ac.id/dashboard/login.php")
        }

        findViewById<ImageButton>(R.id.button2).setOnClickListener {
            openWebView("https://eknows.uinsgd.ac.id/")
        }

        findViewById<ImageButton>(R.id.button3).setOnClickListener {
            openWebView("https://informatika.digital/")
        }

        findViewById<ImageButton>(R.id.button4).setOnClickListener {
            openWebView("https://gemini.google.com/")
        }

        findViewById<ImageButton>(R.id.button5).setOnClickListener {
            openWebView("https://openai.com/index/chatgpt/")
        }

        findViewById<ImageButton>(R.id.button6).setOnClickListener {
            openWebView("https://drive.google.com/drive/")
        }
    }

    // Fungsi untuk membuka WebView dengan URL yang diberikan
    private fun openWebView(url: String) {
        val intent = Intent(this, WebViewActivity::class.java)
        intent.putExtra("URL_TO_LOAD", url)
        startActivity(intent)
    }

    // Fungsi untuk mengatur BottomNavigationView
    private fun setupBottomNavigation() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    // Tidak perlu melakukan apa-apa, karena sudah di MainActivity
                    true
                }
                R.id.nav_search -> {
                    // Navigasi ke NotesActivity
                    val intent = Intent(this, NotesActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }
}
