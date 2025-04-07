// Função para carregar categorias dinamicamente no menu
function carregarCategorias() {
    fetch('/categoria/listar')
        .then(response => response.json())
        .then(categorias => {
            const menu = document.getElementById('categorias-menu');
            menu.innerHTML = ''; // Limpa o menu antes de adicionar as categorias
            categorias.forEach(categoria => {
                const li = document.createElement('li');
                const link = document.createElement('a');
                link.href = `/cursos/categoria/${categoria.id}`;
                link.textContent = categoria.nome;
                li.appendChild(link);
                menu.appendChild(li);
            });
        })
        .catch(error => console.error('Erro ao carregar categorias:', error));
}

// Função para carregar cursos de uma categoria
function carregarCursosPorCategoria(url) {
    const cursosLista = document.getElementById('cursos-lista');
    fetch(url)
        .then(response => response.json())
        .then(cursos => {
            cursosLista.innerHTML = ''; // Limpa a lista de cursos antes de adicionar novos
            cursos.forEach(curso => {
                const div = document.createElement('div');
                div.className = 'curso';
                div.innerHTML = `
                    <img src="${curso.imagem}" alt="${curso.nome}">
                    <h3>${curso.nome}</h3>
                    <p>${curso.descricao}</p>
                    <a href="/cursos/detalhes/${curso.id}">Ver mais</a>
                `;
                cursosLista.appendChild(div);
            });
        })
        .catch(error => console.error('Erro ao carregar cursos:', error));
}

// Adiciona eventos ao menu de categorias
function configurarMenuCategorias() {
    const menu = document.getElementById('categorias-menu');
    menu.addEventListener('click', function (event) {
        if (event.target.tagName === 'A') {
            event.preventDefault();
            const url = event.target.href;
            carregarCursosPorCategoria(url);
        }
    });
}

// Inicializa o script ao carregar a página
document.addEventListener('DOMContentLoaded', function () {
    carregarCategorias();
    configurarMenuCategorias();
});