package com.rsschool.quiz

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.rsschool.quiz.databinding.FragmentQuizBinding
import java.lang.RuntimeException

class QuizFragment() : Fragment() {
    private var listener: IQuizFragment? = null
    private var _binding: FragmentQuizBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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

        // получаем данные из активити
        val question = listener?.onGetQuestionData()

        //есть данные - применяем их к элемента интерфейса
        if (question != null) {
            // задаем текст вопроса
            binding.question.text = question.text
            // задаем варианты ответа и отмечаем ранне выбранный если он есть
            configRadioGroup(question)
            // настраиваем отображение кнопки Next
            configNextButton(numQuestion, countQuestion, question.selected)
            // настраиваем отображение кнопки Previous
            configPrevButton(numQuestion)
            // настраиваем отображение тулбара
            configToolbar(numQuestion)
        }
    }

    private fun configRadioGroup(question: Question) {
        // задаем текст каждого из вариантов ответа
        binding.optionOne.text   = question.arrVariants[0]
        binding.optionTwo.text   = question.arrVariants[1]
        binding.optionThree.text = question.arrVariants[2]
        binding.optionFour.text  = question.arrVariants[3]
        binding.optionFive.text  = question.arrVariants[4]

        // отмечаем ранее выбранный вариант ответа
        binding.radioGroup.check(convertAnswerToIdOption(question.selected))

        // задаем действия на событие выбора варианта ответа
        binding.radioGroup.setOnCheckedChangeListener {
            // если выбран вариант ответа, то активируем кнопку Next
            _, i -> binding.nextButton.isEnabled = true
            // передадим в данные индекс выбранного варианта ответа
            question.selected = convertIdOptionToAnswer(i)
        }
    }

    // по id контрала radioGroup получаем соотвествуюший индекс варианта ответа
    private fun convertIdOptionToAnswer(id: Int): Int {
        return when (id){
            binding.optionOne.id->0
            binding.optionTwo.id->1
            binding.optionThree.id->2
            binding.optionFour.id->3
            binding.optionFive.id->4
            else->-1
        }
    }

    // по индексу варианта ответа получаем id контрала radioGroup
    private fun convertAnswerToIdOption(answer: Int): Int {
        return when (answer){
            0->binding.optionOne.id
            1->binding.optionTwo.id
            2->binding.optionThree.id
            3->binding.optionFour.id
            4->binding.optionFive.id
            else->0
        }
    }

    // настройка отображения кнопки Next
    private fun configNextButton(numQuestion: Int, countQuestion: Int, selectAnswer: Int) {
        binding.nextButton.isEnabled = selectAnswer in 0 .. countQuestion
        if ( numQuestion + 1 == countQuestion ) {
            binding.nextButton.text =  getString(R.string.submit_button)
            binding.nextButton.setOnClickListener { listener?.onSubmitResult() }
        }
        else
            binding.nextButton.setOnClickListener { listener?.onNextQuestion() }
    }

    // настройка отображения кнопки Previous
    private fun configPrevButton(numQuestion: Int) {
        binding.previousButton.isVisible = numQuestion > 0
        binding.previousButton.setOnClickListener{ listener?.onPrevQuestion() }
    }

    // настройка отображения тулбара
    private fun configToolbar(numQuestion: Int) {
        binding.toolbar.title = "Question ${numQuestion + 1}"
        if ( numQuestion == 0 )
            binding.toolbar.navigationIcon = null
        else
            binding.toolbar.setNavigationOnClickListener { listener?.onPrevQuestion() }
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
        fun newInstance(numQuestion: Int, countQuestion: Int): QuizFragment {
            val fragment = QuizFragment()
            val args = Bundle()
            args.putInt(NUM_QUESTION_KEY, numQuestion)
            args.putInt(COUNT_QUESTION_KEY, countQuestion)
            fragment.arguments = args
            return fragment
        }
        private const val NUM_QUESTION_KEY = "NUM_QUESTION"
        private const val COUNT_QUESTION_KEY = "COUNT_QUESTION"
    }
}