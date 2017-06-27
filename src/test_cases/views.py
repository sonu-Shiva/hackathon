# -*- coding: utf-8 -*-
from __future__ import unicode_literals
from django.http import HttpResponse
from django.shortcuts import render
from .models import Project, Reports, ProjectForm
from django.http import HttpResponseRedirect
from django.shortcuts import get_object_or_404
from django.core.urlresolvers import reverse_lazy


def project_view(request):
    """Get all project's."""
    projects = Project.objects.all()
    form = ProjectForm()
    context = {
        "projects": projects,
        "form": form,
    }
    return render(request, "index.html", context)


def reports_view(request):
    projects = Project.objects.all()
    context = {
        "projects": projects,
    }
    return render(request, "reports_list.html", context)


def render_report(request, report_id):
    try:
        report = Reports.objects.get(id=report_id).report
    except Reports.DoesNotExist:
        return HttpResponse(500)
    return HttpResponse(report)


def add_project(request):
    """Add new project."""
    if request.POST:
        new_project = ProjectForm(request.POST)
        if new_project.is_valid():
            new_project.save()
    return HttpResponseRedirect(reverse_lazy('hakuna_matata:projects'))


def remove_project(request, project_id):
    """Remove project."""
    remove_project = get_object_or_404(Project, pk=project_id)
    remove_project.delete()
    return HttpResponseRedirect(reverse_lazy('hakuna_matata:projects'))
