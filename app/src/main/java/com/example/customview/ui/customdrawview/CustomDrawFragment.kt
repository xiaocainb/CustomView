package com.example.customview.ui.customdrawview

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.customview.databinding.CustomDrawFragmentBinding

class CustomDrawFragment : Fragment() {
    private lateinit var binding:CustomDrawFragmentBinding

    companion object {
        fun newInstance() = CustomDrawFragment()
    }

    private lateinit var viewModel: CustomDrawViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= CustomDrawFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CustomDrawViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(binding.customDrawView)
    }

}