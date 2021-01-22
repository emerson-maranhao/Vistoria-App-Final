package br.gov.am.detran.appvistoria.presentation.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import br.gov.am.detran.appvistoria.R
import br.gov.am.detran.appvistoria.databinding.FragmentHomeBinding
import br.gov.am.detran.appvistoria.domain.Survey
import br.gov.am.detran.appvistoria.presentation.ui.detail.SurveyDetailsActivity
import br.gov.am.detran.appvistoria.presentation.ui.search.SearchDataActivity
import br.gov.am.detran.appvistoria.session.UserPreferences
import br.gov.am.detran.appvistoria.until.addOnBackPressedCallbackWithInterval
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.ArrayList


const val TAG = "HomeFragment"
lateinit var t: LinearLayout
var i = 0

private var _binding:FragmentHomeBinding?=null
private val binding: FragmentHomeBinding get()= _binding!!

class HomeFragment : Fragment() {

    private var surveyList = ArrayList<Survey>()
    private var displayList = ArrayList<Survey>()

    private val homeViewModel: HomeViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater,container,false)
        val view= binding.root
        val activity = activity as Context

        t = binding.llmsg
        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = SurveysAdapter(emptyList()) {
            Log.i(TAG, "Empty list")
            t.isVisible = false
        }
        binding.fab.setOnClickListener {
            val intent = Intent(it.context, SearchDataActivity::class.java)
            // start your next activity
            startActivity(intent)
        }
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.showShimmer.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                binding.shimmerViewContainer?.startShimmer()
            } else {
                binding.shimmerViewContainer?.stopShimmer()
            }
            binding.shimmerViewContainer?.isVisible = it
        })
        homeViewModel.showError.observe(this.viewLifecycleOwner, Observer {
            //Toast.makeText(context,it,Toast.LENGTH_SHORT).show()
            if (it.isNullOrBlank()) {
                Log.i("vazio", "${it}")
            } else {
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Atenção!")
                builder.setMessage("Não foi possível conectar ao servidor! Você está Offline.")
                builder.setPositiveButton("Ok") { dialog, i ->
                    dialog.cancel()
                }
                builder.create().show()
            }
        })
        homeViewModel.getSurveys()
        homeViewModel.surveyList.observe(this.viewLifecycleOwner, Observer {
            with(binding.recyclerView) {
                if (it.isEmpty() || it == null) {
                    Log.i("it", "vazio")
                    t.isVisible = true
                } else {
                    binding.recyclerView.visibility = View.VISIBLE
                    Log.i("it", "Vistorias: $it")
                    it.let { surveys ->
                        surveyList.addAll(surveys)

                        t.isVisible = false

                        layoutManager = LinearLayoutManager(context)
                        displayList.clear()
                        displayList.addAll(surveys)
                       binding.recyclerView.addItemDecoration(
                            DividerItemDecoration(
                                context,
                                DividerItemDecoration.VERTICAL
                            )
                        )
                        adapter = SurveysAdapter(displayList) {
                            val intent =
                                SurveyDetailsActivity.getStartIntent(
                                    this.context,
                                    it
                                )
                            this@HomeFragment.startActivity(intent)
                        }
                    }
                }
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        //Carrega o arquivo de menu.
        inflater.inflate(R.menu.main, menu)
        val searchitem = menu.findItem(R.id.search)

        //Pega o Componente.
        val mSearchView = searchitem.actionView as SearchView

        //Define um texto de ajuda:
        mSearchView.queryHint = "Digite aqui..."

        // exemplos de utilização:
        mSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                //Do your search
                mSearchView.clearFocus()
                mSearchView.setQuery("", false)
                searchitem.collapseActionView()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isNotEmpty()) {
                    displayList.clear()
                    val search = newText.toUpperCase(Locale.getDefault())
                    surveyList.forEach {
                        if (it.license_plate.toUpperCase(Locale.getDefault()).contains(search))
                            displayList.add(it)
                    }
                    binding.recyclerView.adapter!!.notifyDataSetChanged()

                } else {
                    displayList.clear()
                    displayList.addAll(surveyList)
                    binding.recyclerView.adapter!!.notifyDataSetChanged()
                }
                return true

            }
        })

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.action_filter ->
                with(binding.recyclerView) {

                    if (surveyList.isEmpty() || surveyList == null) {
                        Log.i("lista", "vazio")
                        t.isVisible = true
                    } else {
                        binding.recyclerView.visibility = View.VISIBLE
                        Log.i(TAG, "Vistorias: $surveyList")
                        var l: List<Survey>
                        surveyList.let { surveys ->
                            if (i == 0) {
                                l = surveys
                                i = 1
                            } else {
                                //  item.setIcon(ContextCompat.getDrawable(context, R.drawable.ic_baseline_filter_list_24))
                                l = surveys.asReversed()
                                i = 0
                            }
                            Log.i("i", "0")
                            Log.i("data1", surveys.toString())
                            t.isVisible = false

                            layoutManager = LinearLayoutManager(context)
                            adapter = SurveysAdapter(l) {
                                val intent =
                                    SurveyDetailsActivity.getStartIntent(
                                        this.context,
                                        it
                                    )
                                this@HomeFragment.startActivity(intent)
                            }
                        }
                    }
                }

        }
        return super.onOptionsItemSelected(item)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(
            true // default to enabled
        ) {
            private var exitToast: Toast? = null
            override fun handleOnBackPressed() {
                if (exitToast == null || exitToast!!.view == null || exitToast!!.view!!.windowToken == null) {
                    exitToast = Toast.makeText(context,
                        getString(R.string.alert_press_again_to_exit),
                        Toast.LENGTH_SHORT
                    )
                    exitToast!!.show()
                } else {
                    exitToast!!.cancel()
                    activity?.let { UserPreferences.clear(it) }
                    activity?.finishAffinity()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            this,  // LifecycleOwner
            callback)
    }
    override fun onDestroyView() {
        _binding=null
        super.onDestroyView()
    }

    private fun configureOnBackPress(context: Context) {
        requireActivity().addOnBackPressedCallbackWithInterval(2000) {
            Toast.makeText(
                context,
                getString(R.string.alert_press_again_to_exit),
                Toast.LENGTH_SHORT
            ).show()
            Log.i(TAG, "BACKONPRESS")
        }

    }

}

