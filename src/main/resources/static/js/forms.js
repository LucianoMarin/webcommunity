document.querySelectorAll('form').forEach(form => {
    form.addEventListener('submit', event => {
        if (form.dataset.submitting === 'true') { event.preventDefault(); return; }
        form.dataset.submitting = 'true';
        form.querySelectorAll('button[type="submit"]').forEach(button => {
            button.disabled = true; button.setAttribute('aria-busy','true');
        });
    });
});
const error = document.querySelector('[role="alert"]');
if (error) { error.tabIndex = -1; error.focus(); }
