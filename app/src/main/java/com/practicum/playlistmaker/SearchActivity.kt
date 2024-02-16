package com.practicum.playlistmaker

import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import org.w3c.dom.Text
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

private const val ITUNES_URL = "https://itunes.apple.com"
private const val KEY_INPUT_TEXT = "INPUT_TEXT"
private const val KEY_SEARCH_TRACKLIST = "TRACKLIST_SEARCH"

class SearchActivity : AppCompatActivity() {

    private var text: String = ""
    private val retrofit = Retrofit.Builder()
        .baseUrl(ITUNES_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunesApi = retrofit.create(ItunesApi::class.java)
    private lateinit var trackSearchAdapter: TrackAdapter
    private lateinit var trackHistoryAdapter: TrackAdapter
    private lateinit var binding: ActivitySearchBinding
    private lateinit var updateButton: Button
    private lateinit var sharedPrefsListener: OnSharedPreferenceChangeListener
    private lateinit var recyclerView: RecyclerView
    private lateinit var imagePlaceholder: ImageView
    private lateinit var textPlaceholder: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences(
            PLAYLISTMAKER_SHARED_PREFS,
            MODE_PRIVATE
        )

        binding.toolbarSearch.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        recyclerView = binding.searchRecyclerView
        recyclerView.layoutManager =
            LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)

        val youSearchedText: TextView = binding.youSearchedText

        val onTrackClickListener = OnTrackClickListener { track ->
            setOnTrackClickListenerLogic(track, sharedPreferences)
        }

        trackHistoryAdapter = TrackAdapter(
            readFromTrackListHistoryFromSharedPrefs(sharedPreferences).toMutableList(),
            onTrackClickListener = onTrackClickListener,
            onActionButtonClickListener = {
                setupCleanHistoryButtonListener(it, sharedPreferences, youSearchedText)
            }
        )

        trackSearchAdapter = TrackAdapter(emptyList(), onTrackClickListener)
        recyclerView.adapter = trackSearchAdapter

        val inputEditText = binding.inputEditText

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
                Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

