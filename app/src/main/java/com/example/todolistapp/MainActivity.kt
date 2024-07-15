package com.example.todolistapp

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var taskInput: EditText
    private lateinit var addTaskButton: Button
    private lateinit var filterSpinner: Spinner
    private lateinit var taskListView: ListView
    private lateinit var editTaskInput: EditText
    private lateinit var saveTaskButton: Button
    private lateinit var deleteTaskButton: Button
    private lateinit var prioritySpinner: Spinner
    private lateinit var tasks: ArrayList<Task>
    private lateinit var tasksAdapter: ArrayAdapter<String>
    private var selectedTaskIndex: Int = -1

    data class Task(var description: String, var priority: String)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        taskInput = findViewById(R.id.task_input)
        addTaskButton = findViewById(R.id.add_task_button)
        filterSpinner = findViewById(R.id.filter_spinner)
        taskListView = findViewById(R.id.task_list_view)
        editTaskInput = findViewById(R.id.edit_task_input)
        saveTaskButton = findViewById(R.id.save_task_button)
        deleteTaskButton = findViewById(R.id.delete_task_button)
        prioritySpinner = findViewById(R.id.priority_spinner)

        tasks = ArrayList()
        tasksAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, tasks.map { it.descriptionWithPriority() })
        taskListView.adapter = tasksAdapter

        setupAddTaskButton()
        setupSpinner()
        setupListView()
        setupSaveTaskButton()
        setupDeleteTaskButton()
    }

    private fun setupAddTaskButton() {
        addTaskButton.setOnClickListener {
            val taskDescription = taskInput.text.toString()
            val taskPriority = prioritySpinner.selectedItem.toString()
            if (taskDescription.isNotEmpty()) {
                val task = Task(taskDescription, taskPriority)
                tasks.add(task)
                updateTaskList()
                taskInput.text.clear()
            }
        }
    }

    private fun setupSpinner() {
        val spinnerAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.filter_options,
            android.R.layout.simple_spinner_item
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        filterSpinner.adapter = spinnerAdapter

        filterSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                // Handle filter logic here
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setupListView() {
        taskListView.setOnItemClickListener { parent, view, position, id ->
            selectedTaskIndex = position
            val task = tasks[position]
            editTaskInput.setText(task.description)
            editTaskInput.visibility = View.VISIBLE
            saveTaskButton.visibility = View.VISIBLE
            deleteTaskButton.visibility = View.VISIBLE
        }

        taskListView.setOnItemLongClickListener { parent, view, position, id ->
            tasks.removeAt(position)
            updateTaskList()
            true
        }
    }

    private fun setupSaveTaskButton() {
        saveTaskButton.setOnClickListener {
            val editedTaskDescription = editTaskInput.text.toString()
            val editedTaskPriority = prioritySpinner.selectedItem.toString()
            if (editedTaskDescription.isNotEmpty() && selectedTaskIndex != -1) {
                tasks[selectedTaskIndex].description = editedTaskDescription
                tasks[selectedTaskIndex].priority = editedTaskPriority
                updateTaskList()
                clearEditFields()
            }
        }
    }

    private fun setupDeleteTaskButton() {
        deleteTaskButton.setOnClickListener {
            if (selectedTaskIndex != -1) {
                tasks.removeAt(selectedTaskIndex)
                updateTaskList()
                clearEditFields()
            }
        }
    }

    private fun updateTaskList() {
        tasksAdapter.clear()
        tasksAdapter.addAll(tasks.map { it.descriptionWithPriority() })
        tasksAdapter.notifyDataSetChanged()
    }

    private fun clearEditFields() {
        editTaskInput.text.clear()
        editTaskInput.visibility = View.GONE
        saveTaskButton.visibility = View.GONE
        deleteTaskButton.visibility = View.GONE
        selectedTaskIndex = -1
    }

    private fun Task.descriptionWithPriority(): String {
        return "$description [$priority]"
    }
}
