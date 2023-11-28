package com.practicum.playlistmaker

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val settingsToolbar: Toolbar = findViewById(R.id.toolbarSettings)

        settingsToolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val shareButton = findViewById<ImageView>(R.id.buttonShare)
        val supportButton = findViewById<ImageView>(R.id.buttonSupport)
        val userAgreementButton = findViewById<ImageView>(R.id.buttonUserAgreement)

        shareButton.setOnClickListener {
            val message = getString(R.string.link_to_android_developer_course)
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, message)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

        supportButton.setOnClickListener {
            val title = getString(R.string.support_email_title)
            val message = getString(R.string.support_email_message)
            val supportIntent = Intent().apply {
                action = Intent.ACTION_SENDTO
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.students_email)))
                putExtra(Intent.EXTRA_SUBJECT, title)
                putExtra(Intent.EXTRA_TEXT, message)
            }
            startActivity(supportIntent)
        }

        userAgreementButton.setOnClickListener {
            val url = Uri.parse(getString(R.string.practicum_offer))
            val intent = Intent(Intent.ACTION_VIEW, url)
            startActivity(intent)
        }
    }
}