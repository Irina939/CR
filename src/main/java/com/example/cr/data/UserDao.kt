package com.example.cr.data

import androidx.room.*

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun getUserById(id: String): User?

    @Query("DELETE FROM users")
    suspend fun deleteAll()

    @Query("UPDATE users SET profileImageUri = :profileImageUri WHERE id = :userId")
    suspend fun updateProfileImage(userId: String, profileImageUri: String)

    @Query("UPDATE users SET points = :points WHERE id = :userId")
    suspend fun updatePoints(userId: String, points: Int)

    @Query("SELECT points FROM users WHERE id = :userId LIMIT 1")
    suspend fun getPointsById(userId: String): Int?
} 