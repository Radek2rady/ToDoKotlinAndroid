package com.example.todoapplication

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import com.example.todoapplication.databinding.ActivityMainBinding
import io.realm.Realm

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.fab.setOnClickListener {
            var addIntent = Intent(this, AddToDoActivity::class.java)
            startActivity(addIntent)
        }

    }

    override fun onResume() {
        super.onResume()

        val realm = Realm.getDefaultInstance()

        val query = realm.where(ToDoItem::class.java)
        val results = query.findAll()

        val listView = findViewById<ListView>(R.id.toDoListView)

        listView.setOnItemClickListener { parent, view, i, l ->
            val selectedToDo = results[i]
            val finishIntent = Intent(this, FinishToDoActivity::class.java)
            finishIntent.putExtra("toDoItem", selectedToDo?.getId())
            startActivity(finishIntent)
        }

        val adapter = ToDoAdapter(this, android.R.layout.simple_list_item_1, results)

        listView.adapter = adapter
    }

    class ToDoAdapter(context: Context, resource: Int, objects: MutableList<ToDoItem>) :
        ArrayAdapter<ToDoItem>(context, resource, objects) {
        override fun getCount(): Int {
            return super.getCount()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val inflator =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val toDoView =
                inflator.inflate(android.R.layout.simple_list_item_1, parent, false) as TextView

            val toDoItem = getItem(position)

            toDoView.text = toDoItem?.name

            if (toDoItem != null) {
                if (toDoItem.important) {
                    toDoView.setTypeface(Typeface.DEFAULT_BOLD)
                }
            }

            return toDoView
        }
    }
}
