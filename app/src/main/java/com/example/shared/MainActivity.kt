package com.example.shared

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Locale
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPref: SharedPreferences
    private var hiddenNumber = 0
    private var score = 0
    private var highestScore = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        sharedPref = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)

        val edtGuess = findViewById<EditText>(R.id.edtGuess)
        val btnGuess = findViewById<Button>(R.id.btnGuess)
        val tvResult = findViewById<TextView>(R.id.tvResult)
        val tvScore = findViewById<TextView>(R.id.tvScore)
        val btnAddTodo = findViewById<Button>(R.id.btnAddTodo)
        val edtTodo = findViewById<EditText>(R.id.edtTodo)
        val tvTodoList = findViewById<TextView>(R.id.tvTodoList)
        val tvQuote = findViewById<TextView>(R.id.tvQuote)
        val switchMode = findViewById<Switch>(R.id.switchMode)
        val btnLanguage = findViewById<Button>(R.id.btnLanguage)

        val isDarkMode = sharedPref.getBoolean("dark_mode", false)
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            switchMode.isChecked = true
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            switchMode.isChecked = false
        }

        // Load Highest Score
        highestScore = sharedPref.getInt("highest_score", 0)
        tvScore.text = "Highest Score: $highestScore"

        // Generate random hidden number between 1 and 100
        hiddenNumber = Random.nextInt(100) + 1

        btnGuess.setOnClickListener {
            val guess = edtGuess.text.toString().toIntOrNull()
            if (guess != null) {
                if (guess == hiddenNumber) {
                    score++
                    tvResult.text = "Correct! You guessed $hiddenNumber."
                    if (score > highestScore) {
                        highestScore = score
                        val editor = sharedPref.edit()
                        editor.putInt("highest_score", highestScore)
                        editor.apply()
                    }
                } else {
                    tvResult.text = "Wrong! Try again."
                }
            }
        }

        btnAddTodo.setOnClickListener {
            val todoText = edtTodo.text.toString()
            val todoList = sharedPref.getStringSet("todo_list", mutableSetOf()) ?: mutableSetOf()
            todoList.add(todoText)
            val editor = sharedPref.edit()
            editor.putStringSet("todo_list", todoList)
            editor.apply()

            // Update To-Do List UI
            tvTodoList.text = todoList.joinToString("\n")
        }

        // Show Random Quote for Motivation
        val quotes = listOf(
            "Believe in yourself!",
            "Stay positive!",
            "You can do it!",
            "Never give up!",
            "Keep pushing forward!"
        )
        tvQuote.text = quotes[Random.nextInt(quotes.size)]

        // Switch Dark Mode / Light Mode
        switchMode.setOnCheckedChangeListener { _, isChecked ->
            val editor = sharedPref.edit()
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                editor.putBoolean("dark_mode", true)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                editor.putBoolean("dark_mode", false)
            }
            editor.apply()
        }

        // Change Language (for example, English / French / Spanish)
        btnLanguage.setOnClickListener {
            val currentLocale = resources.configuration.locale
            val newLocale = if (currentLocale.language == "en") {
                Locale("fr")
            } else {
                Locale("en")
            }
            val config = Configuration(resources.configuration)
            config.setLocale(newLocale)
            resources.updateConfiguration(config, resources.displayMetrics)
            recreate()
        }












    }
}