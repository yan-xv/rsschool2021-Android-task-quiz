package com.rsschool.quiz

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.rsschool.quiz.databinding.FragmentQuizBinding
import java.lang.RuntimeException

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
        initView()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initView (){
        // получим номер текущего вопроса и общего кол-ва вопросов
        val numQuestion = arguments?.getInt(NUM_QUESTION_KEY) ?: 0
        val countQuestion = arguments?.getInt(COUNT_QUESTION_KEY) ?: 0
        // получаем данные по вопросу
        val dataQuestion = arguments?.get(DATA_QUESTION_KEY) as Question
        // применяем данные к элементам UI
        // задаем текст вопроса
        binding.question.text = dataQuestion.text
        // листаем вкладки "прогресс-бара"
        initTabbar(numQuestion)
        // задаем варианты ответа и отмечаем ранне выбранный если он есть
        initRadioGroup(dataQuestion)
        // настраиваем отображение кнопки Next
        initNextButton(numQuestion, countQuestion, dataQuestion.selected)
        // настраиваем отображение кнопки Previous
        initPrevButton(numQuestion)
        // настраиваем отображение тулбара
        initToolbar(numQuestion)
    }

    private fun initTabbar(numQuestion: Int) {
        with(binding)  {
            val listFrameLayouts = listOf(tab1, tab2, tab3, tab4, tab5)
            with(listFrameLayouts[numQuestion]) {
                elevation = tabbar.elevation  // поднимаем вкладку
                foreground = null             // убираем тень
            }
        }
    }

    private fun initRadioGroup(question: Question) {
        with(binding)  {
            val listOption = listOf(optionOne, optionTwo, optionThree, optionFour, optionFive)
            // задаем текст каждого из вариантов ответа
            listOption.forEachIndexed { i, option-> option.text = question.arrVariants[i] }
            // отмечаем ранее выбранный вариант ответа
            if ( question.selected > -1 )
                listOption[question.selected].isChecked = true
            // задаем действия на событие выбора варианта ответа
            radioGroup.setOnCheckedChangeListener { _, i ->
                // если выбран вариант ответа, то активируем кнопку Next
                nextButton.isEnabled = true
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
    private fun initNextButton(numQuestion: Int, countQuestion: Int, selectAnswer: Int) {
        with(binding.nextButton) {
            isEnabled = selectAnswer in 0 until countQuestion
            if (numQuestion + 1 == countQuestion) {
                text = getString(R.string.submit_button)
                setOnClickListener { listener?.onSubmitResult() }
            } else
                setOnClickListener { listener?.onNextQuestion() }
        }
    }

    // настройка отображения кнопки Previous
    private fun initPrevButton(numQuestion: Int) {
        with(binding.previousButton) {
            binding.previousButton.isEnabled = numQuestion > 0
            setOnClickListener { listener?.onPrevQuestion() }
        }
    }

    // настройка отображения тулбара
    private fun initToolbar(numQuestion: Int) {
        with(binding.toolbar) {
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
        if (context is IQuizFragment)
            listener = context
        else
            throw RuntimeException("$context must implement IQuizFragment")
    }

    interface IQuizFragment {
        fun onNextQuestion()
        fun onPrevQuestion()
        fun onSubmitResult()
    }

    companion object {
        @JvmStatic
        fun newInstance(numQuestion: Int, countQuestion: Int, question: Question): QuizFragment {
            return QuizFragment().apply { arguments = bundleOf(
                COUNT_QUESTION_KEY to countQuestion,
                NUM_QUESTION_KEY to numQuestion,
                DATA_QUESTION_KEY to question)
            }
        }
        private const val NUM_QUESTION_KEY = "NUM_QUESTION"
        private const val COUNT_QUESTION_KEY = "COUNT_QUESTION"
        private const val DATA_QUESTION_KEY = "DATA_QUESTION"
    }
}