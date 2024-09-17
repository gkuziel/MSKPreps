package com.gkuziel.mskprep.presentation.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gkuziel.mskprep.compose.MainCompActivity
import com.gkuziel.mskprep.databinding.ActivityMainBinding
import com.gkuziel.mskprep.presentation.details.DetailsActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    private val eventAdapter by lazy {
        EventAdapter {
            DetailsActivity.start(this, it.id)
        }
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()

        viewModel.loadUsers()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collect { eventsUi ->
                    eventAdapter.setItems(eventsUi.events)
                }
            }
        }
    }

    private fun initViews() {
        with(binding) {
            rvEvents.adapter = eventAdapter
            rvEvents.layoutManager = LinearLayoutManager(
                this@MainActivity,
                RecyclerView.VERTICAL,
                false
            )
            btnGotoCompose.setOnClickListener {
                MainCompActivity.start(this@MainActivity)
            }
        }
    }

}