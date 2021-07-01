package com.rsschool.quiz

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.rsschool.quiz.databinding.FragmentResultBinding
import java.lang.RuntimeException

class ResultFragment : Fragment() {
    private var listener: IResultFragment? = null
    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initView() {
        // получаем данные из активити
        val quiz = arguments?.get(QUIZ_KEY) as Quiz
        // применяем их к элемента интерфейса
        with(binding) {
            resultText.text = quiz.getResultText(getString(R.string.result_template))
            backButton.setOnClickListener  { listener?.onReset() }
            exitButton.setOnClickListener  { listener?.onExit()  }
            shareButton.setOnClickListener { listener?.onShare() }
        }
        // звездочки в зависимости от результата
        initStars(quiz.getCountTrueAnswer())
    }

    private fun initStars(countTrueAnswer: Int) {
        with(binding) {
            val listStars = listOf(star1, star2, star3, star4, star5)
            for (i in 0 until countTrueAnswer)
                listStars[i].setImageResource(android.R.drawable.btn_star_big_on)
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is IResultFragment)
            listener = context
        else
            throw RuntimeException("$context must implement IResultFragment")
    }

    interface IResultFragment {
        fun onReset()
        fun onShare()
        fun onExit()
    }

    companion object {
        @JvmStatic
        fun newInstance(quiz: Quiz): ResultFragment {
            return ResultFragment().apply { arguments = bundleOf(QUIZ_KEY to quiz) }
        }
        private const val QUIZ_KEY = "QUIZ"
    }
}
