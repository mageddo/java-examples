$(function () {
  const $dirBadge = $('#dirBadge');
  const $crumbs = $('#crumbs');
  const $errorAlert = $('#errorAlert');
  const $dirForm = $('#dirForm');
  const $dirInput = $('#dirInput');
  const $content = $('#content');

  const dir = GTR.param('dir');

  if (!dir) {
    window.location.href = '/index.html';
    return;
  }

  GTR.saveDir(dir);
  $dirInput.val(dir);
  $dirBadge.text(dir);
  renderCrumbs();
  loadProjects();

  $dirForm.on('submit', function (event) {
    event.preventDefault();
    const newDir = $dirInput.val().trim();
    if (!newDir) {
      return;
    }
    GTR.saveDir(newDir);
    window.location.href = `/projects.html?dir=${encodeURIComponent(newDir)}`;
  });

  function renderCrumbs() {
    $crumbs.html(`
      <a href="/index.html">Diretório</a>
      <span class="sep"><i class="bi bi-chevron-right"></i></span>
      <span>${GTR.esc(dir)}</span>
    `);
  }

  function loadProjects() {
    showLoading();
    GTR.getJson(`/api/projects?dir=${encodeURIComponent(dir)}`, renderProjects, showError);
  }

  function showLoading() {
    $content.html(`
      <div class="spinner-wrap">
        <div class="spinner-border spinner-border-sm" role="status"></div>
        Buscando projetos Gradle…
      </div>
    `);
  }

  function renderProjects(projects) {
    if (!projects || projects.length === 0) {
      $content.html('<div class="empty-state">Nenhum projeto Gradle encontrado neste diretório.</div>');
      return;
    }
    const tiles = projects.map(toTile).join('');
    $content.html(`
      <div class="mb-3 chip dark">${projects.length} projeto(s)</div>
      <div class="d-flex flex-column gap-2">${tiles}</div>
    `);
  }

  function toTile(project) {
    const href = `/results.html?dir=${encodeURIComponent(dir)}&project=${encodeURIComponent(project.path)}`;
    return `
      <a class="list-tile" href="${href}">
        <span>
          <span class="tile-title"><i class="bi bi-folder2-open me-2"></i>${GTR.esc(project.name)}</span>
          <div class="tile-sub">${GTR.esc(project.path)}</div>
        </span>
        <i class="bi bi-chevron-right fs-5 text-muted"></i>
      </a>
    `;
  }

  function showError(message) {
    $content.empty();
    $errorAlert.text(message).removeClass('d-none');
  }
});
