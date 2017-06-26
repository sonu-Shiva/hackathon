# -*- coding: utf-8 -*-
from __future__ import unicode_literals
from django.http import HttpResponse
from django.shortcuts import render
from .models import Project, Reports
from django.http import HttpResponseRedirect
from django.shortcuts import get_object_or_404


def project_view(request):
    """Get all project's."""
    projects = Project.objects.all()
    context = {
        "projects": projects,
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
    name = request.POST['project_name']
    new_project = Project(name=name)
    new_project.save()
    return HttpResponseRedirect('/qabot')


def remove_project(request, proj_id):
    """Remove project."""
    remove_project = get_object_or_404(Project, pk=proj_id)
    remove_project.delete()
    return HttpResponseRedirect('/qabot')
