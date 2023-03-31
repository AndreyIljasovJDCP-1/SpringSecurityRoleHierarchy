document.getElementById('form_id').addEventListener('submit', (event) => {
    let checkboxes = document.querySelectorAll('input');
    for (const checkbox of checkboxes) {
        if (checkbox.checked) {
            return true;
        }
    }
    event.preventDefault();
    alert("Отметьте хотя бы одно значение!");
    return false;
});