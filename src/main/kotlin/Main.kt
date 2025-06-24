package org.example
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.options.*
import com.github.ajalt.clikt.parameters.arguments.*

class CreateCommand : CliktCommand(name = "add", help = "Criar um Item") {
    private val name by argument("name", help = "Nome do Item")

    override fun run() {
        val todo = ToDo()
        todo.create(name)
    }
}

class ListCommand : CliktCommand(name = "ls", help = "Listar Itens") {
    private val id by argument("id", help = "ID opcional do Item para busca específica").optional()

    override fun run() {
        val todo = ToDo()
        todo.read(id ?: "")
    }
}

class DeleteCommand : CliktCommand(name = "rm", help = "Deletar um Item") {
    private val id by argument("id", help = "ID do Item a ser deletado")

    override fun run() {
        val todo = ToDo()
        todo.delete(id)
    }
}

class UpdateCommand : CliktCommand(name = "update", help = "Atualizar um Item") {
    private val id by argument("id", help = "ID do Item a ser atualizado")
    private val name by option("--name", help = "Novo nome do Item")
    private val done by option("--done", help = "Definir como finalizado").flag("--no-done", default = false)
    private val deny by option("--deny", help = "Definir como pendente").flag("--no-deny", default = false)

    override fun run() {
        val todo = ToDo()
        
        val status = when {
            done -> true
            deny -> false
            else -> null
        }

        todo.update(id, name ?: "", status)
    }
}

class App : CliktCommand(
    name = "ToDo",
    help = "CLI de lista de tarefas (ToDo List)",
    printHelpOnEmptyArgs = true,
) {
    override fun run() = Unit
}


fun main(args: Array<String>) = App().subcommands(
    CreateCommand(),
    ListCommand(),
    DeleteCommand(),
    UpdateCommand()
).main(args).also {
    Runtime.getRuntime().addShutdownHook(Thread {
        ToDo.closeConnection()
    })
}
