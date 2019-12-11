var ENTITIES = {};

function isColumnVisible(columnName) {
    return columnName !== 'id';
}

function createTable(data, actions) {
    var table = $('<table/>').attr('border',1);
    var columnFilter = isColumnVisible;
    if(data && data.length) {
        var header = $('<tr/>');
        $.each( data[0], function( key, val ) {
            if(! $.isFunction(columnFilter) || columnFilter(key)) {
                header.append($('<th/>').text(key));
            }
        });
        if($.isArray(actions) && actions.length) {
            header.append($('<th/>').text('Действия'));
        }
        table.append(header);
        $.each( data, function(i, obj) {
            var row = $('<tr/>');
            $.each( obj, function(key, val) {
                if(! $.isFunction(columnFilter) || columnFilter(key)) {
                    if($.isArray(val)) {
                        row.append($('<td/>').append(createTable(val)));
                    } else if($.isPlainObject(val)) {
                        if('name' in val) {
                            row.append($('<td/>').text(val.name));
                        } else {
                            row.append($('<td/>').text(JSON.stringify(val)));
                        }
                    } else {
                        row.append($('<td/>').text(val));
                    }
                }
            });
            if($.isArray(actions)) {
                $.each(actions, function(i, action) {
                    var visible = !$.isFunction(action.isVisible) || action.isVisible(obj)
                    if(visible) {
                        $.each(action.fields, function(j, field){
                            row.append($('<span/>').text(field.name));
                            row.append(field.generator(obj));
                        });
                        row.append($('<button/>').text(action.name).addClass('actionButton')
                            .click({obj:obj}, action.onclick));
                    }
                });
            }
            table.append(row);
         });
    }
    return table;
}

function loadTable(selector, url, data, actions, dataConsumer) {
    $.getJSON(url, data).done(function(json) {
        dataConsumer(json);
        $(selector).empty().append(createTable(json, actions));
    }).fail(function( jqxhr, textStatus, error ) {
        var err = textStatus + ", " + error;
        console.log( "Request Failed: " + err );
    });
}

function createEntityContainers() {
    var result = [];
    $.each(ENTITIES, function(id, entity) {
        var entityContainer = $('<div/>').attr('id', id+"Container").addClass('entityContainer');
        entityContainer.append($('<strong/>').text(entity.name));
        entityContainer.append($('<div/>').attr('id', id+"Table"));
        ENTITIES[id].reloadFunction = function() {
            loadTable('#'+id+"Table", entity.url, [], entity.actions, function(json){
                ENTITIES[id].rows=json;
            });
        };
        entityContainer.append($('<button/>').text('Перечитать данные').addClass('reloadTableButton').click(
            ENTITIES[id].reloadFunction
        ));
        result.push(entityContainer);
    });
    return result;
}