            override fun afterTextChanged(s: Editable?) {
                setTextWatcherLogic(s, inputEditText, youSearchedText)
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)
        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search()
            }
            false
        }

        if (savedInstanceState != null) {
            text = savedInstanceState.getString(KEY_INPUT_TEXT).toString()
            inputEditText.setText(text)
            trackSearchAdapter.updateList {
                savedInstanceState.getParcelableArrayList(
                    KEY_SEARCH_TRACKLIST
                ) ?: mutableListOf()
            }
        }

        val imm: InputMethodManager =
            getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

        binding.clearIcon.setOnClickListener {
            setClearIconOnClickListenerLogic(inputEditText, imm)
        }

        updateButton = binding.buttonUpdate
        updateButton.setOnClickListener {
            search()
        }

        sharedPrefsListener = OnSharedPreferenceChangeListener { sharedPreferences, key ->
            historyAdapterRestore(sharedPreferences, key, trackHistoryAdapter)
        }

        inputEditText.setOnFocusChangeListener { _, hasFocus ->
            setOnFocusChangeListenerLogic(hasFocus, inputEditText, youSearchedText)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_INPUT_TEXT, text)
        outState.putParcelableArrayList(
            KEY_SEARCH_TRACKLIST,
            ArrayList(trackSearchAdapter.trackList)
        )
    }

    private fun setTextPlaceholder(text: String) {
        val textPlaceholder = binding.textPlaceholder
        if (text.isNotEmpty()) {
            textPlaceholder.isVisible = true
            trackSearchAdapter.clearList()
            textPlaceholder.text = text
        } else {
            textPlaceholder.isVisible = false
        }
    }

    private fun setImagePlaceholder(image: Int) {
        val imagePlaceholder = binding.imagePlaceholder
        when (image) {
            android.R.color.transparent -> {
                imagePlaceholder.isVisible = false
                updateButton.isVisible = false
            }

            R.drawable.search_error_icon -> {
                imagePlaceholder.isVisible = true
                imagePlaceholder.setImageResource(image)
                updateButton.isVisible = true
            }

            else -> {
                imagePlaceholder.isVisible = true
                imagePlaceholder.setImageResource(image)
                updateButton.isVisible = false
            }
        }
    }

    private fun search() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                try {
                    val iTunesApi = retrofit.create(ItunesApi::class.java)
                    val trackResponse = iTunesApi.searchTrack(text)
                    if (trackResponse.results.isNotEmpty()) {
                        trackSearchAdapter.updateList { trackResponse.results }
                        setTextPlaceholder("")
                        setImagePlaceholder(android.R.color.transparent)
                    } else {
                        setTextPlaceholder(getString(R.string.nothing_found))
                        setImagePlaceholder(R.drawable.not_found_icon)
                    }
                } catch (e: IOException) {
                    when(e) {
                        is HttpException -> {
                            Log.e("SearchActivity", "non-2xx HTTP response", e)
                            setTextPlaceholder(getString(R.string.server_error))
                            setImagePlaceholder(R.drawable.search_error_icon)
                        }
                        else -> {
                            Log.e("SearchActivity", "Network error", e)
                            setTextPlaceholder(getString(R.string.search_error_message))
                            setImagePlaceholder(R.drawable.search_error_icon)
                        }
                    }
                } finally {
                    this@launch.cancel()
                }
            }
        }
    }

    private fun setupCleanHistoryButtonListener(
        trackAdapter: TrackAdapter,
        sharedPreferences: SharedPreferences,
        youSearchedText: TextView
    ) {
        trackAdapter.clearList()
        writeTrackListHistoryToSharedPrefs(sharedPreferences, trackAdapter.trackList)
        recyclerView.adapter = trackSearchAdapter
        youSearchedText.isVisible = false
        trackSearchAdapter.clearList()
    }

    private fun setOnFocusChangeListenerLogic(
        hasFocus: Boolean,
        inputEditText: EditText,
        youSearchedText: TextView
    ) {
        val showHistory =
            hasFocus && inputEditText.text.isEmpty() && !trackHistoryAdapter.isEmpty()
        recyclerView.adapter = if (showHistory) trackHistoryAdapter else trackSearchAdapter
        youSearchedText.isVisible = showHistory
    }

    private fun setOnTrackClickListenerLogic(track: Track, sharedPreferences: SharedPreferences) {
        if (recyclerView.adapter == trackSearchAdapter) {
            trackHistoryAdapter.updateList {
                val historyList = addToHistoryList(track, it)
                writeTrackListHistoryToSharedPrefs(sharedPreferences, historyList)
                historyList
            }
        }
        val audioplayerIntent = Intent(this, AudioplayerActivity::class.java)
        audioplayerIntent.putExtra(KEY_SELECTED_TRACK_DETAILS, track)
        startActivity(audioplayerIntent)
    }

    private fun setClearIconOnClickListenerLogic(inputEditText: EditText, imm: InputMethodManager) {
        inputEditText.setText("")
        imm.hideSoftInputFromWindow(inputEditText.windowToken, 0)
        trackSearchAdapter.clearList()
    }

    private fun setTextWatcherLogic(
        s: Editable?,
        inputEditText: EditText,
        youSearchedText: TextView
    ) {
        binding.clearIcon.isVisible = !s.isNullOrEmpty()
        text = inputEditText.text.toString()

        val showHistory =
            inputEditText.hasFocus() && s?.isEmpty() == true && !trackHistoryAdapter.isEmpty()

        recyclerView.adapter = if (showHistory) trackHistoryAdapter else trackSearchAdapter
        youSearchedText.isVisible = showHistory

        if (recyclerView.adapter == trackHistoryAdapter) {
            imagePlaceholder.isVisible = false
            textPlaceholder.isVisible = false
            updateButton.isVisible = false
        }
        if (s?.isNotBlank() == true) {
            searchDebounce()
        } else {
            handler.removeCallbacks(searchRunnable)
            trackSearchAdapter.clearList()
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed(isClickAllowedRunnable, TRACK_CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }
}
