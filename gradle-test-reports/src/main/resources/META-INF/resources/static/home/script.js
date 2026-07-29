$(function () {
  const $dirInput = $('#dirInput');
  const $dirForm = $('#dirForm');
  const $errorAlert = $('#errorAlert');

  $dirInput.val(GTR.loadDir());
  $dirInput.trigger('focus');

  $dirForm.on('submit', function (event) {
    event.preventDefault();
    const dir = $dirInput.val().trim();
    if (!dir) {
      $errorAlert.text('Informe um diretório válido.').removeClass('d-none');
      return;
    }
    GTR.saveDir(dir);
    window.location.href = `/projects.html?dir=${encodeURIComponent(dir)}`;
  });
});
