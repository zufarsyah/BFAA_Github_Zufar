package com.zufarsyah.github.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.zufarsyah.github.ui.adapter.UserAdapter
import com.zufarsyah.github.databinding.FragmentFollowBinding
import com.zufarsyah.github.viewmodels.FollowViewModel

class FollowFragment : Fragment() {
    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!
    private lateinit var username: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val model = ViewModelProvider(this).get(FollowViewModel::class.java)

        val arguments = arguments
        username = arguments?.getString(ARG_SECTION_USERNAME).toString()
        val position = arguments?.getInt(ARG_SECTION_NUMBER, 0)

        val layoutManager = LinearLayoutManager(activity)
        binding.rvGithub.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(activity, layoutManager.orientation)
        binding.rvGithub.addItemDecoration(itemDecoration)
        val adapter = UserAdapter()
        adapter.notifyDataSetChanged()
        binding.rvGithub.adapter = adapter

        if (position == 1 ){
            showLoading(true)
            model.setListFollowers(username)
            model.getListFollowers().observe(viewLifecycleOwner, {
                if (it != null) {
                    adapter.setList(it)
                    showLoading(false)
                }
            })
        } else if (position == 2) {
            showLoading(true)
            model.setListFollowing(username)
            model.getListFollowing().observe(viewLifecycleOwner, {
                if (it != null) {
                    adapter.setList(it)
                    showLoading(false)
                }
            })
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"
        private const val ARG_SECTION_USERNAME = "section_username"
        @JvmStatic
        fun newInstance(index: Int, userName: String) =
            FollowFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, index)
                    putString(ARG_SECTION_USERNAME, userName)
                }
            }
    }

}