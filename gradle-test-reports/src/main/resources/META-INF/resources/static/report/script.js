let MAX_SEC = 0;

function durFmt(value) {
  let pct = 0;
  if (MAX_SEC > 0) {
    pct = (value / MAX_SEC) * 100;
  }
  let text = '';
  if (value != null) {
    text = `${value.toFixed(3)}s`;
  }
  return `<div class="dur-cell"><span class="dur-value">${text}</span>`
    + `<div class="dur-bar flex-grow-1"><span style="width:${pct}%"></span></div></div>`;
}

function resFmt(value) {
  const colors = {passed: 'success', skipped: 'secondary', failed: 'danger'};
  let color = colors[value];
  if (!color) {
    color = 'danger';
  }
  return `<span class="badge bg-${color}">${value}</span>`;
}

$(function () {
  const $resultBadge = $('#resultBadge');
  const $crumbs = $('#crumbs');
  const $errorAlert = $('#errorAlert');
  const $summary = $('#summary');
  const $loading = $('#loading');
  const $tbl = $('#tbl');

  const dir = GTR.param('dir');
  const project = GTR.param('project');
  const result = GTR.param('result');

  if (!project || !result) {
    window.location.href = '/index.html';
    return;
  }

  if (dir) {
    GTR.saveDir(dir);
  }

  const projectName = GTR.basename(project);
  $resultBadge.text(`${projectName} · ${result}`);
  $.fn.bootstrapTable.defaults.iconsPrefix = 'bi';
  renderCrumbs();
  loadTests();

  function renderCrumbs() {
    const projectsHref = `/projects.html?dir=${encodeURIComponent(dir)}`;
    const resultsHref = `/results.html?dir=${encodeURIComponent(dir)}&project=${encodeURIComponent(project)}`;
    $crumbs.html(`
      <a href="/index.html">Diretório</a>
      <span class="sep"><i class="bi bi-chevron-right"></i></span>
      <a href="${projectsHref}">Projetos</a>
      <span class="sep"><i class="bi bi-chevron-right"></i></span>
      <a href="${resultsHref}">${GTR.esc(projectName)}</a>
      <span class="sep"><i class="bi bi-chevron-right"></i></span>
      <span>${GTR.esc(result)}</span>
    `);
  }

  function loadTests() {
    const url = `/api/tests?project=${encodeURIComponent(project)}&result=${encodeURIComponent(result)}`;
    GTR.getJson(url, renderTests, showError);
  }

  function renderTests(data) {
    $loading.addClass('d-none');
    if (!data || data.length === 0) {
      $summary.html('<div class="empty-state w-100">Nenhum teste encontrado para esta suíte.</div>');
      return;
    }
    MAX_SEC = Math.max.apply(null, data.map(function (row) {
      return row.sec || 0;
    }));
    renderSummary(data);
    $tbl.removeClass('d-none').bootstrapTable({data: data, iconsPrefix: 'bi'});
  }

  function renderSummary(data) {
    const totals = {total: data.length, passed: 0, failed: 0, skipped: 0, sec: 0};
    data.forEach(function (row) {
      totals.sec += row.sec || 0;
      if (totals[row.result] != null) {
        totals[row.result] += 1;
      }
    });
    $summary.html(`
      ${stat('total', 'testes', totals.total)}
      ${stat('passed', 'passados', totals.passed)}
      ${stat('failed', 'com erro', totals.failed)}
      ${stat('skipped', 'ignorados', totals.skipped)}
      ${stat('total', 'duração total', formatTime(totals.sec))}
    `);
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
    if (seconds < 60) {
      return `${seconds.toFixed(1)}s`;
    }
    return `${seconds.toFixed(1)}s (${(seconds / 60).toFixed(1)} min)`;
  }

  function showError(message) {
    $loading.addClass('d-none');
    $errorAlert.text(message).removeClass('d-none');
  }
});
