package com.example.customview.ui.customedittextview

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.customview.R

class CustomEditTextFragment : Fragment() {

    companion object {
        fun newInstance() = CustomEditTextFragment()
    }

    private lateinit var viewModel: CustomEditTextViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.custom_edit_text_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CustomEditTextViewModel::class.java)
        // TODO: Use the ViewModel
    }

}