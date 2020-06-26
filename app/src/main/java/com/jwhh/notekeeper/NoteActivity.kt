package com.jwhh.notekeeper

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.content_note.*
import kotlinx.android.synthetic.main.content_note_list.*


class NoteActivity : AppCompatActivity() {
    private val tag = this::class.simpleName
    private var notePosition = POSITION_NOT_SET

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)
        setSupportActionBar(findViewById(R.id.toolbar))

        // Populating spinner

        val adapterCourses = ArrayAdapter(this,
            android.R.layout.simple_spinner_item,
            DataManager.courses.values.toList())
        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerCourses.adapter = adapterCourses

        // Gets the index of selected course and call the function to display
        notePosition = savedInstanceState?.getInt(NOTE_POSITION, POSITION_NOT_SET) ?:
            intent.getIntExtra(NOTE_POSITION, POSITION_NOT_SET)

        if (notePosition != POSITION_NOT_SET)
            displayNote()
        else {
            createNewNote()
        }
        Log.d(tag, "onCreate")
    }

    private fun displayNote() {
        if (notePosition > DataManager.notes.lastIndex) {
            Log.e(tag, "Invalid note position $notePosition, valid note position is ${DataManager.notes.lastIndex}")
            return
        }
        Log.i(tag, "Displaying note for the position $notePosition")

        val note = DataManager.notes[notePosition]
        textNoteTitle.setText(note.title)
        textNoteText.setText(note.text)

        // To set the index of course to be displayed
        val coursePosition = DataManager.courses.values.indexOf(note.course)
        spinnerCourses.setSelection(coursePosition)
    }

    private fun createNewNote() {
        DataManager.notes.add(NoteInfo())
        notePosition = DataManager.notes.lastIndex
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(NOTE_POSITION, notePosition)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_send_mail -> {
                sendEmail()
                true
            }
            R.id.action_next -> {
                if (notePosition < DataManager.notes.lastIndex) {
                    moveNext()
                }
                true
            }
            R.id.action_cancel -> {
                isDestroyed
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun sendEmail() {
        // val course = spinnerCourses.onItemSelectedListener
        val subject: String = textNoteTitle.text.toString()
        val text = """
            Checkout what I learned in the Plural sight course 
            Title: ${textNoteTitle.text}
            Text: ${textNoteText.text}
            """.trimIndent()
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "message/rfc2822"
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        intent.putExtra(Intent.EXTRA_TEXT, text)
        startActivity(intent)
    }

    private fun moveNext() {
        ++notePosition
        displayNote()
        invalidateOptionsMenu()
    }

    // Handles edge cases for previous and next menuItems
    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if (notePosition >= DataManager.notes.lastIndex) {
            val menuItem = menu?.findItem(R.id.action_next)
            if (menuItem != null) {
                menuItem.icon = getDrawable(R.drawable.ic_baseline_block_24)
                menuItem.isEnabled = false
            }
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onPause() {
        super.onPause()
        if (isDestroyed) {
            if (saveNote())
                DataManager.notes.removeAt(DataManager.notes.lastIndex)
        } else
            saveNote()
        Log.d(tag, "onPause")
    }

    private fun saveNote(): Boolean {
        val note = DataManager.notes[notePosition]
        note.title = textNoteTitle.text.toString()
        note.text = textNoteText.text.toString()
        note.course = spinnerCourses.selectedItem as CourseInfo
        return true
    }
}