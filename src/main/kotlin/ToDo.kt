package org.example

import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement

private val todoDir: File by lazy {
    val dir = File(System.getProperty("user.home"), ".todo")
    if (!dir.exists()) {
        dir.mkdirs()
    }
    dir
}

private val dbFile: File by lazy {
    File(todoDir, "todo.db")
}

private val DATABASE_URL = "jdbc:sqlite:${dbFile.absolutePath}"

private val connection: Connection by lazy { 
    DriverManager.getConnection(DATABASE_URL) 
}


private object TextFormat {
    const val RESET = "\u001B[0m"
    const val STRIKETHROUGH = "\u001B[9m"
    
    const val GREEN = "\u001B[32m"
    const val YELLOW = "\u001B[33m"
    const val RED = "\u001B[31m"
    const val CYAN = "\u001B[36m"
    const val GRAY = "\u001B[90m"
}


private fun strikethrough(text: String): String {
    val ansiMethod = "${TextFormat.STRIKETHROUGH}${TextFormat.GRAY}$text${TextFormat.RESET}"
    
    val unicodeMethod = text.map { "$it\u0336" }.joinToString("")
    
    return "${TextFormat.GRAY}$unicodeMethod${TextFormat.RESET}"
}

class ToDo {
    private val statement: Statement = connection.createStatement()

    init {
        statement.executeUpdate("""
            CREATE TABLE IF NOT EXISTS ToDo (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                done BOOLEAN DEFAULT FALSE
            )
        """)
    }

    fun create(name: String) {
        if (name.isNotBlank()) {
            connection.prepareStatement("INSERT INTO ToDo (name, done) VALUES (?, ?)").use { stmt ->
                stmt.setString(1, name)
                stmt.setBoolean(2, false)
                stmt.executeUpdate()
            }

            read()
        } else {
            println("${TextFormat.RED}Erro: Nenhum nome inserido!${TextFormat.RESET}")
        }
    }

    fun read(id: String = "") {
        val whereClause = if (id.isNotBlank()) "WHERE id = ?" else ""
        val query = "SELECT * FROM ToDo $whereClause"
        
        connection.prepareStatement(query).use { stmt ->
            if (id.isNotBlank()) {
                stmt.setInt(1, id.toIntOrNull() ?: 0)
            }
            
            val result = stmt.executeQuery()
            var found = false
            
            while (result.next()) {
                found = true

                val id = result.getString("id")
                val name = result.getString("name")
                val done = result.getBoolean("done")

                val checkbox = if (done) "${TextFormat.GREEN}[x]${TextFormat.RESET}" else "${TextFormat.YELLOW}[ ]${TextFormat.RESET}"
                
                val displayName = if (done) strikethrough(name) else "${TextFormat.CYAN}$name${TextFormat.RESET}"

                println("${TextFormat.CYAN}$id${TextFormat.RESET} ${TextFormat.GRAY}|${TextFormat.RESET} $checkbox $displayName")
            }
            
            if (id.isNotBlank() && !found) {
                println("Item ID $id não encontrado.")
            }
        }
    }

    fun update(id: String, name: String = "", done: Boolean? = null) {
        try {
            val idInt = id.toIntOrNull()
            
            if (idInt == null) {
                println("ID inválido: $id. Deve ser um número.")
                return
            }
            
            var updated = false
            
            if (done != null) {
                connection.prepareStatement("UPDATE ToDo SET done = ? WHERE id = ?").use { stmt ->
                    stmt.setBoolean(1, done)
                    stmt.setInt(2, idInt)

                    val rowsAffected = stmt.executeUpdate()
                    
                    if (rowsAffected > 0) {
                        updated = true
                    }
                }
            }
            
            if (name.isNotBlank()) {
                connection.prepareStatement("UPDATE ToDo SET name = ? WHERE id = ?").use { stmt ->
                    stmt.setString(1, name)
                    stmt.setInt(2, idInt)

                    val rowsAffected = stmt.executeUpdate()
                    
                    if (rowsAffected > 0) {
                        updated = true
                    }
                }
            }
            
            if (!updated) {
                println("Item $id não encontrado.")
                return
            }
            
            if (updated) {
                read(id)
            }
        } catch (e: Exception) {
            println("Erro ao atualizar Item: ${e.message}")
        }
    }

    fun delete(id: String) {
        try {
            val idInt = id.toIntOrNull()

            if (idInt == null) {
                println("ID inválido: $id. Deve ser um número.")
                return
            }
            
            connection.prepareStatement("DELETE FROM ToDo WHERE id = ?").use { stmt ->
                stmt.setInt(1, idInt)

                val rowsAffected = stmt.executeUpdate()
                
                if (rowsAffected > 0) {
                    read()
                } else {
                    println("Item $id não encontrado. \n")

                    read()
                }
            }
        } catch (e: Exception) {
            println("Erro ao deletar Item: ${e.message}")
        }
    }
    
    companion object {
        fun closeConnection() {
            if (!connection.isClosed) {
                connection.close()
            }
        }
    }
}