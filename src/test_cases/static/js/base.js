$(document).ready(function(){
    // Dragging Feature.
    $( "#sortable" ).sortable({
        revert: true,
        items: 'tr.actions-formset',
        stop: function(event, ui){ rearrageSequenceNumbers(ui.item); }
    }).disableSelection();

    // Disabling sort on delete button.
    $('#sortable td.dont-move').mousedown(function(event){
        event.stopImmediatePropagation();
    });

    selectUnselectAll('.select-all-usecases-ck', '.usecase-ck');

    var id_list = [];
    $(document).on('click', '.usecase-ck', function(){
        var current_usecase_id = $(this).parent('td').find('label a').attr('href').split('/').slice(-1)[0];
        var index = id_list.indexOf(current_usecase_id);
        if ( $(this).is(':checked') ) {
            if ( index < 0 ) {
                id_list.push(current_usecase_id);
            }
        } else {
            if ( index > -1 ) {
                id_list.splice(index, 1);
            }
        }
        $("#run_multiple_ucs").attr('href', "http://192.168.22.115:8080/CodeLessAutomation/Controller/?usecase_id=" + id_list);
    });

    // Actions formset.
    var prefix = $('#id_hidden_actions_prefix').val();
    $('.actions-formset').formset({
        prefix: prefix,
        addText: '<button type="button" class="btn btn-primary" id="id_' + prefix + '"><i class="fa fa-plus" aria-hidden="true"> Add Action</i></button>',
        deleteText: '<i class="fa fa-trash font-size-25" aria-hidden="true"></i>',
        addClass: 'add-row',
        added: onAddEvent,
        removed: onDeleteEvent
    });
})

function onAddEvent(ele) {
    var new_sequence_number = parseInt($(ele).prev().find("input[id$='-seq']").val()) + 1;
    new_sequence_number = new_sequence_number ? new_sequence_number : 1;
    $(ele).find("input[id$='-seq']").val(new_sequence_number);

    $(ele).find("select").each(function(){
        $(this).val($(this).find('option:first').val());
    });
}

function onDeleteEvent(ele) {
    var deleted_seq = parseInt($(ele).find('input[id$=-seq]').val());
    $('tr.actions-formset:visible:nth-child(n+' + deleted_seq + ')').each(function(){
        $(this).find('input[id$=-seq]').val(deleted_seq);
        deleted_seq += 1;
    });
    var id_val = $(ele).find('input[id$=hidden_id]').val();
    if ( id_val ) {
        $("#id_deleted_actions").val( $("#id_deleted_actions").val() + id_val + ';' );
    }
}

function selectUnselectAll(parentCheckboxClass, childCheckboxesClass) {
    $(document).on('click', parentCheckboxClass, function(){
        if ( $(this).is(':checked') ) {
            $(document).find(childCheckboxesClass).each(function(){
                $(this).prop('checked', true);
            });
        } else {
            $(document).find(childCheckboxesClass).each(function(){
                $(this).prop('checked', false);
            });
        }
    });

    $(document).on('click', childCheckboxesClass, function(){
        if ( !$(this).is(':checked') ) {
            $(document).find(parentCheckboxClass).prop('checked', false);
        }
    });
}


function rearrageSequenceNumbers(ele) {
    var current = parseInt($(ele).find('input[id$=-seq]').val());
    var above = $(ele).prev().find('input[id$=-seq]').val();
    var below = $(ele).next().find('input[id$=-seq]').val();

    above = above ? parseInt(above) : null;
    below = below ? parseInt(below) : null;

    if ( below && below < current) {
        if ( below < current ) {
            $('tr.actions-formset:visible:nth-child(n+' + below + ')').each(function(){
                if ( parseInt($(this).find('input[id$=-seq]').val()) <= current ) {
                    $(this).find('input[id$=-seq]').val(below);
                    below += 1;
                } else {
                    return;
                }
            })
        }
    } else if ( above && above > current) {
        if ( above > current ) {
            $($('tr.actions-formset:visible:not(:nth-child(n+' + (above + 1) + '))').get().reverse()).each(function(){
                if ( parseInt($(this).find('input[id$=-seq]').val()) > current - 1 ) {
                    $(this).find('input[id$=-seq]').val(above);
                    above -= 1;
                } else {
                    return;
                }
            })
        }
    }
}
