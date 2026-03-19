package com.example.myapplication

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.compose.ui.graphics.Color

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "noblesse_db", null, 2) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS products (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                brand TEXT NOT NULL,
                price TEXT NOT NULL,
                category INTEGER NOT NULL,
                photoTopColor INTEGER NOT NULL,
                photoBottomColor INTEGER NOT NULL,
                photoUri TEXT
            )
        """)
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                fullName TEXT NOT NULL,
                email TEXT NOT NULL UNIQUE,
                phone TEXT NOT NULL UNIQUE,
                password TEXT NOT NULL,
                dateOfBirth TEXT
            )
        """)
        // Insert data awal produk
        perfumeList.forEach { insertProduct(db, it) }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS products")
        db.execSQL("DROP TABLE IF EXISTS users")
        onCreate(db)
    }

    // ── Products ──────────────────────────────────

    private fun insertProduct(db: SQLiteDatabase, item: PerfumeItem) {
        val cv = ContentValues().apply {
            put("name", item.name)
            put("brand", item.brand)
            put("price", item.price)
            put("category", item.category)
            put("photoTopColor", item.photoTop.value.toLong())
            put("photoBottomColor", item.photoBottom.value.toLong())
            put("photoUri", item.photoUri)
        }
        db.insert("products", null, cv)
    }

    fun addProduct(item: PerfumeItem) {
        insertProduct(writableDatabase, item)
    }

    fun deleteProduct(id: Int) {
        writableDatabase.delete("products", "id = ?", arrayOf(id.toString()))
    }

    fun getAllProducts(): List<PerfumeItem> {
        val list = mutableListOf<PerfumeItem>()
        val cursor = readableDatabase.rawQuery("SELECT * FROM products ORDER BY id ASC", null)
        cursor.use {
            while (it.moveToNext()) {
                val name = it.getString(it.getColumnIndexOrThrow("name"))
                val photoResId = perfumeList.find { p -> p.name == name }?.photoResId
                list.add(
                    PerfumeItem(
                        id = it.getInt(it.getColumnIndexOrThrow("id")),
                        name = name,
                        brand = it.getString(it.getColumnIndexOrThrow("brand")),
                        price = it.getString(it.getColumnIndexOrThrow("price")),
                        category = it.getInt(it.getColumnIndexOrThrow("category")),
                        photoTop = Color(it.getLong(it.getColumnIndexOrThrow("photoTopColor")).toULong()),
                        photoBottom = Color(it.getLong(it.getColumnIndexOrThrow("photoBottomColor")).toULong()),
                        photoUri = it.getString(it.getColumnIndexOrThrow("photoUri")),
                        photoResId = photoResId
                    )
                )
            }
        }
        return list
    }

    fun productCount(): Int {
        val cursor = readableDatabase.rawQuery("SELECT COUNT(*) FROM products", null)
        return cursor.use { if (it.moveToFirst()) it.getInt(0) else 0 }
    }

    // ── Users ─────────────────────────────────────

    fun registerUser(user: UserData): Boolean {
        return try {
            val cv = ContentValues().apply {
                put("fullName", user.fullName)
                put("email", user.email)
                put("phone", user.phone)
                put("password", user.password)
                put("dateOfBirth", user.dateOfBirth)
            }
            writableDatabase.insertOrThrow("users", null, cv)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun findUser(credential: String): UserData? {
        val cursor = readableDatabase.rawQuery(
            "SELECT * FROM users WHERE email = ? OR phone = ? LIMIT 1",
            arrayOf(credential, credential)
        )
        return cursor.use {
            if (it.moveToFirst()) {
                UserData(
                    id = it.getInt(it.getColumnIndexOrThrow("id")),
                    fullName = it.getString(it.getColumnIndexOrThrow("fullName")),
                    email = it.getString(it.getColumnIndexOrThrow("email")),
                    phone = it.getString(it.getColumnIndexOrThrow("phone")),
                    password = it.getString(it.getColumnIndexOrThrow("password")),
                    dateOfBirth = it.getString(it.getColumnIndexOrThrow("dateOfBirth")) ?: ""
                )
            } else null
        }
    }

    fun emailExists(email: String): Boolean {
        val cursor = readableDatabase.rawQuery(
            "SELECT COUNT(*) FROM users WHERE email = ?", arrayOf(email)
        )
        return cursor.use { it.moveToFirst() && it.getInt(0) > 0 }
    }

    fun phoneExists(phone: String): Boolean {
        val cursor = readableDatabase.rawQuery(
            "SELECT COUNT(*) FROM users WHERE phone = ?", arrayOf(phone)
        )
        return cursor.use { it.moveToFirst() && it.getInt(0) > 0 }
    }
}

data class UserData(
    val id: Int = 0,
    val fullName: String,
    val email: String,
    val phone: String,
    val password: String,
    val dateOfBirth: String = ""
)
