package com.intesasanpaolo.ispmobileinspector.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.intesasanpaolo.ispmobileinspector.Listener
import com.intesasanpaolo.ispmobileinspector.databinding.SearchFragmentBinding
import com.intesasanpaolo.ispmobileinspector.utils.MobileInspectorUtils
import com.intesasanpaolo.ispmobileinspector.utils.MobileInspectorUtils.readFile

class SearchFragment : Fragment() {
    private var _binding: SearchFragmentBinding? = null
    private val binding get() = _binding!!

    private var mCardBackend = ArrayList<Card>()
    private var mCardRawLog = ArrayList<CardRawLog>()

    private var mAdapterBackend: CardAdapter? = null
    private var mAdapterRawLog: CardAdapterRawLog? = null

    private var mlistener: Listener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        _binding = SearchFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //set backend adapter
        mAdapterBackend = CardAdapter(mCardBackend, requireContext())
        binding.rvCardBackend.layoutManager = LinearLayoutManager(activity)
        binding.rvCardBackend.adapter = mAdapterBackend

        //set raw adapter
        mAdapterRawLog =
            mlistener?.let { it -> CardAdapterRawLog(mCardRawLog, requireContext(), it) }
        binding.rvCardRawLog.layoutManager = LinearLayoutManager(activity)
        binding.rvCardRawLog.adapter = mAdapterRawLog

        //set list
        mCardRawLog.addAll(ArrayList())
        mCardBackend.addAll(ArrayList())

        mAdapterBackend?.refreshData()
        mAdapterRawLog?.refreshData()

        binding.buttonSearch.setOnClickListener {
            val text = binding.searchEdiText.text

            if (text.toString() != "") {

                mCardRawLog.clear()
                val searchRawList = checkTextRawLog(text.toString(), getCardRawLogList())
                mCardRawLog.addAll(searchRawList)
                mAdapterRawLog?.refreshData()

                mCardBackend.clear()
                val searchBackendList = checkTextBackend(text.toString(), getCardList())
                mCardBackend.addAll(searchBackendList)
                mAdapterBackend?.refreshData()

                if (searchRawList.isEmpty() && searchBackendList.isEmpty()) {
                    binding.noResult.visibility = View.VISIBLE
                }
            } else {
                binding.noResult.visibility = View.VISIBLE
            }
        }
        binding.buttonBack.setOnClickListener {
            //activity?.onBackPressed()
            mlistener?.returnToHome()
        }
    }

    private fun checkTextRawLog(text: String, list: ArrayList<CardRawLog>): ArrayList<CardRawLog> {
        val searchList = ArrayList<CardRawLog>()

        for (item in list) {
            var isEquals = false
            if (item.timeStamp?.contains(text) == true) isEquals = true
            if (item.message?.contains(text) == true) isEquals = true
            if (item.caller?.contains(text) == true) isEquals = true
            if (item.level?.contains(text) == true) isEquals = true
            if (item.method?.contains(text) == true) isEquals = true
            if (item.requestBody?.contains(text) == true) isEquals = true
            if (item.responseBody?.contains(text) == true) isEquals = true
            if (item.statusCode?.contains(text) == true) isEquals = true

            if (isEquals) searchList.add(item)
        }

        if (searchList.isEmpty()) {
            binding.rawLog.visibility = View.GONE
        } else {
            binding.noResult.visibility = View.GONE
            binding.rawLog.visibility = View.VISIBLE
        }
        return searchList
    }

    private fun checkTextBackend(text: String, list: ArrayList<Card>): ArrayList<Card> {
        val searchList = ArrayList<Card>()
        for (item in list) {
            var isEquals = false
            if (item.timeStamp?.contains(text) == true) isEquals = true
            if (item.status?.contains(text) == true) isEquals = true

            if (isEquals) searchList.add(item)
        }
        if (searchList.isEmpty()) {
            binding.backendLog.visibility = View.GONE
        } else {
            binding.noResult.visibility = View.GONE
            binding.backendLog.visibility = View.VISIBLE
        }
        return searchList
    }


    companion object {
        private const val LIST_HOME_KEY: String = "listHOME"
        private const val LIST_DASH_KEY: String = "listDASH"


        const val TAG = "SearchFragment"
        fun newInstance() = SearchFragment()
    }

    private fun getCardList(): ArrayList<Card> {
        val jsonString = context?.assets?.readFile("Card.json")
        val formattedJson = MobileInspectorUtils.getDecodedJsonObject(jsonString)
        return MobileInspectorUtils.mapCardList(formattedJson)
    }

    private fun getCardRawLogList(): ArrayList<CardRawLog> {
        val jsonString = context?.assets?.readFile("CardRawLog.json")
        val formattedJson = MobileInspectorUtils.getDecodedJsonObject(jsonString)
        return MobileInspectorUtils.mapCardRawLogList(formattedJson)
    }
}

