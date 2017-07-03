"""Hakuna-Matata url's."""


from django.conf.urls import url
from . import views

urlpatterns = (
    url(r'^$', view=views.project_view, name="projects"),
    url(r'^(?P<project_id>\d+)/usecases$', view=views.usecases_view, name="usecases"),
    url(r'^add-usecase/(?P<project_id>\d+)$', view=views.add_usecases_view, name="add_usecases"),
    url(r'^delete-usecases/(?P<project_id>\d+)$', view=views.delete_usecases_view, name="delete_usecases"),
    url(r'^(?P<project_id>\d+)/job/(?P<job_id>\d+)$', view=views.job_view, name="job"),
    url(r'^delete-job-usecases/(?P<project_id>\d+)/job/(?P<job_id>\d+)$', view=views.delete_job_usecases_view, name="delete_job_usecases"),
    url(r'^delete-job/(?P<project_id>\d+)/job/(?P<job_id>\d+)$', view=views.delete_job_view, name="delete_job"),
    url(r'^add-jobs/(?P<project_id>\d+)$', view=views.add_jobs_view, name="add_jobs"),
    url(r'^(?P<project_id>\d+)/usecases/actions/(?P<usecase_id>\d+)$', view=views.actions_view, name="actions"),
    url(r'^reports/(?P<project_id>\d+)/$', view=views.reports_view, name="reports"),
    url(r'^report/(?P<report_id>\d+)$', view=views.render_report, name="report"),
    url(r'^add_project/$', views.add_project, name='add_project'),
    url(r'^remove_project/(?P<project_id>\d+)/$', views.remove_project, name='remove_project'),
)
