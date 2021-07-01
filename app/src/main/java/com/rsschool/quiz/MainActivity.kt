package com.rsschool.quiz

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() , QuizFragment.IQuizFragment, ResultFragment.IResultFragment {
    private var numQuestion = 0
    private var countQuestion = 5
    private val quiz:Quiz = Quiz()

    override fun onCreate(savedInstanceState: Bundle?) {
        window.addFlags(FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        countQuestion = quiz.questions.size
        openQuizFragment(numQuestion, countQuestion)
    }

    private fun openQuizFragment(numQuestion: Int, countQuestion: Int) {
        // создаем новый экземпляр фрагмента с вопросом под указанным номером
        val quizFragment = QuizFragment.newInstance(numQuestion, countQuestion, quiz.questions[numQuestion])
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, quizFragment).commit()
        // сменим тему
        setupTheme()
    }

    private fun openResultFragment() {
        val resultFragment = ResultFragment.newInstance(quiz)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, resultFragment).commit()
        // сбросим номер текущего вопроса
        numQuestion = 0
    }

    override fun onNextQuestion() = openQuizFragment(++numQuestion, countQuestion)
    override fun onPrevQuestion() = openQuizFragment(--numQuestion, countQuestion)
    override fun onSubmitResult() = openResultFragment()

    override fun onReset() = reset()
    override fun onShare() = share()
    override fun onExit()  = exit()

    override fun onBackPressed() = if ( numQuestion > 0 ) onPrevQuestion() else exit()

    private fun reset() {
        quiz.resetAnswer()
        openQuizFragment(numQuestion, countQuestion)
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun share() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.subject))
        intent.putExtra(Intent.EXTRA_TEXT, createMsg())

        if ( packageManager.queryIntentActivities(intent, 0).size > 0) {
            startActivity(intent)
        } else {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createMsg(): String{
        var msg = quiz.getResultText(getString(R.string.result_template))
        msg += "\n\n" + quiz.getTextQuestions(getString(R.string.answer_template))
        return msg
    }

    private fun exit() {
        AlertDialog.Builder(this)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle("Закрыть квиз?")
            .setMessage("Внимание! Результат будет потерян!")
            .setPositiveButton("Да") { _, _ ->
                finish()
                exitProcess(0)
            }
            .setNegativeButton("Нет", null)
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
        // поменем статус бара вслед за темой
        setupStatusBarColor()
    }

    private fun setupStatusBarColor() {
        val typedValue = TypedValue()
        theme.resolveAttribute(R.attr.colorPrimaryVariant, typedValue, true)
        window.statusBarColor = typedValue.data
    }
}