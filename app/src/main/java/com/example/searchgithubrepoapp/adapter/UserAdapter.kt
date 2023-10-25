package com.example.searchgithubrepoapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.searchgithubrepoapp.databinding.ItemUserBinding
import com.example.searchgithubrepoapp.model.User

class UserAdapter(val onClick: (User) -> Unit) : ListAdapter<User,UserAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val viewBinding:ItemUserBinding):
        RecyclerView.ViewHolder(viewBinding.root) {

            fun bind(item:User){
                viewBinding.usernameTextView.text = item.userName
                viewBinding.root.setOnClickListener{
                    onClick(item)
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemUserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object: DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem == newItem // data class 함수기 때문에 따로 equals() 구현 안해도 이렇게 쓸 수 있음
            }

        }
    }


}