package com.github.gituser.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.gituser.databinding.FragmentFollowBinding

class FollowFragment : Fragment() {
    private lateinit var binding: FragmentFollowBinding
    private val followViewModel by viewModels<FollowViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvFollow.setHasFixedSize(true)

        val username = arguments?.getString(ARG_SECTION_USERNAME)
        username?.let { followViewModel.getFollower(it) }
        followViewModel.follower.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let { data ->
                setUsersData(data)
            }
        })
        followViewModel.isLoading.observe(viewLifecycleOwner, {
            showLoading(it)
        })
    }

    private fun setUsersData(listItems: List<GithubFollowItem>) {
        val listFollowerUsers = ArrayList<Users>()
        for (item in listItems) {
            val user = Users(
                item.login,
                item.avatarUrl
            )
            listFollowerUsers.add(user)
        }

        binding.rvFollow.layoutManager = LinearLayoutManager(activity)
        val adapter = ListUserAdapter(listFollowerUsers)
        binding.rvFollow.adapter = adapter

        adapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Users) {
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        private const val ARG_SECTION_USERNAME = "section_username"
        @JvmStatic
        fun newInstance(username: String) =
            FollowFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_SECTION_USERNAME, username)
                }
            }
    }
}