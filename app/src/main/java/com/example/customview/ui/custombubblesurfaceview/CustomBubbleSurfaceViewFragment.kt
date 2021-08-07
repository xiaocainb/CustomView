package com.example.customview.ui.custombubblesurfaceview

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.customview.R

class CustomBubbleSurfaceViewFragment : Fragment() {

    companion object {
        fun newInstance() = CustomBubbleSurfaceViewFragment()
    }

    private lateinit var viewModel: CustomBubbleSurfaceViewViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.custom_bubble_surface_view_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CustomBubbleSurfaceViewViewModel::class.java)
        // TODO: Use the ViewModel
    }

}