package com.rsschool.quiz

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.TypedValue
import android.view.WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
import android.widget.Toast
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
    private val quiz:Quiz = Quiz()

    override fun onCreate(savedInstanceState: Bundle?) {
        window.addFlags(FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        countQuestion = quiz.questions.size
        openQuizFragment(numQuestion, countQuestion);
    }

    private fun openQuizFragment(numQuestion: Int, countQuestion: Int) {
        // создаем новый экземпляр фрагмента вопросом под указаннвм номером
        val quizFragment: Fragment = QuizFragment.newInstance(numQuestion, countQuestion)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, quizFragment).commit()
        // сменим тему
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

    override fun onGetQuestionData(): Question {
        return quiz.questions[numQuestion]
    }

    override fun onGetQuizData(): Quiz {
        return quiz
    }

    override fun onReset() {
        reset()
    }

    override fun onShare() {
        share()
    }

    override fun onExit() {
        exit()
    }

    override fun onBackPressed() {
        if ( numQuestion > 0)
            onPrevQuestion()
        else
            exit()
    }

    private fun reset() {
        quiz?.resetAnswer()
        openQuizFragment(numQuestion, countQuestion)
    }

    private fun share() {
        val intent = Intent(Intent.ACTION_SEND) // it's not ACTION_SEND
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, "Quiz results")
        intent.putExtra(Intent.EXTRA_TEXT, quiz?.getResultTextForEmail())

        if (checkIntent(intent)) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkIntent(intent: Intent): Boolean {
        val activities = packageManager.queryIntentActivities(intent, 0)
        return activities != null && activities.size > 0
    }

    private fun exit() {
        AlertDialog.Builder(this)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle("Closing Activity")
            .setMessage("Are you sure you want to close this activity?")
            .setPositiveButton("Yes") { _, _ -> finish() }
            .setNegativeButton("No", null)
            .show()
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