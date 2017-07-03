"""Hakuna Matata models file."""

# -*- coding: utf-8 -*-
from __future__ import unicode_literals
from django.db import models


class Project(models.Model):
    """Project model class."""

    name = models.CharField(max_length=255, blank=False, verbose_name='Project Name')

    class Meta:
        """Meta Data."""

        ordering = ['name']

    def __str__(self):
        """String representation of the model."""
        return self.name


class Jobs(models.Model):
    """Jobs model class."""

    name = models.CharField(blank=False, max_length=255)
    project = models.ForeignKey(Project)

    class Meta:
        """Meta Data."""

        ordering = ['name', 'project__name']

    def __str__(self):
        """String representation of the model."""
        return self.name


class UseCase(models.Model):
    """UseCase model class."""

    project = models.ForeignKey(Project)
    use_case_name = models.CharField(max_length=255, blank=False, verbose_name='Use Case Name')
    use_case_description = models.TextField(blank=True, verbose_name='Use Case Description', help_text='A short description of your Use Case')
    jobs = models.ManyToManyField(Jobs, through='JobUseCases')

    class Meta:
        """Meta Data."""

        ordering = ['project__name', 'use_case_name']

    def __str__(self):
        """String representation of the model."""
        return "%s - %s" % (self.project.name, self.use_case_name)


class JobUseCases(models.Model):
    """JobUseCases model class."""

    usecase = models.ForeignKey(UseCase, on_delete=models.CASCADE)
    job = models.ForeignKey(Jobs, on_delete=models.CASCADE)
    seq = models.IntegerField(blank=False, null=True)

    class Meta:
        """Meta Data."""

        ordering = ['job__name', 'seq']


class Action(models.Model):
    """Action model class."""

    seq = models.IntegerField(blank=False, null=True, verbose_name='Sequence Number', help_text='All actions of a particular usecase are executed in the order of this sequence number.')
    use_case = models.ForeignKey(UseCase)
    description = models.TextField(blank=True, verbose_name='Description', help_text='A description of this Action')
    action = models.CharField(blank=False, max_length=255)
    locators = models.CharField(blank=True, max_length=255)
    element_identifier = models.CharField(max_length=1024, blank=True)
    element_value = models.CharField(max_length=1024, blank=True)

    class Meta:
        """Meta Data."""

        ordering = ['use_case__project__name', 'use_case__use_case_name', 'seq']

    def __str__(self):
        """String representation of the model."""
        return "%s - %s - %s" % (self.use_case.project.name, self.use_case.use_case_name, self.description)


class Reports(models.Model):
    """Reports model class."""

    use_case = models.ForeignKey(UseCase)
    time = models.CharField(max_length=500, blank=True)
    report = models.TextField()
