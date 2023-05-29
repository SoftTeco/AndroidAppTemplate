package com.softteco.template.presentation.features.apis

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.softteco.template.domain.model.ApiEntry
import com.softteco.template.domain.model.Output
import com.softteco.template.presentation.R
import com.softteco.template.presentation.base.BaseFragment
import com.softteco.template.presentation.common.navigateSafe
import com.softteco.template.presentation.databinding.FragmentApiListBinding
import com.softteco.template.presentation.extensions.applyTheme
import com.softteco.template.presentation.extensions.launchWhileStarted
import com.softteco.template.presentation.extensions.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ApiListFragment : BaseFragment(R.layout.fragment_api_list) {

    override val binding by viewBinding(FragmentApiListBinding::bind)

    private val viewModel: ApiListViewModel by viewModels()

    override fun subscribeUi() {
        binding.layoutSwipeRefresh.applyTheme()
        binding.toolbar.title = getString(R.string.list_screen_title)
        binding.layoutSwipeRefresh.setOnRefreshListener(viewModel::fetchAllApis)

        viewLifecycleOwner.launchWhileStarted {
            viewModel.apiList.collectLatest { apiList ->
                binding.rvApiList.swapAdapter(
                    getNewAdapter(apiList),
                    false
                )
            }
        }

        viewLifecycleOwner.launchWhileStarted {
            viewModel.fetchApiListOutput.collectLatest { output: Output<List<ApiEntry>>? ->
                output?.let {
                    binding.layoutSwipeRefresh.isRefreshing = when (output.status) {
                        Output.Status.SUCCESS -> false
                        Output.Status.ERROR -> {
                            output.message?.let {
                                showError(it) {
                                    viewModel.fetchAllApis()
                                }
                            }
                            false
                        }

                        Output.Status.LOADING -> true
                    }
                }
            }
        }
    }

    private fun getNewAdapter(apiList: List<ApiEntry>): ApiListAdapter {
        return ApiListAdapter(
            apiList,
            onClick = { position ->
                findNavController().navigateSafe(ApiListFragmentDirections.apisToDetails(apiList[position]))
            },
            onClickFavorite = viewModel::onToggleFavorite
        )
    }
}
