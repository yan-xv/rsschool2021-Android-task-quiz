package com.rsschool.quiz

interface IQuizFragment {
    fun onNextQuestion()
    fun onPrevQuestion()
    fun onSubmitResult()
    fun onGetQuestionData(): Question
    fun onGetQuizData(): Quiz
    fun onReset()
    fun onShare()
    fun onExit()
}