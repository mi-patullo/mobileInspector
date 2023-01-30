package com.intesasanpaolo.ispmobileinspector.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.intesasanpaolo.ispmobileinspector.Listener
import com.intesasanpaolo.ispmobileinspector.databinding.DettaglioFragmentBinding

class DettaglioFragment : Fragment() {
    private var _binding: DettaglioFragmentBinding? = null
    private var mlistener: Listener? = null


    private val binding get() = _binding!!
    private var mCard: CardRawLog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mCard = arguments?.getParcelable(LIST_KEY)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mlistener = context as? Listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = DettaglioFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        /*//val textView: TextView = binding.textDashboard
        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })*/
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.idTimestampResponse.text = mCard?.timeStamp
        binding.idTimestampResponse2.text = mCard?.level
        binding.idTimestampResponse3.text = mCard?.id.toString()
        binding.idTimestampResponse4.text = mCard?.url
        binding.idTimestampResponse5.text = mCard?.method
        binding.idTimestampResponse6.text = mCard?.requestBody
        binding.idTimestampResponse7.text = mCard?.responseBody
        binding.idTimestampResponse8.text = mCard?.statusCode

        binding.buttonBack.setOnClickListener {
            mlistener?.returnToHome()
        }
    }

    companion object {
        private const val LIST_KEY: String = "list"

        const val TAG = "DettaglioFragment"
        fun newInstance(card: CardRawLog) = DettaglioFragment().apply {
            val bundle = Bundle().apply {
                putParcelable(LIST_KEY, card)
            }
            arguments = bundle
        }
    }
}

