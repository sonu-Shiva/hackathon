# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.contrib import admin
from .models import Project, UseCase, Action


@admin.register(Project)
class ProjectAdmin(admin.ModelAdmin):
    pass


@admin.register(UseCase)
class UseCaseAdmin(admin.ModelAdmin):
    pass


@admin.register(Action)
class ActionAdmin(admin.ModelAdmin):
    pass
