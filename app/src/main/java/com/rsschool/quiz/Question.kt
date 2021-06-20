package com.rsschool.quiz

data class Question(val trueVariant: Int = -1,
                    var selected: Int = -1,
                    val text:String = "question",
                    val arrVariants: Array<String> = arrayOf<String>("1","2","3","4","5") ) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Question

        if (trueVariant != other.trueVariant) return false
        if (selected != other.selected) return false
        if (text != other.text) return false
        if (!arrVariants.contentEquals(other.arrVariants)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = trueVariant
        result = 31 * result + selected
        result = 31 * result + text.hashCode()
        result = 31 * result + arrVariants.contentHashCode()
        return result
    }
}
/*
class Question(vquestion: String, variantAnswer: Array<String>, trueAnswer: Int) {
    private val trueAnswer: Int = -1
    private var userAnswer: Int = -1
    private val question = "question"
    private val variantAnswer = arrayOf<String>("1","2","3","4","5")
}
*/
