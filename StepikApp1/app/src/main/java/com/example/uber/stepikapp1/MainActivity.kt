package com.example.uber.stepikapp1

import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.zipWith
import io.reactivex.schedulers.Schedulers
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection
import java.util.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.list_view.*
import kotlinx.android.synthetic.main.list_view.view.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    lateinit var vText:TextView
    var request:Disposable?=null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null)
        Log.e("tag","Turned on!")
        setContentView(R.layout.activity_main)


            val o = createRequest("https://api.rss2json.com/v1/api.json?rss_url=http%3A%2F%2Ffeeds.bbci.co.uk%2Fnews%2Frss.xml")
                    .map{ Gson().fromJson(it,Feed::class.java) }
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())


            request = o.subscribe({

//                showLinearLayout(it.items)
                showRecListView(it.items)

            },{Log.e("test","",it)})



        Log.v("tag","Запущен onCreate")

    }


//    fun showLinearLayout(feedList:ArrayList<FeedItem>){
//
//        for (f in feedList) {
//            val view = layoutInflater.inflate(R.layout.list_view, act1_listView,false)
//            view.item_title.text=f.title
//            act1_listView.addView(view)
//        }
//
//    }


//    fun showListView(feedList: ArrayList<FeedItem>){
//        act1_listView.adapter = Adapter(feedList)
//    }

    fun showRecListView(feedList: ArrayList<FeedItem>){
        act1_recView.adapter = RecAdapter(feedList)
        act1_recView.layoutManager = LinearLayoutManager(this)
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data!=null){
        val v = data.getStringExtra("text")
        vText.text = v
        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        request?.dispose()
    }


}

//class AT(val act:MainActivity): AsyncTask<String,Int,String>(){
//    override fun doInBackground(vararg p0: String?): String {
//        return "XXX"
//    }
//
//    override fun onPostExecute(result: String?) {
//        super.onPostExecute(result)
//
//    }
//} Даже вывод в отдельный класс не решает проблему



class Feed(
        val items:ArrayList<FeedItem>
)


class FeedItem(
        val title:String,
        val link:String,
        val thumbnail:String,
        val description:String
)





class RecAdapter(val items:ArrayList<FeedItem>):RecyclerView.Adapter<RecHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecHolder {

        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_view, parent,false)

        return RecHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecHolder, position: Int) {

        val item = items[position]
        holder.bind(item)

    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

}









class RecHolder(view:View):RecyclerView.ViewHolder(view){

    fun bind(item: FeedItem){
        itemView.item_title.text=item.title
        itemView.item_desc.text=item.description

        Picasso.with(itemView.item_thumb.context).load(item.thumbnail).into(itemView.item_thumb)


        itemView.setOnClickListener{
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(item.link)
            itemView.item_thumb.context.startActivity(i)
        }
    }

}







class Adapter(val items: ArrayList<FeedItem>): BaseAdapter(){
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {

        val inflater = LayoutInflater.from(p2!!.context)
        val view = p1 ?: inflater.inflate(R.layout.list_view, p2,false)
        val item = getItem(p0) as FeedItem
        view.item_title.text=item.title

        return view
    }

    override fun getItem(p0: Int): Any {
        return items[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return items.size
    }

}