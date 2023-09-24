package com.github.gituser.ui.main.detail


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.gituser.databinding.FragmentFollowBinding
import com.github.gituser.domain.user.entity.UserEntity
import com.github.gituser.ui.common.showToast
import com.github.gituser.ui.main.FollowFragmentState
import com.github.gituser.ui.main.FollowViewModel
import com.github.gituser.ui.main.ListUserAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class FollowingFragment : Fragment() {
    private lateinit var binding: FragmentFollowBinding
    private val followViewModel by viewModels<FollowViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()

        val username = arguments?.getString(ARG_SECTION_USERNAME)
        username?.let {
            Log.d(TAG, "onViewCreated: FollowingFragment created")
            followViewModel.getUserFollowing(it)
        }
        observe()
//        followViewModel.following.observe(viewLifecycleOwner, {
//            it.getContentIfNotHandled()?.let { data ->
//                setUsersData(data)
//            }
//        })
    }

    private fun observe() {
        followViewModel.stateFollowing
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED).
            onEach { state -> handleStateChanges(state) }.
            launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun handleStateChanges(state: FollowFragmentState) {
        Log.d(TAG, "FRAGMENT STATE $state")
        when(state){
            is FollowFragmentState.Init -> Unit
            is FollowFragmentState.IsLoading -> handleLoading(state.isLoading)
            is FollowFragmentState.IsError -> handleError(state.message)
            is FollowFragmentState.IsSuccess -> handleSuccess(state.data)
        }

    }

    private fun handleSuccess(data: List<UserEntity>) {
        Log.d(TAG, "Handle Success")
        binding.rvFollow.adapter?.let { adapter ->
            if(adapter is ListUserAdapter){
              adapter.updateList(data)
                Log.d(TAG, "following adapter has been update")
            }
        }
    }

    private fun handleError(message: String) {
        return requireActivity().showToast(message)
    }

    private fun setupRecyclerView(){
        val adapter = ListUserAdapter(mutableListOf())
        adapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserEntity) {
                showSelectedUser(data)
            }
        })
        with(binding){
            rvFollow.adapter = adapter
            rvFollow.isNestedScrollingEnabled = false
            rvFollow.layoutManager = LinearLayoutManager(requireActivity())
//            rvFollow.setHasFixedSize(true)
        }
    }
    private fun showSelectedUser(user: UserEntity) {
        val moveWithDataIntent = Intent(requireActivity(), DetailActivity::class.java)
        moveWithDataIntent.putExtra(DetailActivity.EXTRA_USER, user)
        requireActivity().startActivity(moveWithDataIntent)
        requireActivity().finish()
    }

    private fun handleLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val TAG="FOLLOWING_FRAGMENT_TAG"
        private const val ARG_SECTION_USERNAME = "section_username"
        @JvmStatic
        fun newInstance(username: String) =
            FollowingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_SECTION_USERNAME, username)
                }
            }
    }
}