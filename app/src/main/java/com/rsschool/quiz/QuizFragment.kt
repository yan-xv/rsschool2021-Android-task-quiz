package com.rsschool.quiz

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.rsschool.quiz.databinding.FragmentQuizBinding
import java.lang.RuntimeException

interface IQuizFragment {
    fun onNextQuestion()
    fun onPrevQuestion()
    fun onSubmitResult()
}

class QuizFragment : Fragment() {
    private var listener: IQuizFragment? = null
    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // получим номер текущего вопроса и общего кол-ва вопросов
        val numQuestion = arguments?.getInt(NUM_QUESTION_KEY) ?: 0
        val countQuestion = arguments?.getInt(COUNT_QUESTION_KEY) ?: 0
        // получаем данные
        val dataQuestion = arguments?.get(DATA_QUESTION_KEY) as Question

        binding.apply {
            // листаем вкладки "прогресс-бара"
            val listFrameLayouts = listOf(frameLayout1, frameLayout2, frameLayout3, frameLayout4, frameLayout5)
            listFrameLayouts[numQuestion].apply {
                elevation = linearLayout.elevation
                foreground = null
            }

            //применяем данные к элемента интерфейса
            // задаем текст вопроса
            question.text = dataQuestion.text
        }
        // задаем варианты ответа и отмечаем ранне выбранный если он есть
        configRadioGroup(dataQuestion)
        // настраиваем отображение кнопки Next
        configNextButton(numQuestion, countQuestion, dataQuestion.selected)
        // настраиваем отображение кнопки Previous
        configPrevButton(numQuestion)
        // настраиваем отображение тулбара
        configToolbar(numQuestion)
    }

    private fun configRadioGroup(question: Question) {
        binding.apply {
            val listOption = listOf(optionOne, optionTwo, optionThree, optionFour, optionFive)
            // задаем текст каждого из вариантов ответа
            listOption.forEachIndexed {i, option-> option.text = question.arrVariants[i]  }
            // отмечаем ранее выбранный вариант ответа
            if ( question.selected > -1 )
            listOption[question.selected].isChecked = true
            // задаем действия на событие выбора варианта ответа
            radioGroup.setOnCheckedChangeListener {
                // если выбран вариант ответа, то активируем кнопку Next
                    _, i -> nextButton.isEnabled = true
                // передадим в данные индекс выбранного варианта ответа
                question.selected = listOption.convertIdOptionToAnswer(i)
            }
        }
    }

    // по id контрала radioGroup получаем соотвествуюший индекс варианта ответа
    private fun List<RadioButton>.convertIdOptionToAnswer(id: Int): Int {
        for ((i, option) in this.withIndex())
            if (option.id == id)
                return i
        return -1
    }


    // настройка отображения кнопки Next
    private fun configNextButton(numQuestion: Int, countQuestion: Int, selectAnswer: Int) {
        binding.nextButton.apply {
            isEnabled = selectAnswer in 0..countQuestion
            if (numQuestion + 1 == countQuestion) {
                text = getString(R.string.submit_button)
                setOnClickListener { listener?.onSubmitResult() }
            } else
                setOnClickListener { listener?.onNextQuestion() }
        }
    }

    // настройка отображения кнопки Previous
    private fun configPrevButton(numQuestion: Int) {
        binding.previousButton.apply {
            isVisible = numQuestion > 0
            setOnClickListener { listener?.onPrevQuestion() }
        }
    }

    // настройка отображения тулбара
    private fun configToolbar(numQuestion: Int) {
        binding.toolbar.apply {
            title = getString(R.string.question_num) + "${numQuestion + 1}"
            if (numQuestion == 0)
                navigationIcon = null
            else
                setNavigationOnClickListener { listener?.onPrevQuestion() }
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is IQuizFragment) {
            listener = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement IQuizFragment"
            )
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(numQuestion: Int, countQuestion: Int, question: Question): QuizFragment {
            val fragment = QuizFragment()
            fragment.arguments = bundleOf(
                COUNT_QUESTION_KEY to countQuestion,
                NUM_QUESTION_KEY to numQuestion,
                DATA_QUESTION_KEY to question)
            return fragment
        }
        private const val NUM_QUESTION_KEY = "NUM_QUESTION"
        private const val COUNT_QUESTION_KEY = "COUNT_QUESTION"
        private const val DATA_QUESTION_KEY = "DATA_QUESTION"
    }
}