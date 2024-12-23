package com.sgu.givingsgu.activity.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.sgu.givingsgu.R
import com.sgu.givingsgu.databinding.FragmentHomeBinding
import com.sgu.givingsgu.viewmodel.HomeViewModel
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.sgu.givingsgu.adapter.ProjectAdapter
import com.sgu.givingsgu.adapter.ProjectHighLightAdapter
import com.sgu.givingsgu.model.Project


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var projectAdapter: ProjectAdapter
    private lateinit var projectHighLightAdapter: ProjectHighLightAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        settingUpListener()
    }


    fun init() {
        // init
        Glide.with(this)
            .asGif()
            .load(R.drawable.donation_gif)
            .into(binding.iconProject);
        initProjectHighLight()
        initProject()

    }


    private fun initProject() {
        binding.projectProgressBar.visibility = View.VISIBLE
        viewModel.project.observe(viewLifecycleOwner, Observer {
            projectAdapter = ProjectAdapter(it.toMutableList())
            binding.projectRecyclerView.adapter = projectAdapter
            binding.projectRecyclerView.isNestedScrollingEnabled = true
            binding.projectRecyclerView.layoutManager = LinearLayoutManager(context)
            binding.projectProgressBar.visibility = View.GONE
        })
        viewModel.fetchAllProject()
    }

    private fun initProjectHighLight() {
        binding.projectHighLightProgressBar.visibility = View.VISIBLE
        viewModel.project.observe(viewLifecycleOwner, Observer {
            val projectList = it.map { projectResponse -> projectResponse.project }
            projectHighLightAdapter = ProjectHighLightAdapter(projectList as MutableList<Project>)
            binding.projectHighLightRecyclerView.adapter = projectHighLightAdapter

            binding.projectHighLightRecyclerView.adapter = projectHighLightAdapter
            binding.projectHighLightRecyclerView.isNestedScrollingEnabled = true
            binding.projectHighLightRecyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            binding.projectHighLightProgressBar.visibility = View.GONE
        })
        viewModel.fetchAllProject()
    }


    fun settingUpListener() {

        binding.appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            // Kiểm tra xem AppBarLayout có đang mở hoàn toàn không
            binding.swipeRefreshLayout.isEnabled = verticalOffset == 0
        })

        // Setup swipe to refresh listener
        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.projectProgressBar.visibility = View.VISIBLE
            binding.projectHighLightProgressBar.visibility = View.VISIBLE

            binding.projectRecyclerView.visibility = View.GONE
            binding.projectHighLightRecyclerView.visibility = View.GONE

            binding.swipeRefreshLayout.isRefreshing = true
            viewModel.fetchAllProject()

            binding.swipeRefreshLayout.postDelayed({
                binding.swipeRefreshLayout.isRefreshing = false
                binding.projectProgressBar.visibility = View.GONE
                binding.projectHighLightProgressBar.visibility = View.GONE
                binding.projectRecyclerView.visibility = View.VISIBLE
                binding.projectHighLightRecyclerView.visibility = View.VISIBLE
            }, 3000)
        }


    }
}