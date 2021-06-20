package com.rsschool.quiz

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.TypedValue
import android.view.WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment


enum class Themes(@StyleRes val themeId: Int) {
    FIRST(R.style.Theme_Quiz_First),
    SECOND(R.style.Theme_Quiz_Second);
}

class MainActivity : AppCompatActivity() , IQuizFragment {
    private var numQuestion = 0
    private var countQuestion = 5
    private val quiz:Quiz =  Quiz()

    override fun onCreate(savedInstanceState: Bundle?) {
        window.addFlags(FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        countQuestion = quiz.questions.size
        openQuizFragment(numQuestion, countQuestion);
    }

    private fun openQuizFragment(numQuestion: Int, countQuestion: Int) {
        val quizFragment: Fragment = QuizFragment.newInstance(numQuestion, countQuestion)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, quizFragment).commit()
        setupTheme()
    }

    private fun openResultFragment() {
        val resultFragment: Fragment = ResultFragment.newInstance()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, resultFragment).commit()
    }

    override fun onNextQuestion() {
        openQuizFragment(++numQuestion, countQuestion)
    }

    override fun onPrevQuestion() {
        openQuizFragment(--numQuestion, countQuestion)
    }

    override fun onSubmitResult() {
        numQuestion = 0;
        openResultFragment()
    }

    override fun onGetData(): Question {
        return quiz.questions[numQuestion]
    }

    override fun onBackPressed() {
        if ( numQuestion > 0)
            onPrevQuestion()
        else {
            AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Closing Activity")
                .setMessage("Are you sure you want to close this activity?")
                .setPositiveButton("Yes",
                    DialogInterface.OnClickListener { dialog, which -> finish() })
                .setNegativeButton("No", null)
                .show()
        }
    }

    private fun setupTheme() {
        setTheme(when (numQuestion % 5){
            1->R.style.Theme_Quiz_Second
            2->R.style.Theme_Quiz_Third
            3->R.style.Theme_Quiz_Fourth
            4->R.style.Theme_Quiz_Fifth
            else->R.style.Theme_Quiz_First
        })

        setupStatusBarColor()
    }

    private fun setupStatusBarColor() {
        val typedValue = TypedValue()
        theme.resolveAttribute(R.attr.colorPrimaryDark, typedValue, true)
        window.statusBarColor = typedValue.data
    }
}