package com.intesasanpaolo.ispmobileinspector.ui.home

import android.content.Context
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
import com.intesasanpaolo.ispmobileinspector.contract.HomeContract
import com.intesasanpaolo.ispmobileinspector.databinding.FragmentHomeBinding
import com.intesasanpaolo.ispmobileinspector.ui.Card
import com.intesasanpaolo.ispmobileinspector.ui.CardAdapter
import com.intesasanpaolo.ispmobileinspector.utils.MobileInspectorUtils
import com.intesasanpaolo.ispmobileinspector.utils.MobileInspectorUtils.getDecodedJsonObject
import com.intesasanpaolo.ispmobileinspector.utils.MobileInspectorUtils.readFile
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment(), HomeContract.View {

    private var cards = ArrayList<Card>()
    private var mAdapter: CardAdapter? = null

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    private var mlistener: Listener? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mlistener = context as? Listener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*nextTodoButton.setOnClickListener {
            presenter.getTodo(id = ++todoId)
        }*/
        //movements = createMovementList()

        // // // // // // // //
        //presenter.getListTodo()

        mAdapter = CardAdapter(cards, requireContext())

        binding.rvCards.layoutManager = LinearLayoutManager(activity)
        binding.rvCards.adapter = mAdapter

        cards.addAll(getCardList())
        mAdapter?.refreshData()

        binding.buttonAll.isSelected = true //selected by default

        binding.buttonAll.setOnClickListener {

            it.isSelected = true
            deselectOtherBtns(
                getSelectedBtnPos(buttonContainerHome, binding.buttonAll),
                buttonContainerHome
            )
            cards.clear()
            cards.addAll(getCardList())
            mAdapter?.refreshData()
        }

        binding.buttonOk.setOnClickListener {
            it.isSelected = true
            deselectOtherBtns(
                getSelectedBtnPos(buttonContainerHome, binding.buttonOk),
                buttonContainerHome
            )
            cards.clear()
            cards.addAll(createOkList(getCardList()))
            mAdapter?.refreshData()
        }

        binding.buttonKo.setOnClickListener {
            it.isSelected = true
            deselectOtherBtns(
                getSelectedBtnPos(buttonContainerHome, binding.buttonKo),
                buttonContainerHome
            )
            cards.clear()
            cards.addAll(createKoList(getCardList()))
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getCardList(): ArrayList<Card> {
        val jsonString = context?.assets?.readFile("Card.json")
        val formattedJson = getDecodedJsonObject(jsonString)
        return MobileInspectorUtils.mapCardList(formattedJson)
    }

    private fun createOkList(cardList: ArrayList<Card>): ArrayList<Card> {
        val listOk = ArrayList<Card>()

        for (item in cardList) {
            if (item.status == "200" || item.status == "0") {
                listOk.add(item)
            }
        }
        return listOk
    }

    private fun createKoList(cardList: ArrayList<Card>): ArrayList<Card> {
        val listKo = ArrayList<Card>()

        for (item in cardList) {
            if (item.status != "200" && item.status != "0") {
                listKo.add(item)
            }
        }
        return listKo
    }

    companion object {

        const val TAG = "HomeFragment"
        fun newInstance() = HomeFragment()
    }
}