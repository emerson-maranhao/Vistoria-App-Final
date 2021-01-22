package br.gov.am.detran.appvistoria.presentation.ui.add

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.CheckBox
import android.widget.TextView
import br.gov.am.detran.appvistoria.R
import br.gov.am.detran.appvistoria.databinding.FilterChildLayoutBinding
import br.gov.am.detran.appvistoria.databinding.FilterHeaderLayoutBinding
import java.util.*

class FIltersExpandableAdapter(
    private val _context: Activity,
    private val _listDataHeader: List<FilterHeader>,
    private val _listDataChild: HashMap<String, List<String>>
) : BaseExpandableListAdapter() {
    var activity = _context
    private var inflater: LayoutInflater = LayoutInflater.from(_context)


    var childCheckboxElectricState = HashMap<Int, Int>()
    var childCheckboxExternalState = HashMap<Int, Int>()
    var childCheckboxInternalState = HashMap<Int, Int>()
    var childCheckboxMandatoryState = HashMap<Int, Int>()

    var listOfStatusFilterElectric = ArrayList<String>()
    var listOfStatusFilterExternal = ArrayList<String>()
    var listOfStatusFilterInternal = ArrayList<String>()
    var listOfStatusFilterMandatory = ArrayList<String>()

    var check_states = ArrayList<ArrayList<Int>>()
    override fun getGroupCount(): Int {
        return _listDataHeader.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return _listDataChild[_listDataHeader[groupPosition].title]
            ?.size!!
    }

    override fun getGroup(groupPosition: Int): FilterHeader {
        return _listDataHeader[groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {

        return _listDataChild[_listDataHeader[groupPosition].title]
            ?.get(childPosition)!!
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    @SuppressLint("InflateParams")
    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup
    ): View {
        var convertView = convertView
        val headerTitle = getGroup(groupPosition).title
        //if (convertView == null) {
        //LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.filter_header_layout, null)
        //inflater.inflate(R.layout.filter_header_layout, parent)
        //  }
        val binding=FilterHeaderLayoutBinding.bind(convertView)
        //val lblListHeader = convertView.findViewById<TextView>(R.id.lblListHeader)
       binding.lblListHeader.setTypeface(null, Typeface.BOLD)
        binding.lblListHeader.text = headerTitle
        val groupStatus = convertView.findViewById<TextView>(R.id.groupStatus)
        groupStatus.text = _listDataHeader[groupPosition].activeFilter
        return binding.root
    }

    @SuppressLint("InflateParams")
    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup
    ): View {
        var convertView = convertView
        val childText = getChild(groupPosition, childPosition) as String

        val headerText = getGroup(groupPosition)
        //if (convertView == null) {
        val infalInflater = this.activity
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        convertView =
            Objects.requireNonNull(infalInflater).inflate(R.layout.filter_child_layout, null)
        // }
        val binding = FilterChildLayoutBinding.bind(convertView)
//        val filterName = convertView
//            .findViewById<TextView>(R.id.textviewFilterName)
        binding.textviewFilterName.text = childText
        val mode = _context.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)
        when (mode) {
            Configuration.UI_MODE_NIGHT_YES -> {
                binding.textviewFilterName.setTextColor(Color.WHITE)
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                binding.textviewFilterName.setTextColor(Color.BLACK)

            }
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
            }
        }


        val filterCheckBoxElectric = binding.filterNameElectric
        val filterCheckBoxExternal = binding.filterNameInternal
        val filterCheckBoxInternal = binding.filterNameExternal
        val filterCheckBoxMandatory = binding.filterNameMandatory

        if (groupPosition == 0) {
            filterCheckBoxElectric.visibility = View.VISIBLE
            filterCheckBoxExternal.visibility = View.GONE
            filterCheckBoxInternal.visibility = View.GONE
            filterCheckBoxMandatory.visibility = View.GONE
        }
        if (groupPosition == 1) {
            filterCheckBoxElectric.visibility = View.GONE
            filterCheckBoxExternal.visibility = View.VISIBLE
            filterCheckBoxInternal.visibility = View.GONE
            filterCheckBoxMandatory.visibility = View.GONE
        }
        if (groupPosition == 2) {
            filterCheckBoxElectric.visibility = View.GONE
            filterCheckBoxExternal.visibility = View.GONE
            filterCheckBoxInternal.visibility = View.VISIBLE
            filterCheckBoxMandatory.visibility = View.GONE
        }
        if (groupPosition == 3) {
            filterCheckBoxElectric.visibility = View.GONE
            filterCheckBoxExternal.visibility = View.GONE
            filterCheckBoxInternal.visibility = View.GONE
            filterCheckBoxMandatory.visibility = View.VISIBLE
        }

        val view = parent.getChildAt(groupPosition)

        try {
            if (childCheckboxElectricState.size > 0) {
                if (childCheckboxElectricState.get(childPosition) != null) {
                    filterCheckBoxElectric.isChecked =
                        childCheckboxElectricState.get(childPosition) != 0
                }
            }
            if (childCheckboxExternalState.size > 0) {
                if (childCheckboxExternalState.get(childPosition) != null) {
                    filterCheckBoxExternal.isChecked =
                        childCheckboxExternalState.get(childPosition) != 0
                }
            }
            if (childCheckboxInternalState.size > 0) {
                if (childCheckboxInternalState.get(childPosition) != null) {
                    filterCheckBoxInternal.isChecked =
                        childCheckboxInternalState.get(childPosition) != 0
                }
            }
            if (childCheckboxMandatoryState.size > 0) {
                if (childCheckboxMandatoryState.get(childPosition) != null) {
                    filterCheckBoxMandatory.isChecked =
                        childCheckboxMandatoryState.get(childPosition) != 0
                }
            }

            filterCheckBoxElectric.setOnClickListener { v ->
                if (filterCheckBoxElectric.isChecked) {
                    childCheckboxElectricState[childPosition] = 1
                    listOfStatusFilterElectric.add(_listDataChild[headerText.title]!![childPosition])
                } else {
                    childCheckboxElectricState[childPosition] = 0
                    listOfStatusFilterElectric.remove(_listDataChild[headerText.title]!![childPosition])
                }
                if (_listDataChild[headerText.title] != null) headerText.activeFilter =
                    checkedStatusCombinedStringElectric
                getValuesStatusEletric()
                notifyDataSetChanged()
            }
            filterCheckBoxExternal.setOnClickListener { v ->
                if (filterCheckBoxExternal.isChecked) {
                    childCheckboxExternalState[childPosition] = 1
                    listOfStatusFilterExternal.add(_listDataChild[headerText.title]!![childPosition])
                } else {
                    childCheckboxExternalState[childPosition] = 0
                    listOfStatusFilterExternal.remove(_listDataChild[headerText.title]!![childPosition])
                }

                if (_listDataChild[headerText.title] != null) headerText.activeFilter =
                    checkedStatusCombinedStringExternal
                getValuesStatusExternal()
                notifyDataSetChanged()
            }
            filterCheckBoxInternal.setOnClickListener { v ->

                if (filterCheckBoxInternal.isChecked) {
                    childCheckboxInternalState[childPosition] = 1
                    listOfStatusFilterInternal.add(_listDataChild[headerText.title]!![childPosition])
                } else {
                    childCheckboxInternalState[childPosition] = 0
                    listOfStatusFilterInternal.remove(_listDataChild[headerText.title]!![childPosition])
                }

                if (_listDataChild[headerText.title] != null) headerText.activeFilter =
                    checkedStatusCombinedStringInternal
                getValuesStatusInternal()

                notifyDataSetChanged()

            }
            filterCheckBoxMandatory.setOnClickListener { v ->
                if (filterCheckBoxMandatory.isChecked) {
                    childCheckboxMandatoryState[childPosition] = 1
                    listOfStatusFilterMandatory.add(_listDataChild[headerText.title]!![childPosition])
                } else {
                    childCheckboxMandatoryState[childPosition] = 0
                    listOfStatusFilterMandatory.remove(_listDataChild[headerText.title]!![childPosition])
                }

                if (_listDataChild[headerText.title] != null) headerText.activeFilter =
                    checkedStatusCombinedStringMandatory
                getValuesStatusMandatory()

                notifyDataSetChanged()

            }


        } catch (e: Exception) {
        }
        return binding.root
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    private val checkedStatusCombinedStringElectric: String
        get() {
            var status = ""
            for (i in listOfStatusFilterElectric.indices) {
                status += listOfStatusFilterElectric[i]
                if (i != listOfStatusFilterElectric.size - 1) {
                    status += ", "
                }
            }
            return status
        }
    private val checkedStatusCombinedStringExternal: String
        get() {
            var status = ""
            for (i in listOfStatusFilterExternal.indices) {
                status += listOfStatusFilterExternal[i]
                if (i != listOfStatusFilterExternal.size - 1) {
                    status += ", "
                }
            }
            return status
        }
    private val checkedStatusCombinedStringInternal: String
        get() {
            var status = ""
            for (i in listOfStatusFilterInternal.indices) {
                status += listOfStatusFilterInternal[i]
                if (i != listOfStatusFilterInternal.size - 1) {
                    status += ", "
                }
            }
            return status
        }
    private val checkedStatusCombinedStringMandatory: String
        get() {
            var status = ""
            for (i in listOfStatusFilterMandatory.indices) {
                status += listOfStatusFilterMandatory[i]
                if (i != listOfStatusFilterMandatory.size - 1) {
                    status += ", "
                }
            }
            return status
        }


    fun getValuesStatusEletric(): String {

        return listOfStatusFilterElectric.joinToString()
    }

    fun getValuesStatusExternal(): String {

        return listOfStatusFilterExternal.joinToString()
    }

    fun getValuesStatusInternal(): String {
        return listOfStatusFilterInternal.joinToString()

    }

    fun getValuesStatusMandatory(): String {

        return listOfStatusFilterMandatory.joinToString()
    }


}
