package com.example.uber.stepikapp1

import android.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import io.realm.RealmList
import kotlinx.android.synthetic.main.activity_main.*

class MainFragment:Fragment(){

    lateinit var vText: TextView
    var request: Disposable?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.activity_main,container,false)
        return view
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        val o = createRequest("https://api.rss2json.com/v1/api.json?rss_url=http%3A%2F%2Ffeeds.bbci.co.uk%2Fnews%2Frss.xml")
                .map{ Gson().fromJson(it,FeedAPI::class.java) }
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())


        request = o.subscribe({

            val feed = Feed(it.items.mapTo(RealmList<FeedItem>(),
                    {feed -> FeedItem(feed.title,feed.link,feed.thumbnail,feed.description) }))

            Realm.getDefaultInstance().executeTransaction { realm->

                val oldlist =  realm.where(Feed::class.java).findAll()
                if(oldlist.size>0) {
                    for (item in oldlist){
                        item.deleteFromRealm()}
                }

                realm.copyToRealm(feed)


            }
            showRecListView()



        },{ Log.e("test","",it)})



        Log.v("tag","Запущен onCreate")





    }



    fun showRecListView(){

        Realm.getDefaultInstance().executeTransaction {realm->
            if (!isVisible)
                return@executeTransaction

            val feed = realm.where(Feed::class.java).findAll()
            if (feed.size>0)
            {
                act1_recView.adapter = RecAdapter(feed[0]!!.items)
                act1_recView.layoutManager = LinearLayoutManager(activity)

            }

        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        request?.dispose()
    }




}