package com.example.kotlin_mini_kino

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.kotlin_mini_kino.databinding.ActivityMainBinding
import com.example.kotlin_mini_kino.model.MovieEntry
import kotlinx.serialization.json.Json
import java.io.File
import kotlinx.serialization.encodeToString

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val movieList = mutableListOf<MovieEntry>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val genres = listOf<String>("Akcja","Komiedia","Dramat","Sci-Fi","Fantasy","Horror")
        val genreAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            genres
        ) // odpowiada za sposób wyświetlania się listy

        genreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) // odpowiada za sposób wyświetlania elementów listy

        binding.genreSpinner.adapter = genreAdapter

        val movieList = mutableListOf<MovieEntry>()

        val movieTitles = mutableListOf<String>()
        val listAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            movieTitles
        )
        binding.listViewMovies.adapter = listAdapter

        binding.addMovieToListButton.setOnClickListener {
            val title = binding.titleEditText.text.toString()
            val genre = binding.genreSpinner.selectedItem.toString()
            val rating = binding.ratingBar.rating.toInt()

            if(title.isNotBlank()) {
                val newMovie = MovieEntry(title, genre, rating)
                movieList.add(newMovie)

                movieTitles.add("$title – $genre (* $rating)")
                listAdapter.notifyDataSetChanged() // tak jakby `INotifyPropertyChanged z AvaloniaUI, powoduję zmianę UI
                Toast.makeText(
                    this,
                    "Dodano $title do listy",
                    Toast.LENGTH_SHORT
                ).show()

                binding.titleEditText.text.clear()
                binding.ratingBar.rating = 0f
                binding.genreSpinner.setSelection(0)
            }
            else {
                Toast.makeText(
                    this,
                    "Podaj tytuł filmu",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        binding.saveButton.setOnClickListener {
            saveMoviesToFile()
        }
    }
    private fun saveMoviesToFile() {
        try {
            val json = Json { prettyPrint = true }
            val jsonString = Json.encodeToString(movieList)

            val file = File(filesDir, "movies.json")
            // filesDir:
            // - obiekt typu File
            // - /data/data/<nazwa.pakietu>/files/
            // - jest właściwością dziedziczoną po klasie Context
            file.writeText(jsonString)

            Toast.makeText(
                this,
                "Zapisano do pliku",
                Toast.LENGTH_SHORT
            ).show()
        }
        catch(e: Exception) {
            Toast.makeText(
                this,
                "Błąd zapisu danych",
                Toast.LENGTH_SHORT
            ).show()
            e.printStackTrace()
        }
    }
}