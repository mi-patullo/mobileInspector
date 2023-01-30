package com.intesasanpaolo.ispmobileinspector.ui.dashboard

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.forEachIndexed
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.intesasanpaolo.ispmobileinspector.Listener
import com.intesasanpaolo.ispmobileinspector.databinding.FragmentDashboardBinding
import com.intesasanpaolo.ispmobileinspector.ui.CardAdapterRawLog
import com.intesasanpaolo.ispmobileinspector.ui.CardRawLog
import com.intesasanpaolo.ispmobileinspector.utils.MobileInspectorUtils
import com.intesasanpaolo.ispmobileinspector.utils.MobileInspectorUtils.readFile
import kotlinx.android.synthetic.main.fragment_dashboard.*


class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private var _binding: FragmentDashboardBinding? = null

    private var mAdapter: CardAdapterRawLog? = null
    private var cards = ArrayList<CardRawLog>()

    private var mlistener: Listener? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mlistener = context as? Listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        /*//val textView: TextView = binding.textDashboard
        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })*/
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAdapter = mlistener?.let { CardAdapterRawLog(cards, requireContext(), it) }

        binding.rvCards.layoutManager = LinearLayoutManager(activity)
        binding.rvCards.adapter = mAdapter

        cards.addAll(getCardRawLogList())
        mAdapter?.refreshData()

        binding.buttonAll.isSelected = true //selected by default

        binding.buttonAll.setOnClickListener {
            it.isSelected = true
            deselectOtherBtns(
                getSelectedBtnPos(buttonContainer, binding.buttonAll),
                buttonContainer
            )
            cards.clear()
            cards.addAll(getCardRawLogList())
            mAdapter?.refreshData()
        }

        binding.buttonDebug.setOnClickListener {

            it.isSelected = true
            deselectOtherBtns(
                getSelectedBtnPos(buttonContainer, binding.buttonDebug),
                buttonContainer
            )
            cards.clear()
            cards.addAll(createList(getCardRawLogList(), "debug"))
            mAdapter?.refreshData()
        }

        binding.buttonInfo.setOnClickListener {
            it.isSelected = true
            deselectOtherBtns(
                getSelectedBtnPos(buttonContainer, binding.buttonInfo),
                buttonContainer
            )
            cards.clear()
            cards.addAll(createList(getCardRawLogList(), "info"))
            mAdapter?.refreshData()
        }

        binding.buttonError.setOnClickListener {
            it.isSelected = true
            deselectOtherBtns(
                getSelectedBtnPos(buttonContainer, binding.buttonError),
                buttonContainer
            )
            cards.clear()
            cards.addAll(createList(getCardRawLogList(), "error"))
            mAdapter?.refreshData()
        }

        binding.buttonVerb.setOnClickListener {
            it.isSelected = true
            deselectOtherBtns(
                getSelectedBtnPos(buttonContainer, binding.buttonVerb),
                buttonContainer
            )
            cards.clear()
            cards.addAll(createList(getCardRawLogList(), "verbose"))
            mAdapter?.refreshData()
        }

        binding.buttonWarn.setOnClickListener {
            it.isSelected = true
            deselectOtherBtns(
                getSelectedBtnPos(buttonContainer, binding.buttonWarn),
                buttonContainer
            )
            cards.clear()
            cards.addAll(createList(getCardRawLogList(), "warn"))
            mAdapter?.refreshData()
        }

        binding.buttonSearch.setOnClickListener {
            mlistener?.goToSearch()
        }
        binding.buttonShare.setOnClickListener {
            mlistener?.shareTransferDetails()
        }
    }

    private fun getSelectedBtnPos(pCardContainer: LinearLayout, button: AppCompatButton): Int {
        pCardContainer.forEachIndexed { i, btn ->
            if (btn == button) {
                return i
            }
        }
        return -1 //error
    }

    private fun deselectOtherBtns(pSelectedBtnPos: Int, pCardContainer: LinearLayout) {
        pCardContainer.forEachIndexed { i, btn ->
            if (i != pSelectedBtnPos) {
                btn.isSelected = false
            }
        }
    }

    private fun createList(cardList: ArrayList<CardRawLog>, level: String): ArrayList<CardRawLog> {
        val newList = ArrayList<CardRawLog>()

        for (item in cardList) {
            if (item.level == level) {
                newList.add(item)
            }
        }
        return newList
    }

    private fun getCardRawLogList(): ArrayList<CardRawLog> {
        val jsonString = context?.assets?.readFile("CardRawLog.json")
        val formattedJson = MobileInspectorUtils.getDecodedJsonObject(jsonString)
        return MobileInspectorUtils.mapCardRawLogList(formattedJson)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        const val TAG = "DashboardFragment"
        fun newInstance() = DashboardFragment()
    }
}