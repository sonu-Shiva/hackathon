"""Admin file to register the models."""

# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.contrib import admin
from .models import Project, UseCase, Action, Reports, Jobs, JobUseCases


@admin.register(Project)
class ProjectAdmin(admin.ModelAdmin):
    """Project admin."""

    list_display = ['name']
    search_fields = ['name']


class JobUseCasesInline(admin.TabularInline):
    """JobUseCases inline admin."""

    model = JobUseCases
    extra = 0


@admin.register(UseCase)
class UseCaseAdmin(admin.ModelAdmin):
    """UseCase admin."""

    list_display = ['use_case_name', 'project', 'use_case_description']
    search_fields = ['project__name', 'use_case_name']
    inlines = [JobUseCasesInline]


@admin.register(Jobs)
class JobsAdmin(admin.ModelAdmin):
    """Jobs admin."""

    list_display = ['name', 'project']
    search_fields = ['name', 'project__name']
    inlines = [JobUseCasesInline]


@admin.register(JobUseCases)
class JobUseCasesAdmin(admin.ModelAdmin):
    """Jobs admin."""

    list_display = ['seq', 'job', 'usecase']
    search_fields = ['job__name']


@admin.register(Action)
class ActionAdmin(admin.ModelAdmin):
    """Actions admin."""

    list_display = ['use_case', 'seq', 'description', 'action', 'locators', 'element_identifier', 'element_value']
    search_fields = ['use_case__project__name', 'use_case__use_case_name']


@admin.register(Reports)
class ReportsAdmin(admin.ModelAdmin):
    """Reports admin."""

    pass
