package com.rsschool.quiz

import java.io.Serializable

class Quiz : Serializable {
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

    fun getResultText(template: String): String {
        return String.format(template, 100 * getCountTrueAnswer()/getCountQuestions())
    }

    fun getTextQuestions(template: String): String {
        val sb = StringBuilder()
        questions.forEachIndexed { i, it ->
            sb.append("${i + 1}) ${it.text} \n")
            .append(String.format(template, it.getTextSelectedAnswer()))
            .append("\n\n")
        }
        return sb.toString()
    }

    fun getCountTrueAnswer(): Int {
        return questions.filter { it.trueVariant == it.selected }.size
    }

    private fun getCountQuestions(): Int {
        return questions.size
    }

    fun resetAnswer() {
        questions.forEach { it.selected = -1 }
    }

    private fun Question.getTextSelectedAnswer() : String {
        return if (selected in arrVariants.indices)
            arrVariants[selected]
        else
            ""
    }
}