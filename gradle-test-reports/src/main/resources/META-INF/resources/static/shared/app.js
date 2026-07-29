const GTR = (function () {
  const DIR_KEY = 'gtr.dir';

  function saveDir(dir) {
    if (dir) {
      localStorage.setItem(DIR_KEY, dir);
    }
  }

  function loadDir() {
    return localStorage.getItem(DIR_KEY) || '';
  }

  function param(name) {
    const value = new URLSearchParams(window.location.search).get(name);
    if (value) {
      return value;
    }
    return '';
  }

  function esc(value) {
    return $('<div>').text(value == null ? '' : value).html();
  }

  function basename(path) {
    if (!path) {
      return '';
    }
    const parts = path.split('/').filter(Boolean);
    if (parts.length === 0) {
      return path;
    }
    return parts[parts.length - 1];
  }

  function getJson(url, onOk, onErr) {
    $.ajax({url: url, dataType: 'json'})
      .done(function (data) {
        onOk(data);
      })
      .fail(function (xhr) {
        const fallback = `Falha ao carregar dados (HTTP ${xhr.status}).`;
        onErr(fallback);
      });
  }

  return {
    saveDir: saveDir,
    loadDir: loadDir,
    param: param,
    esc: esc,
    basename: basename,
    getJson: getJson
  };
})();
