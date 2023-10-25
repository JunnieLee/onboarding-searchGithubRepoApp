package com.example.searchgithubrepoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.searchgithubrepoapp.adapter.UserAdapter
import com.example.searchgithubrepoapp.databinding.ActivityMainBinding
import com.example.searchgithubrepoapp.model.Repo
import com.example.searchgithubrepoapp.model.UserDto
import com.example.searchgithubrepoapp.network.GithubService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var userAdapter :UserAdapter

    private val handler = Handler(Looper.getMainLooper())
    private var searchFor: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userAdapter = UserAdapter{
            val intent = Intent(this@MainActivity, RepoActivity::class.java)
            intent.putExtra("username",it.userName)
            startActivity(intent)
        }
        // recyclerView에 adapter 연결
        binding.userRecyclerView.apply{
            layoutManager = LinearLayoutManager(context)
            adapter = userAdapter
        }

        val runnable = Runnable{
            searchUser()
        }

        // 검색창에 검색어 쓰면 곧바로 유저 검색 가능하도록
        binding.searchEditText.addTextChangedListener {
            searchFor = it.toString()
            handler.removeCallbacks(runnable) // 바로 앞에 대기하고 있던 작업이 있을 경우 remove해줌
            handler.postDelayed(
                runnable,
                300
            ) // 앞에 있던거 있으면 지우고, 300ms 이후에 현재 request가 실행되도록
        } // debouncing

    }

    private fun searchUser(){
        val githubService = APIClient.retrofit.create(GithubService::class.java)
        // 유저 정보 fetch
        githubService.searchUsers(searchFor).enqueue(object : Callback<UserDto> {
            override fun onResponse(call: Call<UserDto>, response: Response<UserDto>) {
                Log.e("MainActivity", "Search User with ${searchFor} : ${response.body().toString()}")
                userAdapter.submitList(response.body()?.items)
            }
            override fun onFailure(call: Call<UserDto>, t: Throwable) {
                Toast.makeText(this@MainActivity, "에러가 발생했습니다.", Toast.LENGTH_SHORT).show()
                t.printStackTrace()
            }
        })
    }
}