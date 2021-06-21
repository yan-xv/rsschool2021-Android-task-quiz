package com.rsschool.quiz

import android.os.Parcel
import android.os.Parcelable

class Quiz() : Parcelable {
    val questions = listOf(
        Question(1, -1,"Какая из приставок единиц измерения больше?",
            arrayOf("тера","зетта","дека","пета","гига") ),
        Question(2, -1,"Укажите первые 6 цифр десятичной части числа Пи.",
            arrayOf("415196","151429","141592","761435","151492") ),
        Question(3, -1,"Какая из мер длины меньше?",
            arrayOf("Парсек","Ярд","Фут","Дюйм","Лига") ),
        Question(4, -1,"Какой степени двойки соответствует число 65 536",
            arrayOf("10","32","14","20","16") ),
        Question(0, -1,"Найдите лишние число среди простых чисел.", arrayOf("0","1","2","3","101") )
    )

    constructor(parcel: Parcel) : this()

    fun getResultText(): String {
        return "Отвечено верно на ${getCountTrueAnswer()} из ${getCountQuestions()} вопросов"
    }

    fun getResultTextForEmail(): String {
        var text = getResultText() + "\n\n"
        questions.forEachIndexed {
                i,it->text += "$i+1) ${it.text} \nВаш ответ: ${it.arrVariants[it.selected]}\n\n" }
        return text
    }

    private fun getCountTrueAnswer(): Int {
        return questions.filter { it.trueVariant == it.selected }.size
    }

    private fun getCountQuestions(): Int {
        return questions.size
    }

    fun resetAnswer() {
        questions.forEach { it.selected = -1 }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Quiz> {
        override fun createFromParcel(parcel: Parcel): Quiz {
            return Quiz(parcel)
        }

        override fun newArray(size: Int): Array<Quiz?> {
            return arrayOfNulls(size)
        }
    }
}