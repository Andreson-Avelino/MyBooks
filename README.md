📚 MyBooks:
MyBooks é um aplicativo Android desenvolvido em Kotlin para organizar livros lidos, consultar informações de obras e manter uma lista de favoritos. O projeto foi desenvolvido com finalidade acadêmica para o curso de Análise e Desenvolvimento de Sistemas da ULBRA.

Funcionalidades:
- Cadastrar livros informando título, autor e sinopse.
- Consultar a lista de livros cadastrados e visualizar seus detalhes.
- Editar informações de livros já cadastrados.
- Excluir livros do catálogo pessoal.
- Marcar e desmarcar livros como favoritos e consultar a lista de favoritos.
- Pesquisar obras pela Google Books API e preencher automaticamente os dados encontrados.
- Consultar e gerenciar os livros armazenados localmente sem conexão com a internet. A busca online exige conexão.

Tecnologias utilizadas:
Kotlin ->	Desenvolvimento da aplicação Android
Android Studio / Gradle ->	Ambiente de desenvolvimento e compilação
XML ->	Definição das interfaces
SQLite / SQLiteOpenHelper ->	Persistência local dos livros
LivroDAO -> Operações de acesso ao banco de dados
Retrofit e Gson ->	Requisições à Google Books API e processamento de JSON
RecyclerView -> Exibição das listas de livros e resultados de busca
Glide	-> Carregamento de imagens

Compatibilidade definida no projeto: Android 8.0 (API 26) ou superior (minSdk = 26). O projeto utiliza compileSdk e targetSdk 37.

Instalação do aplicativo (APK):
1. Acesse a versão pública v1.0.0.
2. Na seção Assets, baixe o arquivo APK disponibilizado.
3. Abra o arquivo no dispositivo Android.
4. Se o Android solicitar, autorize a instalação de aplicativos dessa fonte.
5. Conclua a instalação e abra o MyBooks.
Não é necessário criar conta ou realizar login para usar o aplicativo. A instalação deve ser feita apenas a partir da página oficial do projeto.

Executar o código-fonte no Android Studio:
1. Instale o Android Studio e os componentes do SDK Android exigidos pelo projeto.
2. Clone o repositório:
   git clone https://github.com/Andreson-Avelino/MyBooks.git
3. Abra a pasta MyBooks no Android Studio e aguarde a sincronização do Gradle.
4. Para habilitar a pesquisa online, configure no arquivo local.properties, na raiz do projeto, a propriedade:
   GOOGLE_BOOKS_API_KEY=SUA_CHAVE_DA_GOOGLE_BOOKS_API
5. Execute o aplicativo em um emulador ou dispositivo Android compatível.
Segurança: não publique sua chave de API ou o arquivo local.properties no GitHub. O cadastro e a consulta de livros locais não dependem da pesquisa online.

Banco de dados:
O aplicativo utiliza o banco SQLite mybooks.db, criado automaticamente na primeira execução pela classe DatabaseHelper. A versão do banco no código analisado é 1.
Estrutura da tabela principal:
CREATE TABLE livros (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    titulo TEXT NOT NULL,
    autor TEXT NOT NULL,
    sinopse TEXT NOT NULL,
    favorito INTEGER NOT NULL DEFAULT 0
);

- id: identificador do livro.
- titulo: título da obra.
- autor: nome do autor ou autores.
- sinopse: descrição do livro.
- favorito: indica se o livro está nos favoritos (0 ou 1).

Observação: a rotina atual de atualização do banco (onUpgrade) exclui e recria a tabela livros; portanto, uma futura mudança de versão pode apagar os registros locais se não for implementada uma migração de dados.

Links:
- Código-fonte: https://github.com/Andreson-Avelino/MyBooks
- Versão para instalação: https://github.com/Andreson-Avelino/MyBooks/releases/tag/v1.0.0

Finalidade acadêmica:
Projeto de implementação de aplicativo Android desenvolvido para fins acadêmicos no curso de Análise e Desenvolvimento de Sistemas — ULBRA.
