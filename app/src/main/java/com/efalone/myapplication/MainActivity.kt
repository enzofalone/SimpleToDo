package com.efalone.myapplication

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener {
            override fun onItemLongClicked(position: Int) {
                //remove item from the list
                listOfTasks.removeAt(position)
                //notify adapter
                adapter.notifyDataSetChanged()
                //save data in save file
                saveItems()
            }
        }

        //Load all the data beforehand
        loadItems()

        //Look for recyclerView in layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        //Create adapter and pass data in constructor
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)
        //Attach adapter to recyclerview
        recyclerView.adapter = adapter

        //Set layout manager to position items
        recyclerView.layoutManager = LinearLayoutManager(this)

        //Button setup
        findViewById<Button>(R.id.button).setOnClickListener {
            val inputTextField = findViewById<EditText>(R.id.addTaskField)

            //get text from @+id/addTaskField
            val userInputtedTask = inputTextField.text.toString()

            //add string from addTaskField and enter in listOfTasks
            listOfTasks.add(userInputtedTask)

            //notify the adaptor there is new data
            adapter.notifyItemInserted(listOfTasks.size - 1)

            //reset @+id/addTaskField by leaving an empty string
            inputTextField.setText("")

            //save task to save file
            saveItems()
        }


    }

    //get the file we need
    fun getDataFile() : File {
        //every line of this file represents a task
        return File(filesDir, "data.txt")
    }

    //load the items by reading every line in our save file
    fun loadItems() {
        try {
            listOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch(ioException: IOException) {
            ioException.printStackTrace()
        }
    }

    //save items by writing them into our save file
    fun saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), listOfTasks)
        } catch(ioException: IOException) {
            ioException.printStackTrace()
        }
    }
}