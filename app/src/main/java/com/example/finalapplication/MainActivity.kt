package com.example.finalapplication

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.finalapplication.databinding.ActivityMainBinding
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.preference.PreferenceManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabItem
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{
    lateinit var binding: ActivityMainBinding
    lateinit var toggle: ActionBarDrawerToggle //토글 버튼 추가

    lateinit var headerView: View
    lateinit var sharedPreferences: SharedPreferences

    //fragment
    class MyFragmentPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
        val fragments : List<Fragment>
        init {
            fragments = listOf(FoodFragment(), LikeFragment())
        }

        override fun getItemCount(): Int {
            return fragments.size
        }

        override fun createFragment(position: Int): Fragment {
            return fragments[position]
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(findViewById(R.id.tool_bar))



        // DrawerLayout Toggle
        toggle = ActionBarDrawerToggle(
            this,
            binding.drawer,
            R.string.drawer_opened,
            R.string.drawer_closed
        )
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toggle.syncState()

        //fragment 이동
        binding.viewpager.adapter = MyFragmentPagerAdapter(this)
        TabLayoutMediator(binding.tabs, binding.viewpager){
            tab, position -> tab.text = when(position){
                0-> "home"
                1->"like"
                else -> null
            }
        }.attach()

        //검색하기

        binding.mainDrawerView.setNavigationItemSelectedListener(this)
        // 로그인페이지로 이동
        headerView = binding.mainDrawerView.getHeaderView(0)
        val button = headerView.findViewById<Button>(R.id.btnAuth)
        button.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            if(button.text.equals("로그인")){
                intent.putExtra("status","logout")
            } else if(button.text.equals("로그아웃")){
                intent.putExtra("status","login")
            }
            startActivity(intent)
            binding.drawer.closeDrawers()
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        //검색페이지로 이동
        if(item.itemId === R.id.gotosearch) {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()

        val button = headerView.findViewById<Button>(R.id.btnAuth)
        val tv = headerView.findViewById<TextView>(R.id.tvID)

        if(MyApplication.checkAuth()){
            button.text = "로그아웃"
            //tv.text = "${MyApplication.email}"
            tv.visibility = View.VISIBLE
        } else {
            button.text = "로그인"
            tv.visibility = View.GONE
        }
    }

    // drawer 메뉴
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_board -> {
                val intent = Intent(this, BoardActivity::class.java)
                startActivity(intent)
                binding.drawer.closeDrawers() // 드로어 닫기
                true
            }
            R.id.item_mymenu -> {
                val intent = Intent(this, MymenuActivity::class.java)
                startActivity(intent)
                binding.drawer.closeDrawers()
                true
            }
            R.id.menu_main_setting -> {
                val intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
                binding.drawer.closeDrawers()
                true
            }
        }
        return false
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        return super.onCreateOptionsMenu(menu)
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean{
//        if(item.itemId === R.id.gotosearch) {
//            val intent = Intent(this, SearchActivity::class.java)
//            startActivity(intent)
//        }
//        return super.onOptionsItemSelected(item)
//    }

    override fun onResume() {
        super.onResume()

        val username = headerView.findViewById<TextView>(R.id.tvID)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val idStr = sharedPreferences.getString("id", "user1")
        username.text = idStr

    }

}