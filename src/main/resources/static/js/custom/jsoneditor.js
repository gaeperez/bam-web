// Global variables
var editor_type;
var editor_shape;
var mapLayerShapeEditors = new Object();
var mapLayerShapeHidden = new Object();

// Wait until document is loaded
$(function () {
    // Add to every select.shape in the page (not the new ones)
    $("select.layerShape").each(function () {
        // Add database index
        $(this).data("defaultValue", $(this).val());

        createLayerShapeEditors($(this));
        // Put the onchange method
        $(this).change(function () {
            createLayerShapeEditors($(this));
        });
    });

    // Create the editor for agent type
    $("#type").data("defaultValue", $("#type").val());
    createTypeEditor($("#type"));
    // Add onChange function
    $('#type').change(function () {
        createTypeEditor($(this));
    });

    // Create the editor for agent shape
    $("#agentShape").data("defaultValue", $("#agentShape").val());
    createShapeEditor($("#agentShape"));
    // Add onChange function
    $('#agentShape').change(function () {
        createShapeEditor($(this));
    });
});

function createTypeEditor(selector) {
    // Get the editor, if exists, and destroy it
    if (editor_type)
        editor_type.destroy();

    // Get the selected value in the selector
    var savedIndex = selector.val();
    // Get the corresponding json schema for the selected value
    var selectedJsonSchema = mapTypes.get(savedIndex).customParametersSchema;
    // Parse the json schema
    var jsonParsed = JSON.parse(selectedJsonSchema);

    // Create the editor
    editor_type = createEditor($("#editor_type")[0], jsonParsed);

    // Set database value (if necessary)
    if (selector.data("defaultValue") == savedIndex) {
        var savedJson = $("#type_json").val();
        if (savedJson !== "")
            editor_type.setValue(JSON.parse(savedJson));
    }
}

function createShapeEditor(selector) {
    // Get the editor, if exists, and destroy it
    if (editor_shape)
        editor_shape.destroy();

    // Get the selected value in the selector
    var savedIndex = selector.val();
    // Get the corresponding json schema for the selected value
    var selectedJsonSchema = mapShapes.get(savedIndex).dimensionSchema;
    // Parse the json schema
    var jsonParsed = JSON.parse(selectedJsonSchema);

    // Create the editor
    editor_shape = createEditor($("#editor_agent_shape")[0], jsonParsed);

    // Set database value (if necessary)
    if (selector.data("defaultValue") == savedIndex) {
        var savedJson = $("#agent_shape_json").val();
        if (savedJson !== "")
            editor_shape.setValue(JSON.parse(savedJson));
    }
}

function createLayerShapeEditors(selector) {
    // Get the editor (if exists)
    var editorId = selector.data("editorId");
    if (!editorId) {
        // If not exists, create it and put a unique id
        editorId = uniqueId();
        selector.data("editorId", editorId);
    } else
        // If exists, destroy it
        mapLayerShapeEditors[editorId].destroy();

    // Get the selected value in the selector
    var savedIndex = selector.val();
    // Get the corresponding json schema for the selected value
    var selectedJsonSchema = mapShapes.get(savedIndex).dimensionSchema;
    // Parse the json schema
    var jsonParsed = JSON.parse(selectedJsonSchema);

    // Get the editor element in the HTML
    var editorElement = selector.closest(".containerElement").find(".editor_layer_shape");
    // Create the editor
    var editor = createEditor(editorElement[0], jsonParsed);

    // Set database value (if necessary)
    var savedJson = selector.closest(".containerElement").find(".layer_shape_json");
    if (selector.data("defaultValue") == savedIndex) {
        if (savedJson.val() !== "")
            editor.setValue(JSON.parse(savedJson.val()));
    }

    // Save the created editor in the map
    mapLayerShapeEditors[editorId] = editor;
    // Save the associated hidden field in the other map
    mapLayerShapeHidden[editorId] = savedJson;
}

/**
 * Function to genarate the HTML dynamic fields.
 * @param fieldName
 * @param jsonParsed
 */
function createEditor(editorElement, jsonParsed) {
    var options = {
        // Enable fetching schemas via ajax
        ajax: true,
        // The schema for the editor
        schema: jsonParsed,
        // Properties
        disable_edit_json: true,
        disable_properties: true,
        // Theme
        theme: 'bootstrap4',
        iconlib: 'jqueryui'
    }

    // Initialize the editor
    return new JSONEditor(editorElement, options);
}

// Should work for most cases
function uniqueId() {
    return Math.round(new Date().getTime() + (Math.random() * 100));
}

/**
 * Onsubmit function to set the value of the dynamic fields as a JSON in the hidden variables of the view.
 */
function onSubmitForm() {
    // Get information from dynamic fields of Type and set into the hidden field
    let json = editor_type.getValue();
    $("#type_json").val(JSON.stringify(json, null, 2));

    // Get information from dynamic fields of agent Shape and set into the hidden field
    json = editor_shape.getValue();
    $("#agent_shape_json").val(JSON.stringify(json, null, 2));

    // Both maps have the same size
    for (const [key, value] of Object.entries(mapLayerShapeEditors)) {
        var inputHidden = mapLayerShapeHidden[key];

        inputHidden.val(JSON.stringify(value.getValue(), null, 2));
    }
}

function onClickAddLayer() {
    // Get the template to insert
    var fragment = document.getElementById("layer");
    // Get the position to insert into
    var divLayers = document.getElementById("layers");

    // Get the HTML of the template
    var cloneFragment = fragment.innerHTML;
    // Concatenate the HTML of the fragment in the corresponding position
    divLayers.innerHTML += cloneFragment;

    // Get the new HTML fragment to put the default value
    var lastChild = $('#layers select.layerShape:last');
    lastChild.data("defaultValue", lastChild.val());
    createLayerShapeEditors(lastChild);

    // Put the onchange method on the last created
    lastChild.change(function () {
        createLayerShapeEditors(lastChild);
    });
}

function onClickDeleteLayer() {

}