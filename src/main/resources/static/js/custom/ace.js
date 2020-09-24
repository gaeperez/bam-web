$(function () {
    // Set editor
    var editor = ace.edit("editor");
    editor.setTheme("ace/theme/monokai");
    editor.getSession().setMode("ace/mode/json");
    editor.setOptions({
        enableBasicAutocompletion: true,
        enableSnippets: true,
        maxLines: 30,
        minLines: 2
    });
    editor.setShowPrintMargin(false);
    editor.setHighlightActiveLine(true);
    editor.setAutoScrollEditorIntoView(true);

    // Set button for giving examples
    var button = document.getElementById("btnSchemaExample");
    button.addEventListener("click", writeExample);

    // Set button for formatting
    button = document.getElementById("btnSchemaFormat");
    button.addEventListener("click", formatCode);

    // Set value in the text area
    var textarea = $('#textAreaSchema');
    editor.getSession().setValue(textarea.text());
    editor.getSession().on('change', function () {
        textarea.val(editor.getSession().getValue());
    });

    // Format code if there is something
    formatCode();
});

function writeExample() {
    var editor = ace.edit("editor");
    editor.getSession().setValue("{\"$id\":\"https://example.com/agent.schema.json\",\"$schema\":\"http://json-schema.org/draft-07/schema#\",\"title\":\"Agent\",\"type\":\"object\",\"properties\":{\"name\":{\"type\":\"string\",\"description\":\"Something's name.\"}}}");

    formatCode();
}

function formatCode() {
    var editor = ace.edit("editor");
    var val = editor.getSession().getValue();

    if (val != "") {
        // Format code
        var o = JSON.parse(val);
        val = JSON.stringify(o, null, 4);
        editor.session.setValue(val);
    }
}