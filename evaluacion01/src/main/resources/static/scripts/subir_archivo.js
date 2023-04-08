// Para la entrada del archivo Excel
const input_file = document.getElementById("file");
const card_upload = document.getElementById("upload-zone");
const hint_data = document.getElementById("hint");

card_upload.addEventListener("dragover", (e) => {
    e.preventDefault();
    card_upload.classList.add("dragover");
});

["dragleave", "dragend"].forEach((event_type) => {
    card_upload.addEventListener(event_type, (e) => {
        e.preventDefault();
        card_upload.classList.remove("dragover");
    });
});

card_upload.addEventListener("drop", (e) => {
    e.preventDefault();
    if (e.dataTransfer.files.length) {
        input_file.files = e.dataTransfer.files;
        hint_data.innerHTML = input_file.files[0].name;
    }
});

input_file.addEventListener("change", (e) => {
    hint_data.innerHTML = e.target.files[0].name;
    card_upload.classList.add("dragover");
});
