package com.example.searchgithubrepoapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.searchgithubrepoapp.adapter.RepoAdapter
import com.example.searchgithubrepoapp.databinding.ActivityRepoBinding
import com.example.searchgithubrepoapp.model.Repo
import com.example.searchgithubrepoapp.network.GithubService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RepoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRepoBinding
    private lateinit var repoAdapter: RepoAdapter
    private var page = 0
    private var hasMore = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRepoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra("username")?:return // null이면 아래 내용을 실행할 필요가 없음

        binding.usernameTextView.text = username

        repoAdapter = RepoAdapter{
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.htmlUrl))
            startActivity(intent)
        }
        val linearLayoutManager = LinearLayoutManager(this@RepoActivity)

        // recyclerView에 adapter 적용
        binding.repoRecyclerView.apply{
            layoutManager = linearLayoutManager
            adapter = repoAdapter
        }

        // scroll 함에 따라 paging 처리
        binding.repoRecyclerView.addOnScrollListener(object:RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val totalCount = linearLayoutManager.itemCount
                val lastVisiblePosition = linearLayoutManager.findLastCompletelyVisibleItemPosition()

                if (lastVisiblePosition >= (totalCount-1) && hasMore){ // 현재 scroll끝이 마지막 item을 보고 있다면
                    page+=1
                    listRepo(username,page)
                }
            }
        })

        listRepo(username,0)
    }

    private fun listRepo(username:String, page:Int){
        val githubService = APIClient.retrofit.create(GithubService::class.java)
        // 레포 정보 fetch
        githubService.listRepos(username, page).enqueue(object : Callback<List<Repo>> {
            override fun onResponse(call: Call<List<Repo>>, response: Response<List<Repo>>) {
                Log.e("RepoActivity", "Search Repo : ${response.body().toString()}")
                hasMore = response.body()?.count() == 30
                // adapter에 api call response로 가져온 데이터를 넣어준다
                repoAdapter.submitList(repoAdapter.currentList + response.body().orEmpty())
            }

            override fun onFailure(call: Call<List<Repo>>, t: Throwable) {

            }
        })
    }
}