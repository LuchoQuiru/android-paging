/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.codelabs.paging.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.codelabs.paging.Injection
import com.example.android.codelabs.paging.databinding.ActivityArticlesBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.paging.PagingData
import com.example.android.codelabs.paging.data.Article
import kotlinx.coroutines.flow.Flow

class ArticleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityArticlesBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val viewModel by viewModels<ArticleViewModel>(
            factoryProducer = { Injection.provideViewModelFactory(owner = this) }
        )

        val items = viewModel.items
        val articleAdapter = ArticleAdapter()

        //binding.bindAdapter(articleAdapter = articleAdapter)
        setContent{
            MainScreen(items)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                items.collectLatest {
                    articleAdapter.submitData(it)
                }
            }
        }
    }
}

@Composable
fun MainScreen(items: Flow<PagingData<Article>>) {
    Box(
        Modifier
            .fillMaxSize()
    ) {
        ListaDeElementos(items)
    }
}

@Composable
fun ListaDeElementos(finalTasks: Flow<PagingData<Article>>) {

    LazyColumn {
            items(finalTasks.collect()) { task ->
                Text(
                    Modifier.fillMaxWidth(),
                    text = task.
                )
            }
        }
    }
}



private fun ActivityArticlesBinding.bindAdapter(
    articleAdapter: ArticleAdapter
) {
    list.adapter = articleAdapter
    list.layoutManager = LinearLayoutManager(list.context)
    val decoration = DividerItemDecoration(list.context, DividerItemDecoration.VERTICAL)
    list.addItemDecoration(decoration)
}
