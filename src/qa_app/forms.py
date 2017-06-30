"""Forms.py file."""

from django import forms
from .models import Action, UseCase, Project, Jobs

DEFAULT_OPTION = [["", "Select an option"]]

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


class ActionsFormset(forms.ModelForm):
    """Actions form."""

    hidden_id = forms.CharField(required=False, widget=forms.HiddenInput())
    seq = forms.IntegerField(required=False, widget=forms.TextInput(attrs={'readonly': 'readonly'}))
    action = forms.ChoiceField(choices=ACTION_CHOICES, required=False)
    locators = forms.ChoiceField(choices=DEFAULT_OPTION + LOCATORS_CHOICES, required=False)

    class Meta:
        """Meta Data."""

        model = Action
        fields = ['seq', 'description', 'action', 'locators', 'element_identifier', 'element_value']


class UsecaseForm(forms.ModelForm):
    """Usecase form."""

    class Meta:
        """Meta Data."""

        model = UseCase
        fields = ['use_case_name', 'use_case_description']


class ProjectForm(forms.ModelForm):
    """ProjectForm."""

    class Meta:
        """Meta class."""

        model = Project
        fields = ['name']


class JobsForm(forms.ModelForm):
    """Jobs form."""

    class Meta:
        """Meta Data."""

        model = Jobs
        fields = ['name']
