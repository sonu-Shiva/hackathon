$(document).ready(function(){
    // Dragging Feature.
    $( "#sortable_actions" ).sortable({
        revert: true,
        items: 'tr.actions-formset',
        stop: function(event, ui){ rearrageSequenceNumbers(ui.item, 'actions-formset'); }
    });

    // Disabling sort on delete button.
    $('#sortable_actions td.dont-move').mousedown(function(event){
        event.stopImmediatePropagation();
    });

    selectUnselectAll('.select-all-usecases-ck', '.usecase-ck');

    $("#id_uc_execution_type_0").prop('checked', true);
    $("#id_job_execution_type_0").prop('checked', true);

    var URL = $('#id_url_hidden_input').val();

    $(document).on('click', '.individual_uc', function(){
        var browser = $('#id_browser_usecases').find(":selected").val();
        $(this).attr('href', $(this).attr('href').split('&')[0] + '&' + browser);
    });

    $(document).on('click', '.run-job', function(){
        var browser = $('#id_browser_jobs').find(":selected").val();
        $(this).attr('href', $(this).attr('href').split('&')[0] + '&' + browser);
    });

    $(document).on('click', '#id_run_multiple_ucs', function(){
        var id_list = [];
        $(".usecase-ck:checked").each(function(){
            var current_usecase_id = $(this).parent('td').find('label a').attr('href').split('/').slice(-1)[0];
            id_list.push(current_usecase_id);
        });
        var browser = $('#id_browser_usecases').find(":selected").val();
        var execution_type = $('#id_uc_execution_type').find(':checked').val();
        if ( execution_type === 'Parallel' ) {
            $(this).attr('href', 'javascript:void(0)');
            for (var i = 0; i < id_list.length; i++) {
                var uc_url = URL + '?usecase_id=' + id_list[i] + '&' + browser;
                openInNewTab(uc_url);
            }
        } else {
            $(this).attr('href', URL + "?usecase_id=" + id_list + '&' + browser);
        }
    });

    $(document).on('click', '#id_delete_usecase_btn', function(){
        $(".usecase-ck:checked").each(function(){
            var current_usecase_id = $(this).parent('td').find('label a').attr('href').split('/').slice(-1)[0];
            $("#id_deleted_usecases").val($("#id_deleted_usecases").val() + current_usecase_id + ';');
        });
    });

    // Actions formset.
    var prefix = $('#id_hidden_actions_prefix').val();
    $('.actions-formset').formset({
        prefix: prefix,
        addText: '<button type="button" class="btn btn-primary" id="id_' + prefix + '"><i class="fa fa-plus" aria-hidden="true"> Add Action</i></button>',
        deleteText: '<i class="fa fa-trash font-size-25 padding-top-5" aria-hidden="true"></i>',
        addClass: 'add-row',
        added: onAddEvent,
        removed: onDeleteEvent
    });

    // Dragging Feature.
    $( "#sortable_jobs" ).sortable({
        revert: true,
        items: 'tr.job-usecase',
        stop: function(event, ui){ rearrageSequenceNumbers(ui.item, 'job-usecase'); }
    });

    $(document).on('click', '#id_job_usecases_btn', function(){
        var undeleted_usecases = $('input[id^=id_job_usecase_ck_]:not(:checked)');
        var number_of_usecases = $(undeleted_usecases).length;
        $($(undeleted_usecases).get().reverse()).each(function(){
            $(this).parent('td').find('input[type=hidden][id^=id_job_usecase-]').val(number_of_usecases);
            number_of_usecases -= 1;
        });
    });
})

function openInNewTab(url) {
    var win = window.open(url, '_blank');
}

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

function rearrageSequenceNumbers(ele, parentClass) {
    var current = parseInt($(ele).find('input[id$=-seq]').val());
    var above = $(ele).prev().find('input[id$=-seq]').val();
    var below = $(ele).next().find('input[id$=-seq]').val();

    above = above ? parseInt(above) : null;
    below = below ? parseInt(below) : null;

    if ( below && below < current) {
        if ( below < current ) {
            $('tr.' + parentClass + ':visible:nth-child(n+' + below + ')').each(function(){
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
            $($('tr.' + parentClass + ':visible:not(:nth-child(n+' + (above + 1) + '))').get().reverse()).each(function(){
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
