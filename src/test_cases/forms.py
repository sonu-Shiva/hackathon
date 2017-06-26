"""Forms.py file."""

from django import forms
# from .models import Project, UseCase, Action, Reports


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


class ActionsFormset(forms.Form):
    """Actions form."""

    hidden_id = forms.CharField(required=False, widget=forms.HiddenInput())
    seq = forms.IntegerField(required=False, widget=forms.TextInput(attrs={'readonly': 'readonly'}))
    use_case = forms.ChoiceField(required=False, widget=forms.HiddenInput())
    description = forms.CharField(required=False)
    action = forms.ChoiceField(choices=ACTION_CHOICES, required=False)
    locators = forms.ChoiceField(choices=LOCATORS_CHOICES, required=False)
    element_identifier = forms.CharField(required=False)
    element_value = forms.CharField(required=False)

    def __init__(self, use_case_choices=[], *args, **kwargs):
        """Constructor Method."""
        super(ActionsFormset, self).__init__(*args, **kwargs)
        self.fields['use_case'] = forms.ChoiceField(choices=use_case_choices, required=False)
