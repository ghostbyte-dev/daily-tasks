package com.daniebeler.dailytasks.di

import com.daniebeler.dailytasks.db.Task
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

sealed class TaskItem {
    abstract val stableId: Any

    data class SavedTask(val task: Task, override val stableId: Any = task.id) : TaskItem() {}

    data class PlaceholderTask @OptIn(ExperimentalUuidApi::class) constructor(
        val name: String,
        override val stableId: Any = "draft_${Uuid.random()}"
    ) : TaskItem()
}