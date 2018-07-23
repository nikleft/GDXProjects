package com.example.uber.stepikapp1

import android.content.Intent
import android.net.Uri
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
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import io.realm.RealmList
import io.realm.RealmObject
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.list_view.view.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    lateinit var vText:TextView
    var request:Disposable?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        Log.e("tag","Turned on!")
        setContentView(R.layout.activity_main)


            val o = createRequest("https://api.rss2json.com/v1/api.json?rss_url=http%3A%2F%2Ffeeds.bbci.co.uk%2Fnews%2Frss.xml")
                    .map{ Gson().fromJson(it,FeedAPI::class.java) }
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())


            request = o.subscribe({

                val feed = Feed(it.items.mapTo(RealmList<FeedItem>(),
                        {feed -> FeedItem(feed.title,feed.link,feed.thumbnail,feed.description) }))

                Realm.getDefaultInstance().executeTransaction {realm->

                    val oldlist =  realm.where(Feed::class.java).findAll()
                    if(oldlist.size>0) {
                        for (item in oldlist){
                            item.deleteFromRealm()}
                    }

                    realm.copyToRealm(feed)


                }
                showRecListView()



            },{Log.e("test","",it)})



        Log.v("tag","Запущен onCreate")

    }


//    fun showLinearLayout(feedList:ArrayList<FeedItemAPI>){
//
//        for (f in feedList) {
//            val view = layoutInflater.inflate(R.layout.list_view, act1_listView,false)
//            view.item_title.text=f.title
//            act1_listView.addView(view)
//        }
//
//    }


//    fun showListView(feedList: ArrayList<FeedItemAPI>){
//        act1_listView.adapter = Adapter(feedList)
//    }

    fun showRecListView(){

        Realm.getDefaultInstance().executeTransaction {realm->
                val feed = realm.where(Feed::class.java).findAll()
                if (feed.size>0)
                {
                    act1_recView.adapter = RecAdapter(feed[0]!!.items)
                    act1_recView.layoutManager = LinearLayoutManager(this)

                }

            }

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



class FeedAPI(
        val items:ArrayList<FeedItemAPI>
)


class FeedItemAPI(
        val title:String,
        val link:String,
        val thumbnail:String,
        val description:String
)

open class Feed(
        var items:RealmList<FeedItem> = RealmList<FeedItem>()
):RealmObject()


open class FeedItem(
        var title:String="",
        var link:String="",
        var thumbnail:String="",
        var description:String=""
):RealmObject()





class RecAdapter(val items:RealmList<FeedItem>):RecyclerView.Adapter<RecHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecHolder {

        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_view, parent,false)

        return RecHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecHolder, position: Int) {

        val item = items[position]!!
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







//class Adapter(val items: ArrayList<FeedItemAPI>): BaseAdapter(){
//    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
//
//        val inflater = LayoutInflater.from(p2!!.context)
//        val view = p1 ?: inflater.inflate(R.layout.list_view, p2,false)
//        val item = getItem(p0) as FeedItemAPI
//        view.item_title.text=item.title
//
//        return view
//    }
//
//    override fun getItem(p0: Int): Any {
//        return items[p0]
//    }
//
//    override fun getItemId(p0: Int): Long {
//        return p0.toLong()
//    }
//
//    override fun getCount(): Int {
//        return items.size
//    }
//
//}