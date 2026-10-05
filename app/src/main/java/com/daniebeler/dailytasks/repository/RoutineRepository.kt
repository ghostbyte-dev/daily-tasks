package com.daniebeler.dailytasks.repository

import com.daniebeler.dailytasks.db.Routine
import com.daniebeler.dailytasks.db.RoutineDao
import javax.inject.Inject

class RoutineRepository @Inject constructor(
    private val dao: RoutineDao
) {
    suspend fun getAll() = dao.getAll()
    suspend fun add(routine: Routine) = dao.insert(routine)
    suspend fun update(id: Long, name: String, intervalDays: Int) =
        dao.update(id, name, intervalDays)

    suspend fun markGenerated(id: Long, day: Long) = dao.markGenerated(id, day)
    suspend fun delete(id: Long) = dao.delete(id)
}