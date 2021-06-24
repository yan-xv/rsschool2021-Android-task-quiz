package com.rsschool.quiz

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.rsschool.quiz.databinding.FragmentResultBinding
import java.lang.RuntimeException

interface IResultFragment {
    fun onReset()
    fun onShare()
    fun onExit()
}

class ResultFragment : Fragment() {
    private var listener: IResultFragment? = null
    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // получаем данные из активити
        val quiz = arguments?.get(QUIZ_KEY) as Quiz
        // применяем их к элемента интерфейса
        binding.resultText.text = quiz.getResultText()
        binding.backButton.setOnClickListener { listener?.onReset() }
        binding.exitButton.setOnClickListener { listener?.onExit() }
        binding.shareButton.setOnClickListener { listener?.onShare() }

        val listStars = listOf(
            binding.star1, binding.star2, binding.star3, binding.star4, binding.star5)

        for ( i in 0 until quiz.getCountTrueAnswer())
            listStars[i].setImageResource(android.R.drawable.btn_star_big_on)
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is IResultFragment) {
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
        fun newInstance(quiz: Quiz): ResultFragment {
            val fragment = ResultFragment()
            fragment.arguments = bundleOf(QUIZ_KEY to quiz)
            return fragment
        }

        private const val QUIZ_KEY = "QUIZ"
    }
}