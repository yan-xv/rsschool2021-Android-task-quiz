package com.rsschool.quiz

import java.io.Serializable

data class Question(
    val trueVariant: Int = -1,
    var selected: Int = -1,
    val text: String? = "question",
    val arrVariants: Array<String> = arrayOf("1","2","3","4","5") ) :
    Serializable
