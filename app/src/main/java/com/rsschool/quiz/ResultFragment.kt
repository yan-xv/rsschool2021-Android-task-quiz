package com.rsschool.quiz

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rsschool.quiz.databinding.FragmentQuizBinding
import com.rsschool.quiz.databinding.FragmentResultBinding
import java.lang.RuntimeException

class ResultFragment : Fragment() {
    private var listener: IQuizFragment? = null
    private var _binding: FragmentResultBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        val quiz = listener?.onGetQuizData()
        //есть данные - применяем их к элемента интерфейса
        if (quiz != null) {
            binding.resultText.text = quiz.getResultText()
            binding.backButton.setOnClickListener { listener?.onReset() }
            binding.exitButton.setOnClickListener { listener?.onExit() }
            binding.shareButton.setOnClickListener { listener?.onShare() }
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
        fun newInstance() = ResultFragment()
    }
}