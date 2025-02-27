package com.rohitdubey.quotegenerator

import android.app.ProgressDialog
import android.content.ClipData
import android.content.ClipboardManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class MainActivity : AppCompatActivity() {
    private var dialog: ProgressDialog? = null
    private lateinit var recycler_home: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycler_home = findViewById(R.id.recycler_home)

        RequestManager(this@MainActivity).GetAllQuotes(listener)
        dialog = ProgressDialog(this@MainActivity).apply {
            setTitle("Loading....")
            show()
        }
    }

    private val listener: QuotesResponseListener = object : QuotesResponseListener {
        override fun didFetch(response: List<QuotesResponse>, message: String) {
            dialog?.dismiss()
            recycler_home.apply {
                setHasFixedSize(true)
                layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
                adapter = QuotesListAdapter(this@MainActivity, response, copyListener)
            }
        }

        override fun didError(message: String) {
            dialog?.dismiss()
            Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()
        }
    }

    private val copyListener: CopyListener = object : CopyListener {
        override fun onCopyClicked(text: String) {
            val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("copied_data", text)
            clipboard.setPrimaryClip(clip)
            //Toast.makeText(this@MainActivity, "Quote Copied to Clipboard!", Toast.LENGTH_LONG).show()
        }
    }
}
