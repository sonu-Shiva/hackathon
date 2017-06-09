# -*- coding: utf-8 -*-
from __future__ import unicode_literals
from django.http import HttpResponse
from django.shortcuts import render
from .models import Project, Reports


def project_view(request):
    projects = Project.objects.all()
    context = {
        "projects": projects,
    }
    return render(request, "projects_list.html", context)


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
