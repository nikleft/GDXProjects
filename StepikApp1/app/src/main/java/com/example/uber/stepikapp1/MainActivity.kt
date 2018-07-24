package com.example.uber.stepikapp1

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Html
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
import kotlinx.android.synthetic.main.activity_fragment.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.list_view.view.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        Log.e("tag", "Turned on!")
        setContentView(R.layout.activity_fragment)

        if (savedInstanceState == null) {
            val bundle = Bundle()
            bundle.putString("param", "value")
            val f = MainFragment()
            f.arguments = bundle
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_place, f)
                    .commitAllowingStateLoss()


        }

    }

    fun showArticle(url: String) {

        val bundle = Bundle()
        bundle.putString("url", url)
        val f = SecondFragment()
        f.arguments = bundle

        if (fragment_place2!=null){
            fragment_place2.visibility=View.VISIBLE
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_place2, f)
                    .commitAllowingStateLoss()

        }
        else {

            fragmentManager.beginTransaction()
                    .add(R.id.fragment_place, f)
                    .addToBackStack("main")
                    .commitAllowingStateLoss()

        }
    }

    fun playMusic(url: String) {
        val i = Intent(this, PlayService::class.java)
        i.putExtra("mp3",url)
        startService(i)

    }


}


class FeedAPI(
        val items: ArrayList<FeedItemAPI>
)


class FeedItemAPI(
        val title: String,
        val link: String,
        val thumbnail: String,
        val description: String,
        val guid:String
)

open class Feed(
        var items: RealmList<FeedItem> = RealmList<FeedItem>()
) : RealmObject()


open class FeedItem(
        var title: String = "",
        var link: String = "",
        var thumbnail: String = "",
        var description: String = "",
        var guid:String =""
) : RealmObject()


class RecAdapter(val items: RealmList<FeedItem>) : RecyclerView.Adapter<RecHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecHolder {

        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_view, parent, false)

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


class RecHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(item: FeedItem) {
        itemView.item_title.text = item.title
        itemView.item_desc.text = Html.fromHtml(item.description)

        Picasso.with(itemView.item_thumb.context).load(item.thumbnail).into(itemView.item_thumb)


        itemView.setOnClickListener {

//            (itemView.item_thumb.context as MainActivity).showArticle(item.link)
            (itemView.item_thumb.context as MainActivity).playMusic(item.guid)

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