package com.xloger.xlib.tool

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created on 2018/7/24 17:03.
 * Author: xloger
 * Email:phoenix@xloger.com
 * 一个快速开发的通用 Adapter，用于数据量比较少的场景。（未对 findViewById 优化）
 */
class FastAdapter<T>(val list: List<T>, val layoutResource: Int, val bind: (holder: ViewHolder, t : T) -> Unit) : RecyclerView.Adapter<FastAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(layoutResource, parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        bind(holder, list[position])
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}