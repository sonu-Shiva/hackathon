"""Views file."""

# -*- coding: utf-8 -*-
from __future__ import unicode_literals
from django.http import HttpResponse, HttpResponseRedirect
from django.core.urlresolvers import reverse_lazy
from django.shortcuts import render
from django.forms import formset_factory
from .models import Project, Reports, UseCase, Action
from .forms import ActionsFormset, UsecaseForm
from functools import partial, wraps


def project_view(request):
    """Project screen views."""
    projects = Project.objects.all()
    context = {
        "projects": projects,
    }
    return render(request, "projects_list.html", context)


def reports_view(request):
    """Report screen views."""
    projects = Project.objects.all()
    context = {
        "projects": projects,
    }
    return render(request, "reports_list.html", context)


def render_report(request, report_id):
    """Show reports views."""
    try:
        report = Reports.objects.get(id=report_id).report
    except Reports.DoesNotExist:
        return HttpResponse(500)
    return HttpResponse(report)


def usecases_view(request, project_id):
    """Usecase screen views."""
    try:
        project = Project.objects.get(id=project_id).name
        usecases = UseCase.objects.filter(project__id=project_id).order_by('id')
    except (UseCase.DoesNotExist, Project.DoesNotExist):
        return HttpResponse(500)

    usecase_form = UsecaseForm()

    context = {
        'project_id': project_id,
        'project': project,
        'usecases': usecases,
        'usecase_form': usecase_form,
    }
    return render(request, 'project_usecases.html', context)


def add_usecases_view(request, project_id):
    """View to handle newly added usecases."""
    usecase_form = UsecaseForm(data=request.POST)
    if usecase_form.is_valid():
        clean_data = usecase_form.cleaned_data
        if any(clean_data.values()):
            project = Project.objects.get(id=project_id)
            UseCase.objects.create(project=project, use_case_name=clean_data['use_case_name'], use_case_description=clean_data['use_case_description'])
    return HttpResponseRedirect(reverse_lazy('qabot:usecases', kwargs={'project_id': project_id}))


def actions_view(request, project_id, usecase_id):
    """Action screen views."""
    actions_prefix = 'actions-prefix'
    actions_formset = formset_factory(wraps(ActionsFormset)(partial(
        ActionsFormset)),
        max_num=1000,
        extra=0,
        min_num=1
    )

    if request.method == 'GET':
        actions_obj = Action.objects.filter(use_case__id=usecase_id).order_by('seq')
        initial_data = []
        for obj in actions_obj:
            initial_data.append({
                'hidden_id': obj.id,
                'seq': obj.seq,
                'description': obj.description,
                'action': obj.action,
                'locators': obj.locators,
                'element_identifier': obj.element_identifier,
                'element_value': obj.element_value,
            })
        if not initial_data:
            initial_data = [{'seq': 1}]
        actions_form = actions_formset(initial=initial_data, prefix=actions_prefix)
        context = {
            'project_id': project_id,
            'usecase_id': usecase_id,
            'usecase_name': UseCase.objects.get(id=usecase_id).use_case_name,
            'actions_form': actions_form,
        }
        return render(request, 'usecase_actions.html', context)
    elif request.method == 'POST':
        for action_id in request.POST.get('deleted_actions', '').strip(';').split(';'):
            if action_id:
                try:
                    Action.objects.get(id=int(action_id)).delete()
                except Action.DoesNotExist:
                    pass  # Object must have already been deleted.
        actions_form = actions_formset(data=request.POST, prefix=actions_prefix)

        usecase_obj = UseCase.objects.get(id=usecase_id)
        if actions_form.is_valid():
            for action in actions_form.cleaned_data:
                if any(action.values()):
                    if not action['hidden_id']:
                        action_obj = Action()
                        action_obj.seq = int(action['seq'])
                        action_obj.use_case = usecase_obj
                        action_obj.description = action['description']
                        action_obj.action = action['action']
                        action_obj.locators = action['locators']
                        action_obj.element_identifier = action['element_identifier']
                        action_obj.element_value = action['element_value']
                        action_obj.save()
                    else:
                        action_obj = Action.objects.filter(id=int(action['hidden_id']))
                        action_obj.update(
                            seq=int(action['seq']),
                            description=action['description'],
                            action=action['action'],
                            locators=action['locators'],
                            element_identifier=action['element_identifier'],
                            element_value=action['element_value'],
                        )
        return HttpResponseRedirect(reverse_lazy('qabot:actions', kwargs={'project_id': project_id, 'usecase_id': usecase_id}))
