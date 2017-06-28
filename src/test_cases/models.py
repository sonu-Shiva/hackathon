# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models


class Project(models.Model):
    name = models.CharField(max_length=255, blank=False, verbose_name='Project Name')

    class Meta:
        """Meta Data."""

        ordering = ['name']

    def __str__(self):
        return self.name


class UseCase(models.Model):
    project = models.ForeignKey(Project)
    use_case_name = models.CharField(max_length=255, blank=False, verbose_name='Use Case Name')
    use_case_description = models.TextField(blank=True, verbose_name='Use Case Description', help_text='A short description of your Use Case')

    class Meta:
        """Meta Data."""

        ordering = ['project__name', 'use_case_name']

    def __str__(self):
        return "%s - %s" % (self.project.name, self.use_case_name)


class Action(models.Model):
    seq = models.IntegerField(blank=False, null=True, verbose_name='Sequence Number', help_text='All actions of a particular usecase are executed in the order of this sequence number.')
    use_case = models.ForeignKey(UseCase)
    description = models.TextField(blank=False, verbose_name='Description', help_text='A description of this Action')
    action = models.CharField(blank=False, max_length=255)
    locators = models.CharField(blank=True, max_length=255)
    element_identifier = models.CharField(max_length=1024, blank=True)
    element_value = models.CharField(max_length=1024, blank=True)

    class Meta:
        """Meta Data."""

        ordering = ['use_case__project__name', 'use_case__use_case_name', 'seq']

    def __str__(self):
        return "%s - %s - %s" % (self.use_case.project.name, self.use_case.use_case_name, self.description)


class Reports(models.Model):
    use_case = models.ForeignKey(UseCase)
    time = models.CharField(max_length=500, blank=True)
    report = models.TextField()
