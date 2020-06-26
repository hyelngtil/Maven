package com.jwhh.notekeeper

object DataManager {
    val courses = HashMap<String, CourseInfo>()
    val notes = ArrayList<NoteInfo>()

    init {
        initializeCourses()
        initializeNotes()
    }
    fun addNote(course: CourseInfo, noteTitle: String, noteText: String): Int {
        val note = NoteInfo(course, noteTitle, noteText)
        notes.add(note)
        return notes.lastIndex
    }
    fun findNote(course: CourseInfo, noteTitle: String, noteText: String): NoteInfo? {
        for (note in notes)
            if (course == note.course &&
                    noteTitle == note.title &&
                    noteText == note.text)
                return note
        return null
    }

    private fun initializeCourses() {
        var course = CourseInfo("android_intents","Android Programming with Intents")
        courses[course.courseId] = course

        course = CourseInfo(courseId = "android_async", title =  "Android Async Programming and Services")
        courses[course.courseId] = course

        course = CourseInfo(title = "Kotlin Fundamentals: The Kotlin Language", courseId = "kotlin_lang")
        courses[course.courseId] = course

        course = CourseInfo("kotlin_core", "Kotlin Fundamentals: The New Platform")
        courses[course.courseId] = course
    }
    fun initializeNotes() {
        notes.add(NoteInfo(courses["android_intents"]!!, "Android Programming with Intents",
            "Delegating intent: Pending intents are powerful; they delegate much more than component invocation"))

        notes.add(NoteInfo(courses["android_intents"]!!, "Android Programming with Intents",
            "Dynamic intent resolution: Intents allow components to be resolved at run time"))

        notes.add(NoteInfo(courses["android_async"]!!, "Android Async Programming and Services",
        "Long running operations: Foreground services can be tied to a notification icon"))

        notes.add(NoteInfo(courses["android_async"]!!, "Android Async Programming and Services",
            "Service default threads: Did you know that android service will tie up the UI threads by default?"))

        notes.add(NoteInfo(courses["kotlin_lang"]!!, "Kotlin Fundamentals",
        "The kotlin language: Modern programming language for android"))

        notes.add(NoteInfo(courses["kotlin_lang"]!!, "Kotlin Fundamentals",
            "The kotlin language: Easier to use syntax and type safe properties"))

        notes.add(NoteInfo(courses["kotlin_core"]!!, "Kotlin Fundamentals",
            "The new platform: 100% interoperable with java(the core platform)"))

        notes.add(NoteInfo(courses["kotlin_core"]!!, "Kotlin Fundamentals",
            "The new platform: more efficient coding and enhanced creativity"))
    }
}
