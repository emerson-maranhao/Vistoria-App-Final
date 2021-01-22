package br.gov.am.detran.appvistoria.presentation.ui.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.gov.am.detran.appvistoria.databinding.FragmentInfoBinding


class InfoFragment : Fragment() {
    private var _binding: FragmentInfoBinding? = null
    private val binding: FragmentInfoBinding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

}