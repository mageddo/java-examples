$(function () {
  const $projectBadge = $('#projectBadge');
  const $crumbs = $('#crumbs');
  const $errorAlert = $('#errorAlert');
  const $content = $('#content');

  const dir = GTR.param('dir');
  const project = GTR.param('project');

  if (!project) {
    window.location.href = '/index.html';
    return;
  }

  if (dir) {
    GTR.saveDir(dir);
  }

  const projectName = GTR.basename(project);
  $projectBadge.text(project);
  renderCrumbs();
  loadResults();

  function renderCrumbs() {
    const projectsHref = `/projects.html?dir=${encodeURIComponent(dir)}`;
    $crumbs.html(`
      <a href="/index.html">Diretório</a>
      <span class="sep"><i class="bi bi-chevron-right"></i></span>
      <a href="${projectsHref}">Projetos</a>
      <span class="sep"><i class="bi bi-chevron-right"></i></span>
      <span>${GTR.esc(projectName)}</span>
    `);
  }

  function loadResults() {
    showLoading();
    GTR.getJson(`/api/results?project=${encodeURIComponent(project)}`, renderResults, showError);
  }

  function showLoading() {
    $content.html(`
      <div class="spinner-wrap">
        <div class="spinner-border spinner-border-sm" role="status"></div>
        Lendo resultados de teste…
      </div>
    `);
  }

  function renderResults(resultSets) {
    if (!resultSets || resultSets.length === 0) {
      $content.html('<div class="empty-state">Nenhum resultado de teste encontrado em <code>build/test-results</code>.</div>');
      return;
    }
    const tiles = resultSets.map(toTile).join('');
    $content.html(`
      <div class="mb-3 chip dark">${resultSets.length} suíte(s)</div>
      <div class="d-flex flex-column gap-2">${tiles}</div>
    `);
  }

  function toTile(resultSet) {
    const href = `/report.html?dir=${encodeURIComponent(dir)}`
      + `&project=${encodeURIComponent(project)}`
      + `&result=${encodeURIComponent(resultSet.name)}`;
    return `
      <a class="list-tile flex-column flex-md-row align-items-md-center" href="${href}">
        <span>
          <span class="tile-title"><i class="bi bi-clipboard-data me-2"></i>${GTR.esc(resultSet.name)}</span>
          <div class="tile-sub">${formatTime(resultSet.totalTimeSeconds)} · tempo total da suíte</div>
        </span>
        <span class="stat-row">
          ${stat('total', 'testes', resultSet.total)}
          ${stat('passed', 'passados', resultSet.passed)}
          ${stat('failed', 'com erro', resultSet.failed)}
          ${stat('skipped', 'ignorados', resultSet.skipped)}
        </span>
      </a>
    `;
  }

  function stat(kind, label, value) {
    return `
      <span class="stat ${kind}">
        <span class="stat-value">${value}</span>
        <span class="stat-label">${label}</span>
      </span>
    `;
  }

  function formatTime(seconds) {
    const value = seconds || 0;
    if (value < 60) {
      return `${value.toFixed(1)}s`;
    }
    return `${value.toFixed(1)}s (${(value / 60).toFixed(1)} min)`;
  }

  function showError(message) {
    $content.empty();
    $errorAlert.text(message).removeClass('d-none');
  }
});
