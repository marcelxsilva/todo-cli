# Simple ToDo List CLI in Kotlin

## Instalação Rápida

### Opção 1: Baixar o binário pré-compilado

Você pode baixar a versão mais recente da aplicação diretamente da página de [Releases](https://github.com/marcelxsilva/todo-cli/releases):

- Para Linux: Baixe `todo-linux.tar.gz`
- Para macOS: Baixe `todo-macos.tar.gz`

Após baixar, extraia o arquivo e siga estas etapas:
```bash
# Extrair o arquivo
tar -xzf todo-[seu-sistema].tar.gz

# Copiar todos os arquivos para um diretório no PATH
sudo cp -r todo/* /usr/local/bin/

# Reinicie o terminal e agora você pode usar o comando
todo --help
```

### Opção 2: Compilar do código-fonte

Se preferir compilar do zero, consulte as instruções de compilação abaixo.

- JDK 17 ou superior
- Gradle 7.0 ou superior


### Build

```bash
./gradlew clean build
```

O arquivo JAR será gerado em `build/libs/com.todo.list-1.0-SNAPSHOT.jar`.

### Instalação Manual

Se você compilou o código manualmente, siga estas etapas para instalá-lo:

1. Copie o JAR gerado para `/usr/local/bin`:

```bash
sudo cp build/libs/com.todo.list-*.jar /usr/local/bin/todo.jar
```

2. Crie um script de execução ou use um que já existe neste repositório:

```bash
### Criar o arquivo do script
cat > todo << 'EOF' 
#!/bin/sh
java --enable-native-access=ALL-UNNAMED -jar /usr/local/bin/todo.jar  "$@"
```

### Dar permissão de execução
```
chmod +x todo
```

### Mover para o PATH
```
sudo mv todo /usr/local/bin/
```


### Comandos Disponíveis

- `todo add <nome>` - Adiciona uma nova tarefa
- `todo ls [id]` - Lista todas as tarefas ou uma específica por ID
- `todo update <id> [--name <novo_nome>] [--done|--no-done] [--deny|--no-deny]` - Atualiza uma tarefa
- `todo rm <id>` - Remove uma tarefa

### Exemplos de Uso

```bash
# Adicionar uma nova tarefa
todo add "Beber agua"

# Listar todas as tarefas
todo ls

# Listar uma tarefa específica
todo ls 1

# Marcar uma tarefa como concluída
todo update 1 --done

# Marcar uma tarefa como pendente
todo update 1 --deny

# Atualizar o nome de uma tarefa
todo update 1 --name "Beber mais agua"

# Remover uma tarefa
todo rm 1
```

## Armazenamento de dados

O aplicativo armazena suas tarefas em um banco de dados SQLite localizado em:

```
~/.todo/todo.db
```

Onde `~` representa o diretório home do seu usuário. Este é um local padrão para armazenar dados de aplicativos em sistemas Unix-like (Linux e macOS).