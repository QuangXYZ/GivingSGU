package com.sgu.givingsgu.activity.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import com.sgu.givingsgu.R
import com.sgu.givingsgu.adapter.AdminProjectAdapter
import com.sgu.givingsgu.adapter.AdminProjectHighLightAdapter
import com.sgu.givingsgu.adapter.ProjectAdapter
import com.sgu.givingsgu.adapter.ProjectHighLightAdapter
import com.sgu.givingsgu.databinding.FragmentAdminHomeBinding
import com.sgu.givingsgu.model.Project
import com.sgu.givingsgu.viewmodel.HomeViewModel

class AdminHomeFragment : Fragment() {
    private lateinit var binding: FragmentAdminHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var adminProjectAdapter: AdminProjectAdapter
    private lateinit var adminProjectHighLightAdapter: AdminProjectHighLightAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAdminHomeBinding.inflate(inflater, container, false)
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
            .into(binding.adminIconProject);
        initProjectHighLight()
        initProject()
    }

    private fun initProject() {
        binding.projectProgressBar.visibility = View.VISIBLE
        viewModel.project.observe(viewLifecycleOwner, Observer {
            adminProjectAdapter = AdminProjectAdapter(it.toMutableList())
            binding.adminProjectRecyclerView.adapter = adminProjectAdapter
            binding.adminProjectRecyclerView.isNestedScrollingEnabled = true
            binding.adminProjectRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH)
            binding.adminProjectRecyclerView.layoutManager = LinearLayoutManager(context)
            binding.projectProgressBar.visibility = View.GONE
        })
        viewModel.fetchAllProject()
    }
    private fun initProjectHighLight() {
        binding.projectHighLightProgressBar.visibility = View.VISIBLE
        viewModel.project.observe(viewLifecycleOwner, Observer {
            val projectList = it.map { projectResponse -> projectResponse.project }

            adminProjectHighLightAdapter = AdminProjectHighLightAdapter(projectList as MutableList<Project>)
            binding.adminProjectHighLightRecyclerView.adapter = adminProjectHighLightAdapter
            binding.adminProjectHighLightRecyclerView.adapter = adminProjectHighLightAdapter
            binding.adminProjectHighLightRecyclerView.isNestedScrollingEnabled = true
            binding.adminProjectHighLightRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
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

            binding.adminProjectRecyclerView.visibility = View.GONE
            binding.adminProjectHighLightRecyclerView.visibility = View.GONE

            binding.swipeRefreshLayout.isRefreshing = true
            viewModel.fetchAllProject()

            binding.swipeRefreshLayout.postDelayed({
                binding.swipeRefreshLayout.isRefreshing = false
                binding.projectProgressBar.visibility = View.GONE
                binding.projectHighLightProgressBar.visibility = View.GONE
                binding.adminProjectRecyclerView.visibility = View.VISIBLE
                binding.adminProjectHighLightRecyclerView.visibility = View.VISIBLE
            }, 3000)
        }
    }
}