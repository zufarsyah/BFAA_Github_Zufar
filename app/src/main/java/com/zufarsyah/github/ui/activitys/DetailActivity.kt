package com.zufarsyah.github.ui.activitys

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.zufarsyah.github.R
import com.zufarsyah.github.databinding.ActivityDetailBinding
import com.zufarsyah.github.ui.adapter.SectionPagerAdapter
import com.zufarsyah.github.viewmodels.DetailViewModel
import com.zufarsyah.github.viewmodels.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showLoading(true)

        val model = obtainViewModel(this@DetailActivity)
        val username = intent.getStringExtra(EXTRA_USER)
        val id = intent.getIntExtra(EXTRA_ID, 0)
        val avatarUrl = intent.getStringExtra((EXTRA_AVATAR_URL))

        supportActionBar?.title = "Detail Profile"

        val sectionsPagerAdapter = SectionPagerAdapter(this, username.toString())
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)



        if (username != null) {
            model.setUserDetail(username)
        }

        model.getUserDetail().observe(this, {
            if (it != null) {


                val tab1 = String.format(
                    resources.getString(R.string.dummy_followers),
                    it.followers
                )
                val tab2 = String.format(
                    resources.getString(R.string.dummy_following),
                    it.following
                )

                val TAB_TITLES = arrayListOf(
                    tab1,
                    tab2
                )

                viewPager.adapter = sectionsPagerAdapter
                val tabs: TabLayout = findViewById(R.id.tabs)
                TabLayoutMediator(tabs, viewPager) { tab, position ->
                    tab.text = TAB_TITLES[position]
                }.attach()

                binding.apply {
                    Glide.with(this@DetailActivity)
                        .load(it.avatarUrl)
                        .into(avatar)
                    usernameShare = it.login
                    tvUsername.text = if (it.login != null) String.format(
                        resources.getString(R.string.dummy_username),
                        it.login
                    ) else resources.getString(R.string.dummy_username)
                    tvName.text =
                        if (it.name != null) it.name else resources.getString(R.string.dummy_name)
                    tvCompany.text =
                        if (it.company != null) it.company else resources.getString(R.string.dummy_company)
                    tvLocation.text =
                        if (it.location != null) it.location else resources.getString(R.string.dummy_location)
                    tvFollowers.text = if (it.followers != null) String.format(
                        resources.getString(R.string.dummy_followers),
                        it.followers
                    ) else resources.getString(R.string.dummy_followers)
                    tvFollowing.text = if (it.following != null) String.format(
                        resources.getString(R.string.dummy_following),
                        it.following
                    ) else resources.getString(R.string.dummy_following)
                    tvRepository.text = if (it.repository != null) String.format(
                        resources.getString(R.string.dummy_repository),
                        it.repository
                    ) else resources.getString(R.string.dummy_repository)

                }
                showLoading(false)
            }
        })

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var checked = false

        CoroutineScope(Dispatchers.IO).launch {
            val count = model.checkUser(id)
            withContext(Dispatchers.Main) {
                if (count > 0) {
                    binding.toggleFavorite.isSelected = true
                    checked = true
                } else {
                    binding.toggleFavorite.isSelected = false
                    checked = false
                }
            }
        }

        binding.toggleFavorite.setOnClickListener {
            checked = !checked
            if (checked) {
                if (username != null) {
                    if (avatarUrl != null) {
                        model.addFavorite(username, id, avatarUrl)
                        Toast.makeText(
                            this@DetailActivity,
                            "$username telah ditambahkan dalam favorite",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                model.deleteFavorite(id)
                Toast.makeText(
                    this@DetailActivity,
                    "$username telah dihapus dalam favorite",
                    Toast.LENGTH_SHORT
                ).show()
            }
            binding.toggleFavorite.isSelected = checked
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        setMode(item.itemId)
        return super.onOptionsItemSelected(item)
    }

    private fun setMode(selectedMode: Int) {
        when (selectedMode) {
            R.id.share -> {
                val shareIntent = Intent()
                shareIntent.action = Intent.ACTION_SEND
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_TEXT, "https://github.com/$usernameShare")
                startActivity(Intent.createChooser(shareIntent, "Share via"))
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_detail_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun obtainViewModel(activity: AppCompatActivity): DetailViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(DetailViewModel::class.java)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    companion object {
        const val EXTRA_USER = "extra_user"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_AVATAR_URL = "extra_avatar_url"

        var usernameShare = ""
    }
}

private operator fun Unit.compareTo(i: Int): Int {
    return i
}
