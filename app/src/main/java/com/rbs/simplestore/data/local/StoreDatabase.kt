package com.rbs.simplestore.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rbs.simplestore.data.local.dao.StoreDao
import com.rbs.simplestore.data.local.entity.CartEntity
import com.rbs.simplestore.data.local.entity.UserEntity

@Database(entities = [CartEntity::class, UserEntity::class], version = 1, exportSchema = false)
abstract class StoreDatabase : RoomDatabase() {
    abstract fun storeDao(): StoreDao
}