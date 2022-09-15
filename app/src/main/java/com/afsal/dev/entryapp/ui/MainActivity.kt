package com.afsal.dev.entryapp.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.afsal.dev.entryapp.adapters.ListAdapter
import com.afsal.dev.entryapp.databinding.ActivityMainBinding
import com.afsal.dev.entryapp.viewModel.MainViewModel
import com.afsal.dev.entryapp.viewModel.MainViewModel.Companion.LEFT_LIST
import com.afsal.dev.entryapp.viewModel.MainViewModel.Companion.RIGHT_LIST

class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"

    // private val viewModel:MainViewModel by viewModels()
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var leftAdapter: ListAdapter
    private lateinit var rightAdapter: ListAdapter
    private val selected_from_right = mutableListOf<String>()
    private val selected_from_left = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]



        leftAdapter = ListAdapter() { checkedItem,ischecked ->



            if (ischecked) selected_from_left.add(checkedItem) else selected_from_left.remove(checkedItem)
              Log.d("KKK","Selectd left list ${selected_from_left}")

        }





        rightAdapter = ListAdapter() {itme,ischecked->


             if (ischecked)selected_from_right.add(itme) else  selected_from_right.remove(itme)
            Log.d("KKK","Selectd right list ${selected_from_right}")
        }

        setRecyclerView()



        binding.btAdd.setOnClickListener {
            addToList()
        }

        binding.btDelete.setOnClickListener {
            deleteElement()
        }
        binding.buttonCoppyLeft.setOnClickListener {

            if (selected_from_right.isNotEmpty()){ viewModel.coppyToList(selected_from_right, LEFT_LIST)}

            clearSelectedList()


        }

        binding.coppyRight.setOnClickListener {

            if (selected_from_left.isNotEmpty()) { viewModel.coppyToList(selected_from_left, RIGHT_LIST)

            }
            clearSelectedList()

        }


        binding.buttonMoveRight.setOnClickListener {
            //selected_from_left
            if (selected_from_left.isNotEmpty()) {

                viewModel.moveToList(selected_from_left, RIGHT_LIST)


            }
            clearSelectedList()
        }

        binding.buttonMoveLeft.setOnClickListener {

            if (selected_from_right.isNotEmpty()) {
                viewModel.moveToList(selected_from_right, LEFT_LIST)
                clearSelectedList()
            }
        }

        binding.buttonSwap.setOnClickListener {
            viewModel.swapLists()


            clearSelectedList()
        }


        viewModel.rightList.observe(this, Observer {

            Log.d(TAG, "Elements in rightList ${it.toString()}")
            rightAdapter.differ.submitList(it.toList())
        })

        viewModel.leftList.observe(this, Observer {
            Log.d(TAG, "Elements in leftList ${it.toString()}")
            leftAdapter.differ.submitList(it.toList())
        })


    }


    private fun clearSelectedList() {

        selected_from_right.clear()
        selected_from_left.clear()
    }

    private fun setRecyclerView() {
        binding.leftRv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = leftAdapter
        }
        binding.rightRv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = rightAdapter
        }
    }


    private fun addToList() {
        val element = binding.editTextTextPersonName.text.toString()

        if (element.isNotEmpty()) {
            viewModel.addElementToList(element)
            Log.d("DDD", "element to add $element")
        }

        binding.editTextTextPersonName.text.clear()
    }


    private fun deleteElement() {
        val element = binding.editTextTextPersonName.text.toString()

        if (element.isNotEmpty()) {
            viewModel.removeFromLeft(element)
            viewModel.removeFromRight(element)

        }

        binding.editTextTextPersonName.text.clear()
    }
}