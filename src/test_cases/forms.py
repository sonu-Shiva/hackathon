"""Forms.py file."""

from django import forms
from .models import Action, Project


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
    locators = forms.ChoiceField(choices=LOCATORS_CHOICES, required=False)

    class Meta:
        """Meta Data."""

        model = Action
        fields = ['seq', 'description', 'action', 'locators', 'element_identifier', 'element_value']


class ProjectForm(forms.ModelForm):
    """ProjectForm."""

    class Meta:
        """Meta class."""

        model = Project
        fields = ['name']
