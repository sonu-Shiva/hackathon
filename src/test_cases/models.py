# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models
from django import forms


ACTION_CHOICES = [
    ["Click", "Click"],
    ["EnterText", "Enter Text"],
    ["SelectFromDropDown", "Select From Drop Down"],
    ["SelectFromLookUp", "Select From Look Up"],
    ["URL", "URL"],
    ["PAUSE", "PAUSE"]
]

LOCATORS_CHOICES = [
    ["ID", "ID"],
    ["NAME", "Name"],
    ["XPATH", "X Path"],
    ["CSSSELECTOR", "CSS Selector"],
    ["LINKTEXT", "Link Text"],
    ["CLASSNAME", "Class Name"],
    ["TAGNAME", "Tag Name"],
    ["PARTIALLINKTEXT", "Partial Link Text"]
]


class Project(models.Model):
    name = models.CharField(max_length=255, blank=False, verbose_name='Project Name')

    def __str__(self):
        return self.name


class ProjectForm(forms.ModelForm):
    """docstring for ProjectForm"""
    class Meta:
        model = Project
        exclude = ()


class UseCase(models.Model):
    project = models.ForeignKey(Project)
    use_case_name = models.CharField(max_length=255, blank=False, verbose_name='Use Case Name')
    use_case_description = models.TextField(blank=True, verbose_name='Use Case Description', help_text='A short description of your Use Case')

    def __str__(self):
        return "%s - %s" % (self.project.name, self.use_case_name)


class Action(models.Model):
    use_case = models.ForeignKey(UseCase)
    description = models.TextField(blank=False, verbose_name='Description', help_text='A description of this Action')
    action = models.CharField(choices=ACTION_CHOICES, blank=False, max_length=255)
    locators = models.CharField(choices=LOCATORS_CHOICES, blank=True, max_length=255)
    element_identifier = models.CharField(max_length=1024, blank=True)
    element_value = models.CharField(max_length=1024, blank=True)

    def __str__(self):
        return "%s - %s - %s" % (self.use_case.project.name, self.use_case.use_case_name, self.description)


class Reports(models.Model):
    use_case = models.ForeignKey(UseCase)
    time = models.CharField(max_length=500, blank=True)
    report = models.TextField()
