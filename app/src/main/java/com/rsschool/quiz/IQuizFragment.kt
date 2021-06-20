package com.rsschool.quiz

interface IQuizFragment {
    fun onNextQuestion()
    fun onPrevQuestion()
    fun onSubmitResult()
    fun onGetData(): Question
}