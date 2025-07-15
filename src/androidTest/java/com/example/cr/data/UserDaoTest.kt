package com.example.cr.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var userDao: UserDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        ).build()
        userDao = database.userDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertAndGetUserById() = runBlocking {
        val user = User(id = "1", name = "Test", email = "test@example.com", password = "1234")
        userDao.insert(user)
        val loaded = userDao.getUserById("1")
        Assert.assertEquals(user, loaded)
    }

    @Test
    fun insertAndGetUserByEmail() = runBlocking {
        val user = User(id = "2", name = "Another", email = "another@example.com", password = "5678")
        userDao.insert(user)
        val loaded = userDao.getUserByEmail("another@example.com")
        Assert.assertEquals(user, loaded)
    }

    @Test
    fun deleteAllUsers() = runBlocking {
        val user = User(id = "3", name = "DeleteMe", email = "deleteme@example.com", password = "pass")
        userDao.insert(user)
        userDao.deleteAll()
        val loaded = userDao.getUserById("3")
        Assert.assertNull(loaded)
    }
} 